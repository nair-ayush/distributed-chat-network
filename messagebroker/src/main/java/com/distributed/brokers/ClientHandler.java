package com.distributed.brokers;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.java_websocket.WebSocket;

import com.distributed.models.ChatMessage;
import com.distributed.models.ClientState;
import com.distributed.models.FriendMessage;
import com.distributed.models.Message;
import com.distributed.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientHandler implements Runnable {
  private User user;
  private WebSocket webSocket;
  private ClientState state;
  private static final int CAPACITY = 1000;
  private Queue<Message> mQ;

  private final Lock qLock = new ReentrantLock();
  private final Lock sockLock = new ReentrantLock();
  private final Condition queueNotFull = qLock.newCondition();
  private final Condition queueNotEmpty = qLock.newCondition();
  private final Condition sockActive = sockLock.newCondition();

  public ClientHandler(User user) {
    this.user = user;
    state = ClientState.INACTIVE;
    mQ = new LinkedList<Message>();
  }

  public ClientHandler(User user, WebSocket webSocket) {
    this.user = user;
    this.webSocket = webSocket;
    state = ClientState.ACTIVE;
    mQ = new LinkedList<Message>();
  }

  public void setWebSocket(WebSocket webSocket) {
    sockLock.lock();
    try {
      this.webSocket = webSocket;
      state = ClientState.ACTIVE;
      sockActive.signalAll();
    } finally {
      sockLock.unlock();
    }
  }

  public void removeWebSocket() {
    sockLock.lock();
    try {
      this.webSocket.close();
      this.webSocket = null;
      state = ClientState.INACTIVE;
    } finally {
      sockLock.unlock();
    }
  }

  public void sendFriendResponse(FriendMessage msg) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      String messageJson = mapper.writeValueAsString(msg);
      sockLock.lock();
      webSocket.send(messageJson);
      System.out.println("Sent friend response to UI: " + user.getEmail());
      sockLock.unlock();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    ObjectMapper mapper = new ObjectMapper();
    while (true) {
      qLock.lock();
      try {
        while (mQ.isEmpty()) {
          queueNotFull.await();
        }
        sockLock.lock();
        while (this.state == ClientState.INACTIVE) {
          sockActive.await();
        }

        ChatMessage cMsg = (ChatMessage) mQ.element();

        if (webSocket != null) {
          String messageJson = mapper.writeValueAsString(cMsg);
          webSocket.send(messageJson);
          mQ.poll();
        }
        queueNotEmpty.signalAll();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        qLock.unlock();
        sockLock.unlock();
      }
    }
  }

}
