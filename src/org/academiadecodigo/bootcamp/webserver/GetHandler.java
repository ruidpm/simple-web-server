package org.academiadecodigo.bootcamp.webserver;

import java.io.*;
import java.net.Socket;

class GetHandler implements Runnable{

    private Socket clientSocket;
    private String request;

    public GetHandler(Socket clientSocket, String request){

        this.clientSocket = clientSocket;
        this.request = request;
    }

    @Override
    public void run() {

        checkResource();
    }

    
    private void checkResource(){

        File file = null;
        file = new File("www" + request);

        if (!file.exists() || file.isDirectory()){
            send404(clientSocket);
            return;
        }

        sendResponse(file, clientSocket, request);

        System.out.println("Thread: " + Thread.currentThread().getName());
    }

    private void sendResponse(File file, Socket clientSocket, String request){

        FileInputStream reader = null;
        OutputStream writer = null;
        String extension;
        int fileSize;
        int bytesRead = 0;
        byte[] buffer = new byte[4096];

        fileSize = (int) file.length();
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

                case "WEBM" :
                    writer.write(("Content-Type: video/" + extension + " \r\n").getBytes());
                    break;
            }

            // TODO: 25/06/2019 change to this String type = Files.probeContentType(file.toPath());
            
            writer.write(("Content-Length: " + Double.toString(fileSize) + " \r\n").getBytes());
            writer.write("\r\n".getBytes());

            while ((bytesRead = reader.read(buffer)) != -1) {

                    writer.write(buffer, 0, bytesRead);
            }

        } catch (IOException e) {
            System.out.println("Socket closed by client");;
        } finally {
            close(reader);
            close(writer);
            close(clientSocket);
        }
    }


    private void send404(Socket clientSocket){

        FileInputStream reader = null;
        OutputStream writer = null;
        int fileSize;
        File file = null;
        int bytesRead = 0;
        byte[] buffer = new byte[1024];

        try {

            file = new File("www/404.html");
            fileSize = (int) file.length();
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
        } finally {

            close(reader);
            close(writer);
            close(clientSocket);
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
