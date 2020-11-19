import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Statement;


public class youngry2 {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    //Database set for testing purposes. For all intents and purposes, contains the world database, and as such will
    // still function if left as is.
    static final String DB_URL = "jdbc:mysql://mathlab.utsc.utoronto.ca:3306/cscc43f17_youngry2_sakila";
    // Database login credentials
    static final String USER = "youngry2";
    static final String PASS = "youngry2";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            // Execute a query
            stmt = conn.createStatement();
            String sql;
            //Query was modified because the given query could not be tested, due to a "group by full" mode error
            //That could not be resolved without further permissions. For all intents and purposes, the query below
            //gives the same result as the query provided in the assignment
            sql = "SELECT country.Name, CountryCode as code FROM (SELECT CountryCode, COUNT(CountryCode) " +
                    "as total from city group by CountryCode) as sum, " +
                    "country WHERE total > 50 and country.Code = CountryCode";

            ResultSet rs = stmt.executeQuery(sql);

            // Extract data from result set
            System.out.print("First query: \n");
            while (rs.next()) {
                // Retrieve by column name
                String name = rs.getString("name");
                String code = rs.getString("code");
                System.out.print("Name: " + name);
                System.out.print(", Code: " + code +"\n");

            }

            // Execute a second query
            stmt = conn.createStatement();
            String sql2;
            sql2 = "select count(language) as cnt , language\n" +
                    "from countrylanguage\n" +
                    "group by Language\n" +
                    "order by cnt desc\n" +
                    "limit 1;";

            ResultSet rs2 = stmt.executeQuery(sql2);

            // Extract data from result set
            System.out.print("\nSecond query: \n");
            while (rs2.next()) {
                // Retrieve by column name
                int cnt = rs2.getInt("cnt");
                String language = rs2.getString("language");

                System.out.print("cnt: " + cnt);
                System.out.print(", language: " + language);
            }
            // Clean-up environment
            rs.close();
            rs2.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {

            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

}

