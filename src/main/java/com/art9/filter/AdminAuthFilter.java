package com.art9.filter;

import com.art9.model.Utente;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


public class AdminAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        Utente utente = session == null ? null : (Utente) session.getAttribute("utente");

        if (utente == null) {
            String destinazione = req.getRequestURI();
            String redirectParam = URLEncoder.encode(destinazione, StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/login?redirect=" + redirectParam);
            return;
        }

        if (!utente.isAdmin()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Accesso riservato agli amministratori.");
            return;
        }

        chain.doFilter(request, response);
    }
}
