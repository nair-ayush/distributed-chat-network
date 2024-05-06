package com.distributed.handlers;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.distributed.models.Message;

public class BrokerHandler {
  private Socket socket;
  private ObjectOutputStream brokerOutputStream;
  private static BrokerHandler brokerInstance = null;

  static BrokerHandler getInstance() {
    if (brokerInstance == null) {
      brokerInstance = new BrokerHandler();
    }
    return brokerInstance;
  }

  public BrokerHandler() {
    try {
      int brokerServerPort = Integer.parseInt(System.getenv("BROKER_SERVER_PORT"));
      int defaultBrokerServerPort = 5003;
      String brokerHost = System.getenv("BROKER_HOST");
      String defaultBrokerHost = "localhost";
      socket = new Socket(
          (brokerHost.isEmpty())
              ? defaultBrokerHost
              : brokerHost,
          (brokerServerPort == 0)
              ? defaultBrokerServerPort
              : brokerServerPort);
      brokerOutputStream = new ObjectOutputStream(socket.getOutputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void send(Message msg) {
    System.out.println("Sending message to broker " + msg.getSender().getEmail());
    try {
      brokerOutputStream.writeObject(msg);
      brokerOutputStream.flush();
      brokerOutputStream.reset();
    } catch (IOException e) {

    }
  }

}
