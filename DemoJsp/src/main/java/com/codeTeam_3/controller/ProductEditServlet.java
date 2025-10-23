package com.codeTeam_3.controller;

import com.codeTeam_3.dao.ProductDao;
import com.codeTeam_3.dao.CategoryDao;
import com.codeTeam_3.model.ProductView;
import com.codeTeam_3.model.Category;
import com.codeTeam_3.web.LangUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(urlPatterns={"/products/edit"})
public class ProductEditServlet extends HttpServlet {
    private final ProductDao productDao = new ProductDao();
    private final CategoryDao categoryDao = new CategoryDao();

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String lang = LangUtil.resolveLang(req);
        req.setAttribute("lang", lang);

        int id = Integer.parseInt(req.getParameter("id"));
        ProductView p = productDao.findById(id, lang);
        List<Category> categories = categoryDao.findAll(lang);

        req.setAttribute("p", p);
        req.setAttribute("categories", categories);
        req.getRequestDispatcher("/WEB-INF/views/product-edit.jsp").forward(req, resp);
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String lang = LangUtil.resolveLang(req);
        req.setAttribute("lang", lang);

        int id        = Integer.parseInt(req.getParameter("id"));
        BigDecimal price  = new BigDecimal(req.getParameter("price"));
        BigDecimal weight = new BigDecimal(req.getParameter("weight"));
        int categoryId    = Integer.parseInt(req.getParameter("categoryId"));

        // bản dịch 3 ngôn ngữ (tuỳ bạn muốn lưu 1 hay nhiều)
        String name_vi = req.getParameter("name_vi");
        String desc_vi = req.getParameter("desc_vi");
        String name_en = req.getParameter("name_en");
        String desc_en = req.getParameter("desc_en");
        String name_ja = req.getParameter("name_ja");
        String desc_ja = req.getParameter("desc_ja");

        // Lưu trong transaction nhẹ nhàng (ở đây tuần tự, đủ cho demo)
        productDao.updateCore(id, price, weight, categoryId);
        if (name_vi != null) productDao.upsertTranslation(id, "vi", name_vi, desc_vi);
        if (name_en != null) productDao.upsertTranslation(id, "en", name_en, desc_en);
        if (name_ja != null) productDao.upsertTranslation(id, "ja", name_ja, desc_ja);

        resp.sendRedirect(req.getContextPath()+"/products/detail?id="+id+"&lang="+lang);
    }
}
