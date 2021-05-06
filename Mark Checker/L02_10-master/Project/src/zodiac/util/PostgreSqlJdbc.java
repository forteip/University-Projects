package zodiac.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class creates a connection to the database. To simply closing connections, a new connection
 * is made for each query
 */
public class PostgreSqlJdbc {

  private Connection connection;

  /**
   * Creates a new connection to the database.
   */
  public PostgreSqlJdbc() {
    try {
      connection = DriverManager
          .getConnection("jdbc:postgresql://173.193.99.127:30333/postgres", "postgres",
              "CSCC01");
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      System.exit(0);
    }
  }

  public static void main(String[] args) {
    Connection conn = new PostgreSqlJdbc().getConnection();
    //alter table Assignments drop column
    String sql = "select * from UserAssignMarkMap";
    try {
      PreparedStatement pre = conn.prepareStatement(sql);
      ResultSet set = pre.executeQuery();
      while (set.next()) {
        System.out.println(set.getString("UTOR_Id") + "---" + set.getInt("Assignment_Id")
            + "---" + set.getInt("mark"));
      }
      pre.close();
      conn.close();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  public Connection getConnection() {
    return connection;
  }
}
