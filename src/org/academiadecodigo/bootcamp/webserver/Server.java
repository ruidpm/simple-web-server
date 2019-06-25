package org.academiadecodigo.bootcamp.webserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private String request;


    public Server(int port){

        runServer(port);
    }


    private void runServer(int port) {

        initServer(port);

        while(true) {
            waitForConnection();
            request = null;
            listen();

            if (request != null){
                parseRequest();
            }
        }
        //close(clientSocket);
       // close(serverSocket);
    }


    private void initServer(int port) {

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void waitForConnection(){

        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void listen(){

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            request = reader.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void parseRequest(){

        String verb;

        if (request.split(" ").length < 3){
            return;
        }

        verb = request.split(" ")[0];
        request = request.split(" ")[1];

        if (request.equals("/")){
            request = "/index.html";
        }

        if (verb.toUpperCase().equals("GET")){
            GetHandler.checkContent(request, clientSocket);
        }
    }


    private void close(Closeable closeable){

        if (closeable == null){
            return;
        }

        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
