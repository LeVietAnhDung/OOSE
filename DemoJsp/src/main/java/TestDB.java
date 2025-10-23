import com.codeTeam_3.util.dbMysql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TestDB {
    public static void main(String[] args) {
        String sql = "SELECT p.ProductID, p.Price, p.Weight, " +
                "pt.ProductName, pt.ProductDescription, " +
                "pct.CategoryName " +
                "FROM Product p " +
                "LEFT JOIN ProductTranslation pt " +
                "  ON pt.ProductID = p.ProductID AND pt.LanguageID = ? " +
                "LEFT JOIN ProductCategoryTranslation pct " +
                "  ON pct.ProductCategoryID = p.ProductCategoryID AND pct.LanguageID = ? " +
                "ORDER BY p.ProductID";


        int langId = 1;  // test với 1 (có thể đổi thành 'vi' nếu cột là VARCHAR)

        try (Connection conn = dbMysql.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            System.out.println("✅ Kết nối MySQL thành công!");

            ps.setString(1, "vi");
            ps.setString(2, "vi");


            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.printf(
                            "ID: %d | Name: %s | Price: %s | Weight: %s | Category: %s | Desc: %s%n",
                            rs.getInt("ProductID"),
                            rs.getString("ProductName"),
                            rs.getBigDecimal("Price"),
                            rs.getBigDecimal("Weight"),
                            rs.getString("CategoryName"),
                            rs.getString("ProductDescription")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
