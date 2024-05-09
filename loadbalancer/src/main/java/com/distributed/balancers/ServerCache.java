package com.distributed.balancers;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.distributed.models.MessageType;
import com.distributed.models.ServerMessage;

public class ServerCache {
  private HashMap<Integer, ServerHandler> serverCache;
  private HashMap<String, Integer> serverStatus;
  private final Lock cacheLock = new ReentrantLock();
  private static ServerCache instance;
  static int id = 0;
  private int roundRobinCounter = 0;

  private ServerCache() {
    serverCache = new HashMap<>();
    serverStatus = new HashMap<>();
  }

  public static ServerCache getInstance() {
    if (instance == null) {
      instance = new ServerCache();
    }
    return instance;
  }

  public HashMap<Integer, ServerHandler> getServerCache() {
    return serverCache;
  }

  public ServerHandler getNextServer() {
    ServerHandler server = null;
    cacheLock.lock();
    if (serverCache.size() > 0) {
      server = serverCache.get(roundRobinCounter);
      roundRobinCounter = (roundRobinCounter + 1) % serverCache.size();
    }
    cacheLock.unlock();
    return server;
  }

  public void addServer(ServerHandler handler) {
    cacheLock.lock();
    serverCache.put(id++, handler);
    cacheLock.unlock();
  }

  public void refreshServerStatus() {
    cacheLock.lock();
    serverStatus.clear();
    for (Map.Entry<String, Integer> server : serverStatus.entrySet()) {
      String key = server.getKey();
      serverStatus.put(key, 0);
    }
    cacheLock.unlock();
  }

  public void initiateServerStatus() {
    serverStatus = new HashMap<String, Integer>();
    for (Map.Entry<Integer, ServerHandler> server : serverCache.entrySet()) {
      String key = server.getValue().socketAddress;
      serverStatus.put(key, 0);
    }
  }

  public void initiateServerMapping() {
    serverCache = new HashMap<Integer, ServerHandler>();
  }

  @SuppressWarnings("unlikely-arg-type")
  public void removeServer(String socketAddress) {
    int exitedNodeID = -1;
    if (serverCache.containsValue(socketAddress)) {
      for (Map.Entry<Integer, ServerHandler> entry : serverCache.entrySet()) {
        if (Objects.equals(entry.getValue().getServerAddress(), socketAddress)) {
          exitedNodeID = entry.getKey();
        }
      }
      if (exitedNodeID != -1) {
        cacheLock.lock();
        serverCache.remove(exitedNodeID);
        cacheLock.unlock();
      }
    }
  }

  @SuppressWarnings("unlikely-arg-type")
  public void heartBeatListener(ServerMessage sMsg, String socketAddress) {
    if (serverCache.containsValue(socketAddress)) {
      if (sMsg.getType().equals(MessageType.SERVER_JOINED)) {
        addServer(new ServerHandler(sMsg.getMessage()));
      } else if (sMsg.getType().equals(MessageType.SERVER_EXITED)) {
        System.out.println("ServerCache : Server exited");
        int statusSoFar = serverStatus.get(sMsg.getMessage());
        if (statusSoFar == serverCache.size() - 1) {
          removeServer(sMsg.getMessage());
        } else {
          serverStatus.put(sMsg.getMessage(), statusSoFar + 1);
        }
      }
    }
  }

}
