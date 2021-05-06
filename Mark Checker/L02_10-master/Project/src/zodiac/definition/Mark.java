package zodiac.definition;

import java.util.HashMap;
import java.util.Map;
import zodiac.definition.coursework.Assignment;

public class Mark {

  // Store student information
  private Student student;
  // Assignment and mark the mapping collection
  private Map<Assignment, Integer> markMap;

  /**
   * Constructor
   */
  public Mark() {
    markMap = new HashMap<Assignment, Integer>();
  }

  // get student method
  public Student getStudent() {
    return student;
  }

  // set method
  public void setStudent(Student student) {
    this.student = student;
  }

  // get Assignment and mark the mapping collection
  public Map<Assignment, Integer> getMarkMap() {
    return markMap;
  }

  // set Assignment and mark the mapping collection
  public void setMarkMap(Map<Assignment, Integer> markMap) {
    this.markMap = markMap;
  }
}
