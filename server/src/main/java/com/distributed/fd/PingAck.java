package com.distributed.fd;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.distributed.handlers.BalancerHandler;
import com.distributed.models.MessageType;
import com.distributed.models.ServerMessage;

public class PingAck implements Runnable {
    private BalancerHandler bH;
    private ServerCache serverCache;

    public PingAck(BalancerHandler bH) {
        this.bH = bH;
        this.serverCache = ServerCache.getInstance();
        Thread pingThread = new Thread(new Pinger());
        pingThread.start();
        Thread ackThread = new Thread(new Acknowledger());
        ackThread.start();
    }

    @Override
    public void run() {
        while (true) {
            Map<String, Long> ipToTime = serverCache.getMap();
            for (Map.Entry<String, Long> entry : ipToTime.entrySet()) {
                String key = entry.getKey();
                Long value = entry.getValue();

                long timeSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());

                if (timeSeconds - value > 5) {
                    serverCache.remove(key);
                    bH.send(new ServerMessage(key, MessageType.SERVER_LEFT));
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("PingAck thread interrupted.");
                e.printStackTrace();
            }

        }
    }

}
