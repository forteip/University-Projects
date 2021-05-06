package zodiac;

import java.util.List;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;
import zodiac.action.AssignmentAction;
import zodiac.action.ClassAction;
import zodiac.action.QuestionAction;
import zodiac.action.StudentAction;
import zodiac.dao.ClassDao;
import zodiac.dao.StudentDao;
import zodiac.definition.Class;
import zodiac.definition.Student;
import zodiac.definition.coursework.Question;

public class Main {

  /**
   * Console app to showcase first functions.
   */
  public static void main(String[] args) {

    /**
     * GUI
     */
//    AssignmentGUI gui = new AssignmentGUI();

    Scanner scanner = new Scanner(System.in);
    boolean exit = false;
    String input;
    String Assignment_Input;
    while (!exit) {
      System.out.print("Enter command (help for commands): ");
      input = scanner.nextLine();
      switch (input) {
        case "help": {
          System.out.println("Add student: addStudent");
          System.out.println("Add class: addClass");
          System.out.println("Add new assignment: addAssignment");
          System.out.println("Get classes: getClasses");
          System.out.println("Get students: getStudents");
          System.out.println("Get all assignment ID by course: getAssignments");
          System.out.println("Add a question to assignment by Assignment ID: addQuestion");
          System.out.println("Add an answer to a question by QID: addAnswer");
          System.out.println("Start answer on an assignment: answerAssignment");

          System.out.println("Exit: exit");
          break;
        }
        case "addStudent": {
          System.out.print("Enter UTOR ID: ");
          String utorId = StringUtils.trimToEmpty(scanner.nextLine());
          System.out.print("Enter last name: ");
          String lastName = StringUtils.trimToNull(scanner.nextLine());
          System.out.print("Enter first name: ");
          String firstName = StringUtils.trimToNull(scanner.nextLine());
          System.out.print("Enter course code: ");
          String courseCode = StringUtils.trimToEmpty(scanner.nextLine());
          Student student = new Student(utorId, lastName, firstName);
          System.out.println(new StudentAction().addStudentToClass(student, courseCode));
          break;
        }
        case "addClass": {
          System.out.print("Enter course code: ");
          String courseCode = StringUtils.trimToEmpty(scanner.nextLine());
          System.out.print("Enter course name: ");
          String courseName = StringUtils.trimToEmpty(scanner.nextLine());
          System.out.println(new ClassAction().addClass(courseCode, courseName));
          break;
        }

        case "getClasses": {
          List<Class> classes = new ClassDao().getClasses();
          for (Class course : classes) {
            System.out.println(course.getCourseCode() + " " + course.getClassName());
          }
          break;
        }
        case "getStudents": {
          System.out.print("Enter course code: ");
          String courseCode = StringUtils.trimToEmpty(scanner.nextLine());
          List<Student> students = new StudentDao().getStudentsInClass(courseCode);
          for (Student student : students) {
            System.out.println(
                student.getUtorId() + ": " + student.getFirstName() + " " + student.getLastName());
          }
          break;
        }

        case "addAssignment": {
          System.out.print("Enter Assignment name: ");
          String AssignmentName = StringUtils.trimToEmpty(scanner.nextLine());
          System.out.print("Enter Course ID: ");
          String courseId = StringUtils.trimToEmpty(scanner.nextLine());
          System.out.println("Assignment Id Is: " +
              new AssignmentAction().addAssignment(AssignmentName, courseId) + "\n");
          break;
        }

        case "addQuestion": {
          System.out.print("Enter Assignment ID: ");
          String AssignmentIDstr = StringUtils.trimToEmpty(scanner.nextLine());
          System.out.print("Enter Question: ");
          String AssignmentQuestion = StringUtils.trimToEmpty(scanner.nextLine());
          int AssignmentID = Integer.valueOf(AssignmentIDstr);
          System.out.println(
              new QuestionAction().createMultipleChoiceQuestion(AssignmentID, AssignmentQuestion));
          break;
        }

        case "addAnswer": {
          System.out.print("Enter Question ID: ");
          String QuestionIDstr = StringUtils.trimToEmpty(scanner.nextLine());
          int QuestionID = Integer.valueOf(QuestionIDstr);
          System.out.print("Enter Answer: ");
          String AssignmentAnswer = StringUtils.trimToEmpty(scanner.nextLine());
          System.out.print("Enter Boolean, Is this a correct answer?: ");
          String AnswerCorectness = StringUtils.trimToEmpty(scanner.nextLine());
          Boolean Correctness = Boolean.valueOf(AnswerCorectness);
          System.out
              .println(new QuestionAction().addAnswer(QuestionID, AssignmentAnswer, Correctness));
          break;
        }

        case "getQuestions": {
          System.out.print("Enter Assignment ID: ");
          String AssignmentIDstr = StringUtils.trimToEmpty(scanner.nextLine());
          int AssignmentID = Integer.valueOf(AssignmentIDstr);
          List<Question> QuestionList = new QuestionAction().getQid(AssignmentID);
          for (Question question : QuestionList) {
            System.out.println(question.getQuestion() + " ID: " + question.getQid() + "\n");
          }
          break;
        }

//        case "getAssignments": {
//          System.out.print("Enter Course ID: ");
//          String courseId = StringUtils.trimToEmpty(scanner.nextLine());
//          try {
//            List<Assignment> assignments = new AssignmentAction().checkAssignments(courseId);
//            for (Assignment assignment : assignments) {
//              System.out.println(assignment.getName() + " Id: " + assignment.getId());
//            }
//          } catch (SQLException e) {
//            System.out.println(MessageConstants.ERROR);
//          }
//          break;
//        }
//        case "answerAssignment":{
//        	 System.out.print("Enter User ID: ");
//       	 String userId = StringUtils.trimToEmpty(scanner.nextLine());
//        	 System.out.print("Enter Course ID: ");
//        	 String courseId = StringUtils.trimToEmpty(scanner.nextLine());
//        	 System.out.print("Enter Assignment ID: ");
//        	 String assId = StringUtils.trimToEmpty(scanner.nextLine());
//        		try {
//                  Assignment a = new Assignment(courseId,Integer.valueOf(assId));
//                  Student student = StudentAction.getStudent(userId,courseId);
//
//
//        			if(student!=null) {
//
//                      EventQueue.invokeLater(new Runnable() {
//                        public void run() {
//                          try {
//                            AssignmentUi frame = new AssignmentUi(a,student);
//                            frame.setVisible(true);
//                          } catch (Exception e) {
//                            e.printStackTrace();
//                          }
//                        }
//                      });
//
//            			break;
//        			}
//
//        		} catch (Exception e) {
//        			e.printStackTrace();
//        		}
//
//        break;
//        }
        case "exit":
          exit = true;
          break;
        default:
          System.out.println("Enter 'help' for commands");
          break;
      }
    }
  }
}
