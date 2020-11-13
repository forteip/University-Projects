package zodiac.definition.coursework;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;

public class QuestionTest extends TestCase {

    private Question q;
    private int qID;
    @Override
    protected void setUp() throws Exception {
        qID = 5;
        q = new Question(qID);
    }

    public void testInit(){

        assertEquals(qID,q.getQid());
    }

    public void testInitWrong(){
        assertTrue(qID+5!=q.getQid());
    }


    public void testSetCorrectAnswer(){
        String ans = "correctAnswer";
        q.setCorrectAnswer(ans);
        assertEquals(ans,q.getCorrectAnswer());
    }

    public  void testSetWrongAnswer(){
        String ans = "correctAnswer";
        q.setCorrectAnswer(ans);
        assertTrue(!"i am wrong answer".equals(q.getCorrectAnswer()));
    }

    public void testSetQuestion(){
        String ques = "I am the question" ;
        q.setQuestion(ques);
        assertEquals(ques,q.getQuestion());
    }

    public void testWrongQuestion(){
        String ques = "I am the question not the answer" ;
        assertTrue(!ques.equals(q.getQuestion()));
    }

    public void testEmptyAnswerList(){
       assertTrue( q.getAnswerList().size()==0 && q.getAnswerList().isEmpty()) ;
    }

    public void testSomeAnswersSize(){
        String [] answerStrings = {"Asda","qeqwe","qweqe"};
        ArrayList<String> answer = new ArrayList<String>(Arrays.asList(answerStrings));
        q.setAnswerList(answer);
        assertTrue(answerStrings.length==q.getAnswerList().size() && !q.getAnswerList().isEmpty());
    }

    public void testAnswerInTheList(){
        int index = 2;
        String [] answerStrings = {"Asda","qeqwe","qweqe"};
        ArrayList<String> answer = new ArrayList<String>(Arrays.asList(answerStrings));
        q.setAnswerList(answer);
        assertEquals(answer.get(index),q.getAnswerList().get(index));
    }

    public void testComparison(){
        assertTrue(q.compareTo(q) == 0);
    }


}
