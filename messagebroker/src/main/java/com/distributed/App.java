package com.distributed;

import com.distributed.managers.ClientManager;
import com.distributed.managers.ServerManager;

/**
 * Hello world!
 *
 */
public class App {
        public static void main(String[] args) {
                System.out.println("----------------------------------");
                int serverPort = Integer.parseInt(System.getenv("BROKER_SERVER_PORT"));
                int defaultServerPort = 5003;

                ServerManager sM = new ServerManager(
                                (serverPort == 0)
                                                ? defaultServerPort
                                                : serverPort);
                new Thread(sM).start();

                int brokerPort = Integer.parseInt(System.getenv("BROKER_PORT"));
                int defaultBrokerPort = 5001;

                String loadBalancerHost = System.getenv("LOAD_BALANCER_HOST");
                String defaultLBHost = "localhost";
                int loadBalancerPort = Integer.parseInt(System.getenv("LOAD_BALANCER_PORT"));
                int defaultLBPort = 5002;
                ClientManager cM = new ClientManager(
                                (brokerPort == 0)
                                                ? defaultBrokerPort
                                                : brokerPort,
                                (loadBalancerHost.isEmpty())
                                                ? defaultLBHost
                                                : loadBalancerHost,
                                (loadBalancerPort == 0)
                                                ? defaultLBPort
                                                : loadBalancerPort);
                cM.start();
                System.out.println("MessageBroker Application Started");
        }
}
