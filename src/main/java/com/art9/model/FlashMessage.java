package com.art9.model;

import java.io.Serializable;

public class FlashMessage implements Serializable {

    public enum Tipo {
        SUCCESSO,
        ERRORE
    }

    private final Tipo tipo;
    private final String testo;

    public FlashMessage(Tipo tipo, String testo) {
        this.tipo = tipo;
        this.testo = testo;
    }

    public static FlashMessage successo(String testo) {
        return new FlashMessage(Tipo.SUCCESSO, testo);
    }

    public static FlashMessage errore(String testo) {
        return new FlashMessage(Tipo.ERRORE, testo);
    }

    public Tipo getTipo() {
        return tipo;
    }

    public String getTesto() {
        return testo;
    }
}
