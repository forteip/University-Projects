package zodiac.dao.coursework;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import zodiac.definition.Student;
import zodiac.definition.coursework.Assignment;
import zodiac.util.PostgreSqlJdbc;

public class AssignmentDao {

  public List<Assignment> getAssignments(String courseCode) {
    return getAssignments(courseCode, false);
  }

  public List<Assignment> getAssignments(String courseCode, String userID) {
    List<Assignment> assignments = new ArrayList<>();

    Connection c;
    PreparedStatement stmt;

    String sql = "SELECT Mark, Assignment_Name,Assignment_Id"
        + " FROM Assignments INNER JOIN UserAssignMarkMap ON Assignments.Id = UserAssignMarkMap.Assignment_Id "
        + " WHERE utor_id = ? AND course_code = ?";

    try {
      c = new PostgreSqlJdbc().getConnection();
      stmt = c.prepareStatement(sql);
      stmt.setString(1, userID);
      stmt.setString(2, courseCode);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        int id = rs.getInt("Assignment_Id");
        int mark = rs.getInt("mark");
        String name = rs.getString("Assignment_Name");
        Assignment assignment = new Assignment(name, id);
        assignment.setCurrScore(mark);
        assignment.setHighScore(mark);
        assignments.add(assignment);
      }

      rs.close();
      stmt.close();
      c.close();
    } catch (Exception e) {
      // TODO Error Handling
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }

