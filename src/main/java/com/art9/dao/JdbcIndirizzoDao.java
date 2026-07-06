package com.art9.dao;

import com.art9.model.Indirizzo;
import com.art9.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcIndirizzoDao implements IndirizzoDao {

    private static final String SELECT_BASE =
            "SELECT id_indirizzo, id_utente, etichetta, via, citta, cap, provincia, nazione, predefinito FROM indirizzo";

    @Override
    public List<Indirizzo> findByUtente(int idUtente) {
        String sql = SELECT_BASE + " WHERE id_utente = ? ORDER BY predefinito DESC, id_indirizzo DESC";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Indirizzo> risultato = new ArrayList<>();
                while (rs.next()) {
                    risultato.add(mapRow(rs));
                }
                return risultato;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Errore nel recupero degli indirizzi dell'utente " + idUtente, e);
        }
    }

    @Override
    public Optional<Indirizzo> findById(int idIndirizzo) {
        String sql = SELECT_BASE + " WHERE id_indirizzo = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idIndirizzo);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Errore nel recupero dell'indirizzo " + idIndirizzo, e);
        }
    }

    @Override
    public Indirizzo insert(Indirizzo indirizzo) {
        String sql = "INSERT INTO indirizzo (id_utente, etichetta, via, citta, cap, provincia, nazione, predefinito) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bind(stmt, indirizzo);
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    indirizzo.setIdIndirizzo(keys.getInt(1));
                }
            }
            return indirizzo;
        } catch (SQLException e) {
            throw new DataAccessException("Errore nella creazione dell'indirizzo", e);
        }
    }

    @Override
    public void update(Indirizzo indirizzo) {
        String sql = "UPDATE indirizzo SET etichetta = ?, via = ?, citta = ?, cap = ?, provincia = ?, "
                + "nazione = ?, predefinito = ? WHERE id_indirizzo = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, indirizzo.getEtichetta());
            stmt.setString(2, indirizzo.getVia());
            stmt.setString(3, indirizzo.getCitta());
            stmt.setString(4, indirizzo.getCap());
            stmt.setString(5, indirizzo.getProvincia());
            stmt.setString(6, indirizzo.getNazione());
            stmt.setBoolean(7, indirizzo.isPredefinito());
            stmt.setInt(8, indirizzo.getIdIndirizzo());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Errore nell'aggiornamento dell'indirizzo " + indirizzo.getIdIndirizzo(), e);
        }
    }

    @Override
    public void delete(int idIndirizzo) {
        String sql = "DELETE FROM indirizzo WHERE id_indirizzo = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idIndirizzo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Errore nell'eliminazione dell'indirizzo " + idIndirizzo, e);
        }
    }

    @Override
    public void azzeraPredefinito(int idUtente) {
        String sql = "UPDATE indirizzo SET predefinito = FALSE WHERE id_utente = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Errore nell'aggiornamento degli indirizzi predefiniti", e);
        }
    }

    private void bind(PreparedStatement stmt, Indirizzo indirizzo) throws SQLException {
        stmt.setInt(1, indirizzo.getIdUtente());
        stmt.setString(2, indirizzo.getEtichetta());
        stmt.setString(3, indirizzo.getVia());
        stmt.setString(4, indirizzo.getCitta());
        stmt.setString(5, indirizzo.getCap());
        stmt.setString(6, indirizzo.getProvincia());
        stmt.setString(7, indirizzo.getNazione());
        stmt.setBoolean(8, indirizzo.isPredefinito());
    }

    private Indirizzo mapRow(ResultSet rs) throws SQLException {
        Indirizzo indirizzo = new Indirizzo();
        indirizzo.setIdIndirizzo(rs.getInt("id_indirizzo"));
        indirizzo.setIdUtente(rs.getInt("id_utente"));
        indirizzo.setEtichetta(rs.getString("etichetta"));
        indirizzo.setVia(rs.getString("via"));
        indirizzo.setCitta(rs.getString("citta"));
        indirizzo.setCap(rs.getString("cap"));
        indirizzo.setProvincia(rs.getString("provincia"));
        indirizzo.setNazione(rs.getString("nazione"));
        indirizzo.setPredefinito(rs.getBoolean("predefinito"));
        return indirizzo;
    }
}
