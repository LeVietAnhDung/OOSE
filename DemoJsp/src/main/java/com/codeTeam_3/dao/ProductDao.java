package com.codeTeam_3.dao;

import com.codeTeam_3.model.ProductView;
import com.codeTeam_3.util.dbMysql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {

    // ========== Helpers ==========
    private ProductView map(ResultSet rs) throws SQLException {
        return new ProductView(
                rs.getInt("ProductID"),
                rs.getString("ProductName"),
                rs.getBigDecimal("Price"),
                rs.getBigDecimal("Weight"),
                rs.getString("CategoryName"),
                rs.getString("ProductDescription")
        );
    }

    /** Khối SELECT chuẩn có fallback: ưu tiên bản dịch theo lang, thiếu thì rơi về EN */
    private static final String BASE_SELECT = """
        SELECT  p.ProductID,
                p.Price,
                p.Weight,
                COALESCE(pt_lang.ProductName,         pt_en.ProductName)          AS ProductName,
                COALESCE(pt_lang.ProductDescription,   pt_en.ProductDescription)   AS ProductDescription,
                COALESCE(pct_lang.CategoryName,        pct_en.CategoryName)       AS CategoryName
        FROM Product p
        LEFT JOIN ProductTranslation pt_lang
               ON pt_lang.ProductID = p.ProductID AND pt_lang.LanguageID = ?
        LEFT JOIN ProductTranslation pt_en
               ON pt_en.ProductID   = p.ProductID AND pt_en.LanguageID   = 'en'
        LEFT JOIN ProductCategoryTranslation pct_lang
               ON pct_lang.ProductCategoryID = p.ProductCategoryID AND pct_lang.LanguageID = ?
        LEFT JOIN ProductCategoryTranslation pct_en
               ON pct_en.ProductCategoryID   = p.ProductCategoryID AND pct_en.LanguageID   = 'en'
        """;

    // ========== Queries ==========
    public List<ProductView> findByCategory(int categoryId, String lang, int limit) {
        final String sql = BASE_SELECT + """
            WHERE p.ProductCategoryID = ?
            ORDER BY p.ProductID
            LIMIT ?
        """;

        List<ProductView> list = new ArrayList<>();
        try (Connection con = dbMysql.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            int i = 0;
            ps.setString(++i, lang);
            ps.setString(++i, lang);
            ps.setInt(++i, categoryId);
            ps.setInt(++i, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProductView> findAll(String lang) {
        final String sql = BASE_SELECT + " ORDER BY p.ProductID";

        List<ProductView> list = new ArrayList<>();
        try (Connection con = dbMysql.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            int i = 0;
            ps.setString(++i, lang);
            ps.setString(++i, lang);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ProductView findById(int id, String lang) {
        final String sql = BASE_SELECT + " WHERE p.ProductID = ?";

        try (Connection con = dbMysql.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            int i = 0;
            ps.setString(++i, lang);
            ps.setString(++i, lang);
            ps.setInt(++i, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteById(int id) {
        String delTr = "DELETE FROM ProductTranslation WHERE ProductID = ?";
        String delPd = "DELETE FROM Product WHERE ProductID = ?";

        try (Connection c = dbMysql.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement p1 = c.prepareStatement(delTr);
                 PreparedStatement p2 = c.prepareStatement(delPd)) {
                p1.setInt(1, id);
                p1.executeUpdate();
                p2.setInt(1, id);
                int rows = p2.executeUpdate();
                c.commit();
                return rows > 0;
            } catch (Exception ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateCore(int id, java.math.BigDecimal price, java.math.BigDecimal weight, int categoryId) {
        final String sql = "UPDATE Product SET Price=?, Weight=?, ProductCategoryID=? WHERE ProductID=?";
        try (Connection c = dbMysql.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setBigDecimal(1, price);
            ps.setBigDecimal(2, weight);
            ps.setInt(3, categoryId);
            ps.setInt(4, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void upsertTranslation(int productId, String lang, String name, String desc) {
        final String sql = """
            INSERT INTO ProductTranslation(ProductID, LanguageID, ProductName, ProductDescription)
            VALUES(?,?,?,?)
            ON DUPLICATE KEY UPDATE
              ProductName=VALUES(ProductName),
              ProductDescription=VALUES(ProductDescription)
        """;
        try (Connection c = dbMysql.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setString(2, lang);
            ps.setString(3, name);
            ps.setString(4, desc);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /** Tìm theo tên/mô tả, có lọc category (tuỳ chọn), phân trang limit/offset; dùng COALESCE để search trên chuỗi fallback */
    public List<ProductView> search(String lang, String keyword, Integer categoryId, int limit, int offset) {
        String sql = BASE_SELECT + " WHERE 1=1 ";
        List<Object> params = new ArrayList<>();

        // lang 2 lần cho pt_lang & pct_lang
        params.add(lang);
        params.add(lang);

        if (categoryId != null && categoryId > 0) {
            sql += " AND p.ProductCategoryID = ? ";
            params.add(categoryId);
        }
        if (keyword != null && !keyword.isBlank()) {
            sql += """
                   AND (
                        COALESCE(pt_lang.ProductName,       pt_en.ProductName)       LIKE ?
                     OR COALESCE(pt_lang.ProductDescription, pt_en.ProductDescription) LIKE ?
                   )
                   """;
            String like = "%" + keyword.trim() + "%";
            params.add(like);
            params.add(like);
        }
        sql += " ORDER BY p.ProductID DESC LIMIT ? OFFSET ? ";
        params.add(limit);
        params.add(offset);

        try (Connection c = dbMysql.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));

            List<ProductView> list = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
