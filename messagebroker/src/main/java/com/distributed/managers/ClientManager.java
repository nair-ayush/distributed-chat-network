package com.distributed.managers;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.distributed.brokers.ClientCache;
import com.distributed.brokers.ClientHandler;
import com.distributed.models.ChatMessage;
import com.distributed.models.Message;
import com.distributed.models.UserMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientManager extends WebSocketServer {
  private int port;
  private int lbPort;
  private String lbHost;
  private Socket lbSocket;
  private ObjectOutputStream lbStream;

  public ClientManager(int brokerPort, String lbHost, int lbPort) {
    super(new InetSocketAddress(brokerPort));
    this.port = brokerPort;
    this.lbHost = lbHost;
    this.lbPort = lbPort;
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

      lbSocket = new Socket(this.lbHost, this.lbPort);
      lbStream = new ObjectOutputStream(lbSocket.getOutputStream());
      if (lbSocket.isConnected()) {
        System.out.println(
            "Connected to Load Balancer at " + lbSocket.getInetAddress().getHostAddress() + ":" + lbSocket.getPort());
        System.out.println();
      }
    } catch (UnknownHostException e) {
      System.out.println("Unknown host: " + e.getMessage());
    } catch (IOException e) {
      System.out.println("Error connecting to load balancer: " + e.getMessage());
    }
  }

  private void handleUserMessage(WebSocket conn, UserMessage uMsg) {
    try {
      ClientCache cache = ClientCache.getInstance();
      ClientHandler client = cache.getClient(uMsg.getSender().getEmail());
      if (client == null) {
        client = new ClientHandler(uMsg.getSender(), conn);
        cache.addClient(uMsg.getSender().getEmail(), client);
      } else {
        client.setWebSocket(conn);
      }
      lbStream.writeObject(uMsg);
      lbStream.flush();
      lbStream.reset();
    } catch (IOException e) {
      System.out.println("Error sending message to load balancer: " + e.getMessage());
    }
  }
}
