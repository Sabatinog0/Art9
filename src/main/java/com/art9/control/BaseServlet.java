package com.art9.control;

import com.art9.dao.CategoriaDao;
import com.art9.dao.JdbcCategoriaDao;
import com.art9.model.Carrello;
import com.art9.model.FlashMessage;
import com.art9.model.Utente;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public abstract class BaseServlet extends HttpServlet {

    protected final CategoriaDao categoriaDao = new JdbcCategoriaDao();

    
    protected void forward(HttpServletRequest req, HttpServletResponse resp, String jspPath)
        throws ServletException, IOException {
    String percorsoCorrente = req.getServletPath();
    if (req.getQueryString() != null) {
        percorsoCorrente += "?" + req.getQueryString();
    }
    req.setAttribute("percorsoCorrente", percorsoCorrente);

    req.setAttribute("categorieMenu", categoriaDao.findAll());
    req.setAttribute("carrello", carrelloCorrente(req));
    req.getRequestDispatcher(jspPath).forward(req, resp);
}

    protected Utente utenteCorrente(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session == null ? null : (Utente) session.getAttribute("utente");
    }

    protected boolean isAutenticato(HttpServletRequest req) {
        return utenteCorrente(req) != null;
    }

    protected Carrello carrelloCorrente(HttpServletRequest req) {
        HttpSession session = req.getSession(true);
        Carrello carrello = (Carrello) session.getAttribute("carrello");
        if (carrello == null) {
            carrello = new Carrello();
            session.setAttribute("carrello", carrello);
        }
        return carrello;
    }

    protected void impostaMessaggioSuccesso(HttpServletRequest req, String testo) {
        req.getSession(true).setAttribute("flash", FlashMessage.successo(testo));
    }

    protected void impostaMessaggioErrore(HttpServletRequest req, String testo) {
        req.getSession(true).setAttribute("flash", FlashMessage.errore(testo));
    }

    protected String contextRelative(HttpServletRequest req, String path) {
        return req.getContextPath() + path;
    }

    protected String destinazioneSicura(HttpServletRequest req, String redirect, String fallback) {
        if (redirect != null && redirect.startsWith("/") && !redirect.startsWith("//")) {
            return req.getContextPath() + redirect;
        }
        return req.getContextPath() + fallback;
    }
}
