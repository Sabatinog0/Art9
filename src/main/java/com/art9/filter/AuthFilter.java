package com.art9.filter;

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

public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        boolean autenticato = session != null && session.getAttribute("utente") != null;

        if (autenticato) {
            chain.doFilter(request, response);
            return;
        }

        String destinazione = req.getRequestURI();
        String query = req.getQueryString();
        if (query != null) {
            destinazione += "?" + query;
        }
        String redirectParam = URLEncoder.encode(destinazione, StandardCharsets.UTF_8);
        resp.sendRedirect(req.getContextPath() + "/login?redirect=" + redirectParam);
    }
}
