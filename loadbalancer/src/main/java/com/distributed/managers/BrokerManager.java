package com.distributed.managers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.distributed.balancers.LoadBalancer;

public class BrokerManager extends Thread {
  private int port;
  private ServerSocket serverSocket;

  public BrokerManager(int portNumber) {
    super("BrokerManager");
    port = portNumber;
    try {
      serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void run() {
    while (true) {
      try {
        Socket socket = serverSocket.accept();
        LoadBalancer loadBalancer = new LoadBalancer(socket);
        loadBalancer.start();
      } catch (IOException e) {
        System.out.println("I/O error: " + e);
      }
    }
  }

}
