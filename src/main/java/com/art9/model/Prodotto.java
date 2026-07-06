package com.art9.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;


public class Prodotto implements Serializable {

    private int idProdotto;
    private int idCategoria;
    private String nomeCategoria;
    private String slugCategoria;
    private String nome;
    private String descrizione;
    private BigDecimal prezzo;
    private BigDecimal ivaPercentuale;
    private int quantitaDisponibile;
    private boolean inPreordine;
    private String editore;
    private String immagine;
    private LocalDateTime dataCreazione;

    public Prodotto() {
    }

    public int getIdProdotto() {
        return idProdotto;
    }

    public void setIdProdotto(int idProdotto) {
        this.idProdotto = idProdotto;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public String getSlugCategoria() {
        return slugCategoria;
    }

    public void setSlugCategoria(String slugCategoria) {
        this.slugCategoria = slugCategoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

    public BigDecimal getIvaPercentuale() {
        return ivaPercentuale;
    }

    public void setIvaPercentuale(BigDecimal ivaPercentuale) {
        this.ivaPercentuale = ivaPercentuale;
    }

    public int getQuantitaDisponibile() {
        return quantitaDisponibile;
    }

    public void setQuantitaDisponibile(int quantitaDisponibile) {
        this.quantitaDisponibile = quantitaDisponibile;
    }

    public boolean isInPreordine() {
        return inPreordine;
    }

    public void setInPreordine(boolean inPreordine) {
        this.inPreordine = inPreordine;
    }

    public String getEditore() {
        return editore;
    }

    public void setEditore(String editore) {
        this.editore = editore;
    }

    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public boolean isDisponibile() {
        return quantitaDisponibile > 0 || inPreordine;
    }
    
    public BigDecimal getImponibile() {
        BigDecimal divisore = BigDecimal.ONE.add(ivaPercentuale.divide(BigDecimal.valueOf(100)));
        return prezzo.divide(divisore, 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getValoreIva() {
        return prezzo.subtract(getImponibile());
    }
}
