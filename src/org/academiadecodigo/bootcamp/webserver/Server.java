package org.academiadecodigo.bootcamp.webserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

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
            listen();
            parseRequest();
            close(clientSocket);
        }


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
        }finally {

           // close(reader);  todo close this
        }
    }


    private void parseRequest(){

        String parsedRequest;

        System.out.println("Request: " + request);

        parsedRequest = request.split(" ")[1];
        System.out.println("Parsed request: " + parsedRequest);

        if (parsedRequest.equals("/")){
            sendResponse("/index.html");
            return;
        }

        sendResponse(parsedRequest);
    }


    private void sendResponse(String request){

        File file = null;
        BufferedReader reader = null;
        OutputStream writer = null;


        double fileSize;
        String content;
        String extension;

        System.out.println("Request is: " + request);

        try {
            file = new File("www" + request);
            System.out.println(file.toString());
            fileSize = file.length();
            System.out.println("FileSize: " + Double.toString(fileSize));
            System.out.println("Port: " + clientSocket.getPort());
            System.out.println("Socket closed? " + clientSocket.isClosed());


           /* if (!file.exists()){
                writer.print("HTTP/1.0 404 Not Found".getBytes());
                close(writer);
                close(reader);
                return;
            }*/

            fileSize = file.length();

            reader = new BufferedReader(new FileReader(file));

            writer = clientSocket.getOutputStream();
            writer.write("HTTP/1.0 200 Document Follows\r\n".getBytes());

            extension = request.substring(request.lastIndexOf("." ) +1);

            System.out.println("Content-Type: image/" + extension + " \\r\\n");

            switch (extension){

                case "html":
                    writer.write("Content-Type: text/html; charset=UTF-8\r\n".getBytes());
                    break;
                default :
                    writer.write(("Content-Type: image/" + extension + " \\r\\n\n").getBytes());
                    break;
            }


            writer.write(("Content-Length: " + Double.toString(fileSize) + " \r\n").getBytes());
            writer.write("\r\n".getBytes());

            byte[] fileContents = Files.readAllBytes(file.toPath());

            writer.write(fileContents);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            close(writer);
            close(reader);
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