    return assignments;
  }

  /**
   * Gets an assignment which has the assignment ID aId.
   *
   * @param aId the desired assignment ID.
   * @return the Assignment with the id aId.
   */
  public List<Assignment> getAssignments(Integer aId) {
    List<Assignment> assignments = new ArrayList<>();

    Connection c;
    PreparedStatement stmt;

    String sql =
        "SELECT Id, Assignment_Name, Visibility, Max_Attempt, Open_Time, Close_Time,Deadline,Extra_points "
            + "FROM Assignments "
            + "WHERE id = ? ";

    try {
      c = new PostgreSqlJdbc().getConnection();
      stmt = c.prepareStatement(sql);
      stmt.setInt(1, aId);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        int id = rs.getInt("Id");
        String name = rs.getString("Assignment_Name");
        Assignment assignment = new Assignment(name, id);
//        assignment.setVisibility(rs.getString("Visibility"));

        int maxAttempts = rs.getInt("Max_Attempt");
        assignment.setMaxAttempt(maxAttempts);

        Date openTime = rs.getTimestamp("Open_Time");
        Date closeTime = rs.getTimestamp("Close_Time");
        assignment.setOpenDate(openTime);
        assignment.setCloseDate(closeTime);
        Timestamp timestamp = rs.getTimestamp("deadline");
        if (timestamp != null) {

          assignment.setEarlySubmissionDeadline(new Date(timestamp.getTime()));
        }
        Integer extraPoints = rs.getInt("Extra_points");
        if (extraPoints != null) {
          assignment.setExtraPoints(extraPoints);
        }

        assignments.add(assignment);
      }

      rs.close();
      stmt.close();
      c.close();
    } catch (Exception e) {
      // TODO Error Handling
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }

    return assignments;
  }

  /**
   * Get all assignments of a given course. generateQuestions is false by default.
   *
   * @param courseCode the course code of the assignments
   * @param generateQuestions whether to populate the questions list or not
   * @return list of assignments belonging to the given course
   */
  public List<Assignment> getAssignments(String courseCode, Boolean generateQuestions) {
    List<Assignment> assignments = new ArrayList<>();

    Connection c;
    PreparedStatement stmt;

    String sql =
        "SELECT Id, Assignment_Name, Visibility, Max_Attempt, Open_Time, Close_Time,Deadline,Extra_points "
            + "FROM Assignments "
            + "WHERE Course_Code = ? "
            + "ORDER BY Id ASC";

    try {
      c = new PostgreSqlJdbc().getConnection();
      stmt = c.prepareStatement(sql);
      stmt.setString(1, courseCode);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        int id = rs.getInt("Id");
        String name = rs.getString("Assignment_Name");
        Assignment assignment = new Assignment(name, id);
        assignment.setVisibility(rs.getBoolean("Visibility"));

        // Returns 0 if value is null, therefore 0 means infinite attempts
        int maxAttempts = rs.getInt("Max_Attempt");
        assignment.setMaxAttempt(maxAttempts);

        Date openTime = rs.getTimestamp("Open_Time");
        Date closeTime = rs.getTimestamp("Close_Time");
        assignment.setOpenDate(openTime);
        assignment.setCloseDate(closeTime);

        if (generateQuestions) {
          assignment.setQuestionList(new QuestionDao().getQuestions(id));
        }
        Timestamp timestamp = rs.getTimestamp("deadline");
        if (timestamp != null) {

          assignment.setEarlySubmissionDeadline(new Date(timestamp.getTime()));
        }
        Integer extraPoints = rs.getInt("Extra_points");
        if (extraPoints != null) {
          assignment.setExtraPoints(extraPoints);
        }
        assignments.add(assignment);
      }

      rs.close();
      stmt.close();
      c.close();
    } catch (Exception e) {
      // TODO Error Handling
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }

    return assignments;
  }

  /**
   * Gets the ID, name, and Visibility status of all assignments.
   *
   * @return list of all assignments
   */
  public List<Assignment> getAllAssignments() {
    List<Assignment> assignments = new ArrayList<>();

    Connection c;
    PreparedStatement stmt;

    String sql =
        "SELECT Id, Assignment_Name,Visibility, Max_Attempt, Open_Time, Close_Time,Deadline,Extra_points "
            + "FROM Assignments";

    try {
      c = new PostgreSqlJdbc().getConnection();
      stmt = c.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        int id = rs.getInt("Id");
        String name = rs.getString("Assignment_Name");
        Assignment assignment = new Assignment(name, id);
        assignment.setVisibility(rs.getBoolean("Visibility"));

        int maxAttempt = rs.getInt("Max_Attempt");
        Date openTime = rs.getTimestamp("Open_Time");
        Date closeTime = rs.getTimestamp("Close_Time");

        assignment.setMaxAttempt(maxAttempt);
        assignment.setOpenDate(openTime);
        assignment.setCloseDate(closeTime);
        Timestamp timestamp = rs.getTimestamp("deadline");
        if (timestamp != null) {

          assignment.setEarlySubmissionDeadline(new Date(timestamp.getTime()));
        }
        Integer extraPoints = rs.getInt("Extra_points");
        if (extraPoints != null) {
          assignment.setExtraPoints(extraPoints);
        }

        assignments.add(assignment);
      }

      rs.close();
      stmt.close();
      c.close();
    } catch (Exception e) {
      // TODO Error Handling
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }

    return assignments;
  }

  /**
   * Add an assignment to the database.
   *
   * @param assignmentName name of the assignment
   * @param courseCode the course code that the assignment belongs to
   * @return the generated id of the assignment
   */
  public int addAssignment(String assignmentName, String courseCode, int maxAttempt) {
    String message = "-1";

    Connection c;
    PreparedStatement stmt;

    String sql = "SELECT AddEdit_Assignment(?, ?, ?)";

    try {
      c = new PostgreSqlJdbc().getConnection();
      stmt = c.prepareStatement(sql);

      stmt.setInt(1, -1);
      stmt.setString(2, courseCode);
      stmt.setString(3, assignmentName);

      ResultSet rs = stmt.executeQuery();

      rs.next();

      message = rs.getString(1);

      rs.close();
      stmt.close();
      c.close();

      editAssignmentMaxAttempt(Integer.parseInt(message), maxAttempt);

    } catch (Exception e) {
      // TODO Error Handling
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }

    return Integer.parseInt(message);
  }

  /**
   * Add an assignment to the database.
   *
   * @param assignmentName name of the assignment
   * @param courseCode the course code that the assignment belongs to
   * @return the generated id of the assignment
   */
  public int addAssignment(String assignmentName, String courseCode) {
    return addAssignment(assignmentName, courseCode, 0);
  }

  /**
   * Edit an Assignment's max number of attempts. Max attempt of 0 means infinite attempts
   *
   * @param id the id of an assignment
   * @param maxAttempt the new max number of attempts
   * @return whether the edit was successful or not
   */
  public Boolean editAssignmentMaxAttempt(int id, int maxAttempt) {
    Assignment assignment = new Assignment("", id);
    assignment.setMaxAttempt(maxAttempt);
    return editAssignmentMaxAttempt(assignment);
  }

  /**
   * Edit an Assignment's max number of attempts. Max attempt of 0 means infinite attempts
   *
   * @param assignment the assignment object to change with the new max attempt already set
   * @return whether the edit was successful or not
   */
  public Boolean editAssignmentMaxAttempt(Assignment assignment) {
    Boolean success = false;

    Connection c;
    PreparedStatement stmt;

    String sql = "UPDATE Assignments SET Max_Attempt = ? WHERE Id = ?";

    try {
      c = new PostgreSqlJdbc().getConnection();
      stmt = c.prepareStatement(sql);

      stmt.setInt(1, assignment.getMaxAttempt());
      stmt.setInt(2, assignment.getId());

      success = stmt.executeUpdate() > 0;

      stmt.close();
      c.close();

    } catch (Exception e) {
      // TODO Error Handling
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }

    return success;
  }

  /**
   * Edit the name of a given assignment.
   *
   * @return message generated by the database
   */
  public String editAssignment(int aId, String assignName) {
    String message = "";

    Connection c;
    PreparedStatement stmt;

    String sql = "SELECT AddEdit_Assignment(?, ?, ?)";

    try {
      c = new PostgreSqlJdbc().getConnection();
      stmt = c.prepareStatement(sql);

      stmt.setInt(1, aId);
      stmt.setString(2, null);
      stmt.setString(3, assignName);

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

  public boolean changeAssignmentVisibility(int aId, boolean visible) {
    boolean flag = false;
    Connection c;
    PreparedStatement stmt;

    String sql = "UPDATE Assignments SET Visibility=? WHERE Id = ? ";

    try {
      c = new PostgreSqlJdbc().getConnection();
      stmt = c.prepareStatement(sql);
      stmt.setBoolean(1, visible);
      stmt.setInt(2, aId);

      stmt.execute();
      stmt.close();
      c.close();
      flag = true;
    } catch (Exception e) {
      return flag;
    }
    return flag;

  }

  public boolean editDeadlineAndExtraPoint(int assignmentId, Date deadline, Integer extraPoints) {
    boolean success = false;
    Connection c;
    PreparedStatement stmt;

    String sql = "update Assignments set ";
    if (deadline != null && extraPoints != null) {
      sql += "deadline=?,extra_points=?";
    } else if (deadline != null) {
      sql += "deadline=?";
    } else if (extraPoints != null) {
      sql += "extra_points=?";
    }
    sql += " where id=?";
    try {
      c = new PostgreSqlJdbc().getConnection();
      stmt = c.prepareStatement(sql);
      if (deadline != null && extraPoints != null) {
        stmt.setTimestamp(1, new Timestamp(deadline.getTime()));
        stmt.setInt(2, extraPoints);
        stmt.setInt(3, assignmentId);
      } else if (deadline != null) {
        stmt.setTimestamp(1, new Timestamp(deadline.getTime()));
        stmt.setInt(2, assignmentId);
      } else if (extraPoints != null) {
        stmt.setInt(1, extraPoints);
        stmt.setInt(2, assignmentId);
      }
      success = stmt.executeUpdate() > 0;
      stmt.close();
      c.close();
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }
    return success;
  }

  public int getStudentUsedAttempts(int assignmentId, String studentUtorId) {
    int usedAttempts = 0;

    Connection c;
    PreparedStatement stmt;

    String sql =
        "SELECT UsedAttempts FROM UserAssignMarkMap WHERE Assignment_Id = ? AND" + " UTOR_Id = ?";

    try {
      c = new PostgreSqlJdbc().getConnection();
      stmt = c.prepareStatement(sql);

      stmt.setInt(1, assignmentId);
      stmt.setString(2, studentUtorId);

      ResultSet rs = stmt.executeQuery();

      rs.next();

      usedAttempts = rs.getInt("UsedAttempts");

      rs.close();
      stmt.close();
      c.close();
    } catch (Exception e) {
      // TODO Error Handling
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }

    return usedAttempts;
  }

  public int getStudentUsedAttempts(Assignment assignment, Student student) {
    return getStudentUsedAttempts(assignment.getId(), student.getUtorId());
  }

  /**
   * Get all student's used attempts for an assignment.
   *
   * @param id the id of the assignment
   * @return map of used attempts keyed to student's utor id
   */
  public Map<String, Integer> getAllStudentsUsedAttempts(int id) {
    Map<String, Integer> allUsedAttempts = new HashMap<>();

    Connection c;
    PreparedStatement stmt;

    String sql = "SELECT UTOR_Id, UsedAttempts FROM UserAssignMarkMap WHERE Assignment_Id = ?";

    try {
      c = new PostgreSqlJdbc().getConnection();
      stmt = c.prepareStatement(sql);

      stmt.setInt(1, id);

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String utorId = rs.getString("UTOR_Id");
        Integer usedAttempts = rs.getInt("UsedAttempts");
        allUsedAttempts.put(utorId, usedAttempts);
      }

      rs.close();
      stmt.close();
      c.close();
    } catch (Exception e) {
      // TODO Error Handling
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }

    return allUsedAttempts;
  }

  public Map<String, Integer> getAllStudentsUsedAttempts(Assignment assignment) {
    return getAllStudentsUsedAttempts(assignment.getId());
  }

  /**
   * Get the course an assignment id belongs to. Used when only the assignment id is known and
   * nothing else
   *
   * @param id the known id of the assignment
   * @return the course code the assignment belongs to
   */
  public String getCourseOfAssignment(int id) {
    String courseCode = "";

    Connection c;
    PreparedStatement stmt;
    String sql = "SELECT Course_Code FROM Assignments WHERE Id=?";

    try {
      c = new PostgreSqlJdbc().getConnection();
      stmt = c.prepareStatement(sql);

      stmt.setInt(1, id);

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        courseCode = rs.getString("Course_Code");
      }

      rs.close();
      stmt.close();
      c.close();
    } catch (Exception e) {
      // TODO Error Handling
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }

    return courseCode;

  }

  /**
   * Change an assignment open time on the database.
   *
   * @param id the id of the assignment
   * @param openTime the new open time
   * @return whether the change was successful
   */
  public Boolean editOpenTime(int id, Date openTime) {

    Boolean successful = false;

    Connection c;
    PreparedStatement stmt;

    String sql = "UPDATE Assignments SET Open_Time = ? " + "WHERE id = ?";

    try {
      c = new PostgreSqlJdbc().getConnection();
      stmt = c.prepareStatement(sql);

      stmt.setTimestamp(1, new Timestamp(openTime.getTime()));
      stmt.setInt(2, id);

      successful = stmt.executeUpdate() > 0;

      c.close();
      stmt.close();

    } catch (Exception e) {
      // TODO Error Handling
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }

    return successful;

  }

  /**
   * Change an assignment close time on the database.
   *
   * @param id the id of the assignment
   * @param closeDate the new open time
   * @return whether the change was successful
   */
  public Boolean editCloseTime(int id, Date closeDate) {

    Boolean successful = false;

    Connection c;
    PreparedStatement stmt;

    String sql = "UPDATE Assignments SET Close_Time = ? " + "WHERE id = ?";

    try {
      c = new PostgreSqlJdbc().getConnection();
      stmt = c.prepareStatement(sql);

      stmt.setTimestamp(1, new Timestamp(closeDate.getTime()));
      stmt.setInt(2, id);

      successful = stmt.executeUpdate() > 0;

      c.close();
      stmt.close();

    } catch (Exception e) {
      // TODO Error Handling
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }

    return successful;

  }

}
