package com.art9.dao;

import com.art9.model.Prodotto;
import com.art9.model.Preferito;
import com.art9.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class JdbcPreferitoDao implements PreferitoDao {

    private static final String SELECT_BASE =
            "SELECT f.id_utente, f.data_aggiunta, p.id_prodotto, p.id_categoria, c.nome AS nome_categoria, "
                    + "c.slug AS slug_categoria, p.nome, p.descrizione, p.prezzo, p.iva_percentuale, "
                    + "p.quantita_disponibile, p.in_preordine, p.editore, p.immagine, p.data_creazione "
                    + "FROM preferito f "
                    + "JOIN prodotto p ON f.id_prodotto = p.id_prodotto "
                    + "JOIN categoria c ON p.id_categoria = c.id_categoria";

    @Override
    public List<Preferito> findByUtente(int idUtente) {
        String sql = SELECT_BASE + " WHERE f.id_utente = ? ORDER BY f.data_aggiunta DESC";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Preferito> risultato = new ArrayList<>();
                while (rs.next()) {
                    risultato.add(mapRow(rs));
                }
                return risultato;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Errore nel recupero dei preferiti dell'utente " + idUtente, e);
        }
    }

    @Override
    public boolean esiste(int idUtente, int idProdotto) {
        String sql = "SELECT 1 FROM preferito WHERE id_utente = ? AND id_prodotto = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            stmt.setInt(2, idProdotto);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Errore nella verifica del preferito", e);
        }
    }

    @Override
    public void aggiungi(int idUtente, int idProdotto) {
        String sql = "INSERT IGNORE INTO preferito (id_utente, id_prodotto) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            stmt.setInt(2, idProdotto);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Errore nell'aggiunta ai preferiti", e);
        }
    }

    @Override
    public void rimuovi(int idUtente, int idProdotto) {
        String sql = "DELETE FROM preferito WHERE id_utente = ? AND id_prodotto = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            stmt.setInt(2, idProdotto);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Errore nella rimozione dai preferiti", e);
        }
    }

    private Preferito mapRow(ResultSet rs) throws SQLException {
        Preferito preferito = new Preferito();
        preferito.setIdUtente(rs.getInt("id_utente"));
        Timestamp dataAggiunta = rs.getTimestamp("data_aggiunta");
        if (dataAggiunta != null) {
            preferito.setDataAggiunta(dataAggiunta.toLocalDateTime());
        }

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
        preferito.setProdotto(prodotto);

        return preferito;
    }
}
