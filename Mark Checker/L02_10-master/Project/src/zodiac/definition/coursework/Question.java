package zodiac.definition.coursework;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by radiantwings on 10/24/17. This class represents a Question which would be included in
 * >= 0 Assignments
 */
public class Question implements Comparable {

  private int qid;
  private List<String> answerList;
  private String question;
  private String correctAnswer;
  private String questionType;
  private boolean autoMark;

  // TODO: Add Javadoc
  public Question(int qid) {
    this.qid = qid;
    answerList = new ArrayList<String>();
    this.correctAnswer = "";

  }

  /**
   * Return a list containing possible types of questions.
   *
   * @return list of the type of questions
   */
  public static List<String> getTypes() {
    Field[] types = QuestionTypeConstants.class.getDeclaredFields();

    List<String> typesAsString = new ArrayList<>();

    for (Field type : types) {
      type.setAccessible(true);
      try {
        typesAsString.add((String) type.get(""));
      } catch (IllegalAccessException e) {
        // Should never happen but required to be handled
        e.printStackTrace();
      }
    }

    return typesAsString;
  }

  public int getQid() {
    return qid;
  }

  public List<String> getAnswerList() {
    return answerList;
  }

  public void setAnswerList(List<String> answerList) {
    this.answerList = answerList;
  }

  public String getCorrectAnswer() {
    return correctAnswer;
  }

  public void setCorrectAnswer(String correctAnswer) {
    this.correctAnswer = correctAnswer;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public String getQuestionType() {
    return questionType;
  }

  public void setQuestionType(String questionType) {
    this.questionType = questionType;
  }

  public boolean isAutoMark() {
    return autoMark;
  }

  public void setAutoMark(boolean autoMark) {
    this.autoMark = autoMark;
  }

  @Override
  public int compareTo(Object o) {
    // TODO Auto-generated method stub
    return this.qid - ((Question) o).qid;
  }

  @Override
  public String toString() {
    return question;
  }
}
