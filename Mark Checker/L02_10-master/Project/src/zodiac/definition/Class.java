package zodiac.definition;

import java.util.List;

public class Class {

  private String courseCode;
  private String className;
  private List<Student> students;


  /**
   * Initialize the class with given information.
   */
  public Class(String courseCode, String className, List<Student> students) {
    this.courseCode = courseCode;
    this.className = className;
    this.students = students;
  }

  /**
   * get the code of course.
   *
   * @return the code of course
   */
  public String getCourseCode() {
    return courseCode;
  }

  /**
   * set the code of course.
   */
  public void setCourseCode(String courseCode) {
    this.courseCode = courseCode;
  }

  /**
   * get the name of class.
   *
   * @return the name of class
   */
  public String getClassName() {
    return this.className;
  }

  /**
   * set the name of class.
   */
  public void setClassName(String className) {
    this.className = className;
  }

  /**
   * get a list of Students of class.
   *
   * @return list of students
   */
  public List<Student> getStudents() {
    return this.students;
  }

  /**
   * set a list of Students.
   */
  public void setStudents(List<Student> students) {
    this.students = students;
  }
}
