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
      ServerHandler sH = ServerCache.getInstance().getNextServer();
      if (sH != null) {
        sH.send(defaultMessage);
        System.out.println("Message sent to server: " + sH.getServerAddress());
      } else {
        System.out.println("No server available to process the request");
      }

    } catch (IOException e) {
      System.out.println("LoadBalancer.java : Client disconnected");
    } catch (ClassNotFoundException e) {
      System.out.println("LoadBalancer.java : Client disconnected");
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
