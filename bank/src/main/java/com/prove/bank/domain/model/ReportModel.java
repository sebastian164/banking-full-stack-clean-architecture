package com.prove.bank.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportModel {
    private Long clientId;
    private String clientName;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalDebits;
    private BigDecimal totalCredits;
    private String pdfBase64;
    private List<AccountStatementModel> statements;
}
