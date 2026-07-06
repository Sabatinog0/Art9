package com.art9.dao;

import com.art9.model.Recensione;
import com.art9.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class JdbcRecensioneDao implements RecensioneDao {

    private static final String SELECT_BASE =
            "SELECT r.id_recensione, r.id_utente, u.nome AS nome_utente, u.cognome AS cognome_utente, "
                    + "r.id_prodotto, p.nome AS nome_prodotto, r.voto, r.testo, r.data_recensione "
                    + "FROM recensione r "
                    + "JOIN utente u ON r.id_utente = u.id_utente "
                    + "JOIN prodotto p ON r.id_prodotto = p.id_prodotto";

    @Override
    public List<Recensione> findByProdotto(int idProdotto) {
        String sql = SELECT_BASE + " WHERE r.id_prodotto = ? ORDER BY r.data_recensione DESC";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProdotto);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Recensione> risultato = new ArrayList<>();
                while (rs.next()) {
                    risultato.add(mapRow(rs));
                }
                return risultato;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Errore nel recupero delle recensioni del prodotto " + idProdotto, e);
        }
    }

    @Override
    public List<Recensione> findAll() {
        String sql = SELECT_BASE + " ORDER BY r.data_recensione DESC";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<Recensione> risultato = new ArrayList<>();
            while (rs.next()) {
                risultato.add(mapRow(rs));
            }
            return risultato;
        } catch (SQLException e) {
            throw new DataAccessException("Errore nel recupero delle recensioni", e);
        }
    }

    @Override
    public boolean esisteRecensione(int idUtente, int idProdotto) {
        String sql = "SELECT 1 FROM recensione WHERE id_utente = ? AND id_prodotto = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            stmt.setInt(2, idProdotto);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Errore nella verifica della recensione esistente", e);
        }
    }

    @Override
    public Recensione insert(Recensione recensione) {
        String sql = "INSERT INTO recensione (id_utente, id_prodotto, voto, testo) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, recensione.getIdUtente());
            stmt.setInt(2, recensione.getIdProdotto());
            stmt.setInt(3, recensione.getVoto());
            stmt.setString(4, recensione.getTesto());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    recensione.setIdRecensione(keys.getInt(1));
                }
            }
            return recensione;
        } catch (SQLException e) {
            throw new DataAccessException("Errore nella creazione della recensione", e);
        }
    }

    @Override
    public void delete(int idRecensione) {
        String sql = "DELETE FROM recensione WHERE id_recensione = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idRecensione);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Errore nell'eliminazione della recensione " + idRecensione, e);
        }
    }

    @Override
    public double mediaVoti(int idProdotto) {
        String sql = "SELECT AVG(voto) FROM recensione WHERE id_prodotto = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProdotto);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double media = rs.getDouble(1);
                    return rs.wasNull() ? 0.0 : media;
                }
                return 0.0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Errore nel calcolo della media voti del prodotto " + idProdotto, e);
        }
    }

    private Recensione mapRow(ResultSet rs) throws SQLException {
        Recensione recensione = new Recensione();
        recensione.setIdRecensione(rs.getInt("id_recensione"));
        recensione.setIdUtente(rs.getInt("id_utente"));
        recensione.setNomeUtente(rs.getString("nome_utente") + " " + rs.getString("cognome_utente"));
        recensione.setIdProdotto(rs.getInt("id_prodotto"));
        recensione.setNomeProdotto(rs.getString("nome_prodotto"));
        recensione.setVoto(rs.getInt("voto"));
        recensione.setTesto(rs.getString("testo"));
        Timestamp dataRecensione = rs.getTimestamp("data_recensione");
        if (dataRecensione != null) {
            recensione.setDataRecensione(dataRecensione.toLocalDateTime());
        }
        return recensione;
    }
}
