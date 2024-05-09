package com.distributed.balancers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.distributed.models.Message;
import com.distributed.models.ServerMessage;

public class ServerHandler extends Thread {
  private Socket socket;
  private ObjectOutputStream outStream;
  private ObjectInputStream inStream;
  String socketAddress;

  public ServerHandler(Socket socket) {
    super("ServerHandler :: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
    this.socket = socket;
    try {
      outStream = new ObjectOutputStream(socket.getOutputStream());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public ServerHandler(String socketAddress) {
    this.socketAddress = socketAddress;
  }

  public void send(Message msg) {
    try {
      outStream.writeObject(msg);
      outStream.flush();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void run() {
    try {
      inStream = new ObjectInputStream(socket.getInputStream());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    try {
      while (true) {
        ServerMessage sMsg = (ServerMessage) inStream.readObject();
        System.out.println(sMsg);
        ServerCache.getInstance().heartBeatListener(sMsg, socket.getInetAddress().getHostAddress());
      }
    } catch (ClassNotFoundException | IOException e) {
      // TODO Auto-generated catch block
      System.out.println("Serverhandler : Server disconnected");
    }
  }

  public Socket getSocket() {
    return socket;
  }

  public String getServerAddress() {
    try {
      return InetAddress.getLocalHost().toString();
    } catch (UnknownHostException e) {
      throw new RuntimeException(e);
    }
  }

}
