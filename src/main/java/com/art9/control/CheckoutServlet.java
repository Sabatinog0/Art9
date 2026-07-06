package com.art9.control;

import com.art9.dao.IndirizzoDao;
import com.art9.dao.JdbcIndirizzoDao;
import com.art9.dao.JdbcMetodoPagamentoDao;
import com.art9.dao.JdbcOrdineDao;
import com.art9.dao.MetodoPagamentoDao;
import com.art9.dao.OrdineDao;
import com.art9.model.Carrello;
import com.art9.model.CarrelloItem;
import com.art9.model.Circuito;
import com.art9.model.Indirizzo;
import com.art9.model.MetodoPagamento;
import com.art9.model.Ordine;
import com.art9.model.RigaOrdine;
import com.art9.model.StatoOrdine;
import com.art9.model.Utente;
import com.art9.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@WebServlet("/checkout")
public class CheckoutServlet extends BaseServlet {

    private final IndirizzoDao indirizzoDao = new JdbcIndirizzoDao();
    private final MetodoPagamentoDao metodoPagamentoDao = new JdbcMetodoPagamentoDao();
    private final OrdineDao ordineDao = new JdbcOrdineDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Carrello carrello = carrelloCorrente(req);
        if (carrello.isEmpty()) {
            impostaMessaggioErrore(req, "Il tuo carrello e' vuoto: aggiungi qualche prodotto prima di procedere.");
            resp.sendRedirect(contextRelative(req, "/carrello"));
            return;
        }

        Utente utente = utenteCorrente(req);
        req.setAttribute("pageTitle", "Checkout");
        req.setAttribute("indirizzi", indirizzoDao.findByUtente(utente.getIdUtente()));
        req.setAttribute("metodiPagamento", metodoPagamentoDao.findByUtente(utente.getIdUtente()));
        forward(req, resp, "/WEB-INF/views/carrello/checkout.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Carrello carrello = carrelloCorrente(req);
        Utente utente = utenteCorrente(req);

        if (carrello.isEmpty()) {
            impostaMessaggioErrore(req, "Il tuo carrello e' vuoto.");
            resp.sendRedirect(contextRelative(req, "/carrello"));
            return;
        }

        Map<String, String> errori = new HashMap<>();
        String indirizzoSpedizione = risolviIndirizzo(req, utente, errori);
        Integer idMetodoPagamento = risolviMetodoPagamento(req, utente, errori);

        if (!errori.isEmpty()) {
            req.setAttribute("pageTitle", "Checkout");
            req.setAttribute("errori", errori);
            req.setAttribute("indirizzi", indirizzoDao.findByUtente(utente.getIdUtente()));
            req.setAttribute("metodiPagamento", metodoPagamentoDao.findByUtente(utente.getIdUtente()));
            forward(req, resp, "/WEB-INF/views/carrello/checkout.jsp");
            return;
        }

        Ordine ordine = new Ordine();
        ordine.setIdUtente(utente.getIdUtente());
        ordine.setIdMetodoPagamento(idMetodoPagamento);
        ordine.setStato(StatoOrdine.IN_ATTESA);
        ordine.setTotale(carrello.getTotale());
        ordine.setIndirizzoSpedizione(indirizzoSpedizione);

        List<RigaOrdine> righe = new java.util.ArrayList<>();
        for (CarrelloItem item : carrello.getItems()) {
            RigaOrdine riga = new RigaOrdine();
            riga.setIdProdotto(item.getIdProdotto());
            riga.setNomeProdotto(item.getNome());
            riga.setPrezzoUnitario(item.getPrezzoUnitario());
            riga.setIvaPercentuale(item.getIvaPercentuale());
            riga.setQuantita(item.getQuantita());
            righe.add(riga);
        }
        ordine.setRighe(righe);

        Ordine ordineSalvato = ordineDao.inserisciConRighe(ordine);
        carrello.svuota();

        impostaMessaggioSuccesso(req, "Ordine #" + ordineSalvato.getIdOrdine() + " confermato! Grazie per il tuo acquisto.");
        resp.sendRedirect(contextRelative(req, "/ordini/dettaglio?id=" + ordineSalvato.getIdOrdine()));
    }

