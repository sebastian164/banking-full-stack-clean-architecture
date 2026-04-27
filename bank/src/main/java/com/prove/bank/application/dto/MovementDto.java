package com.prove.bank.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class MovementDto {
    private Long id;
    private LocalDate date;
    @NotBlank
    private String type;
    @NotNull
    private BigDecimal value;
    private BigDecimal balance;
    @NotBlank
    private String accountNumber;
}
