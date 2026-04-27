package com.prove.bank.application.strategy.report;

import com.prove.bank.domain.model.ReportModel;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Base64ReportExporter implements ReportExporter {
    private final PdfReportExporter pdfReportExporter;

    @Override
    public ReportExportFormat format() {
        return ReportExportFormat.BASE64;
    }

    @Override
    public byte[] export(ReportModel report) {
        return Base64.getEncoder().encode(pdfReportExporter.export(report));
    }
}
