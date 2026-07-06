package com.art9.dao;

import com.art9.model.Circuito;
import com.art9.model.MetodoPagamento;
import com.art9.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMetodoPagamentoDao implements MetodoPagamentoDao {

    private static final String SELECT_BASE =
            "SELECT id_metodo, id_utente, intestatario, numero_mascherato, scadenza, circuito, predefinito FROM metodo_pagamento";

    @Override
    public List<MetodoPagamento> findByUtente(int idUtente) {
        String sql = SELECT_BASE + " WHERE id_utente = ? ORDER BY predefinito DESC, id_metodo DESC";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            try (ResultSet rs = stmt.executeQuery()) {
                List<MetodoPagamento> risultato = new ArrayList<>();
                while (rs.next()) {
                    risultato.add(mapRow(rs));
                }
                return risultato;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Errore nel recupero dei metodi di pagamento", e);
        }
    }

    @Override
    public Optional<MetodoPagamento> findById(int idMetodo) {
        String sql = SELECT_BASE + " WHERE id_metodo = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idMetodo);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Errore nel recupero del metodo di pagamento " + idMetodo, e);
        }
    }

    @Override
    public MetodoPagamento insert(MetodoPagamento metodoPagamento) {
        String sql = "INSERT INTO metodo_pagamento (id_utente, intestatario, numero_mascherato, scadenza, circuito, predefinito) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, metodoPagamento.getIdUtente());
            stmt.setString(2, metodoPagamento.getIntestatario());
            stmt.setString(3, metodoPagamento.getNumeroMascherato());
            stmt.setString(4, metodoPagamento.getScadenza());
            stmt.setString(5, metodoPagamento.getCircuito().name());
            stmt.setBoolean(6, metodoPagamento.isPredefinito());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    metodoPagamento.setIdMetodo(keys.getInt(1));
                }
            }
            return metodoPagamento;
        } catch (SQLException e) {
            throw new DataAccessException("Errore nella creazione del metodo di pagamento", e);
        }
    }

    @Override
    public void update(MetodoPagamento metodoPagamento) {
        String sql = "UPDATE metodo_pagamento SET intestatario = ?, numero_mascherato = ?, scadenza = ?, "
                + "circuito = ?, predefinito = ? WHERE id_metodo = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, metodoPagamento.getIntestatario());
            stmt.setString(2, metodoPagamento.getNumeroMascherato());
            stmt.setString(3, metodoPagamento.getScadenza());
            stmt.setString(4, metodoPagamento.getCircuito().name());
            stmt.setBoolean(5, metodoPagamento.isPredefinito());
            stmt.setInt(6, metodoPagamento.getIdMetodo());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Errore nell'aggiornamento del metodo di pagamento " + metodoPagamento.getIdMetodo(), e);
        }
    }

    @Override
    public void delete(int idMetodo) {
        String sql = "DELETE FROM metodo_pagamento WHERE id_metodo = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idMetodo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Errore nell'eliminazione del metodo di pagamento " + idMetodo, e);
        }
    }

    @Override
    public void azzeraPredefinito(int idUtente) {
        String sql = "UPDATE metodo_pagamento SET predefinito = FALSE WHERE id_utente = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Errore nell'aggiornamento dei metodi di pagamento predefiniti", e);
        }
    }

    private MetodoPagamento mapRow(ResultSet rs) throws SQLException {
        MetodoPagamento metodo = new MetodoPagamento();
        metodo.setIdMetodo(rs.getInt("id_metodo"));
        metodo.setIdUtente(rs.getInt("id_utente"));
        metodo.setIntestatario(rs.getString("intestatario"));
        metodo.setNumeroMascherato(rs.getString("numero_mascherato"));
        metodo.setScadenza(rs.getString("scadenza"));
        metodo.setCircuito(Circuito.valueOf(rs.getString("circuito")));
        metodo.setPredefinito(rs.getBoolean("predefinito"));
        return metodo;
    }
}
