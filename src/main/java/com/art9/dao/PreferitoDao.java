package com.art9.dao;

import com.art9.model.Preferito;

import java.util.List;

public interface PreferitoDao {

    List<Preferito> findByUtente(int idUtente);

    boolean esiste(int idUtente, int idProdotto);

    void aggiungi(int idUtente, int idProdotto);

    void rimuovi(int idUtente, int idProdotto);
}
