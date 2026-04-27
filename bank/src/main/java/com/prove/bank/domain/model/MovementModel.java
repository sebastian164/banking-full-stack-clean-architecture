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
public class MovementModel {
    private Long id;
    private LocalDate date;
    private String type;
    private BigDecimal value;
    private BigDecimal balance;
    private String accountNumber;
}
