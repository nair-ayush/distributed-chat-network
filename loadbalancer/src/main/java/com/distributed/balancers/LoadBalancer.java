package com.distributed.balancers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.distributed.models.Message;

public class LoadBalancer extends Thread {
  private Socket socket;
  private ObjectInputStream inStream;

  public LoadBalancer(Socket socket) {
    this.socket = socket;
  }

  public void loadProcessing() {
    try {
      Message defaultMessage = (Message) inStream.readObject();
      if (defaultMessage == null) {
        socket.close();
      }
      String clientEmail = defaultMessage.getSender().getEmail();
      System.out.println("Received message from: " + clientEmail + " with type: " + defaultMessage.getType());

    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public void run() {
    try {
      inStream = new ObjectInputStream(socket.getInputStream());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    while (true) {
      loadProcessing();
    }
  }

}
