package com.art9.control;

import com.art9.dao.IndirizzoDao;
import com.art9.dao.JdbcIndirizzoDao;
import com.art9.model.Indirizzo;
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


@WebServlet({"/account/indirizzi", "/account/indirizzi/elimina", "/account/indirizzi/predefinito"})
public class IndirizziServlet extends BaseServlet {

    private final IndirizzoDao indirizzoDao = new JdbcIndirizzoDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utente utente = utenteCorrente(req);
        req.setAttribute("pageTitle", "I tuoi indirizzi");
        req.setAttribute("indirizzi", indirizzoDao.findByUtente(utente.getIdUtente()));
        forward(req, resp, "/WEB-INF/views/account/indirizzi.jsp");
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
            aggiungiIndirizzo(req, resp, utente);
            return;
        }
        resp.sendRedirect(contextRelative(req, "/account/indirizzi"));
    }

    private void aggiungiIndirizzo(HttpServletRequest req, HttpServletResponse resp, Utente utente)
            throws ServletException, IOException {
        String etichetta = req.getParameter("etichetta");
        String via = req.getParameter("via");
        String citta = req.getParameter("citta");
        String cap = req.getParameter("cap");
        String provincia = req.getParameter("provincia");
        String nazione = req.getParameter("nazione");

        Map<String, String> errori = new HashMap<>();
        if (ValidationUtil.isBlank(etichetta) || etichetta.trim().length() > 40) {
            errori.put("etichetta", "Assegna un nome a questo indirizzo (max 40 caratteri).");
        }
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
            req.setAttribute("pageTitle", "I tuoi indirizzi");
            req.setAttribute("errori", errori);
            req.setAttribute("indirizzi", indirizzoDao.findByUtente(utente.getIdUtente()));
            forward(req, resp, "/WEB-INF/views/account/indirizzi.jsp");
            return;
        }

        boolean primoIndirizzo = indirizzoDao.findByUtente(utente.getIdUtente()).isEmpty();

        Indirizzo indirizzo = new Indirizzo();
        indirizzo.setIdUtente(utente.getIdUtente());
        indirizzo.setEtichetta(etichetta.trim());
        indirizzo.setVia(via.trim());
        indirizzo.setCitta(citta.trim());
        indirizzo.setCap(cap.trim());
        indirizzo.setProvincia(provincia.trim().toUpperCase());
        indirizzo.setNazione(nazione.trim());
        indirizzo.setPredefinito(primoIndirizzo);
        indirizzoDao.insert(indirizzo);

        impostaMessaggioSuccesso(req, "Indirizzo aggiunto correttamente.");
        resp.sendRedirect(contextRelative(req, "/account/indirizzi"));
    }

    private void eliminaSeProprietario(HttpServletRequest req, Utente utente) {
        parseId(req.getParameter("id")).ifPresent(id -> {
            Optional<Indirizzo> indirizzo = indirizzoDao.findById(id);
            if (indirizzo.isPresent() && indirizzo.get().getIdUtente() == utente.getIdUtente()) {
                indirizzoDao.delete(id);
                impostaMessaggioSuccesso(req, "Indirizzo eliminato.");
            }
        });
    }

    private void impostaPredefinitoSeProprietario(HttpServletRequest req, Utente utente) {
        parseId(req.getParameter("id")).ifPresent(id -> {
            Optional<Indirizzo> indirizzoOpt = indirizzoDao.findById(id);
            if (indirizzoOpt.isPresent() && indirizzoOpt.get().getIdUtente() == utente.getIdUtente()) {
                indirizzoDao.azzeraPredefinito(utente.getIdUtente());
                Indirizzo indirizzo = indirizzoOpt.get();
                indirizzo.setPredefinito(true);
                indirizzoDao.update(indirizzo);
                impostaMessaggioSuccesso(req, "Indirizzo predefinito aggiornato.");
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
