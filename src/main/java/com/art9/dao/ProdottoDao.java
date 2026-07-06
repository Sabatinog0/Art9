package com.art9.dao;

import com.art9.model.Prodotto;

import java.util.List;
import java.util.Optional;

public interface ProdottoDao {

    
     
    
    /**
     * 
     *
     * @param idCategoria id della categoria da filtrare, null per non filtrare
     * @param query       testo di ricerca, null per non filtrare
     * @param offset      numero di risultati da saltare (paginazione)
     * @param limit       numero massimo di risultati da restituire
     */
    
    List<Prodotto> findByFiltri(Integer idCategoria, String query, int offset, int limit);

    int countByFiltri(Integer idCategoria, String query);

    Optional<Prodotto> findById(int idProdotto);

    List<Prodotto> suggerimenti(String query, int limit);

    List<Prodotto> findAll();

    Prodotto insert(Prodotto prodotto);

    void update(Prodotto prodotto);

    void delete(int idProdotto);

    void decrementaQuantita(int idProdotto, int quantita);
}
