package com.art9.control;

import com.art9.dao.JdbcPreferitoDao;
import com.art9.dao.JdbcProdottoDao;
import com.art9.dao.PreferitoDao;
import com.art9.dao.ProdottoDao;
import com.art9.model.Categoria;
import com.art9.model.Prodotto;
import com.art9.model.Utente;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet("/catalogo")
public class ProdottoListServlet extends BaseServlet {

    private static final int DIMENSIONE_PAGINA = 12;

    private final ProdottoDao prodottoDao = new JdbcProdottoDao();
    private final PreferitoDao preferitoDao = new JdbcPreferitoDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String slugCategoria = trimToNull(req.getParameter("categoria"));
        String query = trimToNull(req.getParameter("q"));
        int pagina = parsePaginaPositiva(req.getParameter("pagina"));

        Categoria categoriaCorrente = null;
        Integer idCategoria = null;
        if (slugCategoria != null) {
            Optional<Categoria> trovata = categoriaDao.findBySlug(slugCategoria);
            if (trovata.isPresent()) {
                categoriaCorrente = trovata.get();
                idCategoria = categoriaCorrente.getIdCategoria();
            }
        }

        int totaleProdotti = prodottoDao.countByFiltri(idCategoria, query);
        int totalePagine = Math.max(1, (int) Math.ceil(totaleProdotti / (double) DIMENSIONE_PAGINA));
        if (pagina > totalePagine) {
            pagina = totalePagine;
        }
        int offset = (pagina - 1) * DIMENSIONE_PAGINA;

        List<Prodotto> prodotti = prodottoDao.findByFiltri(idCategoria, query, offset, DIMENSIONE_PAGINA);

        boolean mostraHero = categoriaCorrente == null && query == null && pagina == 1;

        req.setAttribute("pageTitle", titoloPagina(categoriaCorrente, query));
        req.setAttribute("prodotti", prodotti);
        req.setAttribute("categoriaCorrente", categoriaCorrente);
        req.setAttribute("query", query);
        req.setAttribute("paginaCorrente", pagina);
        req.setAttribute("totalePagine", totalePagine);
        req.setAttribute("totaleProdotti", totaleProdotti);
        req.setAttribute("mostraHero", mostraHero);

        Utente utente = utenteCorrente(req);
        if (utente != null) {
            Set<Integer> preferitiUtente = preferitoDao.findByUtente(utente.getIdUtente()).stream()
                    .map(preferito -> preferito.getProdotto().getIdProdotto())
                    .collect(Collectors.toSet());
            req.setAttribute("preferitiUtente", preferitiUtente);
        }

        forward(req, resp, "/WEB-INF/views/catalogo/lista.jsp");
    }

    private String titoloPagina(Categoria categoria, String query) {
        if (query != null) {
            return "Risultati per \"" + query + "\"";
        }
        if (categoria != null) {
            return categoria.getNome();
        }
        return "Catalogo";
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private int parsePaginaPositiva(String value) {
        try {
            int pagina = Integer.parseInt(value);
            return Math.max(pagina, 1);
        } catch (NumberFormatException | NullPointerException e) {
            return 1;
        }
    }
}
