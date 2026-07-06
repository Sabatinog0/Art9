package com.art9.control;

import com.art9.dao.JdbcUtenteDao;
import com.art9.dao.UtenteDao;
import com.art9.model.Utente;
import com.art9.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends BaseServlet {

    private final UtenteDao utenteDao = new JdbcUtenteDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (isAutenticato(req)) {
            resp.sendRedirect(destinazioneSicura(req, req.getParameter("redirect"), "/catalogo"));
            return;
        }
        req.setAttribute("pageTitle", "Accedi");
        forward(req, resp, "/WEB-INF/views/auth/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String redirect = req.getParameter("redirect");

        Optional<Utente> utenteOpt = email == null ? Optional.empty() : utenteDao.findByEmail(email.trim().toLowerCase());

        if (utenteOpt.isEmpty() || !PasswordUtil.matches(password, utenteOpt.get().getPasswordHash())) {
            req.setAttribute("pageTitle", "Accedi");
            req.setAttribute("erroreLogin", "Email o password non corretti.");
            req.setAttribute("valori", java.util.Map.of("email", email == null ? "" : email));
            req.setAttribute("redirect", redirect);
            forward(req, resp, "/WEB-INF/views/auth/login.jsp");
            return;
        }

        Utente utente = utenteOpt.get();
        HttpSession session = req.getSession(true);
        session.setAttribute("utente", utente);
        impostaMessaggioSuccesso(req, "Bentornato, " + utente.getNome() + "!");

        resp.sendRedirect(destinazioneSicura(req, redirect, "/catalogo"));
    }
}
