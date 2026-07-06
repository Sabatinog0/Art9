package com.art9.control;

import com.art9.dao.JdbcRecensioneDao;
import com.art9.dao.RecensioneDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet({"/admin/recensioni", "/admin/recensioni/elimina"})
public class AdminRecensioniServlet extends BaseServlet {

    private final RecensioneDao recensioneDao = new JdbcRecensioneDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageTitle", "Gestione recensioni");
        req.setAttribute("recensioni", recensioneDao.findAll());
        forward(req, resp, "/WEB-INF/views/admin/recensioni.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int idRecensione = Integer.parseInt(req.getParameter("id"));
            recensioneDao.delete(idRecensione);
            impostaMessaggioSuccesso(req, "Recensione eliminata.");
        } catch (NumberFormatException | NullPointerException e) {
            impostaMessaggioErrore(req, "Impossibile eliminare la recensione.");
        }
        resp.sendRedirect(contextRelative(req, "/admin/recensioni"));
    }
}
