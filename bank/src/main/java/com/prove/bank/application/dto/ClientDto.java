package com.prove.bank.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {
    private Long id;
    @NotBlank
    private String name;
    private String gender;
    private Integer age;
    @NotBlank
    private String identification;
    private String address;
    private String phone;
    @NotBlank
    private String password;
    @NotNull
    private Boolean status;
}
