package certification.core;

import java.sql.*;

public class testSql {
    public static void main(String[] args) {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        // 注册驱动
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/certificationsystem?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT&allowPublicKeyRetrieval=true&useSSL=false", "root", "123456");

            st = conn.createStatement();
            // 查询
            String sql = "select * from certs";
            rs = st.executeQuery(sql);
            // 遍历
            while (rs.next()) {
                String serial = rs.getString("serial");
                String commenName = rs.getString("commenName");

                System.out.println("serial == " + serial + "   commenName == " + commenName);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

            if (rs != null) { try {
                rs.close(); } catch (SQLException e) { // TODO Auto-generated catch block
                e.printStackTrace(); } rs = null; } if (st != null) { try { st.close(); }
            catch (SQLException e) { // TODO Auto-generated catch block
                e.printStackTrace(); } st = null; } if (conn != null) { try { conn.close(); }
            catch (SQLException e) { // TODO Auto-generated catch block
                e.printStackTrace(); } conn = null; }

        }
    }
}
