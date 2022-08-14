package com.github.tcp.jdk.baeldung;

import com.github.tcp.jdk.baeldung.sample.GreetClient;
import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class GreetSampleTest {

    @Test
    void givenGreetClient_whenServerRespondsWhenStarted_thenCorrect() throws IOException {
        GreetClient client = new GreetClient();
        client.startConnection("127.0.0.1",6666);
        String response = client.sendMessage("Hello world");
        Assertions.assertThat(response).isEqualTo("hello client");
    }

}
