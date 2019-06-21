package org.academiadecodigo.bootcamp.webserver;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;
    private Socket clientSocket;

    public Server(int port){

        runServer(port);
    }


    private void runServer(int port) {

        initServer(port);

        waitConnection();
        listen();


        close(clientSocket);

        close(serverSocket);
    }


    private void initServer(int port) {

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void waitConnection(){

        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void listen(){

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println(reader.readLine());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void close(Closeable closeable){

        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
