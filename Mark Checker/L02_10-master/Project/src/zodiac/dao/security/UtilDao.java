package zodiac.dao.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import zodiac.util.PostgreSqlJdbc;

public class UtilDao {

  /**
   * Get the time on the server. Use when comparing current time to prevent users from changing
   * their own system time to when they want.
   *
   * @return Date with the current time from the server
   * @throws SQLException When an error occurs from the server. This should only occur when there is
   * an issue on the server. Show an error message on the UI in this case
   */
  public Date getServerTime() throws SQLException {

    Connection c;
    PreparedStatement stmt;

    String sql = "SELECT now()";

    c = new PostgreSqlJdbc().getConnection();
    stmt = c.prepareStatement(sql);

    ResultSet rs = stmt.executeQuery();

    rs.next();

    Date now = rs.getTimestamp(1);

    c.close();
    stmt.close();
    rs.close();

    return now;

  }

}
