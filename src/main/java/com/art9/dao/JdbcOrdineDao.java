package com.art9.dao;

import com.art9.model.Ordine;
import com.art9.model.RigaOrdine;
import com.art9.model.StatoOrdine;
import com.art9.util.DatabaseManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcOrdineDao implements OrdineDao {

    private static final String SELECT_ORDINE_BASE =
            "SELECT o.id_ordine, o.id_utente, u.nome AS nome_cliente, u.cognome AS cognome_cliente, "
                    + "u.email AS email_cliente, o.id_metodo_pagamento, o.data_ordine, o.stato, o.totale, "
                    + "o.indirizzo_spedizione FROM ordine o JOIN utente u ON o.id_utente = u.id_utente";

    @Override
    public Ordine inserisciConRighe(Ordine ordine) {
        String sqlOrdine = "INSERT INTO ordine (id_utente, id_metodo_pagamento, stato, totale, indirizzo_spedizione) "
                + "VALUES (?, ?, ?, ?, ?)";
        String sqlRiga = "INSERT INTO riga_ordine (id_ordine, id_prodotto, nome_prodotto, prezzo_unitario, "
                + "iva_percentuale, quantita) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlDecrementa = "UPDATE prodotto SET quantita_disponibile = GREATEST(quantita_disponibile - ?, 0) "
                + "WHERE id_prodotto = ?";

        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement stmt = conn.prepareStatement(sqlOrdine, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setInt(1, ordine.getIdUtente());
                    if (ordine.getIdMetodoPagamento() != null) {
                        stmt.setInt(2, ordine.getIdMetodoPagamento());
                    } else {
                        stmt.setNull(2, java.sql.Types.INTEGER);
                    }
                    stmt.setString(3, ordine.getStato().name());
                    stmt.setBigDecimal(4, ordine.getTotale());
                    stmt.setString(5, ordine.getIndirizzoSpedizione());
                    stmt.executeUpdate();
                    try (ResultSet keys = stmt.getGeneratedKeys()) {
                        if (keys.next()) {
                            ordine.setIdOrdine(keys.getInt(1));
                        }
                    }
                }

                try (PreparedStatement stmtRiga = conn.prepareStatement(sqlRiga);
                     PreparedStatement stmtDecrementa = conn.prepareStatement(sqlDecrementa)) {
                    for (RigaOrdine riga : ordine.getRighe()) {
                        stmtRiga.setInt(1, ordine.getIdOrdine());
                        if (riga.getIdProdotto() != null) {
                            stmtRiga.setInt(2, riga.getIdProdotto());
                        } else {
                            stmtRiga.setNull(2, java.sql.Types.INTEGER);
                        }
                        stmtRiga.setString(3, riga.getNomeProdotto());
                        stmtRiga.setBigDecimal(4, riga.getPrezzoUnitario());
                        stmtRiga.setBigDecimal(5, riga.getIvaPercentuale());
                        stmtRiga.setInt(6, riga.getQuantita());
                        stmtRiga.executeUpdate();

                        if (riga.getIdProdotto() != null) {
                            stmtDecrementa.setInt(1, riga.getQuantita());
                            stmtDecrementa.setInt(2, riga.getIdProdotto());
                            stmtDecrementa.executeUpdate();
                        }
                    }
                }

                conn.commit();
                return ordine;
            } catch (SQLException e) {
                conn.rollback();
                throw new DataAccessException("Errore nella creazione dell'ordine, transazione annullata", e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Errore di connessione durante la creazione dell'ordine", e);
        }
    }

    @Override
    public Optional<Ordine> findById(int idOrdine) {
        String sql = SELECT_ORDINE_BASE + " WHERE o.id_ordine = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idOrdine);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                Ordine ordine = mapRow(rs);
                ordine.setRighe(findRighe(conn, idOrdine));
                return Optional.of(ordine);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Errore nel recupero dell'ordine " + idOrdine, e);
        }
    }

    @Override
    public List<Ordine> findByUtente(int idUtente) {
        String sql = SELECT_ORDINE_BASE + " WHERE o.id_utente = ? ORDER BY o.data_ordine DESC";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Ordine> risultato = new ArrayList<>();
                while (rs.next()) {
                    Ordine ordine = mapRow(rs);
                    ordine.setRighe(findRighe(conn, ordine.getIdOrdine()));
                    risultato.add(ordine);
                }
                return risultato;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Errore nel recupero degli ordini dell'utente " + idUtente, e);
        }
    }

    @Override
    public List<Ordine> findByFiltri(LocalDate dal, LocalDate al, String cliente, int offset, int limit) {
        StringBuilder sql = new StringBuilder(SELECT_ORDINE_BASE);
        List<Object> parametri = new ArrayList<>();
        appendFiltri(sql, parametri, dal, al, cliente);
        sql.append(" ORDER BY o.data_ordine DESC LIMIT ? OFFSET ?");
        parametri.add(limit);
        parametri.add(offset);

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            bindParametri(stmt, parametri);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Ordine> risultato = new ArrayList<>();
                while (rs.next()) {
                    risultato.add(mapRow(rs));
                }
                return risultato;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Errore nella ricerca ordini", e);
        }
    }

    @Override
    public int countByFiltri(LocalDate dal, LocalDate al, String cliente) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM ordine o JOIN utente u ON o.id_utente = u.id_utente");
        List<Object> parametri = new ArrayList<>();
        appendFiltri(sql, parametri, dal, al, cliente);

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            bindParametri(stmt, parametri);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Errore nel conteggio ordini", e);
        }
    }

    private void appendFiltri(StringBuilder sql, List<Object> parametri, LocalDate dal, LocalDate al, String cliente) {
        List<String> condizioni = new ArrayList<>();
        if (dal != null) {
            condizioni.add("o.data_ordine >= ?");
            parametri.add(Timestamp.valueOf(dal.atStartOfDay()));
        }
        if (al != null) {
            condizioni.add("o.data_ordine < ?");
            parametri.add(Timestamp.valueOf(al.plusDays(1).atStartOfDay()));
        }
        if (cliente != null && !cliente.isBlank()) {
            condizioni.add("(u.nome LIKE ? OR u.cognome LIKE ? OR u.email LIKE ?)");
            String pattern = "%" + cliente.trim() + "%";
            parametri.add(pattern);
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
    public void aggiornaStato(int idOrdine, StatoOrdine nuovoStato) {
        String sql = "UPDATE ordine SET stato = ? WHERE id_ordine = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nuovoStato.name());
            stmt.setInt(2, idOrdine);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Errore nell'aggiornamento dello stato dell'ordine " + idOrdine, e);
        }
    }

    @Override
    public boolean utenteHaAcquistato(int idUtente, int idProdotto) {
        String sql = "SELECT 1 FROM ordine o JOIN riga_ordine r ON o.id_ordine = r.id_ordine "
                + "WHERE o.id_utente = ? AND r.id_prodotto = ? LIMIT 1";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            stmt.setInt(2, idProdotto);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Errore nella verifica dell'acquisto", e);
        }
    }

    @Override
    public BigDecimal fatturatoTotale() {
        String sql = "SELECT COALESCE(SUM(totale), 0) FROM ordine WHERE stato <> 'ANNULLATO'";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
        } catch (SQLException e) {
            throw new DataAccessException("Errore nel calcolo del fatturato totale", e);
        }
    }

    private List<RigaOrdine> findRighe(Connection conn, int idOrdine) throws SQLException {
        String sql = "SELECT id_riga, id_ordine, id_prodotto, nome_prodotto, prezzo_unitario, iva_percentuale, quantita "
                + "FROM riga_ordine WHERE id_ordine = ? ORDER BY id_riga";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idOrdine);
            try (ResultSet rs = stmt.executeQuery()) {
                List<RigaOrdine> righe = new ArrayList<>();
                while (rs.next()) {
                    RigaOrdine riga = new RigaOrdine();
                    riga.setIdRiga(rs.getInt("id_riga"));
                    riga.setIdOrdine(rs.getInt("id_ordine"));
                    int idProdotto = rs.getInt("id_prodotto");
                    riga.setIdProdotto(rs.wasNull() ? null : idProdotto);
                    riga.setNomeProdotto(rs.getString("nome_prodotto"));
                    riga.setPrezzoUnitario(rs.getBigDecimal("prezzo_unitario"));
                    riga.setIvaPercentuale(rs.getBigDecimal("iva_percentuale"));
                    riga.setQuantita(rs.getInt("quantita"));
                    righe.add(riga);
                }
                return righe;
            }
        }
    }

    private Ordine mapRow(ResultSet rs) throws SQLException {
        Ordine ordine = new Ordine();
        ordine.setIdOrdine(rs.getInt("id_ordine"));
        ordine.setIdUtente(rs.getInt("id_utente"));
        ordine.setNomeCliente(rs.getString("nome_cliente"));
        ordine.setCognomeCliente(rs.getString("cognome_cliente"));
        ordine.setEmailCliente(rs.getString("email_cliente"));
        int idMetodoPagamento = rs.getInt("id_metodo_pagamento");
        ordine.setIdMetodoPagamento(rs.wasNull() ? null : idMetodoPagamento);
        Timestamp dataOrdine = rs.getTimestamp("data_ordine");
        if (dataOrdine != null) {
            ordine.setDataOrdine(dataOrdine.toLocalDateTime());
        }
        ordine.setStato(StatoOrdine.valueOf(rs.getString("stato")));
        ordine.setTotale(rs.getBigDecimal("totale"));
        ordine.setIndirizzoSpedizione(rs.getString("indirizzo_spedizione"));
        return ordine;
    }
}
