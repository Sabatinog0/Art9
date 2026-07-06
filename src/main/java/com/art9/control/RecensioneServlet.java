package com.art9.control;

import com.art9.dao.JdbcOrdineDao;
import com.art9.dao.JdbcRecensioneDao;
import com.art9.dao.OrdineDao;
import com.art9.dao.RecensioneDao;
import com.art9.model.Recensione;
import com.art9.model.Utente;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/recensioni")
public class RecensioneServlet extends BaseServlet {

    private final RecensioneDao recensioneDao = new JdbcRecensioneDao();
    private final OrdineDao ordineDao = new JdbcOrdineDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Utente utente = utenteCorrente(req);
        Integer idProdotto = parseInt(req.getParameter("idProdotto"));
        Integer voto = parseInt(req.getParameter("voto"));
        String testo = req.getParameter("testo");

        if (idProdotto == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        boolean votoValido = voto != null && voto >= 1 && voto <= 5;
        boolean testoValido = testo != null && testo.trim().length() >= 2 && testo.trim().length() <= 1000;
        boolean haAcquistato = ordineDao.utenteHaAcquistato(utente.getIdUtente(), idProdotto);
        boolean giaRecensito = recensioneDao.esisteRecensione(utente.getIdUtente(), idProdotto);

        if (!votoValido || !testoValido || !haAcquistato || giaRecensito) {
            impostaMessaggioErrore(req, giaRecensito
                    ? "Hai gia' recensito questo prodotto."
                    : "Non e' possibile pubblicare questa recensione.");
            resp.sendRedirect(contextRelative(req, "/prodotto?id=" + idProdotto));
            return;
        }

        Recensione recensione = new Recensione();
        recensione.setIdUtente(utente.getIdUtente());
        recensione.setIdProdotto(idProdotto);
        recensione.setVoto(voto);
        recensione.setTesto(testo.trim());
        recensioneDao.insert(recensione);

        impostaMessaggioSuccesso(req, "Recensione pubblicata, grazie per il tuo feedback!");
        resp.sendRedirect(contextRelative(req, "/prodotto?id=" + idProdotto + "#recensioni"));
    }

    private Integer parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException | NullPointerException e) {
            return null;
        }
    }
}
