package com.prove.bank.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountStatementModel {
    private LocalDate date;
    private String client;
    private String accountNumber;
    private String accountType;
    private BigDecimal initialBalance;
    private Boolean status;
    private BigDecimal movement;
    private BigDecimal availableBalance;
}
