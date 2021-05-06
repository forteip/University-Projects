package zodiac.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import zodiac.definition.Class;
import zodiac.util.ActiveUser;
import zodiac.util.PostgreSqlJdbc;

/**
 * Data Access Object for Class.
 */
public class ClassDao {

  /**
   * Gets all the classes in the database along with the students in each class.
   *
   * @return list of Classes, each class' student list is already populated
   */
  public List<Class> getClasses() {

    List<Class> classes = new ArrayList<>();

    Connection c;
    Statement stmt;

    String sql = "SELECT Course_Code, Class_Name"
        + " FROM Classes"
        + " ORDER BY Course_Code desc";

    try {
      c = new PostgreSqlJdbc().getConnection();
      stmt = c.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String code = rs.getString("Course_Code");
        String name = rs.getString("Class_Name");
        Class newClass = new Class(code, name, null);
        classes.add(newClass);
      }

      rs.close();
      stmt.close();
      c.close();
    } catch (Exception e) {
      // TODO Error Handling
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }
    return classes;

  }

  /**
   * Add/Edit a Class to the database.
   *
   * <p>If the course code does not exist, a new course is added. If the course code does exist, the
   * class name of the course is changed.
   *
   * @param courseCode the course code
   * @param className the descriptive name of the class
   * @return message that explains if the course was added or if a class name was updated
   */
  public String addEditClass(String courseCode, String className) {
    String message = "";

    Connection c;
    PreparedStatement stmt;

    String sql = "SELECT Add_Class(?, ?, ?)";
    try {
      c = new PostgreSqlJdbc().getConnection();
      stmt = c.prepareStatement(sql);

      stmt.setString(1, courseCode);
      stmt.setString(2, className);
      stmt.setString(3, ActiveUser.INSTANCE.getUser().getUtorId());

      ResultSet rs = stmt.executeQuery();

      rs.next();

      message = rs.getString(1);

      rs.close();
      stmt.close();
      c.close();

    } catch (Exception e) {
      // TODO Error Handling
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }

    return message;

  }


}
