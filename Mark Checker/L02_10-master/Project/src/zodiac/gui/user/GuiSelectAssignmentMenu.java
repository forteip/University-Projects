package zodiac.gui.user;

import zodiac.action.AssignmentAction;
import zodiac.action.QuestionAction;
import zodiac.action.StudentAction;
import zodiac.definition.Student;
import zodiac.definition.coursework.Assignment;
import zodiac.gui.GuiSubMenu;

import static zodiac.util.UserAssignmentSelectConstants.*;
import static zodiac.util.UserMainMenuConstants.MAIN_MENU;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;
import zodiac.util.ActiveUser;

public class    GuiSelectAssignmentMenu extends GuiSubMenu {

    private DefaultTableModel tblmodel;

    /**
     * Creates the JPanel for the GuiSelectAssignmentMenu.
     * @return the complete GuiSelectAssignmentMenu JPanel
     */
    public JPanel setUpMenu()
    {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel assignmentPanel = new JPanel(new FlowLayout());

//        String COL_NAMES[] = {ASS_ID, ASS_NAME, ASS_CLASS, ASS_HIGH_SCORE};
        this.tblmodel = new DefaultTableModel(0, COL_NAMES.length) {
            @Override
            public boolean isCellEditable(int i, int i1)
            {
                return false;
            }
        };

        this.tblmodel.addRow(COL_NAMES);

        // Currently holds debug value
        try
        {
            List<Assignment> enrolled = new AssignmentAction().checkAssignments(DEBUG_COURSE);
            List<Assignment> enrolled_marks = new AssignmentAction().checkAssignments(DEBUG_COURSE,ActiveUser.INSTANCE.getUser().getUtorId());

            for (Assignment a : enrolled) {
                for (Assignment ass : enrolled_marks) {
                    if (ass.getId() == a.getId()) {
                        a.setHighScore(ass.getHighScore());
                    }
                }
                Object row[] = {a.getId(), a.getName(), a.getHighScore()};
                this.tblmodel.addRow(row);
            }
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(new JFrame(), e);
        }


        // Create the table and disable multiple selection and column reordering
        JTable table = new JTable(this.tblmodel);
        table.addMouseListener(new TableRowMouseListener());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane pane = new JScrollPane(table);
        pane.setColumnHeaderView(table.getTableHeader());

        assignmentPanel.add(pane);

        JButton backButton = new JButton(BACK_BUTTON);
        backButton.addActionListener(new BackButtonActionListener());

        panel.add(assignmentPanel, BorderLayout.CENTER);
        panel.add(backButton, BorderLayout.PAGE_END);

        return panel;
    }

    private class TableRowMouseListener extends MouseAdapter
    {
        public void mousePressed(MouseEvent mouseEvent)
        {
            JTable list = (JTable) mouseEvent.getSource();
            if (mouseEvent.getClickCount() == 2) {
                // Double-click detected
                int index = list.getSelectedRow();
                    String name = (String) tblmodel.getValueAt(index, 1);
                    Integer id = (Integer)tblmodel.getValueAt(index, 0);
                    if (name != null && id != null)
                    {
                        Assignment a = new Assignment(name, id);
                        a.setQuestionList(new QuestionAction().getQuestionsWithAnswer(id));
                        // Debug line. Replace fixed ID and Course Name later
                        Student student = new StudentAction().getStudent(ActiveUser.INSTANCE.getUser().getUtorId(),DEBUG_COURSE);
                        AssignmentUi frame = new AssignmentUi(a,student);
                        frame.setVisible(true);
                    }
            }
        }
    }

    private class BackButtonActionListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JButton button = (JButton) actionEvent.getSource();
            if (button != null)
            {
                JPanel parentPanel = (JPanel) button.getParent();
                if (parentPanel != null)
                {
                    JPanel cards = (JPanel) parentPanel.getParent();
                    CardLayout cl = (CardLayout)(cards.getLayout());
                    cl.show(cards, MAIN_MENU);
                }
            }
        }
    }

}
