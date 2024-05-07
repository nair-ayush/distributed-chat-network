package com.distributed.brokers;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientCache {
  private HashMap<String, ClientHandler> clientCache;
  private HashMap<String, Thread> clientThreads;
  private HashMap<String, ClientHandler> ipHandlers;
  private final Lock cacheLock = new ReentrantLock();
  private static ClientCache instance;

  public ClientCache() {
    clientCache = new HashMap<String, ClientHandler>();
    clientThreads = new HashMap<String, Thread>();
    ipHandlers = new HashMap<String, ClientHandler>();
  }

  public static ClientCache getInstance() {
    if (instance == null) {
      instance = new ClientCache();
    }
    return instance;
  }

  public void addClient(String username, ClientHandler client, String ip) {
    cacheLock.lock();
    try {
      Thread t = new Thread(client);
      t.start();
      clientCache.put(username, client);
      ipHandlers.put(ip, client);
      clientThreads.put(username, t);
    } finally {
      cacheLock.unlock();
    }
  }

  public void removeClient(String ip) {
    cacheLock.lock();
    try {
      ClientHandler cH = ipHandlers.get(ip);
      cH.removeWebSocket();

    } finally {
      cacheLock.unlock();
    }
  }

  public ClientHandler getClient(String email) {
    cacheLock.lock();
    try {
      return clientCache.get(email);
    } catch (Exception e) {
      System.out.println("Client not found" + e.getMessage());
      return null;
    } finally {
      cacheLock.unlock();
    }
  }

}
