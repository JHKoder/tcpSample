package com.github.tcp.jdk.baeldung.sample;

// GreetServer , EchoServer 와 차이점은 연결 거부 예외나 서버의 연결 재성정 없이 동일한 클라이언트가 연결을 끊었다가 다시 연결할수 있다
// 이 클래스는 여러 클라이언트의 여러 요청에 대해 더 강력하고 탄력적임

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoMultiServer {

    private ServerSocket serverSocket;

    public void start(int port)  {
        try {
            serverSocket = new ServerSocket(port);
            while (true)
                new EchoClientHandler(serverSocket.accept()).start();

        }catch (IOException  e){
            e.printStackTrace();
        }finally {
            stop();
        }
    }

    public void stop(){
        try {
            serverSocket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    //다른 스레드에서 클라이언트가 요청한 모든 새 클라이언트 및 서비스에 대해 새 소켓을 만들어 수행
    private static class EchoClientHandler extends Thread {

        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public EchoClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {

            try {

                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;

                while ((inputLine = in.readLine()) != null) {

                    if (".".equals(inputLine)) {
                        out.println("bye");
                        break;
                    }
                    out.println(inputLine);
                }

                in.close();
                out.close();
                clientSocket.close();

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public static void main(String[] args)  {
        EchoMultiServer server = new EchoMultiServer();
        server.start(5555);
    }

}
