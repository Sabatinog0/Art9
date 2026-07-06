package com.art9.dao;

import com.art9.model.Prodotto;
import com.art9.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcProdottoDao implements ProdottoDao {

    private static final String SELECT_BASE =
            "SELECT p.id_prodotto, p.id_categoria, c.nome AS nome_categoria, c.slug AS slug_categoria, "
                    + "p.nome, p.descrizione, p.prezzo, p.iva_percentuale, p.quantita_disponibile, "
                    + "p.in_preordine, p.editore, p.immagine, p.data_creazione "
                    + "FROM prodotto p JOIN categoria c ON p.id_categoria = c.id_categoria";

    @Override
    public List<Prodotto> findByFiltri(Integer idCategoria, String query, int offset, int limit) {
        StringBuilder sql = new StringBuilder(SELECT_BASE);
        List<Object> parametri = new ArrayList<>();
        appendFiltri(sql, parametri, idCategoria, query);
        sql.append(" ORDER BY p.data_creazione DESC, p.nome ASC LIMIT ? OFFSET ?");
        parametri.add(limit);
        parametri.add(offset);

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            bindParametri(stmt, parametri);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Prodotto> risultato = new ArrayList<>();
                while (rs.next()) {
                    risultato.add(mapRow(rs));
                }
                return risultato;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Errore nella ricerca prodotti", e);
        }
    }

    @Override
    public int countByFiltri(Integer idCategoria, String query) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM prodotto p JOIN categoria c ON p.id_categoria = c.id_categoria");
        List<Object> parametri = new ArrayList<>();
        appendFiltri(sql, parametri, idCategoria, query);

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            bindParametri(stmt, parametri);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Errore nel conteggio prodotti", e);
        }
    }

    private void appendFiltri(StringBuilder sql, List<Object> parametri, Integer idCategoria, String query) {
        List<String> condizioni = new ArrayList<>();
        if (idCategoria != null) {
            condizioni.add("p.id_categoria = ?");
            parametri.add(idCategoria);
        }
        if (query != null && !query.isBlank()) {
            condizioni.add("(p.nome LIKE ? OR p.descrizione LIKE ?)");
            String pattern = "%" + query.trim() + "%";
            parametri.add(pattern);
            parametri.add(pattern);
        }
        if (!condizioni.isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", condizioni));
        }
    }

    private void bindParametri(PreparedStatement stmt, List<Object> parametri) throws SQLException {
        for (int i = 0; i < parametri.size(); i++) {
            stmt.setObject(i + 1, parametri.get(i));
        }
    }

    @Override
    public Optional<Prodotto> findById(int idProdotto) {
        String sql = SELECT_BASE + " WHERE p.id_prodotto = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProdotto);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Errore nel recupero del prodotto " + idProdotto, e);
        }
    }

    @Override
    public List<Prodotto> suggerimenti(String query, int limit) {
        String sql = "SELECT p.id_prodotto, p.id_categoria, c.nome AS nome_categoria, c.slug AS slug_categoria, "
                + "p.nome, p.descrizione, p.prezzo, p.iva_percentuale, p.quantita_disponibile, "
                + "p.in_preordine, p.editore, p.immagine, p.data_creazione "
                + "FROM prodotto p JOIN categoria c ON p.id_categoria = c.id_categoria "
                + "WHERE p.nome LIKE ? ORDER BY p.nome ASC LIMIT ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + query.trim() + "%");
            stmt.setInt(2, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Prodotto> risultato = new ArrayList<>();
                while (rs.next()) {
                    risultato.add(mapRow(rs));
                }
                return risultato;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Errore nel recupero dei suggerimenti di ricerca", e);
        }
    }

    @Override
    public List<Prodotto> findAll() {
        String sql = SELECT_BASE + " ORDER BY p.nome ASC";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<Prodotto> risultato = new ArrayList<>();
            while (rs.next()) {
                risultato.add(mapRow(rs));
            }
            return risultato;
        } catch (SQLException e) {
            throw new DataAccessException("Errore nel recupero dei prodotti", e);
        }
    }

    @Override
    public Prodotto insert(Prodotto prodotto) {
        String sql = "INSERT INTO prodotto (id_categoria, nome, descrizione, prezzo, iva_percentuale, "
                + "quantita_disponibile, in_preordine, editore, immagine) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bindProdotto(stmt, prodotto);
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    prodotto.setIdProdotto(keys.getInt(1));
                }
            }
            return prodotto;
        } catch (SQLException e) {
            throw new DataAccessException("Errore nella creazione del prodotto", e);
        }
    }

    @Override
    public void update(Prodotto prodotto) {
        String sql = "UPDATE prodotto SET id_categoria = ?, nome = ?, descrizione = ?, prezzo = ?, "
                + "iva_percentuale = ?, quantita_disponibile = ?, in_preordine = ?, editore = ?, immagine = ? "
                + "WHERE id_prodotto = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            bindProdotto(stmt, prodotto);
            stmt.setInt(10, prodotto.getIdProdotto());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Errore nell'aggiornamento del prodotto " + prodotto.getIdProdotto(), e);
        }
    }

    @Override
    public void delete(int idProdotto) {
        String sql = "DELETE FROM prodotto WHERE id_prodotto = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProdotto);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Errore nell'eliminazione del prodotto " + idProdotto, e);
        }
    }

    @Override
    public void decrementaQuantita(int idProdotto, int quantita) {
        String sql = "UPDATE prodotto SET quantita_disponibile = GREATEST(quantita_disponibile - ?, 0) WHERE id_prodotto = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantita);
            stmt.setInt(2, idProdotto);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Errore nell'aggiornamento della disponibilita' del prodotto " + idProdotto, e);
        }
    }

    private void bindProdotto(PreparedStatement stmt, Prodotto prodotto) throws SQLException {
        stmt.setInt(1, prodotto.getIdCategoria());
        stmt.setString(2, prodotto.getNome());
        stmt.setString(3, prodotto.getDescrizione());
        stmt.setBigDecimal(4, prodotto.getPrezzo());
        stmt.setBigDecimal(5, prodotto.getIvaPercentuale());
        stmt.setInt(6, prodotto.getQuantitaDisponibile());
        stmt.setBoolean(7, prodotto.isInPreordine());
        stmt.setString(8, prodotto.getEditore());
        stmt.setString(9, prodotto.getImmagine());
    }

    private Prodotto mapRow(ResultSet rs) throws SQLException {
        Prodotto prodotto = new Prodotto();
        prodotto.setIdProdotto(rs.getInt("id_prodotto"));
        prodotto.setIdCategoria(rs.getInt("id_categoria"));
        prodotto.setNomeCategoria(rs.getString("nome_categoria"));
        prodotto.setSlugCategoria(rs.getString("slug_categoria"));
        prodotto.setNome(rs.getString("nome"));
        prodotto.setDescrizione(rs.getString("descrizione"));
        prodotto.setPrezzo(rs.getBigDecimal("prezzo"));
        prodotto.setIvaPercentuale(rs.getBigDecimal("iva_percentuale"));
        prodotto.setQuantitaDisponibile(rs.getInt("quantita_disponibile"));
        prodotto.setInPreordine(rs.getBoolean("in_preordine"));
        prodotto.setEditore(rs.getString("editore"));
        prodotto.setImmagine(rs.getString("immagine"));
        Timestamp dataCreazione = rs.getTimestamp("data_creazione");
        if (dataCreazione != null) {
            prodotto.setDataCreazione(dataCreazione.toLocalDateTime());
        }
        return prodotto;
    }
}
