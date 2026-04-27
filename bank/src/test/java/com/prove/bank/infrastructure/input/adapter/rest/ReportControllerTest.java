package com.prove.bank.infrastructure.input.adapter.rest;

import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ReportControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGenerateJsonReportWithMovements() throws Exception {
        mockMvc.perform(post("/movements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "date": "2026-04-26",
                                  "type": "CREDITO",
                                  "value": 75,
                                  "accountNumber": "225487"
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/reports")
                        .param("clientId", "2")
                        .param("startDate", "2026-04-01")
                        .param("endDate", "2026-04-30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientName").value("Marianela Montalvo"))
                .andExpect(jsonPath("$.totalCredits").exists())
                .andExpect(jsonPath("$.pdfBase64", not(blankOrNullString())))
                .andExpect(jsonPath("$.statements.length()", greaterThanOrEqualTo(1)));
    }

    @Test
    void shouldDownloadPdfReport() throws Exception {
        mockMvc.perform(get("/reports/pdf")
                        .param("clientId", "2")
                        .param("startDate", "2026-01-01")
                        .param("endDate", "2026-12-31"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"account-statement.pdf\""));
    }
}
