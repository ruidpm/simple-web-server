package org.academiadecodigo.bootcamp.webserver.server;

import org.academiadecodigo.bootcamp.webserver.server.handlers.RequestHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private ServerSocket serverSocket;
    private ExecutorService threadPool;


    public Server(){

    }


    public void runServer(int port) {

        initServer(port);
        threadPool = Executors.newFixedThreadPool(1300);

        while(true) {
            waitForConnection();
        }
    }


    private void initServer(int port) {

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server running");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void waitForConnection(){

        Socket clientSocket;

        try {
            clientSocket = serverSocket.accept();

            threadPool.submit(new RequestHandler(clientSocket));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
