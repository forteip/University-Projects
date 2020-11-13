package zodiac.dao.coursework;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import zodiac.definition.coursework.Question;
import zodiac.util.PostgreSqlJdbc;

public class QuestionDao {

  public List<Question> getAllQuestions() {
    return getAllQuestions(false);
  }

  /**
   * Get a list of all questions in the system.
   * generateAnswers defaults as False.
   *
   * @param generateAnswers whether to populate the answers list or not
   * @return list of all questions
   */
  public List<Question> getAllQuestions(Boolean generateAnswers) {
    List<Question> questions = new ArrayList<>();

    Connection c;
    PreparedStatement stmt;

    String sql = "SELECT q.Id, q.Question FROM Questions q"
        + " ORDER BY q.Id DESC";

    try {
      c = new PostgreSqlJdbc().getConnection();
      stmt = c.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        int id = rs.getInt("Id");
        String question = rs.getString("Question");
        Question questionObject = new Question(id);
        questionObject.setQuestion(question);
        if (generateAnswers) {
          generateAnswers(questionObject);
        }
        questions.add(questionObject);
      }

      rs.close();
      stmt.close();
      c.close();
    } catch (Exception e) {
      // TODO Error Handling
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }

    return questions;
  }

  public List<Question> getQuestions(int assignmentId) {
    return getQuestions(assignmentId, false);
  }

  /**
   * Get a list of questions in the system belonging to the given assignment.
   * generateAnswers defaults as False.
   *
   * @param assignmentId id of the assignment
   * @param generateAnswers whether to populate the answers list or not
   * @return list of questions belonging to the given assignment
   */
  public List<Question> getQuestions(int assignmentId, Boolean generateAnswers) {
    List<Question> questions = new ArrayList<>();

    Connection c;
    PreparedStatement stmt;

    String sql = "SELECT q.Id, q.Question FROM Questions q"
        + " INNER JOIN AssignmentQuestionMap m"
        + " ON m.Question_Id = q.Id"
        + " WHERE m.Assignment_Id = ?"
        + " ORDER BY q.Id DESC";

    try {
      c = new PostgreSqlJdbc().getConnection();
      stmt = c.prepareStatement(sql);
      stmt.setInt(1, assignmentId);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        int id = rs.getInt("Id");
        String question = rs.getString("Question");
        Question questionObject = new Question(id);
        questionObject.setQuestion(question);
        if (generateAnswers) {
          generateAnswers(questionObject);
        }
        questions.add(questionObject);
      }

      rs.close();
      stmt.close();
      c.close();
    } catch (Exception e) {
      // TODO Error Handling
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }

    return questions;
  }

  /**
   * Add a question to the database.
   *
   * @param question what the question is asking
   * @return created question with the id
   */
  public Question addQuestion(String question) {
    Connection c;
    PreparedStatement stmt;

    Question createdQuestion = null;

    String sql = "INSERT INTO Questions (Question) VALUES (?) RETURNING ID";

    try {
      c = new PostgreSqlJdbc().getConnection();
      stmt = c.prepareStatement(sql);
      stmt.setString(1, question);
      ResultSet rs = stmt.executeQuery();
      rs.next();
      int id = rs.getInt(1);
      createdQuestion = new Question(id);
      createdQuestion.setQuestion(question);

      rs.close();
      stmt.close();
      c.close();
    } catch (Exception e) {
      // TODO Error Handling
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }
    return createdQuestion;
  }

  /**
   * Add a question to an assignment.
   *
   * @param questionId id of the question to add
   * @param assignmentId id of the assignment to add the question to
   * @return whether the query failed, succeeded or potentially more details
   */
  public String addQuestionToAssignment(int questionId, int assignmentId) {
    Connection c;
    PreparedStatement stmt;

    String message = "Failed";

    String sql = "INSERT INTO AssignmentQuestionMap (Assignment_Id, Question_Id) VALUES (?, ?)";

    try {
      c = new PostgreSqlJdbc().getConnection();
      stmt = c.prepareStatement(sql);
      stmt.setInt(1, assignmentId);
      stmt.setInt(2, questionId);
      if (stmt.executeUpdate() > 0) {
        message = "Success";
      }

      stmt.close();
      c.close();
    } catch (Exception e) {
      // TODO Error Handling
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }

    return message;
  }

  /**
   * Add an answer to the question.
   *
   * @param questionId id of the question to add it
   * @param answer the answer to add to the question
   * @param correct whether the answer is correct or not
   * @return whether the query failed, succeeded or potentially more details
   */
  public String addAnswerToQuestion(int questionId, String answer, boolean correct) {
    Connection c;
    PreparedStatement stmt;

    String message = "Failed";

    String sql = "INSERT INTO QuestionAnswerMap (Question_Id, Answer, Correct) VALUES (?, ?, ?)";

    try {
      c = new PostgreSqlJdbc().getConnection();
      stmt = c.prepareStatement(sql);
      stmt.setInt(1, questionId);
      stmt.setString(2, answer);
      stmt.setBoolean(3, correct);
      if (stmt.executeUpdate() > 0) {
        message = "Success";
      }

      stmt.close();
      c.close();
    } catch (Exception e) {
      // TODO Error Handling
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }

    return message;
  }

  private void generateAnswers(Question question) {
    Connection c;
    PreparedStatement stmt;

    String sql = "SELECT Answer, Correct "
        + "FROM QuestionAnswerMap "
        + "WHERE Question_Id = ?";
    try {
      c = new PostgreSqlJdbc().getConnection();
      stmt = c.prepareStatement(sql);
      stmt.setInt(1, question.getQid());
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        String answer = rs.getString("Answer");
        Boolean correct = rs.getBoolean("Correct");
        question.getAnswerList().add(answer);
        if (correct) {
          question.setCorrectAnswer(answer);
        }
      }

      rs.close();
      stmt.close();
      c.close();
    } catch (Exception e) {
      // TODO Error Handling
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }
  }
}
