package com.art9.control;

import com.art9.dao.JdbcProdottoDao;
import com.art9.dao.ProdottoDao;
import com.art9.model.Prodotto;
import com.art9.util.JsonUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebServlet("/catalogo/suggerimenti")
public class SearchSuggestServlet extends BaseServlet {

    private static final int LIMITE_SUGGERIMENTI = 8;

    private final ProdottoDao prodottoDao = new JdbcProdottoDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String query = req.getParameter("q");
        List<Suggerimento> risultato;
        if (query == null || query.trim().length() < 2) {
            risultato = Collections.emptyList();
        } else {
            List<Prodotto> prodotti = prodottoDao.suggerimenti(query, LIMITE_SUGGERIMENTI);
            risultato = new ArrayList<>();
            for (Prodotto p : prodotti) {
                risultato.add(new Suggerimento(p.getIdProdotto(), p.getNome(), p.getPrezzo(), p.getImmagine()));
            }
        }
        JsonUtil.write(resp, risultato);
    }

    private static final class Suggerimento {
        final int idProdotto;
        final String nome;
        final BigDecimal prezzo;
        final String immagine;

        Suggerimento(int idProdotto, String nome, BigDecimal prezzo, String immagine) {
            this.idProdotto = idProdotto;
            this.nome = nome;
            this.prezzo = prezzo;
            this.immagine = immagine;
        }
    }
}
