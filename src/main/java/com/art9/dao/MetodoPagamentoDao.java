package com.art9.dao;

import com.art9.model.MetodoPagamento;

import java.util.List;
import java.util.Optional;

public interface MetodoPagamentoDao {

    List<MetodoPagamento> findByUtente(int idUtente);

    Optional<MetodoPagamento> findById(int idMetodo);

    MetodoPagamento insert(MetodoPagamento metodoPagamento);

    void update(MetodoPagamento metodoPagamento);

    void delete(int idMetodo);

    void azzeraPredefinito(int idUtente);
}
