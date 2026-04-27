package com.prove.bank.infrastructure.input.adapter.rest;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateAccountForExistingClient() throws Exception {
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "number": "900101",
                                  "type": "Corriente",
                                  "initialBalance": 250,
                                  "status": true,
                                  "clientId": 1
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.number").value("900101"))
                .andExpect(jsonPath("$.currentBalance").value(250));
    }

    @Test
    void shouldListAccounts() throws Exception {
        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(5)));
    }

    @Test
    void shouldPatchAccountStatus() throws Exception {
        mockMvc.perform(patch("/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "status": false
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(false));
    }

    @Test
    void shouldDeleteCreatedAccount() throws Exception {
        String response = mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "number": "900102",
                                  "type": "Ahorro",
                                  "initialBalance": 50,
                                  "status": true,
                                  "clientId": 1
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = Long.valueOf(response.replaceAll(".*\\\"id\\\":(\\d+).*", "$1"));
        mockMvc.perform(delete("/accounts/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnControlledErrorWhenAccountHasMovements() throws Exception {
        String response = mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "number": "900103",
                                  "type": "Ahorro",
                                  "initialBalance": 150,
                                  "status": true,
                                  "clientId": 1
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = Long.valueOf(response.replaceAll(".*\\\"id\\\":(\\d+).*", "$1"));

        mockMvc.perform(post("/movements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "accountNumber": "900103",
                                  "type": "Retiro",
                                  "value": -25
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/accounts/{id}", id))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("No se puede eliminar la cuenta porque tiene movimientos asociados"));
    }

    @Test
    void shouldReturnControlledErrorWhenPayloadIsInvalid() throws Exception {
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "type": "Ahorro",
                                  "initialBalance": 50,
                                  "status": true,
                                  "clientId": 1
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Solicitud invalida"))
                .andExpect(jsonPath("$.details[0]").exists());
    }
}
