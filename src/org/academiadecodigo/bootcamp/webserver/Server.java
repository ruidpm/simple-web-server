package org.academiadecodigo.bootcamp.webserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    //private String request;
    private ExecutorService threadPool;


    public Server(int port){

        runServer(port);
    }


    private void runServer(int port) {

        initServer(port);
        threadPool = Executors.newFixedThreadPool(1300);

        while(true) {
            waitForConnection();
           // request = null;

            /*listen();

            if (request != null){
                parseRequest();
            }*/
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

        try {
            clientSocket = serverSocket.accept();

            threadPool.submit(new RequestHandler(clientSocket));
           // Thread thread = new Thread(new RequestHandler(clientSocket));
            //thread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*private void close(Closeable closeable){

        if (closeable == null){
            return;
        }

        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
