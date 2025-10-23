// src/main/java/com/codeTeam_3/controller/CategoryServlet.java
package com.codeTeam_3.controller;

import com.codeTeam_3.dao.CategoryDao;
import com.codeTeam_3.model.Category;
import com.codeTeam_3.util.dbMysql;
import com.codeTeam_3.web.LangUtil;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet(urlPatterns = {"/categories"})
public class CategoryServlet extends HttpServlet {
    private CategoryDao dao;

    public void init(ServletConfig config) throws ServletException {

        // 1) Ping DB để chắc chắn pool Hikari sẵn sàng
        ServletContext servletContext = config.getServletContext();
        try (Connection connection = dbMysql.getConnection()) {
            servletContext.log("dbMysql pool ready: " + connection.getMetaData().getURL());
        } catch (Exception exception) {
            throw new ServletException("Cannot connect to DB at startup", exception);
        }

        // 2) Tạo DAO (DAO tự dùng dbMysql.getConnection() bên trong)
        this.dao = new CategoryDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String lang = LangUtil.resolveLang(req);
        req.setAttribute("lang", lang);

        List<Category> categories = dao.findAll(lang);
        req.setAttribute("categories", categories);

        req.getRequestDispatcher("/WEB-INF/views/categories.jsp").forward(req, resp);
    }
}
