package com.prove.bank.application.service;

import com.prove.bank.application.dto.ReportDto;
import com.prove.bank.application.mapper.ApplicationDtoMapper;
import com.prove.bank.application.port.in.ReportUseCase;
import com.prove.bank.application.strategy.report.ReportExportFormat;
import com.prove.bank.application.strategy.report.ReportExporterFactory;
import com.prove.bank.domain.exception.NotFoundException;
import com.prove.bank.domain.model.AccountModel;
import com.prove.bank.domain.model.AccountStatementModel;
import com.prove.bank.domain.model.MovementModel;
import com.prove.bank.domain.model.ReportModel;
import com.prove.bank.domain.spi.AccountRepositoryPort;
import com.prove.bank.domain.spi.ClientRepositoryPort;
import com.prove.bank.domain.spi.MovementRepositoryPort;
import com.prove.bank.util.constants.messages.ClientMessages;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService implements ReportUseCase {
    private final ClientRepositoryPort clientRepository;
    private final AccountRepositoryPort accountRepository;
    private final MovementRepositoryPort movementRepository;
    private final ReportExporterFactory reportExporterFactory;
    private final ApplicationDtoMapper mapper;

    @Transactional(readOnly = true)
    public ReportDto generate(Long clientId, LocalDate startDate, LocalDate endDate) {
        String clientName = clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException(ClientMessages.NOT_FOUND))
                .getName();
        List<AccountStatementModel> statements = new ArrayList<>();
        for (AccountModel account : accountRepository.findByClientId(clientId)) {
            List<MovementModel> movements = movementRepository.findByAccountNumberAndDateBetween(
                    account.getNumber(), startDate, endDate);
            for (MovementModel movement : movements) {
                statements.add(AccountStatementModel.builder()
                        .date(movement.getDate())
                        .client(clientName)
                        .accountNumber(account.getNumber())
                        .accountType(account.getType())
                        .initialBalance(account.getInitialBalance())
                        .status(account.getStatus())
                        .movement(movement.getValue())
                        .availableBalance(movement.getBalance())
                        .build());
            }
        }
        BigDecimal credits = statements.stream()
                .map(AccountStatementModel::getMovement)
                .filter(value -> value.signum() > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal debits = statements.stream()
                .map(AccountStatementModel::getMovement)
                .filter(value -> value.signum() < 0)
                .map(BigDecimal::abs)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        ReportModel report = ReportModel.builder()
                .clientId(clientId)
                .clientName(clientName)
                .startDate(startDate)
                .endDate(endDate)
                .totalCredits(credits)
                .totalDebits(debits)
                .statements(statements)
                .build();
        report.setPdfBase64(new String(reportExporterFactory.get(ReportExportFormat.BASE64).export(report)));
        return mapper.toDto(report);
    }

    public byte[] toPdfBytes(ReportDto report) {
        return reportExporterFactory.get(ReportExportFormat.PDF).export(mapper.toModel(report));
    }
}
