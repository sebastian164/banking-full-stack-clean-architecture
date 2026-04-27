package com.prove.bank.application.mapper;

import com.prove.bank.application.dto.AccountDto;
import com.prove.bank.application.dto.AccountStatementDto;
import com.prove.bank.application.dto.ClientDto;
import com.prove.bank.application.dto.MovementDto;
import com.prove.bank.application.dto.ReportDto;
import com.prove.bank.domain.model.AccountModel;
import com.prove.bank.domain.model.AccountStatementModel;
import com.prove.bank.domain.model.ClientModel;
import com.prove.bank.domain.model.MovementModel;
import com.prove.bank.domain.model.ReportModel;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ApplicationDtoMapper {
    public ClientModel toModel(ClientDto dto) {
        if (dto == null) return null;
        return ClientModel.builder()
                .id(dto.getId())
                .name(dto.getName())
                .gender(dto.getGender())
                .age(dto.getAge())
                .identification(dto.getIdentification())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .password(dto.getPassword())
                .status(dto.getStatus())
                .build();
    }

    public ClientDto toDto(ClientModel model) {
        if (model == null) return null;
        return ClientDto.builder()
                .id(model.getId())
                .name(model.getName())
                .gender(model.getGender())
                .age(model.getAge())
                .identification(model.getIdentification())
                .address(model.getAddress())
                .phone(model.getPhone())
                .password(model.getPassword())
                .status(model.getStatus())
                .build();
    }

    public AccountModel toModel(AccountDto dto) {
        if (dto == null) return null;
        return AccountModel.builder()
                .id(dto.getId())
                .number(dto.getNumber())
                .type(dto.getType())
                .initialBalance(dto.getInitialBalance())
                .currentBalance(dto.getCurrentBalance())
                .status(dto.getStatus())
                .clientId(dto.getClientId())
                .clientName(dto.getClientName())
                .build();
    }

    public AccountDto toDto(AccountModel model) {
        if (model == null) return null;
        return AccountDto.builder()
                .id(model.getId())
                .number(model.getNumber())
                .type(model.getType())
                .initialBalance(model.getInitialBalance())
                .currentBalance(model.getCurrentBalance())
                .status(model.getStatus())
                .clientId(model.getClientId())
                .clientName(model.getClientName())
                .build();
    }

    public MovementModel toModel(MovementDto dto) {
        if (dto == null) return null;
        return MovementModel.builder()
                .id(dto.getId())
                .date(dto.getDate())
                .type(dto.getType())
                .value(dto.getValue())
                .balance(dto.getBalance())
                .accountNumber(dto.getAccountNumber())
                .build();
    }

    public MovementDto toDto(MovementModel model) {
        if (model == null) return null;
        return MovementDto.builder()
                .id(model.getId())
                .date(model.getDate())
                .type(model.getType())
                .value(model.getValue())
                .balance(model.getBalance())
                .accountNumber(model.getAccountNumber())
                .build();
    }

    public ReportModel toModel(ReportDto dto) {
        if (dto == null) return null;
        List<AccountStatementModel> statements = dto.getStatements() == null ? List.of()
                : dto.getStatements().stream().map(this::toModel).toList();
        return ReportModel.builder()
                .clientId(dto.getClientId())
                .clientName(dto.getClientName())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .totalDebits(dto.getTotalDebits())
                .totalCredits(dto.getTotalCredits())
                .pdfBase64(dto.getPdfBase64())
                .statements(statements)
                .build();
    }

    public ReportDto toDto(ReportModel model) {
        if (model == null) return null;
        List<AccountStatementDto> statements = model.getStatements() == null ? List.of()
                : model.getStatements().stream().map(this::toDto).toList();
        return ReportDto.builder()
                .clientId(model.getClientId())
                .clientName(model.getClientName())
                .startDate(model.getStartDate())
                .endDate(model.getEndDate())
                .totalDebits(model.getTotalDebits())
                .totalCredits(model.getTotalCredits())
                .pdfBase64(model.getPdfBase64())
                .statements(statements)
                .build();
    }

    private AccountStatementModel toModel(AccountStatementDto dto) {
        return AccountStatementModel.builder()
                .date(dto.getDate())
                .client(dto.getClient())
                .accountNumber(dto.getAccountNumber())
                .accountType(dto.getAccountType())
                .initialBalance(dto.getInitialBalance())
                .status(dto.getStatus())
                .movement(dto.getMovement())
                .availableBalance(dto.getAvailableBalance())
                .build();
    }

    private AccountStatementDto toDto(AccountStatementModel model) {
        return AccountStatementDto.builder()
                .date(model.getDate())
                .client(model.getClient())
                .accountNumber(model.getAccountNumber())
                .accountType(model.getAccountType())
                .initialBalance(model.getInitialBalance())
                .status(model.getStatus())
                .movement(model.getMovement())
                .availableBalance(model.getAvailableBalance())
                .build();
    }
}
