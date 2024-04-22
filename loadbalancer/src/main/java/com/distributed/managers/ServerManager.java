package com.distributed.managers;

import java.net.ServerSocket;

import com.distributed.balancers.ServerCache;
import com.distributed.balancers.ServerHandler;

public class ServerManager extends Thread {
  private ServerSocket serverSocket;
  private int port;

  public ServerManager(int port) {
    super("ServerManager");
    this.port = port;
  }

  public void run() {
    try {
      serverSocket = new ServerSocket(port);
      ServerHandler sH = new ServerHandler(serverSocket.accept());
      // print the server details
      System.out.println("New Server Connected from: " + sH.getServerAddress());
      ServerCache.getInstance().addServer(sH);
      sH.start();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
