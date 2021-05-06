package zodiac.gui.admin;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import zodiac.action.QuestionAction;
import zodiac.dao.coursework.AssignmentDao;
import zodiac.definition.Student;
import zodiac.definition.coursework.Assignment;
import zodiac.definition.security.SecurityConstants;
import zodiac.definition.security.User;
import zodiac.gui.user.AssignmentUi;
import zodiac.util.ActiveUser;

//import com.intellij.ui.components.JBScrollPane;
//import com.intellij.ui.components.JBList;


public class GetAssignmentsMenu {

  private JPanel panel;
  private JList assignList;
  private DefaultListModel model;

  /**
   * Generate the contents of the Assignment manager menu.
   */
  public JPanel generateContents() {
    this.panel = new JPanel();
    this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));

    JPanel searchPanel = new JPanel(new FlowLayout());
    JLabel prompt = new JLabel("Enter a Course Code: ");
    JTextField textCourseCode = new JTextField();
    textCourseCode.setPreferredSize(new Dimension(300, 25));
    textCourseCode.setMinimumSize(new Dimension(300, 25));
    JButton submit = new JButton("Search");

    submit.addActionListener(new getAssignmentsListener(textCourseCode));

    searchPanel.add(prompt);
    searchPanel.add(textCourseCode);
    searchPanel.add(submit);

    this.model = new DefaultListModel();

    assignList = new JList(model);
    JScrollPane scrollPane = new JScrollPane(assignList);

    this.panel.add(searchPanel);
    this.panel.add(scrollPane);

    addMouseListener(assignList);

    return this.panel;
  }

  // Open assignment editor when on double click assignment
  private void addMouseListener(JList list) {
    list.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        JList list = (JList) evt.getSource();
        if (evt.getClickCount() == 2) {
          // Double-click detected
          int index = list.locationToIndex(evt.getPoint());
          Assignment assignment = (Assignment) model.get(index);
          assignment.setQuestionList(new QuestionAction().getQuestionsWithAnswer(assignment.getId()));
          User user = ActiveUser.INSTANCE.getUser();
          if(user.getRole() == SecurityConstants.STUDENT_ROLE){
            new AssignmentUi(assignment,new Student(user.getUtorId(),user.getLastName(),user.getFirstName())).setVisible(true);
          }else{
            new EditAssignmentMenu(assignment).setVisible(true);
          }

        }
      }
    });
  }

  private class getAssignmentsListener implements ActionListener {

    private JTextField courseCode;

    getAssignmentsListener(JTextField code) {
      this.courseCode = code;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
      System.out.println(this.courseCode);
      List<Assignment> results = new AssignmentDao().getAssignments (this.courseCode.getText());
      model.clear();
      for (Assignment a : results) {
//                System.out.println(a.getName());
        // What the model displays is defined in Assignment.ToString
        model.addElement(a);
      }

      assignList = new JList(model);
      panel.revalidate();
    }
  }
}
