package com.prove.bank.util.constants.messages;

public final class ReportMessages {
    public static final String PDF_FILE_NAME = "account-statement.pdf";
    public static final String BANK_NAME = "BANCO PICHINCHA";
    public static final String TITLE = "Estado de cuenta";
    public static final String CLIENT_LABEL = "Cliente: ";
    public static final String PERIOD_LABEL = "Periodo: ";
    public static final String TOTAL_CREDITS = "Total creditos";
    public static final String TOTAL_DEBITS = "Total debitos";
    public static final String MOVEMENTS = "Movimientos";
    public static final String DATE = "Fecha";
    public static final String ACCOUNT = "Cuenta";
    public static final String TYPE = "Tipo";
    public static final String MOVEMENT = "Movimiento";
    public static final String AVAILABLE_BALANCE = "Saldo disponible";
    public static final String TRUNCATED_REPORT = "Reporte truncado visualmente. Consulte JSON para el detalle completo.";
    public static final String NO_MOVEMENTS = "No hay movimientos para el rango seleccionado.";
    public static final String FOOTER = "Documento generado automaticamente. Informacion sujeta a verificacion bancaria.";

    private ReportMessages() {
    }
}
