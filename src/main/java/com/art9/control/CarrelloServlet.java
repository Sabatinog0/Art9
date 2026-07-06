package com.art9.control;

import com.art9.dao.JdbcProdottoDao;
import com.art9.dao.ProdottoDao;
import com.art9.model.Carrello;
import com.art9.model.CarrelloItem;
import com.art9.model.Prodotto;
import com.art9.util.JsonUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@WebServlet({"/carrello", "/carrello/aggiungi", "/carrello/aggiorna", "/carrello/rimuovi"})
public class CarrelloServlet extends BaseServlet {

    private final ProdottoDao prodottoDao = new JdbcProdottoDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageTitle", "Il tuo carrello");
        forward(req, resp, "/WEB-INF/views/carrello/carrello.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getServletPath();
        Carrello carrello = carrelloCorrente(req);

        Map<String, Object> risposta = switch (path) {
            case "/carrello/aggiungi" -> aggiungi(req, carrello);
            case "/carrello/aggiorna" -> aggiorna(req, carrello);
            case "/carrello/rimuovi" -> rimuovi(req, carrello);
            default -> erroreGenerico();
        };

        JsonUtil.write(resp, risposta);
    }

    private Map<String, Object> aggiungi(HttpServletRequest req, Carrello carrello) {
        Integer idProdotto = parseInt(req.getParameter("idProdotto"));
        int quantita = parseIntConDefault(req.getParameter("quantita"), 1);
        if (idProdotto == null || quantita <= 0) {
            return risposta(false, carrello, "Richiesta non valida.");
        }

        Optional<Prodotto> prodottoOpt = prodottoDao.findById(idProdotto);
        if (prodottoOpt.isEmpty()) {
            return risposta(false, carrello, "Prodotto non trovato.");
        }
        Prodotto prodotto = prodottoOpt.get();
        if (prodotto.getQuantitaDisponibile() <= 0 && !prodotto.isInPreordine()) {
            return risposta(false, carrello, "Prodotto non disponibile.");
        }
        if (!prodotto.isInPreordine()) {
            int giaNelCarrello = carrello.getItems().stream()
                    .filter(item -> item.getIdProdotto() == prodotto.getIdProdotto())
                    .mapToInt(CarrelloItem::getQuantita)
                    .findFirst().orElse(0);
            int quantitaMassimaAggiungibile = prodotto.getQuantitaDisponibile() - giaNelCarrello;
            if (quantitaMassimaAggiungibile <= 0) {
                return risposta(false, carrello, "Hai gia' raggiunto la disponibilita' massima per questo prodotto.");
            }
            quantita = Math.min(quantita, quantitaMassimaAggiungibile);
        }

        carrello.aggiungi(prodotto, quantita);
        return risposta(true, carrello, "\"" + prodotto.getNome() + "\" aggiunto al carrello.");
    }

    private Map<String, Object> aggiorna(HttpServletRequest req, Carrello carrello) {
        Integer idProdotto = parseInt(req.getParameter("idProdotto"));
        Integer quantita = parseInt(req.getParameter("quantita"));
        if (idProdotto == null || quantita == null) {
            return risposta(false, carrello, "Richiesta non valida.");
        }

        int quantitaClamp = quantita;
        Optional<CarrelloItem> itemEsistente = carrello.getItems().stream()
                .filter(item -> item.getIdProdotto() == idProdotto)
                .findFirst();
        if (itemEsistente.isPresent() && itemEsistente.get().getQuantitaDisponibile() > 0) {
            quantitaClamp = Math.min(quantitaClamp, itemEsistente.get().getQuantitaDisponibile());
        }
        carrello.aggiornaQuantita(idProdotto, quantitaClamp);

        Map<String, Object> risposta = risposta(true, carrello, "Carrello aggiornato.");
        carrello.getItems().stream()
                .filter(item -> item.getIdProdotto() == idProdotto)
                .findFirst()
                .ifPresent(item -> risposta.put("subtotaleRiga", formattaValuta(item.getSubtotale())));
        return risposta;
    }

    private Map<String, Object> rimuovi(HttpServletRequest req, Carrello carrello) {
        Integer idProdotto = parseInt(req.getParameter("idProdotto"));
        if (idProdotto == null) {
            return risposta(false, carrello, "Richiesta non valida.");
        }
        carrello.rimuovi(idProdotto);
        return risposta(true, carrello, "Prodotto rimosso dal carrello.");
    }

    private Map<String, Object> risposta(boolean success, Carrello carrello, String messaggio) {
        Map<String, Object> dati = new LinkedHashMap<>();
        dati.put("success", success);
        dati.put("numeroArticoli", carrello.getNumeroArticoli());
        dati.put("totale", formattaValuta(carrello.getTotale()));
        dati.put("messaggio", messaggio);
        return dati;
    }

    private Map<String, Object> erroreGenerico() {
        Map<String, Object> dati = new LinkedHashMap<>();
        dati.put("success", false);
        dati.put("messaggio", "Operazione non riconosciuta.");
        return dati;
    }

    private String formattaValuta(BigDecimal valore) {
        return NumberFormat.getCurrencyInstance(Locale.ITALY).format(valore);
    }

    private Integer parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException | NullPointerException e) {
            return null;
        }
    }

    private int parseIntConDefault(String value, int valoreDefault) {
        Integer parsed = parseInt(value);
        return parsed == null ? valoreDefault : parsed;
    }
}
