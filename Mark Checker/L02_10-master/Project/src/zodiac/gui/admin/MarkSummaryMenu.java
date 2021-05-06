package zodiac.gui.admin;

import zodiac.action.ClassAction;
import zodiac.gui.GuiSubMenu;
import static zodiac.util.MarkSummaryConstants.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;

/**
 * A class that represents the MarkSummaryMenu, a menu that allows
 * Professors to see the marks of various classes and their average.
 */
public class MarkSummaryMenu extends GuiSubMenu{
    private DefaultTableModel tblmdl;
    private JPanel panel;
    private JScrollPane table;

    @Override
    public JPanel setUpMenu() {
        this.panel = new JPanel();
        this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));

        // Create elements for search bar
        JPanel searchBar = new JPanel(new FlowLayout());
        JTextField textField = new JTextField();
        textField.setMinimumSize(new Dimension(100, 25));
        textField.setPreferredSize(new Dimension(100, 25));
        textField.setMaximumSize(new Dimension(100, 25));
        JButton button = new JButton(SEARCH_BUTTON_TEXT);
        // Create label that will display class average
        JLabel label = new JLabel(MARKS_AVERAGE_TEXT);
        button.addActionListener(new GetMarksActionListener(textField, label));

        // add elements to search bar
        searchBar.add(new JLabel(SEARCH_BAR_TEXT));
        searchBar.add(textField);
        searchBar.add(button);

        this.tblmdl = new UneditableTableModel(0, MARKS_TABLE_COLUMNS.length);
        // Add all panels and elements to the main panel
        this.panel.add(searchBar);
        this.panel.add(label);

        return this.panel;
    }

    /**
     * The class for the Action Listener that is called when
     * the user wants to get the marks of a course
     */
    private class GetMarksActionListener implements ActionListener
    {
        JTextField textField;
        JLabel averageLabel;

        /**
         * Constructor for the GetMarksActionListener
         * @param textField the JTextField where the course code is given
         * @param averageLabel the JLabel that displays the class average
         */
        public GetMarksActionListener(JTextField textField, JLabel averageLabel) {
            this.textField = textField;
            this.averageLabel = averageLabel;
        }

        /**
         * Method to call when the connected button is called
         * @param actionEvent
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            tblmdl = new UneditableTableModel(0, MARKS_TABLE_COLUMNS.length);
            List<String[]> rows = new ClassAction().getMarksInClass(this.textField.getText());
            for (String[] row : rows)
            {
                tblmdl.addRow(row);
            }

            if (table != null && table.isVisible())
            {
                panel.remove(table);
            }
            JTable myTable = new JTable(tblmdl);
            table = new JScrollPane(myTable);
            panel.add(table);
            averageLabel.setText(MARKS_AVERAGE_TEXT + getMarksAverage().toString());
            panel.revalidate();
        }

        /**
         * Gets the average of all the marks in the table model tblmdl
         * @return the average of the marks in tblmdl
         */
        private Integer getMarksAverage()
        {
            if (tblmdl.getRowCount() == 0) {return 0;}

            int res = 0;
            for (int i = 0; i < tblmdl.getRowCount(); i++)
            {
                res += Integer.valueOf((String)tblmdl.getValueAt(i, tblmdl.getColumnCount() - 1));
            }
            return res / tblmdl.getRowCount();

        }
    }

    /**
     * A class that represents an uneditable table
     */
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
}
