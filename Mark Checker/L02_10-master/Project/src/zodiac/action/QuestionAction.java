package zodiac.action;

import java.util.ArrayList;
import java.util.List;
import zodiac.dao.coursework.AssignmentDao;
import zodiac.dao.coursework.QuestionDao;
import zodiac.definition.MessageConstants;
import zodiac.definition.coursework.Question;
import zodiac.definition.security.SecurityConstants;
import zodiac.util.ActiveUser;

public class QuestionAction {

  public String createQuestion(int assignmentid, String question) {
    if (ActiveUser.INSTANCE.canWrite(new AssignmentDao().getCourseOfAssignment(assignmentid))) {
      int newQuestion = new QuestionDao().addQuestion(question).getQid();
      return new QuestionDao().addQuestionToAssignment(newQuestion, assignmentid);
    } else {
      return MessageConstants.NO_PERMISSION_MESSAGE;
    }
  }

  public String addAnswer(int questionId, String answer, boolean correct) {
    if (ActiveUser.INSTANCE.getUser().getRole().equals(SecurityConstants.PROFESSOR_ROLE)) {
      return new QuestionDao().addAnswerToQuestion(questionId, answer, correct);
    } else {
      return MessageConstants.NO_PERMISSION_MESSAGE;
    }
  }

  public List<Question> getQid(int assignmentId) {
    if (ActiveUser.INSTANCE.canRead(new AssignmentDao().getCourseOfAssignment(assignmentId))) {
      return new QuestionDao().getQuestions(assignmentId);
    } else {
      return new ArrayList<>();
    }
  }

  public List<Question> getQuestionsWithAnswer(int assignmentId) {
    if (ActiveUser.INSTANCE.canRead(new AssignmentDao().getCourseOfAssignment(assignmentId))) {
      return new QuestionDao().getQuestions(assignmentId, true);
    } else {
      return new ArrayList<>();
    }
  }
}