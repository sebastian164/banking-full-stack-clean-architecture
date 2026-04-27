package com.prove.bank.util.constants.messages;

public final class ApiErrorMessages {
    public static final String INVALID_REQUEST = "Solicitud invalida";
    public static final String MISSING_REQUIRED_PARAMETER = "Parametro requerido ausente: ";
    public static final String INVALID_PARAMETER = "Parametro invalido: ";
    public static final String INVALID_REQUEST_BODY = "Cuerpo de la solicitud invalido";
    public static final String DATA_INTEGRITY_CONSTRAINT = "La operacion no se puede realizar por restricciones de integridad";
    public static final String UNEXPECTED_ERROR = "No se pudo completar la operacion";

    private ApiErrorMessages() {
    }
}
