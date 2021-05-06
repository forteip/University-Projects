package zodiac.gui.admin;

//import com.intellij.ui.components.JBList;
//import com.intellij.ui.components.JBScrollPane;
import zodiac.action.QuestionAction;
import zodiac.dao.coursework.AssignmentDao;
import zodiac.dao.coursework.QuestionDao;
import zodiac.definition.coursework.Assignment;
import zodiac.definition.coursework.Question;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents the menu for adding
 * Questions to Assignments
 */
public class AddQuestionToAssignmentMenu {
    private JPanel panel;
    private Assignment currAssign;
    private List<Integer> selectedQuestions;
    private JList<Question> questionList;
    private DefaultListModel<Question> model;

    /**
     * Generates the contents for the AddQuestiontoAssignment
     * menu.
     * @return the JPanel with all elements added
     */
    public JPanel generateContents()
    {
        panel = new JPanel(new FlowLayout());

        // Create combo box with all assignments
        List<Assignment> assignments = new AssignmentDao().getAllAssignments();
        JComboBox assignmentCb = new JComboBox(assignments.toArray());
        assignmentCb.addItemListener(new AssignmentSelectListener());

        // Create button to add questions to assignments
        JButton addButton = new JButton("Add Questions");
        addButton.addActionListener(new AddQuestionListener());

        selectedQuestions = new ArrayList<>();

        // Create the Question list
        this.model = new DefaultListModel<>();
        this.questionList = new JList<>(this.model);
        this.questionList.getSelectionModel().addListSelectionListener(new QuestionListSelectListener(this.questionList));
        JScrollPane pane = new JScrollPane(this.questionList);

        // Add elements to the panel
        panel.add(assignmentCb);
        panel.add(pane);
        panel.add(addButton);

        return panel;
    }

    /**
     * Listener for the button that adds Questions to an assignment
     */
    private class AddQuestionListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            for (Integer i : selectedQuestions)
            {
                new QuestionAction().addQuestionToAssignment(model.get(i).getQid(), currAssign.getId());
            }

        }
    }

    /**
     * Listener for when a different assignment has been selected
     */
    private class AssignmentSelectListener implements ItemListener
    {
        @Override
        public void itemStateChanged(ItemEvent itemEvent) {
            model.clear();
            List<Question> questions = new QuestionAction().getAllQuestions();
            Assignment a = (Assignment)itemEvent.getItem();
            currAssign = a;
            List<Question> omitted = a.getQuestionList();

            for (Question q: questions)
            {
                if (!omitted.contains(q))
                {
                    model.addElement(q);
                }
            }

            questionList = new JList(model);
            questionList.getSelectionModel().addListSelectionListener(new QuestionListSelectListener(questionList));
            panel.revalidate();

        }
    }

    /**
     * A listener for when an item has been selected from the
     * Questions list.
     */
    private class QuestionListSelectListener implements ListSelectionListener
    {
        private JList test;

        public QuestionListSelectListener(JList test) {
            this.test = test;
        }

        @Override
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            selectedQuestions.clear();
            for (int i = 0; i < model.size(); i++)
            {
                if (test.isSelectedIndex(i)) {
                    selectedQuestions.add(i);
                }
            }
//            System.out.println(selectedQuestions);

        }
    }
}
