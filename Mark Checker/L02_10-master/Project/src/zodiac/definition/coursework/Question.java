package zodiac.definition.coursework;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by radiantwings on 10/24/17. This class represents a Question which would be included in
 * >= 0 Assignments
 */
public class Question implements Comparable{

  private int qid;
  private List<String> answerList;
  private String question;
  private String correctAnswer;

  // TODO: Add Javadoc
  public Question(int qid) {
    this.qid = qid;
    answerList = new ArrayList<String>();
    this.correctAnswer = "";

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

  @Override
  public int compareTo(Object o) {
      // TODO Auto-generated method stub
      return this.qid - ((Question)o).qid;
  }

  @Override
  public String toString() {
    return question;
  }
}
