package com.distributed.managers;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.distributed.models.ChatMessage;
import com.distributed.models.Message;
import com.distributed.models.UserMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientManager extends WebSocketServer {
  private int port;
  private Socket lbSocket;
  private ObjectOutputStream lbStream;

  public ClientManager(int port) {
    super(new InetSocketAddress(port));
    this.port = port;
  }

  @Override
  public void onOpen(WebSocket conn, ClientHandshake handshake) {

    System.out.println("New connection from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
  }

  @Override
  public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    System.out.println("Connection closed by " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
  }

  @Override
  public void onMessage(WebSocket conn, String message) {
    // System.out.println(
    // "Message from " +
    // conn.getRemoteSocketAddress().getAddress().getHostAddress());
    ObjectMapper mapper = new ObjectMapper();

    try {
      Message msg = mapper.readValue(message, Message.class);
      switch (msg.getType()) {
        case REGISTER:
        case LOGIN:
        case LOGOUT:
          UserMessage uMsg = mapper.readValue(message, UserMessage.class);
          System.out.println(uMsg);
          handleUserMessage(conn, uMsg);
          break;

        case CHAT_MESSAGE:
          ChatMessage cMsg = mapper.readValue(message, ChatMessage.class);
          System.out.println(cMsg);
          System.out.println("Valid type");
          break;

        default:
          System.out.println("Invalid type");
          break;
      }
    } catch (JsonProcessingException e) {
      System.out.println("Error parsing message" + e.getMessage());
    }

  }

  @Override
  public void onError(WebSocket conn, Exception ex) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onStart() {
    System.out.println("WebSocketServer started on " + port);
    try {
      String loadBalancerHost = System.getenv("LOAD_BALANCER_HOST");
      String defaultLBHost = "localhost";
      int loadBalancerPort = Integer.parseInt(System.getenv("LOAD_BALANCER_PORT"));
      int defaultLBPort = 5002;
      lbSocket = new Socket(
          loadBalancerHost.isEmpty()
              ? defaultLBHost
              : loadBalancerHost,
          loadBalancerPort == 0
              ? defaultLBPort
              : loadBalancerPort);
      lbStream = new ObjectOutputStream(lbSocket.getOutputStream());
      if (lbSocket.isConnected()) {
        System.out.println(
            "Connected to Load Balancer at " + lbSocket.getInetAddress().getHostAddress() + ":" + lbSocket.getPort());
      }
    } catch (UnknownHostException e) {
      System.out.println("Unknown host: " + e.getMessage());
    } catch (IOException e) {
      System.out.println("Error connecting to load balancer: " + e.getMessage());
    }
  }

  private void handleUserMessage(WebSocket conn, UserMessage uMsg) {
    try {
      lbStream.writeObject(uMsg);
      lbStream.flush();
      lbStream.reset();
    } catch (IOException e) {
      System.out.println("Error sending message to load balancer: " + e.getMessage());
    }
  }
}
