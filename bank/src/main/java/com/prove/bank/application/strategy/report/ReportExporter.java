package com.prove.bank.application.strategy.report;

import com.prove.bank.domain.model.ReportModel;

public interface ReportExporter {
    ReportExportFormat format();
    byte[] export(ReportModel report);
}
