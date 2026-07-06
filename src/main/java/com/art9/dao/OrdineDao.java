package com.art9.dao;

import com.art9.model.Ordine;
import com.art9.model.StatoOrdine;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrdineDao {

    Ordine inserisciConRighe(Ordine ordine);

    Optional<Ordine> findById(int idOrdine);

    List<Ordine> findByUtente(int idUtente);

    List<Ordine> findByFiltri(LocalDate dal, LocalDate al, String cliente, int offset, int limit);

    int countByFiltri(LocalDate dal, LocalDate al, String cliente);

    void aggiornaStato(int idOrdine, StatoOrdine nuovoStato);

    boolean utenteHaAcquistato(int idUtente, int idProdotto);

    BigDecimal fatturatoTotale();
}
