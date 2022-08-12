package com.github.tcp.jdk.bealdung;

import com.github.tcp.jdk.bealdung.sample.EchoClient;
import com.github.tcp.jdk.bealdung.sample.EchoMultiServer;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EchoMultiSampleTest {

    private static int port;

    @BeforeEach
    void start() throws IOException, InterruptedException {
        ServerSocket s = new ServerSocket(0);
        port = s.getLocalPort();
        s.close();

        Executors.newSingleThreadExecutor().submit(() -> new EchoMultiServer().start(port));
        Thread.sleep(500);
    }

    @Test
    void givenClient1_whenServerResponds_thenCorrect() throws IOException {
        EchoClient client = new EchoClient();
        client.startConnection("127.0.0.1", port);
        String msg1 = client.sendMessage("hello");
        String msg2 = client.sendMessage("world");
        String terminate = client.sendMessage(".");

        Assertions.assertThat(msg1).isEqualTo("hello");
        Assertions.assertThat(msg2).isEqualTo("world");
        Assertions.assertThat(terminate).isEqualTo("bye");
    }

    @Test
    void givenClient2_whenServerResponds_thenCorrect() throws IOException {
        EchoClient client = new EchoClient();
        client.startConnection("127.0.0.1", port);
        String msg1 = client.sendMessage("hello");
        String msg2 = client.sendMessage("world");
        String terminate = client.sendMessage(".");

        Assertions.assertThat(msg1).isEqualTo("hello");
        Assertions.assertThat(msg2).isEqualTo("world");
        Assertions.assertThat(terminate).isEqualTo("bye");
    }

    @Test
    void givenClient3_whenServerResponds_thenCorrect() throws IOException {
        EchoClient client = new EchoClient();
        client.startConnection("127.0.0.1", port);
        String msg1 = client.sendMessage("hello");
        String msg2 = client.sendMessage("world");
        String terminate = client.sendMessage(".");

        Assertions.assertThat(msg1).isEqualTo("hello");
        Assertions.assertThat(msg2).isEqualTo("world");
        Assertions.assertThat(terminate).isEqualTo("bye");
    }


}
