package org.academiadecodigo.bootcamp.webserver;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client implements Runnable {


    @Override
    public void run() {

        System.out.println("Thread: " + Thread.currentThread().getName() + " sending request");
        Socket socket = null;

        try {
            Byte[] buffer = new Byte[1024];

            socket = new Socket(InetAddress.getLocalHost(), 9999);
            DataOutputStream stream = new DataOutputStream(socket.getOutputStream());
          //  ByteArrayInputStream in = new ByteArrayInputStream(socket.getInputStream());

            stream.writeBytes("GET /index.html efe");
            //System.out.println("got response: " + in.readLine());

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close(socket);
        }
    }


    public static void main(String[] args) {

        for (int i = 0; i < 32000; i++) {

            Thread thread = new Thread(new Client());
            thread.start();

           for (int j = 0; j < 32000; j++) {

                Thread thread2 = new Thread(new Client());
                thread2.start();
            }

            for (int k = 0; k < 32000; k++) {

                Thread thread4 = new Thread(new Client());
                thread4.start();
            }

            for (int l = 0; l < 32000; l++) {

                Thread thread3 = new Thread(new Client());
                thread3.start();
            }
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
