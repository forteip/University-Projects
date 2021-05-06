package zodiac.gui.user;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import org.apache.commons.lang3.StringUtils;
import zodiac.action.StudentAction;
import zodiac.definition.Student;
import zodiac.definition.coursework.Assignment;
import zodiac.definition.coursework.Question;
import zodiac.definition.coursework.QuestionTypeConstants;
import zodiac.util.PDFGenerator;

public class AssignmentUi extends JFrame implements ActionListener {

  private Map<Integer, String> textFieldAnswers;
  private Map<Integer, String> textFieldUserAnswers;
  private ArrayList<Integer> answers;
  private ArrayList<JRadioButton> rdbts;
  private JButton btnSaveNext;
  private JButton btnPrev;
  private JButton btnPdf;
  private JLabel lblNewLabel;
  private ButtonGroup group;
  private JScrollPane scrollPane;
  private JPanel contentPane;
  private JTextField textField;
  private Assignment assignment;
  private List<Question> questions;
  private TreeMap<Question, List<String>> qas;
  private Student student;
  private AssignmentUi ref = this;
  private int currentAt = 0;

  public AssignmentUi(Assignment ass, Student stud) {
    // -1 id marks a new assignment not in the database
    super("Assignment " + ass.getId());
    this.student = stud;
    this.assignment = ass;
    this.questions = ass.getQuestionList();
    this.createContents();
  }

  /**
   * Create contents of the window.
   */
  protected void createContents() {
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setBounds(100, 100, 450, 350);
    contentPane = new JPanel();
    contentPane.setLayout(null);
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

    setContentPane(contentPane);

    //  buttons
    btnSaveNext = new JButton();
    btnSaveNext.setBounds(346, 291, 94, 28);
    btnSaveNext.setText("Save & Next");
    btnSaveNext.setEnabled(false);
    contentPane.add(btnSaveNext);

    btnPrev = new JButton();
    btnPrev.setBounds(248, 291, 94, 28);
    btnPrev.setText("Back");
    btnPrev.setEnabled(false);
    contentPane.add(btnPrev);

    btnPdf = new JButton();
    btnPdf.setBounds(20, 291, 94, 28);
    btnPdf.setText("PDF");
    contentPane.add(btnPdf);
    // question label
    lblNewLabel = new JLabel("1. " + this.questions.get(0).getQuestion());
    lblNewLabel.setBounds(40, 34, 350, 40);
    contentPane.add(lblNewLabel);
    // note label
    JLabel newLable = new JLabel("Note: your progress will be saved.");
    newLable.setBounds(130, 261, 350, 40);
    contentPane.add(newLable);
    // setting answer options
    group = new ButtonGroup();

    answers = new ArrayList<Integer>();
    // initialize qa set
    qas = new TreeMap<Question, List<String>>();

    for (Question question : this.questions) {
      qas.put(question, question.getAnswerList());
    }
    // init temporal answer
    textFieldAnswers = new HashMap<>();
    textFieldUserAnswers = new HashMap<>();

    TreeMap<Question, String> temporalAnswer = new StudentAction()
        .fetchTempAnswerFromAssignment(student, assignment);
    for (Question question : questions) {
      if (temporalAnswer.get(question) != null) {
        Integer rs = null;
        for (int p = 0; p < question.getAnswerList().size(); p++) {
          if (qas.get(question).get(p).equals(temporalAnswer.get(question))) {

            rs = p;
            break;
          }

        }
        if (rs != null) {
          answers.add(rs);
        }
        if (question.getQuestionType().equals(QuestionTypeConstants.TEXT_FIELD)) {
          textFieldAnswers.put(question.getQid(), temporalAnswer.get(question));
        }
      }


    }

    textField = new JTextField();
    textField.setBounds(50, 80, 350, 60);
    textField.setAlignmentX(CENTER_ALIGNMENT);
    contentPane.add(textField);

    rdbts = new ArrayList<JRadioButton>();
    // adding answer option
    int i;
    for (i = 0; i < 5; i++) {
      JRadioButton rb = new JRadioButton();
      rdbts.add(rb);
      group.add(rb);
      rb.setBounds(50, 80 + i * 50, 350, 37);
      rb.addActionListener(this);
      contentPane.add(rb);
    }

    i = 0;
    // only set avaliable option visible
    for (String answer : this.qas.get(this.questions.get(0))) {
      JRadioButton rb = rdbts.get(i);
      rb.setText(answer);
      i += 1;
    }
    try {
      rdbts.get(this.answers.get(0)).setSelected(true);
      btnSaveNext.setEnabled(true);
    } catch (IndexOutOfBoundsException ex) {

    }

    if (this.questions.get(0).getQuestionType().equals(QuestionTypeConstants.TEXT_FIELD)) {
      textField.setText(textFieldAnswers.get(this.questions.get(0).getQid()));
    }

    setQuestionTypeVisibility(this.questions.get(0).getQuestionType());

    // set the rest of options invisible
    for (int j = i; j < 5; j++) {
      JRadioButton rb = rdbts.get(j);
      rb.setVisible(false);
    }
    // change to submit immediately

    btnSaveNext.addActionListener(this);
    if (this.questions.size() == 1) {
      btnSaveNext.setText("Submit");
    }

    if (questions.get(0).getQuestionType().equals(QuestionTypeConstants.TEXT_FIELD)) {
      btnSaveNext.setEnabled(true);
    }

    btnPrev.addActionListener(this);

    btnPdf.addActionListener(this);

  }

