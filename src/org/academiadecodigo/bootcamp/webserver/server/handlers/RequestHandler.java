package org.academiadecodigo.bootcamp.webserver.server.handlers;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class RequestHandler implements Runnable{

    private Socket clientSocket;
    private String request;


    public RequestHandler(Socket clientSocket){

        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

        listen();
        parseRequest();
    }


    private void listen(){

        BufferedReader reader = null;

        System.out.println("Active threads: " + Thread.activeCount());

        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            request = reader.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void parseRequest(){

        String verb;

        if (request == null){
            close(clientSocket);
            return;
        }

        if (request.split(" ").length < 3){

            close(clientSocket);
            return;
        }

        verb = request.split(" ")[0];
        request = request.split(" ")[1];

        if (request.equals("/")){
            request = "/index.html";
        }

        if (verb.toUpperCase().equals("GET")){

            new GetHandler(clientSocket, request);
            return;
        }

        close(clientSocket);
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
