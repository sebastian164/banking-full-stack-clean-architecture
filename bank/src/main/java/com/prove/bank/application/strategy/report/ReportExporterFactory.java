package com.prove.bank.application.strategy.report;

import com.prove.bank.domain.exception.BusinessException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ReportExporterFactory {
    private final Map<ReportExportFormat, ReportExporter> exporters;

    public ReportExporterFactory(List<ReportExporter> exporters) {
        this.exporters = new EnumMap<>(ReportExportFormat.class);
        exporters.forEach(exporter -> this.exporters.put(exporter.format(), exporter));
    }

    public ReportExporter get(ReportExportFormat format) {
        ReportExporter exporter = exporters.get(format);
        if (exporter == null) {
            throw new BusinessException("Formato de reporte no soportado");
        }
        return exporter;
    }
}
