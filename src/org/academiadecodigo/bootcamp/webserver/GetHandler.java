package org.academiadecodigo.bootcamp.webserver;

import java.io.*;
import java.net.Socket;

public class GetHandler {

    public static void checkContent(String request, Socket clientSocket){

        File file = null;
        file = new File("www" + request);

        if (!file.exists()){
            send404(clientSocket);
            return;
        }

        sendResponse(file, clientSocket, request);
    }

    private static void sendResponse(File file, Socket clientSocket, String request){

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


    private static void send404(Socket clientSocket){

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

    private static void close(Closeable closeable){

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
