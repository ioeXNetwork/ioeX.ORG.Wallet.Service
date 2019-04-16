package org.ioexnetwork.ioexweb;

import org.eclipse.jetty.server.Server;


public class HttpServer {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8989);
        server.setHandler(new IOEXHandle());
        server.start();
        server.join();
    }
}


