package com.github.tcp.jdk.youtube.neuralnine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    private final ArrayList<ConnectionHandler> connections;
    private ServerSocket server;
    private boolean done;
    private ExecutorService pool;


    public Server() {
        connections = new ArrayList<>();
        done = false;
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(9999);
            pool = Executors.newCachedThreadPool();
            while (!done) {
                Socket client = server.accept();
                // 연결하고싶은 클라이언트
                ConnectionHandler handler = new ConnectionHandler(client);
                connections.add(handler);
                pool.execute(handler);
            }
        } catch (IOException e) {
            // TODO: handle
            shutdown();
        }
    }

    public void broadcast(String message) {
        for (ConnectionHandler ch : connections) {
            if (ch != null) {
                ch.sendMessage(message);
            }
        }
    }

    public void shutdown() {
        try {
            done = true;
            pool.shutdown();
            if (!server.isClosed()) {
                server.close();
            }
            for (ConnectionHandler ch : connections) {
                ch.shutdown();
            }
        } catch (IOException e) {
            // ignore
        }

    }

    class ConnectionHandler implements Runnable {

        private Socket client;
        private BufferedReader in;
        private PrintWriter out;
        private String nickname;

        public ConnectionHandler(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                // 클라에서 직접 얻을수 있는 출력 스트림
                // autoFlash 를 True 로 설정하면 실제로 메시지를 보내기위해 항상 수동으로 출력 스트림을 플러시할 필요가 없다
                out = new PrintWriter(client.getOutputStream(), true);

                //입력 스트림은 클라이언트에서 얻는것 이다.
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                // 메시지를 받고싶다면
                // 닉네임을 뭍는 프롬
                out.println("please enter a nickname: ");
                nickname = in.readLine();
                System.out.println(nickname + " connected!");
                broadcast(nickname + " joined the char!");
                String message;

                while ((message = in.readLine()) != null) {
                    // 특정 명렁어로 시작하는 경우
                    // 이름 변경 명령어
                    if (message.startsWith("/nick ")) {
                        // TODO: handle nickname
                        String[] messageSplit = message.split(" ", 2);
                        if (messageSplit.length == 2) {
                            broadcast(nickname + " renamed themselves to " + messageSplit[1]);
                            System.out.println(nickname + " renamed themselves to " + messageSplit[1]);
                            nickname = messageSplit[1];
                            out.println("Successfully changed nickname to " + nickname);
                        } else {
                            out.println("No nickname provided!");
                        }
                    } else if (message.startsWith("/quit")) {
                        // TODO: quit
                        broadcast(nickname + " left the char!");
                        shutdown();
                    } else {
                        broadcast(nickname + " : " + message);
                    }

                }
            } catch (IOException e) {
                // TODO : handle
                shutdown();
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }

        public void shutdown() {
            try {
                in.close();
                out.close();
                if (!client.isClosed()) {
                    client.close();
                }
            } catch (IOException e) {
                // ignore
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}
