package com.distributed.fd;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Acknowledger implements Runnable {
    private DatagramSocket socket;

    public Acknowledger() {
        try {
            socket = new DatagramSocket(8888);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            byte[] buffer = new byte[8];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                if (message.equals("PING")) {
                    String ip = packet.getAddress().getHostAddress();
                    ServerCache.getInstance().add(ip);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
