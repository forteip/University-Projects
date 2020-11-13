package zodiac.definition;

public class Student {

  /*
  For now this is okay, but a student object should not store the class code or tut code.
  A student could be enrolled in multiple courses in the system which would also imply multiple
  tutorials. Whenever we need to check a student's class and/or tutorial, it would be better
  to just do a DB lookup at the time.
   */
  private String utorId;
  private String lastName;
  private String firstName;
  private String classCode;
  private String tutCode;

  /**
   * Initialize the student with given information.
   */
  public Student(String utorId, String lastName, String firstName, String classCode,
      String tutCode) {
    this.utorId = utorId;
    this.lastName = lastName;
    this.firstName = firstName;
    this.classCode = classCode;
    this.tutCode = tutCode;
  }

  /**
   * Initialize the student with only the id and name.
   *
   * @param utorId UTOR ID of the student
   * @param lastName Student's last name
   * @param firstName Student's first name
   */
  public Student(String utorId, String lastName, String firstName) {
    this.utorId = utorId;
    this.lastName = lastName;
    this.firstName = firstName;
    this.classCode = "";
    this.tutCode = "";
  }

  /**
   * set the class code.
   */
  public void setClassCode(String classCode) {
    this.classCode = classCode;
  }

  /**
   * get the class code.
   *
   * @return the code of class
   */
  public String getClassCode() {
    return classCode;
  }

  /**
   * set the code of tut.
   */
  public void setTutCode(String tutCode) {
    this.tutCode = tutCode;
  }

  /**
   * get the code of tut.
   *
   * @return the code of tut
   */
  public String getTutCode() {
    return tutCode;
  }

  /**
   * set the student UTOR ID.
   */
  public void setUtorId(String utorId) {
    this.utorId = utorId;
  }

  /**
   * get the UTOR ID.
   *
   * @return the UTOR ID of student
   */
  public String getUtorId() {
    return this.utorId;
  }

  /**
   * set the last name.
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * get the last name.
   *
   * @return the last name of student
   */
  public String getLastName() {
    return this.lastName;
  }

  /**
   * set the first name.
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * get the first name.
   *
   * @return the last name of student
   */
  public String getFirstName() {
    return this.firstName;
  }

}
















