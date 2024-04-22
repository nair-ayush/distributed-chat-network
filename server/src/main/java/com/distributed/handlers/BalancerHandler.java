package com.distributed.handlers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.distributed.models.ChatMessage;
import com.distributed.models.Message;
import com.distributed.models.MessageType;
import com.distributed.models.ServerMessage;
import com.distributed.models.UserMessage;

public class BalancerHandler implements Runnable {

  private int port;
  private String host;
  private Socket socket;
  private ObjectInputStream inStream;
  private ObjectOutputStream outStream;

  public BalancerHandler(String host, int port) {
    this.host = host;
    this.port = port;
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

}
