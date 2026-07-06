package com.art9.model;

import java.io.Serializable;


public class MetodoPagamento implements Serializable {

    private int idMetodo;
    private int idUtente;
    private String intestatario;
    private String numeroMascherato;
    private String scadenza;
    private Circuito circuito;
    private boolean predefinito;

    public MetodoPagamento() {
    }

    public int getIdMetodo() {
        return idMetodo;
    }

    public void setIdMetodo(int idMetodo) {
        this.idMetodo = idMetodo;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public String getIntestatario() {
        return intestatario;
    }

    public void setIntestatario(String intestatario) {
        this.intestatario = intestatario;
    }

    public String getNumeroMascherato() {
        return numeroMascherato;
    }

    public void setNumeroMascherato(String numeroMascherato) {
        this.numeroMascherato = numeroMascherato;
    }

    public String getScadenza() {
        return scadenza;
    }

    public void setScadenza(String scadenza) {
        this.scadenza = scadenza;
    }

    public Circuito getCircuito() {
        return circuito;
    }

    public void setCircuito(Circuito circuito) {
        this.circuito = circuito;
    }

    public boolean isPredefinito() {
        return predefinito;
    }

    public void setPredefinito(boolean predefinito) {
        this.predefinito = predefinito;
    }

    public static String mascheraNumero(String numeroCartaCompleto) {
        String pulito = numeroCartaCompleto.replaceAll("\\s+", "");
        String ultime4 = pulito.substring(pulito.length() - 4);
        return "**** **** **** " + ultime4;
    }
}
