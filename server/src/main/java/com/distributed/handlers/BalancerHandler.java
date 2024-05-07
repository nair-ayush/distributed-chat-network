package com.distributed.handlers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.distributed.db.DB;
import com.distributed.models.Chat;
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
        UserMessage uMsg;
        switch (msg.getType()) {
          case CHAT_MESSAGE:
          case GET_CHAT_MESSAGES:
            ChatMessage cMsg = (ChatMessage) msg;
            System.out.println(cMsg);
            handleChatMessage(cMsg);
            break;

          case LOGIN:
          case LOGOUT:
            uMsg = (UserMessage) msg;
            System.out.println(uMsg);
            handleAuthentication(uMsg);
            break;

          case GET_FRIENDS:
            uMsg = (UserMessage) msg;
            System.out.println(uMsg);
            handleGetFriends(uMsg);
            break;

          case ADD_FRIEND:
            uMsg = (UserMessage) msg;
            System.out.println(uMsg);
            handleAddFriend(uMsg);
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
    FriendMessage response = new FriendMessage(user, friends, MessageType.GET_FRIENDS_SUCCESS);
    brokerHandler.send(response);
  }

  private void handleAddFriend(UserMessage msg) {
    User user = msg.getSender();
    String friendEmail = msg.getPayload();
    FriendMessage response;
    if (db.doesEmailExist(friendEmail)) {
      db.addFriend(user.getEmail(), friendEmail);
      ArrayList<User> friends = db.getFriends(user.getEmail());
      response = new FriendMessage(user, friends, MessageType.ADD_FRIEND_SUCCESS);
    } else {
      response = new FriendMessage(user, "Email does not exist", MessageType.ADD_FRIEND_FAILURE);
    }
    brokerHandler.send(response);
  }

  private void handleChatMessage(ChatMessage msg) {
    if (msg.getType() == MessageType.GET_CHAT_MESSAGES) {
      Chat chat = db.getChat(msg.getSender().getEmail(), msg.getReceiver().getEmail());
      chat.setType(MessageType.GET_CHAT_MESSAGES_SUCCESS);
      chat.setSender(msg.getSender());
      brokerHandler.send(chat);
      return;
    }
    long timestamp = System.currentTimeMillis();
    msg.setTimestamp(timestamp);
    db.addChatMessage(msg.getSender().getEmail(), msg.getReceiver().getEmail(), msg.getPayload(), timestamp);
    brokerHandler.send(msg);
  }

}
