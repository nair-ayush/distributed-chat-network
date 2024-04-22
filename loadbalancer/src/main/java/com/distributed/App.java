package com.distributed;

import com.distributed.managers.BrokerManager;
import com.distributed.managers.ServerManager;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {

        System.out.println("---------------------------------");
        // Port at which LoadBalancer will communicate with Server for fault tolerance
        // and resource management
        int serverPort = Integer.parseInt(System.getenv("LOAD_BALANCER_SERVER_PORT"));
        int defaultServerPort = 8000;
        ServerManager sM = new ServerManager((serverPort == 0) ? defaultServerPort : serverPort);
        sM.start();
        System.out.println(
                "LoadBalancer: ServerManager started at port " + ((serverPort == 0) ? defaultServerPort : serverPort));

        // Port at which LoadBalancer will listen to incoming requests from
        // MessageBroker
        int brokerPort = Integer.parseInt(System.getenv("LOAD_BALANCER_PORT"));
        int defaultBrokerPort = 5002;
        BrokerManager bM = new BrokerManager((brokerPort == 0) ? defaultBrokerPort : brokerPort);
        bM.start();
        System.out.println(
                "LoadBalancer: BrokerManager started at port " + ((brokerPort == 0) ? defaultBrokerPort : brokerPort));

        System.out.println("LoadBalancer Application Started");
        System.out.println("---------------------------------");
    }
}
