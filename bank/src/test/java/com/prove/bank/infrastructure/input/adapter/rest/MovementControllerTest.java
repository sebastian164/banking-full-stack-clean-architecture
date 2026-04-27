package com.prove.bank.infrastructure.input.adapter.rest;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class MovementControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRejectDebitWithoutBalance() throws Exception {
        mockMvc.perform(post("/movements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "date": "2026-04-25",
                                  "type": "DEBITO",
                                  "value": -10,
                                  "accountNumber": "496825"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Saldo no disponible"));
    }

    @Test
    void shouldCreateCreditMovementAndUpdateBalance() throws Exception {
        mockMvc.perform(post("/movements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "date": "2026-04-25",
                                  "type": "CREDITO",
                                  "value": 600,
                                  "accountNumber": "225487"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.balance").value(1300));
    }

    @Test
    void shouldListMovements() throws Exception {
        mockMvc.perform(get("/movements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(0)));
    }

    @Test
    void shouldRejectDailyLimitExceeded() throws Exception {
        mockMvc.perform(post("/movements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "date": "2026-04-26",
                                  "type": "DEBITO",
                                  "value": -1001,
                                  "accountNumber": "478758"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Cupo diario excedido"));
    }

    @Test
    void shouldDeleteCreatedMovement() throws Exception {
        String response = mockMvc.perform(post("/movements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "date": "2026-04-27",
                                  "type": "CREDITO",
                                  "value": 20,
                                  "accountNumber": "496825"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = Long.valueOf(response.replaceAll(".*\\\"id\\\":(\\d+).*", "$1"));
        mockMvc.perform(delete("/movements/{id}", id))
                .andExpect(status().isNoContent());
    }
}
