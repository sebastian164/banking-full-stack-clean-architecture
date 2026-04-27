package com.prove.bank.application.strategy.report;

import com.prove.bank.domain.model.AccountStatementModel;
import com.prove.bank.domain.model.ReportModel;
import com.prove.bank.util.constants.messages.ReportMessages;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PdfReportExporter implements ReportExporter {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public ReportExportFormat format() {
        return ReportExportFormat.PDF;
    }

    @Override
    public byte[] export(ReportModel report) {
        String content = buildContent(report);
        List<String> objects = new ArrayList<>();
        objects.add("<< /Type /Catalog /Pages 2 0 R >>");
        objects.add("<< /Type /Pages /Kids [3 0 R] /Count 1 >>");
        objects.add("<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] /Contents 4 0 R /Resources << /Font << /F1 5 0 R /F2 6 0 R >> >> >>");
        objects.add("<< /Length " + content.getBytes(StandardCharsets.UTF_8).length + " >>\nstream\n" + content + "endstream");
        objects.add("<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>");
        objects.add("<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica-Bold >>");
        return buildPdf(objects);
    }

    private String buildContent(ReportModel report) {
        StringBuilder content = new StringBuilder();
        drawHeader(content, report);
        drawSummary(content, report);
        drawTable(content, report);
        drawFooter(content);
        return content.toString();
    }

    private void drawHeader(StringBuilder content, ReportModel report) {
        rect(content, 0, 700, 612, 92, "0.000 0.200 0.400");
        rect(content, 0, 688, 612, 12, "1.000 0.867 0.000");
        roundedRect(content, 42, 727, 42, 42, 6, "1.000 0.867 0.000");
        roundedRect(content, 58, 739, 12, 12, 2, "0.000 0.200 0.400");
        text(content, "F2", 20, 96, 752, ReportMessages.BANK_NAME, "1 1 1");
        text(content, "F2", 18, 42, 660, ReportMessages.TITLE, "0.000 0.122 0.286");
        text(content, "F1", 11, 42, 642, ReportMessages.CLIENT_LABEL + safe(report.getClientName()), "0.153 0.255 0.373");
        text(content, "F1", 11, 42, 624, ReportMessages.PERIOD_LABEL + report.getStartDate() + " - " + report.getEndDate(), "0.153 0.255 0.373");
    }

    private void drawSummary(StringBuilder content, ReportModel report) {
        summaryCard(content, 42, 544, ReportMessages.TOTAL_CREDITS, money(report.getTotalCredits()), "0.914 0.980 0.929");
        summaryCard(content, 224, 544, ReportMessages.TOTAL_DEBITS, money(report.getTotalDebits()), "1.000 0.973 0.796");
        summaryCard(content, 406, 544, ReportMessages.MOVEMENTS, String.valueOf(report.getStatements().size()), "0.933 0.961 1.000");
    }

    private void summaryCard(StringBuilder content, int x, int y, String label, String value, String fillColor) {
        roundedRect(content, x, y, 164, 62, 8, fillColor);
        strokeRoundedRect(content, x, y, 164, 62, 8, "0.812 0.847 0.902");
        text(content, "F1", 9, x + 14, y + 39, label.toUpperCase(), "0.420 0.471 0.565");
        text(content, "F2", 16, x + 14, y + 17, value, "0.000 0.200 0.400");
    }

    private void drawTable(StringBuilder content, ReportModel report) {
        int startY = 494;
        roundedRect(content, 42, startY, 528, 26, 5, "0.000 0.200 0.400");
        text(content, "F2", 9, 54, startY + 9, ReportMessages.DATE, "1 1 1");
        text(content, "F2", 9, 124, startY + 9, ReportMessages.ACCOUNT, "1 1 1");
        text(content, "F2", 9, 210, startY + 9, ReportMessages.TYPE, "1 1 1");
        text(content, "F2", 9, 322, startY + 9, ReportMessages.MOVEMENT, "1 1 1");
        text(content, "F2", 9, 438, startY + 9, ReportMessages.AVAILABLE_BALANCE, "1 1 1");

        int y = startY - 24;
        int row = 0;
        for (AccountStatementModel statement : report.getStatements()) {
            if (row >= 18) {
                text(content, "F1", 9, 54, y - 8, ReportMessages.TRUNCATED_REPORT, "0.420 0.471 0.565");
                break;
            }
            String fill = row % 2 == 0 ? "0.985 0.990 1.000" : "1 1 1";
            roundedRect(content, 42, y - 4, 528, 24, 3, fill);
            strokeLine(content, 42, y - 4, 570, y - 4, "0.875 0.902 0.937");
            text(content, "F1", 9, 54, y + 5, statement.getDate().format(DATE_FORMAT), "0.035 0.133 0.322");
            text(content, "F2", 9, 124, y + 5, safe(statement.getAccountNumber()), "0.035 0.133 0.322");
            text(content, "F1", 9, 210, y + 5, safe(statement.getAccountType()), "0.035 0.133 0.322");
            text(content, "F1", 9, 322, y + 5, money(statement.getMovement()), amountColor(statement.getMovement()));
            text(content, "F1", 9, 438, y + 5, money(statement.getAvailableBalance()), "0.035 0.133 0.322");
            y -= 24;
            row++;
        }

        if (report.getStatements().isEmpty()) {
            text(content, "F1", 11, 54, y + 2, ReportMessages.NO_MOVEMENTS, "0.420 0.471 0.565");
        }
    }

    private void drawFooter(StringBuilder content) {
        strokeLine(content, 42, 62, 570, 62, "0.812 0.847 0.902");
        text(content, "F1", 8, 42, 44, ReportMessages.FOOTER, "0.420 0.471 0.565");
        text(content, "F2", 8, 478, 44, ReportMessages.BANK_NAME, "0.000 0.200 0.400");
    }

    private byte[] buildPdf(List<String> objects) {
        StringBuilder pdf = new StringBuilder("%PDF-1.4\n");
        List<Integer> offsets = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            offsets.add(pdf.length());
            pdf.append(i + 1).append(" 0 obj\n").append(objects.get(i)).append("\nendobj\n");
        }
        int xref = pdf.length();
        pdf.append("xref\n0 ").append(objects.size() + 1).append('\n');
        pdf.append("0000000000 65535 f \n");
        for (Integer offset : offsets) {
            pdf.append(String.format("%010d 00000 n \n", offset));
        }
        pdf.append("trailer << /Root 1 0 R /Size ").append(objects.size() + 1).append(" >>\n");
        pdf.append("startxref\n").append(xref).append("\n%%EOF");
        return pdf.toString().getBytes(StandardCharsets.UTF_8);
    }

    private void rect(StringBuilder content, int x, int y, int width, int height, String color) {
        content.append(color).append(" rg ").append(x).append(' ').append(y).append(' ')
                .append(width).append(' ').append(height).append(" re f\n");
    }

    private void strokeRect(StringBuilder content, int x, int y, int width, int height, String color) {
        content.append(color).append(" RG ").append(x).append(' ').append(y).append(' ')
                .append(width).append(' ').append(height).append(" re S\n");
    }

    private void roundedRect(StringBuilder content, int x, int y, int width, int height, int radius, String color) {
        roundedPath(content, x, y, width, height, radius);
        content.append(color).append(" rg f\n");
    }

    private void strokeRoundedRect(StringBuilder content, int x, int y, int width, int height, int radius, String color) {
        roundedPath(content, x, y, width, height, radius);
        content.append(color).append(" RG S\n");
    }

    private void roundedPath(StringBuilder content, int x, int y, int width, int height, int radius) {
        int right = x + width;
        int top = y + height;
        content.append(x + radius).append(' ').append(y).append(" m ")
                .append(right - radius).append(' ').append(y).append(" l ")
                .append(right).append(' ').append(y).append(' ')
                .append(right).append(' ').append(y).append(' ')
                .append(right).append(' ').append(y + radius).append(" c ")
                .append(right).append(' ').append(top - radius).append(" l ")
                .append(right).append(' ').append(top).append(' ')
                .append(right).append(' ').append(top).append(' ')
                .append(right - radius).append(' ').append(top).append(" c ")
                .append(x + radius).append(' ').append(top).append(" l ")
                .append(x).append(' ').append(top).append(' ')
                .append(x).append(' ').append(top).append(' ')
                .append(x).append(' ').append(top - radius).append(" c ")
                .append(x).append(' ').append(y + radius).append(" l ")
                .append(x).append(' ').append(y).append(' ')
                .append(x).append(' ').append(y).append(' ')
                .append(x + radius).append(' ').append(y).append(" c h ");
    }

    private void strokeLine(StringBuilder content, int x1, int y1, int x2, int y2, String color) {
        content.append(color).append(" RG ").append(x1).append(' ').append(y1)
                .append(" m ").append(x2).append(' ').append(y2).append(" l S\n");
    }

    private void text(StringBuilder content, String font, int size, int x, int y, String value, String color) {
        content.append("BT ").append(color).append(" rg /").append(font).append(' ').append(size)
                .append(" Tf ").append(x).append(' ').append(y).append(" Td (")
                .append(escape(value)).append(") Tj ET\n");
    }

    private String money(BigDecimal value) {
        return "$ " + (value == null ? "0.00" : value.setScale(2, RoundingMode.HALF_UP).toPlainString());
    }

    private String amountColor(BigDecimal value) {
        return value != null && value.signum() < 0 ? "0.650 0.165 0.165" : "0.086 0.443 0.173";
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private String escape(String value) {
        return safe(value)
                .replace("\\", "\\\\")
                .replace("(", "\\(")
                .replace(")", "\\)");
    }
}
