package com.github.tcp.jdk.baeldung.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class GreetServer {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out =  new PrintWriter(System.out);
    private BufferedReader in;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("server 연결 대기중 ");
        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String greet = in.readLine();
        System.out.println("server get "+greet);
        if ("Hello world".equals(greet)) {
            out.println("hello client");
        } else {
            out.println("unrecognised greeting");
        }
    }

    public void stop(String msg) throws IOException {
        out.print(msg);
        out.close();
        clientSocket.close();
        serverSocket.close();
    }

    public static void main(String[] args) throws IOException {
        GreetServer server = new GreetServer();
        server.start(6666);
        server.stop("clo");
    }

}
