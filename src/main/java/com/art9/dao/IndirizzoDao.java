package com.art9.dao;

import com.art9.model.Indirizzo;

import java.util.List;
import java.util.Optional;

public interface IndirizzoDao {

    List<Indirizzo> findByUtente(int idUtente);

    Optional<Indirizzo> findById(int idIndirizzo);

    Indirizzo insert(Indirizzo indirizzo);

    void update(Indirizzo indirizzo);

    void delete(int idIndirizzo);

    void azzeraPredefinito(int idUtente);
}
