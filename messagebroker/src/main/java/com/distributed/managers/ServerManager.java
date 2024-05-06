package com.distributed.managers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.distributed.brokers.ServerHandler;

public class ServerManager implements Runnable {
  private int port;
  private ServerSocket serverSocket;
  private Socket socket;

  public ServerManager(int port) {
    this.port = port;
    try {
      serverSocket = new ServerSocket(this.port);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void run() {
    while (true) {
      try {
        socket = serverSocket.accept();
        System.out.println("MessageBroker : Server connected");
        System.out.println("----------------------------------");
      } catch (IOException e) {
        System.out.println("I/O error: " + e);
      }
      // new thread for a client
      new Thread(new ServerHandler(socket)).start();
    }
  }
}
