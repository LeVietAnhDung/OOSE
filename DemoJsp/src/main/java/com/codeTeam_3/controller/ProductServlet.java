// src/main/java/com/codeTeam_3/controller/ProductServlet.java
package com.codeTeam_3.controller;

import com.codeTeam_3.dao.ProductDao;
import com.codeTeam_3.model.ProductView;

import com.codeTeam_3.web.LangUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = {"/products", "/products/detail"})
public class ProductServlet extends HttpServlet {
    private final ProductDao dao = new ProductDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

//        final String lang = Optional.ofNullable(req.getParameter("lang")).orElse("vi");
//        req.setAttribute("lang", lang);

        String lang = LangUtil.resolveLang(req);
        req.setAttribute("lang", lang);


        String path = req.getServletPath();

        if ("/products/detail".equals(path)) {
            // ----- DETAIL -----
            String idStr = req.getParameter("id");
            if (idStr == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing id");
                return;
            }
            int id;
            try {
                id = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id");
                return;
            }

            ProductView pv = dao.findById(id, lang);
            if (pv == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
                return;
            }
            req.setAttribute("p", pv);

            // forward tới view trong WEB-INF
            req.getRequestDispatcher("/WEB-INF/views/product-detail.jsp").forward(req, resp);
            return;
        }

        
// ----- SEARCH/ LIST -----
String q = req.getParameter("q");
Integer cat = null;
try { cat = req.getParameter("cat")==null? null : Integer.parseInt(req.getParameter("cat")); } catch (Exception ignored){}
if (q != null && !q.isBlank()) {
    var items = dao.search(lang, q, cat, 50, 0);
    req.setAttribute("products", items);
    req.setAttribute("q", q);
    req.getRequestDispatcher("/WEB-INF/views/products.jsp").forward(req, resp);
    return;
}

        req.setAttribute("products", dao.findAll(lang));
        req.getRequestDispatcher("/WEB-INF/views/products.jsp").forward(req, resp);
    }
}
