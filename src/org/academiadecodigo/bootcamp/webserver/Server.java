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
                checkContent();
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

        request = request.split(" ")[1];

        if (request.equals("/")){
            request = "/index.html";
        }
    }


    private void checkContent(){

        File file = null;
        file = new File("www" + request);

        if (!file.exists()){
            send404();
            return;
        }

        sendResponse(file);
    }


    private void sendResponse(File file){

        FileInputStream reader = null;
        OutputStream writer = null;
        String extension;
        double fileSize;
        int bytesRead = 0;
        byte[] buffer = new byte[1024];

        fileSize = file.length();
        try {
            writer = clientSocket.getOutputStream();
            reader = new FileInputStream(file);

            writer.write("HTTP/1.0 200 Document Follows\r\n".getBytes());

            extension = request.substring(request.lastIndexOf("." ) +1);

            switch (extension.toUpperCase()){

                case "HTML":
                    writer.write("Content-Type: text/html; charset=UTF-8\r\n".getBytes());
                    break;

                case "TXT":
                    writer.write("Content-Type: text/html; charset=UTF-8\r\n".getBytes());
                    break;

                case "JPG" :
                    writer.write(("Content-Type: image/" + extension + " \r\n").getBytes());
                    break;

                case "GIF" :
                    writer.write(("Content-Type: image/" + extension + " \r\n").getBytes());
                    break;
            }

            writer.write(("Content-Length: " + Double.toString(fileSize) + " \r\n").getBytes());
            writer.write("\r\n".getBytes());

            while ((bytesRead = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, bytesRead);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close(reader);
            close(writer);
        }
    }


    private void send404(){

        FileInputStream reader = null;
        OutputStream writer = null;
        double fileSize;
        File file = null;
        int bytesRead = 0;
        byte[] buffer = new byte[1024];

        try {

            file = new File("www/404.html");
            fileSize = file.length();
            reader = new FileInputStream(file);
            writer = clientSocket.getOutputStream();

            writer.write("HTTP/1.0 404 Not Found".getBytes());
            writer.write("Content-Type: text/html; charset=UTF-8\r\n".getBytes());
            writer.write(("Content-Length: " + Double.toString(fileSize) + " \r\n").getBytes());
            writer.write("\r\n".getBytes());

            while ((bytesRead = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, bytesRead);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            close(reader);
            close(writer);
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
