package com.distributed;

import com.distributed.db.DB;
import com.distributed.handlers.BalancerHandler;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        System.out.println("---------------------------------");
        DB database = DB.getInstance();
        // port at which server will communicate with LoadBalancer for fault tolerance
        // and resource discovery
        int lbServerPort = Integer.parseInt(System.getenv("LOAD_BALANCER_SERVER_PORT"));
        int lbDefaultServerPort = 8000;
        String lbServerHost = System.getenv("LOAD_BALANCER_SERVER_HOST");
        String defaultHost = "localhost";
        BalancerHandler bH = new BalancerHandler(
                (lbServerHost.isEmpty())
                        ? defaultHost
                        : lbServerHost,
                (lbServerPort == 0)
                        ? lbDefaultServerPort
                        : lbServerPort);
        new Thread(bH).start();
        System.out.println("Server: BalancerHandler started at port "
                + ((lbServerPort == 0) ? lbDefaultServerPort : lbServerPort));

        System.out.println("Server Application Started");
        System.out.println("---------------------------------");

        System.out.println(database.isConnected() ? "Connected to MongoDB" : "Error connecting to MongoDB");
    }
}
