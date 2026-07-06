package com.art9.control;

import com.art9.dao.JdbcUtenteDao;
import com.art9.dao.UtenteDao;
import com.art9.model.Utente;
import com.art9.util.PasswordUtil;
import com.art9.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/account")
public class AccountServlet extends BaseServlet {

    private final UtenteDao utenteDao = new JdbcUtenteDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageTitle", "Il mio account");
        forward(req, resp, "/WEB-INF/views/account/profilo.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String azione = req.getParameter("azione");
        if ("password".equals(azione)) {
            gestisciCambioPassword(req, resp);
        } else {
            gestisciAggiornamentoProfilo(req, resp);
        }
    }

    private void gestisciAggiornamentoProfilo(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Utente utente = utenteCorrente(req);
        String nome = req.getParameter("nome");
        String cognome = req.getParameter("cognome");
        String email = req.getParameter("email");

        Map<String, String> errori = new HashMap<>();
        if (!ValidationUtil.isValidNome(nome)) {
            errori.put("nome", "Il nome deve contenere solo lettere, tra 2 e 50 caratteri.");
        }
        if (!ValidationUtil.isValidNome(cognome)) {
            errori.put("cognome", "Il cognome deve contenere solo lettere, tra 2 e 50 caratteri.");
        }
        String emailNormalizzata = email == null ? "" : email.trim().toLowerCase();
        if (!ValidationUtil.isValidEmail(email)) {
            errori.put("email", "Inserisci un indirizzo email valido.");
        } else if (!emailNormalizzata.equals(utente.getEmail()) && utenteDao.existsByEmail(emailNormalizzata)) {
            errori.put("email", "Questo indirizzo email e' gia' in uso da un altro account.");
        }

        if (!errori.isEmpty()) {
            req.setAttribute("pageTitle", "Il mio account");
            req.setAttribute("erroriProfilo", errori);
            forward(req, resp, "/WEB-INF/views/account/profilo.jsp");
            return;
        }

        utente.setNome(nome.trim());
        utente.setCognome(cognome.trim());
        utente.setEmail(emailNormalizzata);
        utenteDao.updateProfilo(utente);
        req.getSession(true).setAttribute("utente", utente);

        impostaMessaggioSuccesso(req, "Dati personali aggiornati correttamente.");
        resp.sendRedirect(contextRelative(req, "/account"));
    }

    private void gestisciCambioPassword(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Utente utente = utenteCorrente(req);
        String passwordAttuale = req.getParameter("passwordAttuale");
        String nuovaPassword = req.getParameter("nuovaPassword");
        String confermaNuovaPassword = req.getParameter("confermaNuovaPassword");

        Map<String, String> errori = new HashMap<>();
        if (!PasswordUtil.matches(passwordAttuale, utente.getPasswordHash())) {
            errori.put("passwordAttuale", "La password attuale non e' corretta.");
        }
        if (!ValidationUtil.isValidPassword(nuovaPassword)) {
            errori.put("nuovaPassword", "La nuova password deve avere almeno 8 caratteri, una maiuscola, una minuscola, un numero e un simbolo.");
        } else if (!nuovaPassword.equals(confermaNuovaPassword)) {
            errori.put("confermaNuovaPassword", "Le password inserite non coincidono.");
        }

        if (!errori.isEmpty()) {
            req.setAttribute("pageTitle", "Il mio account");
            req.setAttribute("erroriPassword", errori);
            forward(req, resp, "/WEB-INF/views/account/profilo.jsp");
            return;
        }

        String nuovoHash = PasswordUtil.hash(nuovaPassword);
        utenteDao.updatePassword(utente.getIdUtente(), nuovoHash);
        utente.setPasswordHash(nuovoHash);

        impostaMessaggioSuccesso(req, "Password aggiornata correttamente.");
        resp.sendRedirect(contextRelative(req, "/account"));
    }
}
