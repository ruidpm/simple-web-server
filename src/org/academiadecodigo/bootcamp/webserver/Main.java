package org.academiadecodigo.bootcamp.webserver;

import org.academiadecodigo.bootcamp.webserver.server.Server;

public class Main {
    public static void main(String[] args) {

        Server server = new Server();
        server.runServer(9999);
    }
}
