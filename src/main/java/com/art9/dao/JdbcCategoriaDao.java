package com.art9.dao;

import com.art9.model.Categoria;
import com.art9.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcCategoriaDao implements CategoriaDao {

    @Override
    public List<Categoria> findAll() {
        String sql = "SELECT id_categoria, nome, slug, descrizione FROM categoria ORDER BY nome";
        List<Categoria> risultato = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                risultato.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Errore nel recupero delle categorie", e);
        }
        return risultato;
    }

    @Override
    public Optional<Categoria> findById(int idCategoria) {
        String sql = "SELECT id_categoria, nome, slug, descrizione FROM categoria WHERE id_categoria = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCategoria);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Errore nel recupero della categoria " + idCategoria, e);
        }
    }

    @Override
    public Optional<Categoria> findBySlug(String slug) {
        String sql = "SELECT id_categoria, nome, slug, descrizione FROM categoria WHERE slug = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, slug);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Errore nel recupero della categoria '" + slug + "'", e);
        }
    }

    @Override
    public Categoria insert(Categoria categoria) {
        String sql = "INSERT INTO categoria (nome, slug, descrizione) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getSlug());
            stmt.setString(3, categoria.getDescrizione());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    categoria.setIdCategoria(keys.getInt(1));
                }
            }
            return categoria;
        } catch (SQLException e) {
            throw new DataAccessException("Errore nella creazione della categoria", e);
        }
    }

    @Override
    public void update(Categoria categoria) {
        String sql = "UPDATE categoria SET nome = ?, slug = ?, descrizione = ? WHERE id_categoria = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getSlug());
            stmt.setString(3, categoria.getDescrizione());
            stmt.setInt(4, categoria.getIdCategoria());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Errore nell'aggiornamento della categoria " + categoria.getIdCategoria(), e);
        }
    }

    @Override
    public void delete(int idCategoria) {
        String sql = "DELETE FROM categoria WHERE id_categoria = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCategoria);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Errore nell'eliminazione della categoria " + idCategoria, e);
        }
    }

    @Override
    public int countProdotti(int idCategoria) {
        String sql = "SELECT COUNT(*) FROM prodotto WHERE id_categoria = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCategoria);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Errore nel conteggio prodotti della categoria " + idCategoria, e);
        }
    }

    private Categoria mapRow(ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(rs.getInt("id_categoria"));
        categoria.setNome(rs.getString("nome"));
        categoria.setSlug(rs.getString("slug"));
        categoria.setDescrizione(rs.getString("descrizione"));
        return categoria;
    }
}
