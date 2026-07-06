package com.art9.control;

import com.art9.dao.JdbcOrdineDao;
import com.art9.dao.JdbcPreferitoDao;
import com.art9.dao.JdbcProdottoDao;
import com.art9.dao.JdbcRecensioneDao;
import com.art9.dao.OrdineDao;
import com.art9.dao.PreferitoDao;
import com.art9.dao.ProdottoDao;
import com.art9.dao.RecensioneDao;
import com.art9.model.Prodotto;
import com.art9.model.Recensione;
import com.art9.model.Utente;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/prodotto")
public class ProdottoDetailServlet extends BaseServlet {

    private static final int LIMITE_CORRELATI = 4;

    private final ProdottoDao prodottoDao = new JdbcProdottoDao();
    private final RecensioneDao recensioneDao = new JdbcRecensioneDao();
    private final PreferitoDao preferitoDao = new JdbcPreferitoDao();
    private final OrdineDao ordineDao = new JdbcOrdineDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int idProdotto;
        try {
            idProdotto = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException | NullPointerException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Optional<Prodotto> prodottoOpt = prodottoDao.findById(idProdotto);
        if (prodottoOpt.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Prodotto prodotto = prodottoOpt.get();

        List<Recensione> recensioni = recensioneDao.findByProdotto(idProdotto);
        double mediaVoti = recensioneDao.mediaVoti(idProdotto);

        List<Prodotto> correlati = prodottoDao.findByFiltri(prodotto.getIdCategoria(), null, 0, LIMITE_CORRELATI + 1)
                .stream()
                .filter(p -> p.getIdProdotto() != idProdotto)
                .limit(LIMITE_CORRELATI)
                .toList();

        boolean isPreferito = false;
        boolean puoRecensire = false;
        Utente utente = utenteCorrente(req);
        if (utente != null) {
            isPreferito = preferitoDao.esiste(utente.getIdUtente(), idProdotto);
            boolean haAcquistato = ordineDao.utenteHaAcquistato(utente.getIdUtente(), idProdotto);
            boolean haGiaRecensito = recensioneDao.esisteRecensione(utente.getIdUtente(), idProdotto);
            puoRecensire = haAcquistato && !haGiaRecensito;
        }

        req.setAttribute("pageTitle", prodotto.getNome());
        req.setAttribute("prodotto", prodotto);
        req.setAttribute("recensioni", recensioni);
        req.setAttribute("mediaVoti", mediaVoti);
        req.setAttribute("mediaVotiArrotondata", Math.round(mediaVoti));
        req.setAttribute("correlati", correlati);
        req.setAttribute("isPreferito", isPreferito);
        req.setAttribute("puoRecensire", puoRecensire);

        forward(req, resp, "/WEB-INF/views/catalogo/dettaglio.jsp");
    }
}
