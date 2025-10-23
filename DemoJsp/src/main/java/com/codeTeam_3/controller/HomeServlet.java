package com.codeTeam_3.controller;

import com.codeTeam_3.dao.CategoryDao;
import com.codeTeam_3.dao.ProductDao;
import com.codeTeam_3.model.Category;
import com.codeTeam_3.model.ProductView;

import com.codeTeam_3.web.LangUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "home", urlPatterns = {"/home"}) // map /home cho dễ kiểm
public class HomeServlet extends HttpServlet {
    private final CategoryDao categoryDao = new CategoryDao();
    private final ProductDao productDao = new ProductDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

//        final String lang = "vi";
        String lang = LangUtil.resolveLang(req);     // <param/session/cookie

        List<Category> categories = categoryDao.findAll(lang);

        Integer catParam = null;
        try { catParam = Integer.valueOf(req.getParameter("cat")); } catch (Exception ignore) {}
        int activeCatId = (catParam != null) ? catParam :
                (categories.isEmpty() ? -1 : categories.get(0).getId());

        List<ProductView> products = activeCatId > 0
                ? productDao.findByCategory(activeCatId, lang, 30)
                : java.util.Collections.emptyList();

        req.setAttribute("categories", categories);
        req.setAttribute("activeCatId", activeCatId);
        req.setAttribute("products", products);

        req.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(req, resp);
    }
}
