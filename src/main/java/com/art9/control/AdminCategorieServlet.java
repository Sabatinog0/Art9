package com.art9.control;

import com.art9.model.Categoria;
import com.art9.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;


@WebServlet({"/admin/categorie", "/admin/categorie/elimina"})
public class AdminCategorieServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageTitle", "Gestione categorie");
        req.setAttribute("categorie", categoriaDao.findAll());
        forward(req, resp, "/WEB-INF/views/admin/categorie.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getServletPath().endsWith("/elimina")) {
            eliminaCategoria(req, resp);
            return;
        }
        aggiungiCategoria(req, resp);
    }

    private void aggiungiCategoria(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String nome = req.getParameter("nome");
        String descrizione = req.getParameter("descrizione");

        Map<String, String> errori = new HashMap<>();
        if (ValidationUtil.isBlank(nome) || nome.trim().length() < 2) {
            errori.put("nome", "Inserisci un nome categoria valido (almeno 2 caratteri).");
        }
        String slug = nome == null ? "" : generaSlug(nome);
        if (!errori.isEmpty() || categoriaDao.findBySlug(slug).isPresent()) {
            if (errori.isEmpty()) {
                errori.put("nome", "Esiste gia' una categoria con questo nome.");
            }
            req.setAttribute("pageTitle", "Gestione categorie");
            req.setAttribute("errori", errori);
            req.setAttribute("categorie", categoriaDao.findAll());
            forward(req, resp, "/WEB-INF/views/admin/categorie.jsp");
            return;
        }

        Categoria categoria = new Categoria();
        categoria.setNome(nome.trim());
        categoria.setSlug(slug);
        categoria.setDescrizione(descrizione == null ? "" : descrizione.trim());
        categoriaDao.insert(categoria);

        impostaMessaggioSuccesso(req, "Categoria creata correttamente.");
        resp.sendRedirect(contextRelative(req, "/admin/categorie"));
    }

    private void eliminaCategoria(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Optional<Integer> idOpt = parseId(req.getParameter("id"));
        if (idOpt.isPresent()) {
            int id = idOpt.get();
            if (categoriaDao.countProdotti(id) > 0) {
                impostaMessaggioErrore(req, "Non puoi eliminare una categoria che contiene ancora dei prodotti.");
            } else {
                categoriaDao.delete(id);
                impostaMessaggioSuccesso(req, "Categoria eliminata.");
            }
        }
        resp.sendRedirect(contextRelative(req, "/admin/categorie"));
    }

    private String generaSlug(String nome) {
        return nome.trim().toLowerCase(Locale.ITALIAN)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }

    private Optional<Integer> parseId(String value) {
        try {
            return Optional.of(Integer.parseInt(value));
        } catch (NumberFormatException | NullPointerException e) {
            return Optional.empty();
        }
    }
}
