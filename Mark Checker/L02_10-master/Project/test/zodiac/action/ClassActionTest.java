package zodiac.action;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import zodiac.definition.Class;
import zodiac.definition.security.SecurityConstants;
import zodiac.definition.security.User;
import zodiac.util.ActiveUser;
import zodiac.util.PostgreSqlJdbc;

public class ClassActionTest {

  @Before
  public void setup() {
    ActiveUser.INSTANCE
        .setUser(new User("proftest", SecurityConstants.PROFESSOR_ROLE, "test", "prof"));
  }

  @Test
  public void getClasses() throws SQLException {
    String sql = "SELECT course_code FROM userclassmap WHERE utor_id = ?";
    Connection c = new PostgreSqlJdbc().getConnection();
    PreparedStatement stmt = c.prepareStatement(sql);
    stmt.setString(1, ActiveUser.INSTANCE.getUser().getUtorId());
    ResultSet rs = stmt.executeQuery();

    List<String> courseCodes = new ArrayList<>();

    while (rs.next()) {
      courseCodes.add(rs.getString(1));
    }

    c.close();
    stmt.close();
    rs.close();

    List<Class> classes = new ClassAction().getClasses();

    for (Class course : classes) {
      assertTrue(courseCodes.contains(course.getCourseCode()));
    }

  }

}