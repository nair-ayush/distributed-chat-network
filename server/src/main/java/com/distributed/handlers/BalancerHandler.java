package com.distributed.handlers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.distributed.db.DB;
import com.distributed.models.ChatMessage;
import com.distributed.models.FriendMessage;
import com.distributed.models.Message;
import com.distributed.models.MessageType;
import com.distributed.models.ServerMessage;
import com.distributed.models.User;
import com.distributed.models.UserMessage;

public class BalancerHandler implements Runnable {

  private int port;
  private String host;
  private Socket socket;
  private ObjectInputStream inStream;
  private ObjectOutputStream outStream;
  private BrokerHandler brokerHandler;
  private DB db;

  public BalancerHandler(String host, int port) {
    this.host = host;
    this.port = port;
    brokerHandler = BrokerHandler.getInstance();
    db = DB.getInstance();
    try {
      socket = new Socket(host, port);
      if (socket.isConnected()) {
        System.out.println("Connected to LoadBalancer at " + host + ":" + port);
      }
      outStream = new ObjectOutputStream(socket.getOutputStream());
    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    try {
      inStream = new ObjectInputStream(socket.getInputStream());
      while (true) {
        Message msg = (Message) inStream.readObject();
        switch (msg.getType()) {
          case CHAT_MESSAGE:
            ChatMessage cMsg = (ChatMessage) msg;
            System.out.println(cMsg);
            break;
          case LOGIN:
          case LOGOUT:
          case REGISTER:
            UserMessage uMsg = (UserMessage) msg;
            System.out.println(uMsg);
            handleAuthentication(uMsg);
            break;
          case FRIEND_MESSAGE:
            UserMessage fMsg = (UserMessage) msg;
            System.out.println(fMsg);
            handleFriendMessage(fMsg);
            break;
          case GET_FRIENDS:
            UserMessage gMsg = (UserMessage) msg;
            System.out.println(gMsg);
            handleGetFriends(gMsg);
            break;
          default:
            break;
        }
      }
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void send(Message msg) {
    try {
      outStream.writeObject(msg);
      outStream.flush();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void sendServerLeftMessage(String key) {
    ServerMessage sMsg = new ServerMessage(key, MessageType.SERVER_LEFT);
    send(sMsg);
  }

  private void handleAuthentication(UserMessage msg) {
    User user = msg.getSender();
    if (msg.getType() == MessageType.LOGIN) {
      // handle login
      if (!db.doesEmailExist(user.getEmail())) {
        db.createUser(user.getEmail(), user.getName());
      }

    } else {
      // handle logout
    }
  }

  private void handleGetFriends(UserMessage msg) {
    User user = msg.getSender();
    ArrayList<User> friends = db.getFriends(user.getEmail());
    FriendMessage response = new FriendMessage(user, friends, MessageType.GOT_FRIENDS);
    brokerHandler.send(response);
  }

  private void handleFriendMessage(UserMessage msg) {
    // handle friend message
    User user = msg.getSender();
    String receiverEmail = msg.getPayload();
    if (db.doesEmailExist(receiverEmail)) {
      db.addFriend(user.getEmail(), receiverEmail);
      handleGetFriends(msg);
    } else {
      System.out.println("Friend not found");
    }
  }

}
