package com.art9.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Recensione implements Serializable {

    private int idRecensione;
    private int idUtente;
    private String nomeUtente;
    private int idProdotto;
    private String nomeProdotto;
    private int voto;
    private String testo;
    private LocalDateTime dataRecensione;

    public Recensione() {
    }

    public int getIdRecensione() {
        return idRecensione;
    }

    public void setIdRecensione(int idRecensione) {
        this.idRecensione = idRecensione;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public String getNomeUtente() {
        return nomeUtente;
    }

    public void setNomeUtente(String nomeUtente) {
        this.nomeUtente = nomeUtente;
    }

    public int getIdProdotto() {
        return idProdotto;
    }

    public void setIdProdotto(int idProdotto) {
        this.idProdotto = idProdotto;
    }

    public String getNomeProdotto() {
        return nomeProdotto;
    }

    public void setNomeProdotto(String nomeProdotto) {
        this.nomeProdotto = nomeProdotto;
    }

    public int getVoto() {
        return voto;
    }

    public void setVoto(int voto) {
        this.voto = voto;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public LocalDateTime getDataRecensione() {
        return dataRecensione;
    }

    public void setDataRecensione(LocalDateTime dataRecensione) {
        this.dataRecensione = dataRecensione;
    }

    public Date getDataRecensioneAsDate() {
        return dataRecensione == null ? null : Date.from(dataRecensione.atZone(ZoneId.systemDefault()).toInstant());
    }
}
