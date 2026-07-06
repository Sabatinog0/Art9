package com.art9.control;

import com.art9.dao.JdbcUtenteDao;
import com.art9.dao.UtenteDao;
import com.art9.util.JsonUtil;
import com.art9.util.ValidationUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;


@WebServlet("/registrazione/verifica-email")
public class EmailCheckServlet extends BaseServlet {

    private final UtenteDao utenteDao = new JdbcUtenteDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        boolean valida = ValidationUtil.isValidEmail(email);
        boolean esiste = valida && utenteDao.existsByEmail(email.trim().toLowerCase());
        JsonUtil.write(resp, Map.of("valida", valida, "esiste", esiste));
    }
}
