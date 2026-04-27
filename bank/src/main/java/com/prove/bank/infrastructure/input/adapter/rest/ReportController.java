package com.prove.bank.infrastructure.input.adapter.rest;

import com.prove.bank.application.dto.ReportDto;
import com.prove.bank.application.port.in.ReportUseCase;
import com.prove.bank.util.constants.ApiRoutes;
import com.prove.bank.util.constants.messages.ReportMessages;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiRoutes.Reports.ROOT)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReportController {
    private final ReportUseCase service;

    @GetMapping
    public ReportDto json(
            @RequestParam Long clientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return service.generate(clientId, startDate, endDate);
    }

    @GetMapping(ApiRoutes.Reports.PDF)
    public ResponseEntity<byte[]> pdf(
            @RequestParam Long clientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        ReportDto report = service.generate(clientId, startDate, endDate);
        byte[] content = service.toPdfBytes(report);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(ReportMessages.PDF_FILE_NAME).build().toString())
                .body(content);
    }
}
