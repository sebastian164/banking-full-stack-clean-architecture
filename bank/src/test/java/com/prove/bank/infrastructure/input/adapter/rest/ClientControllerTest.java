package com.prove.bank.infrastructure.input.adapter.rest;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
class ClientControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateClient() throws Exception {
        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Ana Ruiz",
                                  "gender": "Femenino",
                                  "age": 29,
                                  "identification": "0102030405",
                                  "address": "Quito",
                                  "phone": "0991111111",
                                  "password": "1234",
                                  "status": true
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Ana Ruiz"));
    }

    @Test
    void shouldListSeededClients() throws Exception {
        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(3)));
    }

    @Test
    void shouldGetClientById() throws Exception {
        mockMvc.perform(get("/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jose Lema"))
                .andExpect(jsonPath("$.phone").value("098254785"))
                .andExpect(jsonPath("$.password").value("1234"));
    }

    @Test
    void shouldPatchClientAddress() throws Exception {
        mockMvc.perform(patch("/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "address": "Nueva direccion"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("Nueva direccion"));
    }

    @Test
    void shouldUpdateClient() throws Exception {
        mockMvc.perform(put("/clients/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Juan Osorio Actualizado",
                                  "gender": "Masculino",
                                  "age": 36,
                                  "identification": "1717171717",
                                  "address": "Quito",
                                  "phone": "098874587",
                                  "password": "1245",
                                  "status": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Juan Osorio Actualizado"))
                .andExpect(jsonPath("$.age").value(36));
    }

    @Test
    void shouldDeleteCreatedClient() throws Exception {
        String response = mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Cliente Temporal",
                                  "gender": "Otro",
                                  "age": 22,
                                  "identification": "9990001112",
                                  "address": "Temporal",
                                  "phone": "0990001112",
                                  "password": "1234",
                                  "status": true
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = Long.valueOf(response.replaceAll(".*\\\"id\\\":(\\d+).*", "$1"));
        mockMvc.perform(delete("/clients/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnControlledErrorWhenClientHasAccounts() throws Exception {
        String clientResponse = mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Cliente Con Cuenta",
                                  "gender": "Otro",
                                  "age": 31,
                                  "identification": "8880001112",
                                  "address": "Temporal",
                                  "phone": "0980001112",
                                  "password": "1234",
                                  "status": true
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long clientId = Long.valueOf(clientResponse.replaceAll(".*\\\"id\\\":(\\d+).*", "$1"));

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "number": "9999990001",
                                  "type": "Ahorros",
                                  "initialBalance": 200,
                                  "currentBalance": 200,
                                  "status": true,
                                  "clientId": %d
                                }
                                """.formatted(clientId)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/movements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "accountNumber": "9999990001",
                                  "type": "Retiro",
                                  "value": -50
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/clients/{id}", clientId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("No se puede eliminar el cliente porque tiene cuentas asociadas"));
    }
}
