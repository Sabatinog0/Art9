package com.art9.model;

import java.io.Serializable;
import java.math.BigDecimal;


public class CarrelloItem implements Serializable {

    private final int idProdotto;
    private final String nome;
    private final BigDecimal prezzoUnitario;
    private final BigDecimal ivaPercentuale;
    private final String immagine;
    private final int quantitaDisponibile;
    private int quantita;

    public CarrelloItem(Prodotto prodotto, int quantita) {
        this.idProdotto = prodotto.getIdProdotto();
        this.nome = prodotto.getNome();
        this.prezzoUnitario = prodotto.getPrezzo();
        this.ivaPercentuale = prodotto.getIvaPercentuale();
        this.immagine = prodotto.getImmagine();
        this.quantitaDisponibile = prodotto.getQuantitaDisponibile();
        this.quantita = quantita;
    }

    public int getIdProdotto() {
        return idProdotto;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getPrezzoUnitario() {
        return prezzoUnitario;
    }

    public BigDecimal getIvaPercentuale() {
        return ivaPercentuale;
    }

    public String getImmagine() {
        return immagine;
    }

    public int getQuantitaDisponibile() {
        return quantitaDisponibile;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public BigDecimal getSubtotale() {
        return prezzoUnitario.multiply(BigDecimal.valueOf(quantita));
    }
}
