package com.art9.control;

import com.art9.model.FlashMessage;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        req.getSession(true).setAttribute("flash", FlashMessage.successo("Sei stato disconnesso correttamente."));
        resp.sendRedirect(contextRelative(req, "/catalogo"));
    }
}
