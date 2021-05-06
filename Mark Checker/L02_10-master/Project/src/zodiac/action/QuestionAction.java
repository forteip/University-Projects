package zodiac.action;

import java.util.ArrayList;
import java.util.List;
import zodiac.dao.coursework.AssignmentDao;
import zodiac.dao.coursework.QuestionDao;
import zodiac.definition.MessageConstants;
import zodiac.definition.coursework.Question;
import zodiac.definition.coursework.QuestionTypeConstants;
import zodiac.definition.security.SecurityConstants;
import zodiac.util.ActiveUser;

public class QuestionAction {

  /**
   * Create a new question but don't add it to any assignments.
   *
   * @param question the question
   * @param questionType the type of question
   * @param autoMark if it should be auto marked or not
   * @return the created question
   */
  public Question createQuestion(String question, String questionType, boolean autoMark) {
    return new QuestionDao().addQuestion(question, questionType, autoMark);
  }

  public String createMultipleChoiceQuestion(int assignmentId, String question) {
    return createQuestion(question, QuestionTypeConstants.MULTIPLE_CHOICE, true, assignmentId);
  }

  /**
   * Create a new question for the given assignment.
   *
   * @param question the question
   * @param questionType the type of question
   * @param autoMark if it should be auto marked or not
   * @param assignmentId the assignment to add the question to
   * @return message to display on the UI
   */
  public String createQuestion(String question, String questionType, boolean autoMark,
      int assignmentId) {
    if (ActiveUser.INSTANCE.canWrite(new AssignmentDao().getCourseOfAssignment(assignmentId))) {
      int newQuestion = new QuestionDao().addQuestion(question, questionType, autoMark).getQid();
      return new QuestionDao().addQuestionToAssignment(newQuestion, assignmentId);
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

  /**
   * Add an existing question to an id.
   *
   * @param questionId the question id
   * @param assignmentId the assignment id
   * @return message to display to the user
   */
  public String addQuestionToAssignment(int questionId, int assignmentId) {
    if (ActiveUser.INSTANCE.canRead(new AssignmentDao().getCourseOfAssignment(assignmentId))) {
      return new QuestionDao().addQuestionToAssignment(questionId, assignmentId);
    } else {
      return MessageConstants.NO_PERMISSION_MESSAGE;
    }
  }

  /**
   * Get all questions for the database.
   *
   * @return list of all questions
   */
  public List<Question> getAllQuestions() {
    return new QuestionDao().getAllQuestions();
  }

}