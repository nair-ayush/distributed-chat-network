package com.distributed.fd;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Pinger implements Runnable {
    private DatagramSocket socket;
    private String message = "PING";

    public Pinger() {
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            byte[] buffer = message.getBytes();
            DatagramPacket packet = null;
            try {
                packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("255.255.255.255"), 8888);
                socket.send(packet);
                Thread.sleep(2000);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
