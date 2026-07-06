package com.art9.control;

import com.art9.dao.JdbcPreferitoDao;
import com.art9.dao.PreferitoDao;
import com.art9.model.Utente;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet({"/preferiti", "/preferiti/aggiungi", "/preferiti/rimuovi"})
public class PreferitiServlet extends BaseServlet {

    private final PreferitoDao preferitoDao = new JdbcPreferitoDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utente utente = utenteCorrente(req);
        req.setAttribute("pageTitle", "I tuoi preferiti");
        req.setAttribute("preferiti", preferitoDao.findByUtente(utente.getIdUtente()));
        forward(req, resp, "/WEB-INF/views/account/preferiti.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Utente utente = utenteCorrente(req);
        String path = req.getServletPath();
        Integer idProdotto = parseInt(req.getParameter("idProdotto"));

        if (idProdotto != null) {
            if (path.endsWith("/aggiungi")) {
                preferitoDao.aggiungi(utente.getIdUtente(), idProdotto);
                impostaMessaggioSuccesso(req, "Prodotto aggiunto ai preferiti.");
            } else if (path.endsWith("/rimuovi")) {
                preferitoDao.rimuovi(utente.getIdUtente(), idProdotto);
                impostaMessaggioSuccesso(req, "Prodotto rimosso dai preferiti.");
            }
        }

        resp.sendRedirect(destinazioneSicura(req, req.getParameter("ritorno"), "/preferiti"));
    }

    private Integer parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException | NullPointerException e) {
            return null;
        }
    }
}
