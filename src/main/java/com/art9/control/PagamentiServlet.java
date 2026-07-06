package com.art9.control;

import com.art9.dao.JdbcMetodoPagamentoDao;
import com.art9.dao.MetodoPagamentoDao;
import com.art9.model.Circuito;
import com.art9.model.MetodoPagamento;
import com.art9.model.Utente;
import com.art9.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@WebServlet({"/account/pagamenti", "/account/pagamenti/elimina", "/account/pagamenti/predefinito"})
public class PagamentiServlet extends BaseServlet {

    private final MetodoPagamentoDao metodoPagamentoDao = new JdbcMetodoPagamentoDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utente utente = utenteCorrente(req);
        req.setAttribute("pageTitle", "Metodi di pagamento");
        req.setAttribute("metodiPagamento", metodoPagamentoDao.findByUtente(utente.getIdUtente()));
        forward(req, resp, "/WEB-INF/views/account/pagamenti.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utente utente = utenteCorrente(req);
        String path = req.getServletPath();

        if (path.endsWith("/elimina")) {
            eliminaSeProprietario(req, utente);
        } else if (path.endsWith("/predefinito")) {
            impostaPredefinitoSeProprietario(req, utente);
        } else {
            aggiungiMetodo(req, resp, utente);
            return;
        }
        resp.sendRedirect(contextRelative(req, "/account/pagamenti"));
    }

    private void aggiungiMetodo(HttpServletRequest req, HttpServletResponse resp, Utente utente)
            throws ServletException, IOException {
        String intestatario = req.getParameter("intestatario");
        String numeroCarta = req.getParameter("numeroCarta");
        String scadenza = req.getParameter("scadenza");
        String circuitoParam = req.getParameter("circuito");

        Map<String, String> errori = new HashMap<>();
        if (!ValidationUtil.isValidNome(intestatario)) {
            errori.put("intestatario", "Inserisci l'intestatario della carta.");
        }
        if (!ValidationUtil.isValidNumeroCarta(numeroCarta)) {
            errori.put("numeroCarta", "Numero carta non valido (13-19 cifre).");
        }
        if (!ValidationUtil.isValidScadenzaCarta(scadenza)) {
            errori.put("scadenza", "Formato scadenza non valido (MM/AA).");
        }
        Circuito circuito = null;
        try {
            circuito = Circuito.valueOf(circuitoParam);
        } catch (IllegalArgumentException | NullPointerException e) {
            errori.put("circuito", "Seleziona il circuito della carta.");
        }

        if (!errori.isEmpty()) {
            req.setAttribute("pageTitle", "Metodi di pagamento");
            req.setAttribute("errori", errori);
            req.setAttribute("metodiPagamento", metodoPagamentoDao.findByUtente(utente.getIdUtente()));
            forward(req, resp, "/WEB-INF/views/account/pagamenti.jsp");
            return;
        }

        boolean primoMetodo = metodoPagamentoDao.findByUtente(utente.getIdUtente()).isEmpty();

        MetodoPagamento metodo = new MetodoPagamento();
        metodo.setIdUtente(utente.getIdUtente());
        metodo.setIntestatario(intestatario.trim());
        metodo.setNumeroMascherato(MetodoPagamento.mascheraNumero(numeroCarta));
        metodo.setScadenza(scadenza.trim());
        metodo.setCircuito(circuito);
        metodo.setPredefinito(primoMetodo);
        metodoPagamentoDao.insert(metodo);

        impostaMessaggioSuccesso(req, "Metodo di pagamento aggiunto correttamente.");
        resp.sendRedirect(contextRelative(req, "/account/pagamenti"));
    }

    private void eliminaSeProprietario(HttpServletRequest req, Utente utente) {
        parseId(req.getParameter("id")).ifPresent(id -> {
            Optional<MetodoPagamento> metodo = metodoPagamentoDao.findById(id);
            if (metodo.isPresent() && metodo.get().getIdUtente() == utente.getIdUtente()) {
                metodoPagamentoDao.delete(id);
                impostaMessaggioSuccesso(req, "Metodo di pagamento eliminato.");
            }
        });
    }

    private void impostaPredefinitoSeProprietario(HttpServletRequest req, Utente utente) {
        parseId(req.getParameter("id")).ifPresent(id -> {
            Optional<MetodoPagamento> metodoOpt = metodoPagamentoDao.findById(id);
            if (metodoOpt.isPresent() && metodoOpt.get().getIdUtente() == utente.getIdUtente()) {
                metodoPagamentoDao.azzeraPredefinito(utente.getIdUtente());
                MetodoPagamento metodo = metodoOpt.get();
                metodo.setPredefinito(true);
                metodoPagamentoDao.update(metodo);
                impostaMessaggioSuccesso(req, "Metodo di pagamento predefinito aggiornato.");
            }
        });
    }

    private Optional<Integer> parseId(String value) {
        try {
            return Optional.of(Integer.parseInt(value));
        } catch (NumberFormatException | NullPointerException e) {
            return Optional.empty();
        }
    }
}
