package com.art9.control;

import com.art9.dao.JdbcOrdineDao;
import com.art9.dao.OrdineDao;
import com.art9.model.Ordine;
import com.art9.model.Utente;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;


@WebServlet("/ordini/dettaglio")
public class OrdineDettaglioServlet extends BaseServlet {

    private final OrdineDao ordineDao = new JdbcOrdineDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int idOrdine;
        try {
            idOrdine = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException | NullPointerException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Optional<Ordine> ordineOpt = ordineDao.findById(idOrdine);
        if (ordineOpt.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Ordine ordine = ordineOpt.get();
        Utente utente = utenteCorrente(req);
        boolean proprietario = ordine.getIdUtente() == utente.getIdUtente();
        if (!proprietario && !utente.isAdmin()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        req.setAttribute("pageTitle", "Ordine #" + ordine.getIdOrdine());
        req.setAttribute("ordine", ordine);
        forward(req, resp, "/WEB-INF/views/ordini/dettaglio.jsp");
    }
}
