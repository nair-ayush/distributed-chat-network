package com.distributed.fd;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServerCache {
    private static ServerCache instance = null;
    private Map<String, Long> ipToTime;
    private final Lock cacheLock = new ReentrantLock();

    public static ServerCache getInstance() {
        if (instance == null) {
            instance = new ServerCache();
        }
        return instance;
    }

    private ServerCache() {
        ipToTime = new HashMap<String, Long>();

    }

    public void add(String ip) {
        cacheLock.lock();
        ipToTime.put(ip, TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
        cacheLock.unlock();
    }

    public void remove(String ip) {
        cacheLock.lock();
        ipToTime.remove(ip);
        cacheLock.unlock();
    }

    public boolean contains(String ip) {
        cacheLock.lock();
        boolean contains = ipToTime.containsKey(ip);
        cacheLock.unlock();
        return contains;
    }

    public Map<String, Long> getMap() {
        return ipToTime;
    }
}
