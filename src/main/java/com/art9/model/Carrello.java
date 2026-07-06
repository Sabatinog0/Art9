package com.art9.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Carrello implements Serializable {

    private final Map<Integer, CarrelloItem> items = new LinkedHashMap<>();

    public void aggiungi(Prodotto prodotto, int quantita) {
        if (quantita <= 0) {
            return;
        }
        CarrelloItem esistente = items.get(prodotto.getIdProdotto());
        if (esistente != null) {
            esistente.setQuantita(esistente.getQuantita() + quantita);
        } else {
            items.put(prodotto.getIdProdotto(), new CarrelloItem(prodotto, quantita));
        }
    }

    public void aggiornaQuantita(int idProdotto, int quantita) {
        if (quantita <= 0) {
            items.remove(idProdotto);
            return;
        }
        CarrelloItem item = items.get(idProdotto);
        if (item != null) {
            item.setQuantita(quantita);
        }
    }

    public void rimuovi(int idProdotto) {
        items.remove(idProdotto);
    }

    public void svuota() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public Collection<CarrelloItem> getItems() {
        return items.values();
    }

    public int getNumeroArticoli() {
        return items.values().stream().mapToInt(CarrelloItem::getQuantita).sum();
    }

    public BigDecimal getTotale() {
        return items.values().stream()
                .map(CarrelloItem::getSubtotale)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
