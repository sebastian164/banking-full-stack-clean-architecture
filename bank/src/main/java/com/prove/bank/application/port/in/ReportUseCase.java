package com.prove.bank.application.port.in;

import com.prove.bank.application.dto.ReportDto;
import java.time.LocalDate;

public interface ReportUseCase {
    ReportDto generate(Long clientId, LocalDate startDate, LocalDate endDate);
    byte[] toPdfBytes(ReportDto report);
}
