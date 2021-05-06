package zodiac.definition.coursework;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by radiantwings on 10/24/17. This class represents an Assignment that Students will
 * complete. An assignment will typically hold >= 1 Questions
 */
public class Assignment {

  private int id;
  private List<Question> questionList;
  private int currScore;
  private int highScore;
  private int maxAttempt;
  private String name;
  private Date openDate;
  private Date closeDate;
  private Date earlySubmissionDeadline;
  private Integer extraPoints;

  // field added
  private Boolean visibility;

  /**
   * Constructor for Assignment.
   */
  public Assignment(String name) {
    // -1 id marks a new assignment not in the database
    this.id = -1;
    this.questionList = new ArrayList<Question>();
    this.currScore = 0;
    this.highScore = 0;
    this.name = name;
    this.visibility = true;
    this.maxAttempt = 0;
  }

  public Assignment(String name, int id) {
    this(name);
    this.id = id;
  }

  public boolean addQuestion(Question q) {
    return this.questionList.add(q);
  }

  // TODO: Add Javadoc
  public boolean removeQuestion(int id) {
    for (int i = 0; i < this.questionList.size(); i++) {
      if (this.questionList.get(i).getQid() == id) {
        this.questionList.remove(i);
        return true;
      }
    }
    return false;
  }

  public List<Question> getQuestionList() {
    return questionList;
  }

  public void setQuestionList(List<Question> questionList) {
    this.questionList = questionList;
  }

  public int getCurrScore() {
    return currScore;
  }

  public void setCurrScore(int currScore) {
    this.currScore = currScore;
  }

  public int getHighScore() {
    return highScore;
  }

  public void setHighScore(int highScore) {
    this.highScore = highScore;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    // Id can not be changed after it is set
    this.id = this.id < 0 ? id : this.id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getVisibility() {
    return visibility;
  }

  public void setVisibility(Boolean visibility) {
    this.visibility = visibility;
  }

  /**
   * Return the number of maximum attempts allowed for an assignment. Returns 0 if infinite attempts
   * are allowed.
   *
   * @return maximum number of attempts allowed. 0 if infinite attempts
   */
  public int getMaxAttempt() {
    return maxAttempt;
  }

  public void setMaxAttempt(int maxAttempt) {
    this.maxAttempt = maxAttempt;
  }

  public Date getOpenDate() {
    return openDate;
  }

  public void setOpenDate(Date openDate) {
    this.openDate = openDate;
  }

  public Date getCloseDate() {
    return closeDate;
  }

  public void setCloseDate(Date closeDate) {
    this.closeDate = closeDate;
  }

  public Integer getExtraPoints() {
    return extraPoints;
  }

  public void setExtraPoints(int extraPoints) {
    this.extraPoints = extraPoints;
  }

  public Date getEarlySubmissionDeadline() {
    return earlySubmissionDeadline;
  }

  public void setEarlySubmissionDeadline(Date earlySubmissionDeadline) {
    this.earlySubmissionDeadline = earlySubmissionDeadline;
  }

  public String toString() {
    Integer myInt = new Integer(this.id);
    return StringUtils.join(myInt, " ", this.name);
  }
}