  public void actionPerformed(ActionEvent e) {

    if (e.getActionCommand() == btnSaveNext.getActionCommand()) {
      // get the answer
      Integer answerInt = 0;
      for (JRadioButton rdb : rdbts) {
        if (rdb.isSelected()) {
          group.clearSelection();
          break;
        }
        answerInt += 1;
      }

      // save the answer or change the existing answer
      try {
        answers.get(currentAt);
        answers.set(currentAt, answerInt);
      } catch (IndexOutOfBoundsException ex) {
        answers.add(answerInt);
      }

      if (currentAt == questions.size() - 1) {
        // close uI and submit the answer
        TreeMap<Question, String> qa = new TreeMap<Question, String>();
        for (int i = 0; i < questions.size(); i++) {
          Question q = questions.get(i);
          String a = "";
          if (questions.get(i).getQuestionType().equals(QuestionTypeConstants.MULTIPLE_CHOICE)) {
            a = String.valueOf(rdbts.get(answers.get(i)).getText());
          } else if (questions.get(i).getQuestionType().equals(QuestionTypeConstants.TEXT_FIELD)) {
            a = StringUtils.trimToEmpty(textField.getText());
            textFieldUserAnswers.put(i, a);
          }
          qa.put(q, a);

        }
        new StudentAction().addAnswerToAssignment(student, assignment, qa);
        Integer result = new StudentAction().validateAnswer(qa);
        new StudentAction().saveMark(assignment.getId(), student.getUtorId(), result);
        lblNewLabel.setText("Your result: " + result);
        Font f = lblNewLabel.getFont();
        lblNewLabel.setFont(new Font(f.getName(), Font.BOLD, f.getSize() + 4));
        textField.setVisible(false);
        for (int j = 0; j < 5; j++) {
          JRadioButton rb = rdbts.get(j);
          rb.setVisible(false);
          contentPane.remove(rb);
        }
        btnPrev.setEnabled(false);
        btnSaveNext.setEnabled(false);
        // display all the answers
        // setting all answers
        GridLayout grid = new GridLayout(0, 1);
        grid.setVgap(8);
        JPanel panel = new JPanel(grid);
        int k;
        for (k = 0; k < questions.size(); k++) {
          Question q = this.questions.get(k);
          lblNewLabel = new JLabel(String.valueOf(k + 1) + ": " + q.getQuestion());
          panel.add(lblNewLabel);
          lblNewLabel = new JLabel("    Answer: " + q.getCorrectAnswer());
          panel.add(lblNewLabel);

          String answer = "";

          if (questions.get(k).getQuestionType().equals(QuestionTypeConstants.TEXT_FIELD)) {
            answer = textFieldUserAnswers.get(k);
          } else if (questions.get(k).getQuestionType().equals(QuestionTypeConstants.MULTIPLE_CHOICE)) {
            answer = rdbts.get((answers.get(k))).getText();
          }

          lblNewLabel = new JLabel("    Your Answer: " + answer);
          panel.add(lblNewLabel);

        }
        panel.revalidate();

        scrollPane = new JScrollPane(panel);

        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        scrollPane.setBounds(35, 80, 380, 200);
        scrollPane.getViewport().setMinimumSize(new Dimension(380, 200));
        scrollPane.getViewport().setPreferredSize(new Dimension(380, 200));

        contentPane.add(scrollPane);
      } else {
        // save current answer into database
        String tempAnswer = "";
        if (questions.get(currentAt).getQuestionType().equals(QuestionTypeConstants.MULTIPLE_CHOICE)) {
          tempAnswer = rdbts.get(answers.get(currentAt)).getText();
        } else if (questions.get(currentAt).getQuestionType().equals(QuestionTypeConstants.TEXT_FIELD)) {
          tempAnswer = StringUtils.trimToEmpty(textField.getText());
        }
        new StudentAction().addTempAnswerToQuestion(student, assignment, questions.get(currentAt),
            tempAnswer);

        currentAt += 1;
        // trying to see if we have next answer
        try {
          answers.get(currentAt);
        } catch (IndexOutOfBoundsException ex) {
          if (questions.get(currentAt).getQuestionType().equals(QuestionTypeConstants.MULTIPLE_CHOICE)) {
            btnSaveNext.setEnabled(false);
          }
        }
        // rendering the next question and answers
        lblNewLabel
            .setText(String.valueOf(currentAt + 1) + ". " + questions.get(currentAt).getQuestion());
        int i = 0;
        for (String answer : this.qas.get(this.questions.get(currentAt))) {
          JRadioButton rb = rdbts.get(i);
          rb.setText(answer);
          rb.setVisible(true);
          i += 1;
        }
        setQuestionTypeVisibility(this.questions.get(currentAt).getQuestionType());
        for (int j = i; j < 5; j++) {
          JRadioButton rb = rdbts.get(j);
          rb.setVisible(false);
        }
        // setting anwser for rendered question if we have
        if (this.questions.get(currentAt).getQuestionType()
            .equals(QuestionTypeConstants.TEXT_FIELD)) {
          textField.setText(textFieldAnswers.get(currentAt));
        }
        try {
          int answer = answers.get(currentAt);
          rdbts.get(answer).setSelected(true);
        } catch (IndexOutOfBoundsException ex) {

        }
        // setting rendering rang
        btnPrev.setEnabled(true);
        // see if we have reach the end
        if (currentAt == questions.size() - 1) {
          btnSaveNext.setText("Submit");
        }
      }


    } else if (e.getActionCommand() == btnPrev.getActionCommand()) {
      if (currentAt == questions.size() - 1) {
        btnSaveNext.setText("Save & Next");
      }
      currentAt -= 1;

      lblNewLabel
          .setText(String.valueOf(currentAt + 1) + ". " + questions.get(currentAt).getQuestion());
      int i = 0;
      for (String answer : this.qas.get(this.questions.get(currentAt))) {
        JRadioButton rb = rdbts.get(i);
        rb.setText(answer);
        i += 1;
      }
      setQuestionTypeVisibility(this.questions.get(currentAt).getQuestionType());
      for (int j = i; j < 5; j++) {
        JRadioButton rb = rdbts.get(j);
        rb.setVisible(false);
      }
      // trying to see if we have prev answer, and set it
      if (this.questions.get(currentAt).getQuestionType()
          .equals(QuestionTypeConstants.TEXT_FIELD)) {
        textField.setText(textFieldAnswers.get(currentAt));
      }
      try {
        int answer = answers.get(currentAt);
        rdbts.get(answer).setSelected(true);
        btnSaveNext.setEnabled(true);
      } catch (IndexOutOfBoundsException ex) {
      }

      if (currentAt == 0) {
        if (questions.get(currentAt).getQuestionType().equals(QuestionTypeConstants.MULTIPLE_CHOICE)) {
          btnPrev.setEnabled(false);
        }
      }
    } else if (e.getActionCommand() == btnPdf.getActionCommand()) {
      String path = (new PDFGenerator(assignment)).generate();

      try {
        if (path != null) {
          Desktop.getDesktop().open(new File(path));
        } else {
          JOptionPane.showMessageDialog(null, "Error occur, PDF File writing failed.");
        }

      } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error occur, PDF File writing failed.");
      }

    } else {
      btnSaveNext.setEnabled(true);
    }
  }

  private void setQuestionTypeVisibility(String type) {
    if (type.equals(QuestionTypeConstants.TEXT_FIELD)) {
      setVisibility(true, false);
    } else if (type.equals(QuestionTypeConstants.MULTIPLE_CHOICE)) {
      setVisibility(false, true);
    }
  }

  private void setVisibility(boolean textFieldVisibility, boolean radioVisibility) {
    textField.setVisible(textFieldVisibility);
    for (JRadioButton button : rdbts) {
      button.setVisible(radioVisibility);
    }
  }

}

