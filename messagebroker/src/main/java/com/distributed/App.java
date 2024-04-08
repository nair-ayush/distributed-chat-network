package com.distributed;

import com.distributed.managers.ClientManager;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        System.out.println("MessageBroker Application Started");
        int brokerPort = Integer.parseInt(System.getenv("BROKER_PORT"));
        int defaultPort = 5001;
        ClientManager cM = new ClientManager((brokerPort == 0) ? defaultPort : brokerPort);
        cM.start();
    }
}
