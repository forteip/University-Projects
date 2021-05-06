package zodiac.action;

import java.util.Date;
import zodiac.dao.MarkDao;
import zodiac.dao.StudentDao;
import zodiac.dao.coursework.AssignmentDao;
import zodiac.definition.Mark;
import zodiac.definition.coursework.Assignment;
import zodiac.util.ActiveUser;

public class MarkAction {

  /**
   * add mark api to add student's mark
   */
  public static String addStudentMark(String utor_id, int assignId, int mark) {
    String result = "";
    String courseCode = new AssignmentDao().getCourseOfAssignment(assignId);
    if (StudentDao.getStudent(utor_id, courseCode) == null) {
      return result;
    }
    Assignment assign = new AssignmentAction().getAssignment(assignId);
    if (assign.getEarlySubmissionDeadline() != null && assign.getExtraPoints() != null) {
      if (new Date().before(assign.getEarlySubmissionDeadline())) {
        mark += assign.getExtraPoints();
      }
    }

    result = new MarkDao().addStudentAssignMark(utor_id, assignId, mark);
    return result;
  }

  /**
   * Gets a list of all mark in the course for this student return a mark object
   */
  public Mark getAllAssignmentsMark(String utor_id, String courseCode) {
    if (ActiveUser.INSTANCE.canRead(courseCode)) {
      return new MarkDao().getStudentMark(utor_id, courseCode);
    } else {
      return null;
    }
  }
}
