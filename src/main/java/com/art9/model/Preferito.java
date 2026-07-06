package com.art9.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Preferito implements Serializable {

    private int idUtente;
    private LocalDateTime dataAggiunta;
    private Prodotto prodotto;

    public Preferito() {
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public LocalDateTime getDataAggiunta() {
        return dataAggiunta;
    }

    public void setDataAggiunta(LocalDateTime dataAggiunta) {
        this.dataAggiunta = dataAggiunta;
    }

    public Prodotto getProdotto() {
        return prodotto;
    }

    public void setProdotto(Prodotto prodotto) {
        this.prodotto = prodotto;
    }
}
