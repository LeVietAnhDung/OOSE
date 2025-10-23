package com.codeTeam_3.controller;

import com.codeTeam_3.dao.ProductDao;
import com.codeTeam_3.web.LangUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;

@WebServlet(urlPatterns={"/products/delete"})
public class ProductDeleteServlet extends HttpServlet {
    private final ProductDao dao = new ProductDao();

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String lang = LangUtil.resolveLang(req);
        int id = Integer.parseInt(req.getParameter("id"));
        dao.deleteById(id);
        resp.sendRedirect(req.getContextPath()+"/products?lang="+lang);
    }
}