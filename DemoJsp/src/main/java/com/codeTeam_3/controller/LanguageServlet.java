// src/main/java/com/codeTeam_3/web/LanguageServlet.java
package com.codeTeam_3.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;

@WebServlet("/lang")
public class LanguageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String lang = req.getParameter("lang");

        if (!"vi".equals(lang) && !"en".equals(lang) && !"ja".equals(lang)) {
            lang = "vi";
        }

        // lưu session + cookie
        req.getSession().setAttribute("lang", lang);
        Cookie ck = new Cookie("lang", lang);
        ck.setPath("/");
        ck.setMaxAge(30*24*60*60);
        resp.addCookie(ck);

        // back param → Referer → /home
        String back = req.getParameter("back");
        if (back == null || back.isBlank()) {
            back = req.getHeader("Referer");
        }
        if (back == null || back.isBlank()) {
            back = req.getContextPath() + "/home";
        }

        // Redirect
        if (back.startsWith("/")) {
            resp.sendRedirect(back);
        } else if (back.startsWith("http")) {
            resp.sendRedirect(back);
        } else {
            resp.sendRedirect(req.getContextPath() + back);
        }
    }
}

