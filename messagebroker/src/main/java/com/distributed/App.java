package com.distributed;

import com.distributed.managers.ClientManager;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        System.out.println("MessageBroker Application Started");
        ClientManager cM = new ClientManager(5001);
        cM.start();
    }
}
