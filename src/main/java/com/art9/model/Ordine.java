package com.art9.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Ordine implements Serializable {

    private int idOrdine;
    private int idUtente;
    private String nomeCliente;
    private String cognomeCliente;
    private String emailCliente;
    private Integer idMetodoPagamento;
    private LocalDateTime dataOrdine;
    private StatoOrdine stato;
    private BigDecimal totale;
    private String indirizzoSpedizione;
    private List<RigaOrdine> righe = new ArrayList<>();

    public Ordine() {
    }

    public int getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(int idOrdine) {
        this.idOrdine = idOrdine;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getCognomeCliente() {
        return cognomeCliente;
    }

    public void setCognomeCliente(String cognomeCliente) {
        this.cognomeCliente = cognomeCliente;
    }

    public String getNomeClienteCompleto() {
        return nomeCliente == null ? null : nomeCliente + " " + cognomeCliente;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public Integer getIdMetodoPagamento() {
        return idMetodoPagamento;
    }

    public void setIdMetodoPagamento(Integer idMetodoPagamento) {
        this.idMetodoPagamento = idMetodoPagamento;
    }

    public LocalDateTime getDataOrdine() {
        return dataOrdine;
    }

    public void setDataOrdine(LocalDateTime dataOrdine) {
        this.dataOrdine = dataOrdine;
    }

    public StatoOrdine getStato() {
        return stato;
    }

    public void setStato(StatoOrdine stato) {
        this.stato = stato;
    }

    public BigDecimal getTotale() {
        return totale;
    }

    public void setTotale(BigDecimal totale) {
        this.totale = totale;
    }

    public String getIndirizzoSpedizione() {
        return indirizzoSpedizione;
    }

    public void setIndirizzoSpedizione(String indirizzoSpedizione) {
        this.indirizzoSpedizione = indirizzoSpedizione;
    }

    public List<RigaOrdine> getRighe() {
        return righe;
    }

    public void setRighe(List<RigaOrdine> righe) {
        this.righe = righe;
    }

    public BigDecimal getImponibileTotale() {
        return righe.stream().map(RigaOrdine::getImponibile).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getIvaTotale() {
        return righe.stream().map(RigaOrdine::getValoreIva).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

 
    public Date getDataOrdineAsDate() {
        return dataOrdine == null ? null : Date.from(dataOrdine.atZone(ZoneId.systemDefault()).toInstant());
    }
}
