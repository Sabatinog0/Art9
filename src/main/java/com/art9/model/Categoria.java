package com.art9.model;

import java.io.Serializable;

public class Categoria implements Serializable {

    private int idCategoria;
    private String nome;
    private String slug;
    private String descrizione;

    public Categoria() {
    }

    public Categoria(int idCategoria, String nome, String slug) {
        this.idCategoria = idCategoria;
        this.nome = nome;
        this.slug = slug;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}
