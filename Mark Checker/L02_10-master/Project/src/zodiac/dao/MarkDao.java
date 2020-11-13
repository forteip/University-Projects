package zodiac.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import zodiac.definition.Mark;
import zodiac.definition.Student;
import zodiac.definition.coursework.Assignment;
import zodiac.util.PostgreSqlJdbc;
/**
 * Data Access Object for Mark.
 */
public class MarkDao {
	/**
	 * 
	 * @param utor_id Student's id
	 * @param assignId assignment's id
	 * @param mark the mark of this assignment
	 * @return message
	 */
	public String addStudentAssignMark(String utor_id, int assignId, int mark) {
		String message = "";
		Connection c;
		PreparedStatement stmt;
		String sql = "SELECT Add_Mark(?, ?, ?)";
		try {
			c = new PostgreSqlJdbc().getConnection();
			stmt = c.prepareStatement(sql);
			stmt.setString(1, utor_id);
			stmt.setInt(2, assignId);
			stmt.setInt(3, mark);
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
	/**
	 * <p>Check all the assignment's marks for this student</p>
	 * @param utorId student's id
	 * @return mark Object
	 */
	public Mark getStudentMark(String utorId) {
		Mark mark = new Mark();
	    Connection c;
	    PreparedStatement stmt;
	    String sql = "SELECT utor_id, last_name, first_name "
	        + "FROM Users "
	        + "WHERE utor_id = ?";
	    try {
	      c = new PostgreSqlJdbc().getConnection();
	      stmt = c.prepareStatement(sql);
	      stmt.setString(1, utorId);
	      ResultSet rs = stmt.executeQuery();
	      
	      while (rs.next()) {
	        String utor_id = rs.getString("utor_id");
	        String lname = rs.getString("last_name");
	        String fname = rs.getString("first_name");
	        mark.setStudent(new Student(utor_id,lname,fname));
	      }
	      rs.close();
	      stmt.close();
	      sql = "SELECT a.id,a.Assignment_Name,u.Mark  "
	  	        + "FROM Assignments a,UserAssignMarkMap u "
	  	        + "WHERE u.utor_id = ? and a.id=u.Assignment_Id "
	  	        + " ORDER BY u.Mark desc";
	      stmt = c.prepareStatement(sql);
	      stmt.setString(1, utorId);
	      ResultSet set = stmt.executeQuery();
	      HashMap<Assignment,Integer> markMap = new HashMap<Assignment,Integer>();
	      while(set.next()){
	    	  int id = set.getInt("id");
	    	  String assignName = set.getString("Assignment_Name");
	    	  int score = set.getInt("Mark");
	    	  markMap.put(new Assignment(assignName,id),score);
	      }
	      set.close();
	      stmt.close();
	      c.close();
	    } catch (Exception e) {
	      // TODO Error Handling
	      System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    }
	    return mark;
	}
	/**
	 * <p>query all assignment's marks of the course for this student</p>
	 * @param utorId student's id
	 * @param course_code course code
	 * @return mark Object
	 */
	public Mark getStudentMark(String utorId,String course_code) {
		Mark mark = new Mark();
	    Connection c;
	    PreparedStatement stmt;
	    String sql = "SELECT utor_id, last_name, first_name "
	        + "FROM Users "
	        + "WHERE utor_id = ?";
	    try {
	      c = new PostgreSqlJdbc().getConnection();
	      stmt = c.prepareStatement(sql);
	      stmt.setString(1, utorId);
	      ResultSet rs = stmt.executeQuery();
	      
	      while (rs.next()) {
	        String utor_id = rs.getString("utor_id");
	        String lname = rs.getString("last_name");
	        String fname = rs.getString("first_name");
	        mark.setStudent(new Student(utor_id,lname,fname));
	      }
	      rs.close();
	      stmt.close();
	      sql = "SELECT a.id,a.Assignment_Name,u.Mark  "
	  	        + "FROM Assignments a,UserAssignMarkMap u "
	  	        + "WHERE u.utor_id = ? and a.id=u.Assignment_Id and a.Course_Code =?"
	  	        + " ORDER BY u.Mark desc";
	      stmt = c.prepareStatement(sql);
	      stmt.setString(1, utorId);
	      stmt.setString(2, course_code);
	      ResultSet set = stmt.executeQuery();
	      HashMap<Assignment,Integer> markMap = new HashMap<Assignment,Integer>();
	      while(set.next()){
	    	  int id = set.getInt("id");
	    	  String assignName = set.getString("Assignment_Name");
	    	  int score = set.getInt("Mark");
	    	  markMap.put(new Assignment(assignName,id),score);
	      }
	      mark.setMarkMap(markMap);
	      set.close();
	      stmt.close();
	      c.close();
	    } catch (Exception e) {
	      // TODO Error Handling
	      System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    }
	    return mark;
	}
}
