package com.art9.dao;

import com.art9.model.Ruolo;
import com.art9.model.Utente;
import com.art9.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Optional;

public class JdbcUtenteDao implements UtenteDao {

    private static final String SELECT_BASE =
            "SELECT id_utente, email, password_hash, nome, cognome, ruolo, data_registrazione FROM utente";

    @Override
    public Optional<Utente> findByEmail(String email) {
        String sql = SELECT_BASE + " WHERE email = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Errore nel recupero dell'utente per email", e);
        }
    }

    @Override
    public Optional<Utente> findById(int idUtente) {
        String sql = SELECT_BASE + " WHERE id_utente = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Errore nel recupero dell'utente " + idUtente, e);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT 1 FROM utente WHERE email = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Errore nella verifica dell'email", e);
        }
    }

    @Override
    public int countClienti() {
        String sql = "SELECT COUNT(*) FROM utente WHERE ruolo = 'CLIENTE'";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new DataAccessException("Errore nel conteggio dei clienti registrati", e);
        }
    }

    @Override
    public Utente insert(Utente utente) {
        String sql = "INSERT INTO utente (email, password_hash, nome, cognome, ruolo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, utente.getEmail());
            stmt.setString(2, utente.getPasswordHash());
            stmt.setString(3, utente.getNome());
            stmt.setString(4, utente.getCognome());
            stmt.setString(5, utente.getRuolo().name());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    utente.setIdUtente(keys.getInt(1));
                }
            }
            return utente;
        } catch (SQLException e) {
            throw new DataAccessException("Errore nella creazione dell'utente", e);
        }
    }

    @Override
    public void updateProfilo(Utente utente) {
        String sql = "UPDATE utente SET email = ?, nome = ?, cognome = ? WHERE id_utente = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, utente.getEmail());
            stmt.setString(2, utente.getNome());
            stmt.setString(3, utente.getCognome());
            stmt.setInt(4, utente.getIdUtente());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Errore nell'aggiornamento del profilo " + utente.getIdUtente(), e);
        }
    }

    @Override
    public void updatePassword(int idUtente, String nuovoHash) {
        String sql = "UPDATE utente SET password_hash = ? WHERE id_utente = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nuovoHash);
            stmt.setInt(2, idUtente);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Errore nell'aggiornamento della password", e);
        }
    }

    private Utente mapRow(ResultSet rs) throws SQLException {
        Utente utente = new Utente();
        utente.setIdUtente(rs.getInt("id_utente"));
        utente.setEmail(rs.getString("email"));
        utente.setPasswordHash(rs.getString("password_hash"));
        utente.setNome(rs.getString("nome"));
        utente.setCognome(rs.getString("cognome"));
        utente.setRuolo(Ruolo.valueOf(rs.getString("ruolo")));
        Timestamp dataRegistrazione = rs.getTimestamp("data_registrazione");
        if (dataRegistrazione != null) {
            utente.setDataRegistrazione(dataRegistrazione.toLocalDateTime());
        }
        return utente;
    }
}
