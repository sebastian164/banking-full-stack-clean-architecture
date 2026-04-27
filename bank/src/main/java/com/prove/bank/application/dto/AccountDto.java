package com.prove.bank.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private Long id;
    @NotBlank
    private String number;
    @NotBlank
    private String type;
    @NotNull
    private BigDecimal initialBalance;
    private BigDecimal currentBalance;
    @NotNull
    private Boolean status;
    @NotNull
    private Long clientId;
    private String clientName;
}
