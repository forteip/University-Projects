package zodiac.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import zodiac.definition.Mark;
import zodiac.definition.coursework.Assignment;
import zodiac.definition.security.SecurityConstants;
import zodiac.definition.security.User;
import zodiac.util.ActiveUser;
import zodiac.util.PostgreSqlJdbc;

public class MarkActionTest {

  private String utorId;

  @Before
  public void setup() throws SQLException {
    ActiveUser.INSTANCE
        .setUser(new User("proftest", SecurityConstants.PROFESSOR_ROLE, "test", "prof"));
    String sql = "SELECT u.UTOR_ID FROM userclassmap u INNER JOIN Assignments a ON u.course_code = a.course_code WHERE a.id=1 LIMIT 1";
    Connection c = new PostgreSqlJdbc().getConnection();

    PreparedStatement stmt = c.prepareStatement(sql);

    ResultSet rs = stmt.executeQuery();

    rs.next();

    utorId = rs.getString(1);

    c.close();
    stmt.close();
    rs.close();
  }

  @Test
  public void testAddMark() throws SQLException {

    String sql = "SELECT COUNT(UTOR_ID) FROM userassignmarkmap WHERE UTOR_ID=? AND assignment_id=1";

    Connection c = new PostgreSqlJdbc().getConnection();

    PreparedStatement stmt = c.prepareStatement(sql);

    stmt.setString(1, utorId);

    ResultSet rs = stmt.executeQuery();

    rs.next();

    int count = rs.getInt(1);

    c.close();
    stmt.close();
    rs.close();

    MarkAction.addStudentMark(utorId, 1, 100);

    sql = "SELECT COUNT(UTOR_ID) FROM userassignmarkmap WHERE UTOR_ID=? AND assignment_id=1";

    c = new PostgreSqlJdbc().getConnection();

    stmt = c.prepareStatement(sql);

    stmt.setString(1, utorId);

    rs = stmt.executeQuery();

    rs.next();

    Assert.assertEquals(count + 1, rs.getInt(1));

    c.close();
    stmt.close();
    rs.close();
  }

  @Test
  public void AddMarkStudentDoesNotExit() {
    String utorId = "DOESNOTEXIST";

    String expected = "Student with utorid DOESNOTEXIST does not exist in class MATB41";

    Assert.assertEquals(expected, MarkAction.addStudentMark(utorId, 1, 100));

  }

  @Test
  public void getMarks() {
    MarkAction.addStudentMark(utorId, 1, 100);
    Mark marks = new MarkAction().getAllAssignmentsMark(utorId, "MATB41");

    Assignment expectedOnlyAssignment = new Assignment("");

    for (Assignment assignment : marks.getMarkMap().keySet()) {
      expectedOnlyAssignment = assignment;
    }

    Assert.assertEquals(100, marks.getMarkMap().get(expectedOnlyAssignment), 0);
  }

  @After
  public void clear() throws SQLException {
    ActiveUser.INSTANCE.logOff();
    String sql = "DELETE FROM userassignmarkmap WHERE UTOR_ID=?";
    Connection c = new PostgreSqlJdbc().getConnection();

    PreparedStatement stmt = c.prepareStatement(sql);

    stmt.setString(1, utorId);

    stmt.executeUpdate();

    c.close();
    stmt.close();
  }

}