package com.art9.control;

import com.art9.dao.JdbcUtenteDao;
import com.art9.dao.UtenteDao;
import com.art9.model.Ruolo;
import com.art9.model.Utente;
import com.art9.util.PasswordUtil;
import com.art9.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/registrazione")
public class RegistrazioneServlet extends BaseServlet {

    private final UtenteDao utenteDao = new JdbcUtenteDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageTitle", "Registrati");
        forward(req, resp, "/WEB-INF/views/auth/registrazione.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confermaPassword = req.getParameter("confermaPassword");
        String nome = req.getParameter("nome");
        String cognome = req.getParameter("cognome");
        String redirect = req.getParameter("redirect");

        Map<String, String> errori = valida(email, password, confermaPassword, nome, cognome);

        if (!errori.isEmpty()) {
            req.setAttribute("pageTitle", "Registrati");
            req.setAttribute("errori", errori);
            req.setAttribute("valori", Map.of("email", email, "nome", nome, "cognome", cognome));
            req.setAttribute("redirect", redirect);
            forward(req, resp, "/WEB-INF/views/auth/registrazione.jsp");
            return;
        }

        Utente nuovoUtente = new Utente();
        nuovoUtente.setEmail(email.trim().toLowerCase());
        nuovoUtente.setPasswordHash(PasswordUtil.hash(password));
        nuovoUtente.setNome(nome.trim());
        nuovoUtente.setCognome(cognome.trim());
        nuovoUtente.setRuolo(Ruolo.CLIENTE);
        utenteDao.insert(nuovoUtente);

        HttpSession session = req.getSession(true);
        session.setAttribute("utente", nuovoUtente);
        impostaMessaggioSuccesso(req, "Registrazione completata! Benvenuto in Art 9, " + nuovoUtente.getNome() + ".");

        resp.sendRedirect(destinazioneSicura(req, redirect, "/catalogo"));
    }

    private Map<String, String> valida(String email, String password, String confermaPassword, String nome, String cognome) {
        Map<String, String> errori = new HashMap<>();

        if (!ValidationUtil.isValidEmail(email)) {
            errori.put("email", "Inserisci un indirizzo email valido.");
        } else if (utenteDao.existsByEmail(email.trim().toLowerCase())) {
            errori.put("email", "Questo indirizzo email e' gia' registrato.");
        }

        if (!ValidationUtil.isValidPassword(password)) {
            errori.put("password", "La password deve avere almeno 8 caratteri, una maiuscola, una minuscola, un numero e un simbolo.");
        } else if (!password.equals(confermaPassword)) {
            errori.put("confermaPassword", "Le password inserite non coincidono.");
        }

        if (!ValidationUtil.isValidNome(nome)) {
            errori.put("nome", "Il nome deve contenere solo lettere, tra 2 e 50 caratteri.");
        }
        if (!ValidationUtil.isValidNome(cognome)) {
            errori.put("cognome", "Il cognome deve contenere solo lettere, tra 2 e 50 caratteri.");
        }

        return errori;
    }
}
