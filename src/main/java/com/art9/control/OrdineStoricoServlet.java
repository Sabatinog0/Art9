package com.art9.control;

import com.art9.dao.JdbcOrdineDao;
import com.art9.dao.OrdineDao;
import com.art9.model.Utente;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/ordini")
public class OrdineStoricoServlet extends BaseServlet {

    private final OrdineDao ordineDao = new JdbcOrdineDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utente utente = utenteCorrente(req);
        req.setAttribute("pageTitle", "I tuoi ordini");
        req.setAttribute("ordini", ordineDao.findByUtente(utente.getIdUtente()));
        forward(req, resp, "/WEB-INF/views/ordini/storico.jsp");
    }
}
