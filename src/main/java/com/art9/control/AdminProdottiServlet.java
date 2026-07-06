package com.art9.control;

import com.art9.dao.JdbcProdottoDao;
import com.art9.dao.ProdottoDao;
import com.art9.model.Prodotto;
import com.art9.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@WebServlet({"/admin/prodotti", "/admin/prodotti/nuovo", "/admin/prodotti/modifica", "/admin/prodotti/elimina"})
public class AdminProdottiServlet extends BaseServlet {

    private final ProdottoDao prodottoDao = new JdbcProdottoDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if (path.endsWith("/nuovo")) {
            req.setAttribute("pageTitle", "Nuovo prodotto");
            req.setAttribute("categorie", categoriaDao.findAll());
            forward(req, resp, "/WEB-INF/views/admin/prodotto-form.jsp");
            return;
        }

        if (path.endsWith("/modifica")) {
            Optional<Prodotto> prodottoOpt = parseId(req.getParameter("id")).flatMap(prodottoDao::findById);
            if (prodottoOpt.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            req.setAttribute("pageTitle", "Modifica prodotto");
            req.setAttribute("prodotto", prodottoOpt.get());
            req.setAttribute("categorie", categoriaDao.findAll());
            forward(req, resp, "/WEB-INF/views/admin/prodotto-form.jsp");
            return;
        }

        req.setAttribute("pageTitle", "Gestione prodotti");
        req.setAttribute("prodotti", prodottoDao.findAll());
        forward(req, resp, "/WEB-INF/views/admin/prodotti.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if (path.endsWith("/elimina")) {
            parseId(req.getParameter("id")).ifPresent(prodottoDao::delete);
            impostaMessaggioSuccesso(req, "Prodotto eliminato dal catalogo.");
            resp.sendRedirect(contextRelative(req, "/admin/prodotti"));
            return;
        }

        if (path.endsWith("/modifica")) {
            Optional<Integer> idEsistente = parseId(req.getParameter("id"));
            if (idEsistente.isEmpty() || prodottoDao.findById(idEsistente.get()).isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Prodotto da modificare non valido.");
                return;
            }
            salvaProdotto(req, resp, idEsistente);
            return;
        }

        salvaProdotto(req, resp, Optional.empty());
    }

    private void salvaProdotto(HttpServletRequest req, HttpServletResponse resp, Optional<Integer> idEsistente)
            throws ServletException, IOException {
        String nome = req.getParameter("nome");
        String descrizione = req.getParameter("descrizione");
        String prezzoParam = req.getParameter("prezzo");
        String ivaParam = req.getParameter("ivaPercentuale");
        String quantitaParam = req.getParameter("quantitaDisponibile");
        boolean inPreordine = "on".equals(req.getParameter("inPreordine"));
        String editore = req.getParameter("editore");
        String immagine = req.getParameter("immagine");
        Integer idCategoria = parseId(req.getParameter("idCategoria")).orElse(null);

        Map<String, String> errori = new HashMap<>();
        if (ValidationUtil.isBlank(nome) || nome.trim().length() < 2) {
            errori.put("nome", "Inserisci il nome del prodotto (almeno 2 caratteri).");
        }
        BigDecimal prezzo = parseBigDecimal(prezzoParam);
        if (prezzo == null || prezzo.signum() < 0) {
            errori.put("prezzo", "Inserisci un prezzo valido.");
        }
        BigDecimal iva = parseBigDecimal(ivaParam);
        if (iva == null || iva.signum() < 0) {
            errori.put("ivaPercentuale", "Inserisci una percentuale IVA valida.");
        }
        Integer quantita = parseId(quantitaParam).orElse(null);
        if (quantita == null || quantita < 0) {
            errori.put("quantitaDisponibile", "Inserisci una quantita' valida (0 o superiore).");
        }
        if (idCategoria == null || categoriaDao.findById(idCategoria).isEmpty()) {
            errori.put("idCategoria", "Seleziona una categoria valida.");
        }
        if (ValidationUtil.isBlank(immagine)) {
            errori.put("immagine", "Indica il percorso dell'immagine (es. img/prodotti/nome.svg).");
        }

        if (!errori.isEmpty()) {
            Prodotto valoriInseriti = new Prodotto();
            valoriInseriti.setIdProdotto(idEsistente.orElse(0));
            valoriInseriti.setNome(nome);
            valoriInseriti.setDescrizione(descrizione);
            valoriInseriti.setEditore(editore);
            valoriInseriti.setImmagine(immagine);
            valoriInseriti.setInPreordine(inPreordine);
            if (idCategoria != null) {
                valoriInseriti.setIdCategoria(idCategoria);
            }

            req.setAttribute("pageTitle", idEsistente.isPresent() ? "Modifica prodotto" : "Nuovo prodotto");
            req.setAttribute("errori", errori);
            req.setAttribute("prodotto", valoriInseriti);
            req.setAttribute("categorie", categoriaDao.findAll());
            forward(req, resp, "/WEB-INF/views/admin/prodotto-form.jsp");
            return;
        }

        Prodotto prodotto = new Prodotto();
        prodotto.setNome(nome.trim());
        prodotto.setDescrizione(descrizione == null ? "" : descrizione.trim());
        prodotto.setPrezzo(prezzo);
        prodotto.setIvaPercentuale(iva);
        prodotto.setQuantitaDisponibile(quantita);
        prodotto.setInPreordine(inPreordine);
        prodotto.setEditore(editore == null ? "" : editore.trim());
        prodotto.setImmagine(immagine.trim());
        prodotto.setIdCategoria(idCategoria);

        if (idEsistente.isPresent()) {
            prodotto.setIdProdotto(idEsistente.get());
            prodottoDao.update(prodotto);
            impostaMessaggioSuccesso(req, "Prodotto aggiornato correttamente.");
        } else {
            prodottoDao.insert(prodotto);
            impostaMessaggioSuccesso(req, "Prodotto creato correttamente.");
        }

        resp.sendRedirect(contextRelative(req, "/admin/prodotti"));
    }

    private BigDecimal parseBigDecimal(String value) {
        try {
            return new BigDecimal(value.trim().replace(",", "."));
        } catch (NumberFormatException | NullPointerException e) {
            return null;
        }
    }

    private Optional<Integer> parseId(String value) {
        try {
            return Optional.of(Integer.parseInt(value.trim()));
        } catch (NumberFormatException | NullPointerException e) {
            return Optional.empty();
        }
    }
}
