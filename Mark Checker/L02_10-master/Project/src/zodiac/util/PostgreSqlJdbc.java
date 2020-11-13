package zodiac.util;

import java.sql.Connection;
import java.sql.DriverManager;

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
      connection = DriverManager.getConnection("jdbc:postgresql://173.193.99.127:30333/postgres",
          "postgres", "CSCC01");
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      System.exit(0);
    }
  }

  public Connection getConnection() {
    return connection;
  }
}
