package com.distributed.brokers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.distributed.models.FriendMessage;
import com.distributed.models.Message;
import com.distributed.models.User;

public class ServerHandler implements Runnable {
  private Socket socket;
  private ObjectInputStream inStream;

  public ServerHandler(Socket socket) {
    this.socket = socket;
  }

  @Override
  public void run() {
    try {
      inStream = new ObjectInputStream(this.socket.getInputStream());
    } catch (Exception e) {
      e.printStackTrace();
    }

    while (true) {
      try {
        Message msg = (Message) inStream.readObject();
        System.out.println("Received message: " + msg.getType());
        User sender = msg.getSender();
        switch (msg.getType()) {
          case GOT_FRIENDS:
            ClientCache cache = ClientCache.getInstance();
            ClientHandler cH = cache.getClient(sender.getEmail());
            cH.sendFriendResponse((FriendMessage) msg);
            break;

          default:
            break;
        }

      } catch (IOException e) {
        e.printStackTrace();
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
  }
}
