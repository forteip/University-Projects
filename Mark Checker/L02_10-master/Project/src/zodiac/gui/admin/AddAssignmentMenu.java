package zodiac.gui.admin;

import zodiac.dao.coursework.AssignmentDao;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddAssignmentMenu {
    public JPanel generateContents()
    {
        // Create the AddClass panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Generate JLabels for each text field
        JLabel label1 = new JLabel("Enter Assignment name: ");
        label1.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        label1.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel label2 = new JLabel("Enter a Course Code: ");
        label2.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        label2.setBorder(new EmptyBorder(20, 0, 10, 0));

        // Create the text fields
        JTextField textAssName = new JTextField();
        textAssName.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        textAssName.setMaximumSize(new Dimension(600, 70));

        JTextField textCourseCode = new JTextField();
        textCourseCode.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        textCourseCode.setMaximumSize(new Dimension(600, 70));

        // Create the submit button and link an action listener to it
        JButton button = new JButton("Submit");
        button.setAlignmentX(JButton.CENTER_ALIGNMENT);

        button.addActionListener(new AddAssignmentListener(textAssName, textCourseCode));

        // Add contents to the panel
        panel.add(label1);
        panel.add(textAssName);
        panel.add(label2);
        panel.add(textCourseCode);
        panel.add(button);

        return panel;
    }

    private class AddAssignmentListener implements ActionListener
    {
        private JTextField assName;
        private JTextField courseCode;

        public AddAssignmentListener(JTextField assName, JTextField courseCode) {
            this.assName = assName;
            this.courseCode = courseCode;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            new AssignmentDao().addAssignment(assName.getText(), courseCode.getText());
        }
    }
}
