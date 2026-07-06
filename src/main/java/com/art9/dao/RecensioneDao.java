package com.art9.dao;

import com.art9.model.Recensione;

import java.util.List;

public interface RecensioneDao {

    List<Recensione> findByProdotto(int idProdotto);

    List<Recensione> findAll();

    boolean esisteRecensione(int idUtente, int idProdotto);

    Recensione insert(Recensione recensione);

    void delete(int idRecensione);

    double mediaVoti(int idProdotto);
}
