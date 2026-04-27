package com.prove.bank.domain.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountModel {
    private Long id;
    private String number;
    private String type;
    private BigDecimal initialBalance;
    private BigDecimal currentBalance;
    private Boolean status;
    private Long clientId;
    private String clientName;
}
