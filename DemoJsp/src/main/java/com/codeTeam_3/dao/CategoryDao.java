package com.codeTeam_3.dao;

import com.codeTeam_3.model.Category;
import com.codeTeam_3.util.dbMysql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {

    public int create(boolean canBeShipped) {
        String sql = "INSERT INTO ProductCategory(CanBeShipped) VALUES (?)";
        try (Connection c = dbMysql.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setBoolean(1, canBeShipped);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
                throw new RuntimeException("No generated key");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void upsertTranslation(int id, String lang, String name) {
        String sql = "INSERT INTO ProductCategoryTranslation(ProductCategoryID, LanguageID, CategoryName) " +
                "VALUES (?,?,?) " +
                "ON DUPLICATE KEY UPDATE CategoryName = VALUES(CategoryName)";
        try (Connection c = dbMysql.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, lang);
            ps.setString(3, name);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Category> findAll(String lang) {
        String sql = """
                    SELECT c.ProductCategoryID,
                           COALESCE(ct.CategoryName, CONCAT('Category ', c.ProductCategoryID)) AS CategoryName,
                           COUNT(p.ProductID) AS ProductCount
                    FROM ProductCategory c
                    LEFT JOIN ProductCategoryTranslation ct
                      ON ct.ProductCategoryID = c.ProductCategoryID AND ct.LanguageID = ?
                    LEFT JOIN Product p ON p.ProductCategoryID = c.ProductCategoryID
                    GROUP BY c.ProductCategoryID, ct.CategoryName
                    ORDER BY c.ProductCategoryID
                """;

        List<Category> list = new ArrayList<>();
        try (Connection c = dbMysql.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, lang);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Category(
                            rs.getInt("ProductCategoryID"),
                            rs.getString("CategoryName"),
                            rs.getLong("ProductCount")
                    ));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
