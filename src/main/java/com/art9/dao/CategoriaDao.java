package com.art9.dao;

import com.art9.model.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaDao {

    List<Categoria> findAll();

    Optional<Categoria> findById(int idCategoria);

    Optional<Categoria> findBySlug(String slug);

    Categoria insert(Categoria categoria);

    void update(Categoria categoria);

    void delete(int idCategoria);

    int countProdotti(int idCategoria);
}