    private String risolviIndirizzo(HttpServletRequest req, Utente utente, Map<String, String> errori) {
        String idIndirizzoParam = req.getParameter("idIndirizzoSalvato");
        if (idIndirizzoParam != null && !idIndirizzoParam.isBlank() && !"nuovo".equals(idIndirizzoParam)) {
            try {
                int idIndirizzo = Integer.parseInt(idIndirizzoParam);
                Optional<Indirizzo> indirizzoOpt = indirizzoDao.findById(idIndirizzo);
                if (indirizzoOpt.isPresent() && indirizzoOpt.get().getIdUtente() == utente.getIdUtente()) {
                    return indirizzoOpt.get().formatoEsteso();
                }
            } catch (NumberFormatException ignored) {
            }
        }

        String via = req.getParameter("via");
        String citta = req.getParameter("citta");
        String cap = req.getParameter("cap");
        String provincia = req.getParameter("provincia");
        String nazione = req.getParameter("nazione");

        if (!ValidationUtil.isValidVia(via)) {
            errori.put("via", "Inserisci un indirizzo valido (almeno 5 caratteri).");
        }
        if (!ValidationUtil.isValidCitta(citta)) {
            errori.put("citta", "Inserisci una citta' valida (2-80 caratteri).");
        }
        if (!ValidationUtil.isValidCap(cap)) {
            errori.put("cap", "Il CAP deve essere di 5 cifre.");
        }
        if (!ValidationUtil.isValidProvincia(provincia)) {
            errori.put("provincia", "Inserisci la sigla provincia (2 lettere maiuscole).");
        }
        if (!ValidationUtil.isValidNazione(nazione)) {
            errori.put("nazione", "Inserisci una nazione valida (2-60 caratteri).");
        }

        if (!errori.isEmpty()) {
            return null;
        }
        return via + ", " + cap + " " + citta + " (" + provincia.toUpperCase() + "), " + nazione;
    }

    private Integer risolviMetodoPagamento(HttpServletRequest req, Utente utente, Map<String, String> errori) {
        String idMetodoParam = req.getParameter("idMetodoSalvato");
        if (idMetodoParam != null && !idMetodoParam.isBlank() && !"nuovo".equals(idMetodoParam)) {
            try {
                int idMetodo = Integer.parseInt(idMetodoParam);
                Optional<MetodoPagamento> metodoOpt = metodoPagamentoDao.findById(idMetodo);
                if (metodoOpt.isPresent() && metodoOpt.get().getIdUtente() == utente.getIdUtente()) {
                    return idMetodo;
                }
            } catch (NumberFormatException ignored) {
            }
        }

        String numeroCarta = req.getParameter("numeroCarta");
        String intestatario = req.getParameter("intestatario");
        String scadenza = req.getParameter("scadenza");
        String circuitoParam = req.getParameter("circuito");

        if (!ValidationUtil.isValidNumeroCarta(numeroCarta)) {
            errori.put("numeroCarta", "Numero carta non valido (13-19 cifre).");
        }
        if (!ValidationUtil.isValidNome(intestatario)) {
            errori.put("intestatario", "Inserisci l'intestatario della carta.");
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
            return null;
        }

        MetodoPagamento nuovoMetodo = new MetodoPagamento();
        nuovoMetodo.setIdUtente(utente.getIdUtente());
        nuovoMetodo.setIntestatario(intestatario.trim());
        nuovoMetodo.setNumeroMascherato(MetodoPagamento.mascheraNumero(numeroCarta));
        nuovoMetodo.setScadenza(scadenza.trim());
        nuovoMetodo.setCircuito(circuito);
        nuovoMetodo.setPredefinito(metodoPagamentoDao.findByUtente(utente.getIdUtente()).isEmpty());
        metodoPagamentoDao.insert(nuovoMetodo);
        return nuovoMetodo.getIdMetodo();
    }
}
