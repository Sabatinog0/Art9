package com.art9.dao;

import com.art9.model.Utente;

import java.util.Optional;

public interface UtenteDao {

    Optional<Utente> findByEmail(String email);

    Optional<Utente> findById(int idUtente);

    boolean existsByEmail(String email);

    int countClienti();

    Utente insert(Utente utente);

    void updateProfilo(Utente utente);

    void updatePassword(int idUtente, String nuovoHash);
}
