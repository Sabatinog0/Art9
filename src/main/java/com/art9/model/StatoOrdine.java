package com.art9.model;

public enum StatoOrdine {
    PREORDINE,
    IN_ATTESA,
    CONFERMATO,
    SPEDITO,
    CONSEGNATO,
    ANNULLATO;

    public String etichetta() {
        return switch (this) {
            case PREORDINE -> "Preordine";
            case IN_ATTESA -> "In attesa";
            case CONFERMATO -> "Confermato";
            case SPEDITO -> "Spedito";
            case CONSEGNATO -> "Consegnato";
            case ANNULLATO -> "Annullato";
        };
    }

    public String cssClass() {
        return switch (this) {
            case PREORDINE -> "preordinestato";
            case IN_ATTESA -> "attesa";
            case CONFERMATO -> "confermato";
            case SPEDITO -> "spedito";
            case CONSEGNATO -> "consegnato";
            case ANNULLATO -> "annullato";
        };
    }
}
