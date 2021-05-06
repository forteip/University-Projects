package zodiac.definition.coursework;

import java.util.ArrayList;
import java.util.Date;
import junit.framework.TestCase;

public class AssignmentTest extends TestCase {

  Assignment assign;
  String name;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    name = "Assignment 1";
    assign = new Assignment(name);

  }

  public void testInit() {
    assertTrue(
        assign.getId() == -1 && assign.getQuestionList().size() == 0 && assign.getMaxAttempt() == 0
            &&
            assign.getCurrScore() == 0 && assign.getHighScore() == 0 && assign.getName()
            .equals(name)
            && assign.getCloseDate() == null && assign.getOpenDate() == null);
  }

  public void testCurrScore() {
    int score = 3;
    assign.setCurrScore(score);
    assertTrue(assign.getCurrScore() == score);
  }

  public void testHighScore() {
    int highScore = 4;
    assertTrue(assign.getHighScore() == highScore);
  }

  public void testQuestionSet() {
    ArrayList<Question> questions = new ArrayList<>();
    assign.setQuestionList(questions);
    assertTrue(assign.getQuestionList() == questions);
  }


  public void testMaxAttempt() {
    int attemp = 3;
    assign.setMaxAttempt(attemp);
    assertTrue(attemp == assign.getMaxAttempt());
  }

  public void testChangeName() {
    String newName = "asd";
    assign.setName(newName);
    assertTrue(newName == assign.getName());
  }

  public void testSetOpenDate() {
    Date openDate = new Date();
    assign.setOpenDate(openDate);
    assertTrue(openDate.compareTo(assign.getOpenDate()) == 0);
  }

  public void testSetCloseDate() {
    Date openDate = new Date();
    assign.setCloseDate(openDate);
    assertTrue(openDate.compareTo(assign.getCloseDate()) == 0);
  }

}
