package com.github.tcp.jdk.bealdung;

import com.github.tcp.jdk.bealdung.sample.EchoClient;
import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EchoSampleTest {

    EchoClient client;

    @BeforeEach
    public void setup() throws IOException {
        client = new EchoClient();
        client.startConnection("127.0.0.1", 4444);
    }


    @AfterEach
    void tearDown() throws IOException {
        client.stopConnection();
    }

    @Test
    void givenClient_when_serverEchoMessage_thenCorrect() throws IOException {
        String resp1 = client.sendMessage("hello");
        String resp2 = client.sendMessage("world");
        String resp3 = client.sendMessage("!");
        String resp4 = client.sendMessage(".");

        Assertions.assertThat(resp1).isEqualTo("hello");
        Assertions.assertThat(resp2).isEqualTo("world");
        Assertions.assertThat(resp3).isEqualTo("!");
        Assertions.assertThat(resp4).isEqualTo("good bye");

    }


}
