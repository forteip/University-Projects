package zodiac.gui.user;

import zodiac.action.AssignmentAction;
import zodiac.definition.coursework.Assignment;

import javax.swing.*;

import static zodiac.util.UserMainMenuConstants.MAIN_MENU;
import static zodiac.util.UserViewMarksConstants.*;

import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a window for Students to view their marks
 * in various courses.
 */
public class UserViewMarks extends UserSubMenu {
    private UneditableTableModel tblmdl;
    private JPanel contentsPanel;
    private JTable table;

    /**
     * Sets up the UserViewMarks menu.
     * @return the completed JPanel menu
     */
    public JPanel setUpMenu()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        this.contentsPanel = new JPanel(new BorderLayout());

        this.tblmdl = new UneditableTableModel(0, USER_VIEW_MARKS_COLUMNS.length);

        // Create the panel responsible for typing in searches
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        // Adds various components of the Search Panel
        JPanel searchBar = new JPanel(new FlowLayout());
        searchBar.add(new JLabel(USER_VIEW_MARKS_ASS_SEARCH_TEXT));
        JTextField text = new JTextField();
        text.setPreferredSize(new Dimension(100, 50));
        searchBar.add(text);
        JButton searchButton = new JButton(USER_VIEW_MARKS_SEARCH);
        searchButton.addActionListener(new SearchActionListener(text));
        searchBar.add(searchButton);
        JButton backButton = new JButton(USER_VIEW_MARKS_BACK);
        backButton.addActionListener(new BackButtonActionListener());

        searchPanel.add(searchBar);
        searchPanel.add(backButton);
        this.contentsPanel.add(searchPanel, BorderLayout.EAST);
        panel.add(this.contentsPanel);

        return panel;
    }

    private class UneditableTableModel extends DefaultTableModel
    {
        public UneditableTableModel(int i, int i1) {
            super(i, i1);
        }

        @Override
        public boolean isCellEditable(int i, int i1)
        {
            return false;
        }
    }

    private class SearchActionListener implements ActionListener
    {
        private JTextField textField;

        public SearchActionListener(JTextField textField) {
            this.textField = textField;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String code = this.textField.getText();
            try
            {
                List<Assignment> res = new AssignmentAction().checkAssignments(code);
//            List<Assignment> res = new ArrayList<>();
                for (Assignment a : res)
                {
                    Object row[] = {a.getId(), a.getName(), a.getHighScore()};
                    tblmdl.addRow(row);
                }
                table = new JTable(tblmdl);
                table.setAlignmentX(0.f);
                contentsPanel.add(table, BorderLayout.CENTER);
                contentsPanel.revalidate();
            }
            catch (SQLException e)
            {
                JOptionPane.showMessageDialog(new JFrame(), e);
            }


        }
    }

    /**
     * Listener for the back to Main menu button.
     * NOTE: Is not very intuitive in terms of function. Need to
     * find more abstract way since this may be reused.
     */
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
                    JPanel parent1 = (JPanel) parentPanel.getParent();
                    JPanel cards = (JPanel) parent1.getParent();
                    cards = (JPanel) cards.getParent();
                    CardLayout cl = (CardLayout)(cards.getLayout());
                    cl.show(cards, MAIN_MENU);
                }
            }
        }
    }
}
