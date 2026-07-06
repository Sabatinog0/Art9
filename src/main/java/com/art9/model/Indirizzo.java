package com.art9.model;

import java.io.Serializable;

public class Indirizzo implements Serializable {

    private int idIndirizzo;
    private int idUtente;
    private String etichetta;
    private String via;
    private String citta;
    private String cap;
    private String provincia;
    private String nazione;
    private boolean predefinito;

    public Indirizzo() {
    }

    public int getIdIndirizzo() {
        return idIndirizzo;
    }

    public void setIdIndirizzo(int idIndirizzo) {
        this.idIndirizzo = idIndirizzo;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public String getEtichetta() {
        return etichetta;
    }

    public void setEtichetta(String etichetta) {
        this.etichetta = etichetta;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getNazione() {
        return nazione;
    }

    public void setNazione(String nazione) {
        this.nazione = nazione;
    }

    public boolean isPredefinito() {
        return predefinito;
    }

    public void setPredefinito(boolean predefinito) {
        this.predefinito = predefinito;
    }

    public String formatoEsteso() {
        return via + ", " + cap + " " + citta + " (" + provincia + "), " + nazione;
    }
}
