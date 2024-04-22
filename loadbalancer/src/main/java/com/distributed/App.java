package com.distributed;

import com.distributed.managers.BrokerManager;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        System.out.println("LoadBalancer Application Started");

        int brokerPort = Integer.parseInt(System.getenv("LOAD_BALANCER_PORT"));
        int defaultPort = 5002;
        BrokerManager bM = new BrokerManager((brokerPort == 0) ? defaultPort : brokerPort);
        bM.start();
    }
}
