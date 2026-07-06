package com.art9.control;

import com.art9.dao.JdbcOrdineDao;
import com.art9.dao.OrdineDao;
import com.art9.model.StatoOrdine;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;


@WebServlet({"/admin/ordini", "/admin/ordini/stato"})
public class AdminOrdiniServlet extends BaseServlet {

    private static final int DIMENSIONE_PAGINA = 20;

    private final OrdineDao ordineDao = new JdbcOrdineDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LocalDate dal = parseData(req.getParameter("dal"));
        LocalDate al = parseData(req.getParameter("al"));
        String cliente = trimToNull(req.getParameter("cliente"));
        int pagina = Math.max(1, parseIntSicuro(req.getParameter("pagina"), 1));

        int totale = ordineDao.countByFiltri(dal, al, cliente);
        int totalePagine = Math.max(1, (int) Math.ceil(totale / (double) DIMENSIONE_PAGINA));
        if (pagina > totalePagine) {
            pagina = totalePagine;
        }
        int offset = (pagina - 1) * DIMENSIONE_PAGINA;

        req.setAttribute("pageTitle", "Gestione ordini");
        req.setAttribute("ordini", ordineDao.findByFiltri(dal, al, cliente, offset, DIMENSIONE_PAGINA));
        req.setAttribute("dal", req.getParameter("dal"));
        req.setAttribute("al", req.getParameter("al"));
        req.setAttribute("cliente", cliente);
        req.setAttribute("paginaCorrente", pagina);
        req.setAttribute("totalePagine", totalePagine);
        req.setAttribute("totaleOrdini", totale);
        req.setAttribute("statiDisponibili", StatoOrdine.values());
        forward(req, resp, "/WEB-INF/views/admin/ordini.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int idOrdine = Integer.parseInt(req.getParameter("id"));
            StatoOrdine nuovoStato = StatoOrdine.valueOf(req.getParameter("stato"));
            ordineDao.aggiornaStato(idOrdine, nuovoStato);
            impostaMessaggioSuccesso(req, "Stato dell'ordine #" + idOrdine + " aggiornato.");
        } catch (IllegalArgumentException | NullPointerException e) {
            impostaMessaggioErrore(req, "Impossibile aggiornare lo stato dell'ordine.");
        }
        resp.sendRedirect(contextRelative(req, "/admin/ordini"));
    }

    private LocalDate parseData(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private int parseIntSicuro(String value, int valoreDefault) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException | NullPointerException e) {
            return valoreDefault;
        }
    }
}
