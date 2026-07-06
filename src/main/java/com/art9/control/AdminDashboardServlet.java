package com.art9.control;

import com.art9.dao.JdbcOrdineDao;
import com.art9.dao.JdbcProdottoDao;
import com.art9.dao.JdbcUtenteDao;
import com.art9.dao.OrdineDao;
import com.art9.dao.ProdottoDao;
import com.art9.dao.UtenteDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin")
public class AdminDashboardServlet extends BaseServlet {

    private final ProdottoDao prodottoDao = new JdbcProdottoDao();
    private final OrdineDao ordineDao = new JdbcOrdineDao();
    private final UtenteDao utenteDao = new JdbcUtenteDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageTitle", "Pannello amministratore");
        req.setAttribute("totaleProdotti", prodottoDao.countByFiltri(null, null));
        req.setAttribute("totaleOrdini", ordineDao.countByFiltri(null, null, null));
        req.setAttribute("totaleClienti", utenteDao.countClienti());
        req.setAttribute("fatturatoTotale", ordineDao.fatturatoTotale());
        forward(req, resp, "/WEB-INF/views/admin/dashboard.jsp");
    }
}
