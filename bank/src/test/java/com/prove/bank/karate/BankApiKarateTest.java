package com.prove.bank.karate;

import com.intuit.karate.junit5.Karate;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BankApiKarateTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        System.setProperty("karate.port", String.valueOf(port));
    }

    @Karate.Test
    Karate bankApi() {
        return Karate.run("bank-api").relativeTo(getClass());
    }
}
