package com.art9.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;


public class RigaOrdine implements Serializable {

    private int idRiga;
    private int idOrdine;
    private Integer idProdotto;
    private String nomeProdotto;
    private BigDecimal prezzoUnitario;
    private BigDecimal ivaPercentuale;
    private int quantita;

    public RigaOrdine() {
    }

    public int getIdRiga() {
        return idRiga;
    }

    public void setIdRiga(int idRiga) {
        this.idRiga = idRiga;
    }

    public int getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(int idOrdine) {
        this.idOrdine = idOrdine;
    }

    public Integer getIdProdotto() {
        return idProdotto;
    }

    public void setIdProdotto(Integer idProdotto) {
        this.idProdotto = idProdotto;
    }

    public String getNomeProdotto() {
        return nomeProdotto;
    }

    public void setNomeProdotto(String nomeProdotto) {
        this.nomeProdotto = nomeProdotto;
    }

    public BigDecimal getPrezzoUnitario() {
        return prezzoUnitario;
    }

    public void setPrezzoUnitario(BigDecimal prezzoUnitario) {
        this.prezzoUnitario = prezzoUnitario;
    }

    public BigDecimal getIvaPercentuale() {
        return ivaPercentuale;
    }

    public void setIvaPercentuale(BigDecimal ivaPercentuale) {
        this.ivaPercentuale = ivaPercentuale;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public boolean isProdottoAncoraDisponibile() {
        return idProdotto != null;
    }

    public BigDecimal getSubtotale() {
        return prezzoUnitario.multiply(BigDecimal.valueOf(quantita));
    }

    public BigDecimal getImponibile() {
        BigDecimal divisore = BigDecimal.ONE.add(ivaPercentuale.divide(BigDecimal.valueOf(100)));
        return getSubtotale().divide(divisore, 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getValoreIva() {
        return getSubtotale().subtract(getImponibile());
    }
}
