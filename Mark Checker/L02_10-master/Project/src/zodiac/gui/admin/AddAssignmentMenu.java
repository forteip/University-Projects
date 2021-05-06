package zodiac.gui.admin;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import zodiac.dao.coursework.AssignmentDao;

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

        JLabel label3 = new JLabel("Enter  early submission deadline: ");
        label3.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        label3.setBorder(new EmptyBorder(40, 0, 10, 0));
        
        JLabel label4 = new JLabel("Enter extra points: ");
        label4.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        label4.setBorder(new EmptyBorder(60, 0, 10, 0));
        
        // Create the text fields
        JTextField textAssName = new JTextField();
        textAssName.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        textAssName.setMaximumSize(new Dimension(600, 70));

        JTextField textCourseCode = new JTextField();
        textCourseCode.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        textCourseCode.setMaximumSize(new Dimension(600, 70));
        
        JTextField textDeadline = new JTextField("2017-11-25 12:00:00");
        textDeadline.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        textDeadline.setMaximumSize(new Dimension(600, 70));
        
        JTextField textExtrapoints = new JTextField();
        textExtrapoints.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        textExtrapoints.setMaximumSize(new Dimension(600, 70));
        textExtrapoints.setText("0");
        // Create the submit button and link an action listener to it
        JButton button = new JButton("Submit");
        button.setAlignmentX(JButton.CENTER_ALIGNMENT);

        button.addActionListener(new AddAssignmentListener(textAssName, textCourseCode,textDeadline,textExtrapoints));

        // Add contents to the panel
        panel.add(label1);
        panel.add(textAssName);
        panel.add(label2);
        panel.add(textCourseCode);
        panel.add(label3);
        panel.add(textDeadline);
        panel.add(label4);
        panel.add(textExtrapoints);
        panel.add(button);

        return panel;
    }

    private class AddAssignmentListener implements ActionListener
    {
        private JTextField assName;
        private JTextField courseCode;
        private JTextField textDeadline;
        private JTextField textExtrapoints;

        public AddAssignmentListener(JTextField assName, JTextField courseCode,JTextField textDeadline,JTextField textExtrapoints) {
            this.assName = assName;
            this.courseCode = courseCode;
            this.textDeadline = textDeadline;
            this.textExtrapoints = textExtrapoints;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
			AssignmentDao dao = new AssignmentDao();
			int id = dao.addAssignment(assName.getText(), courseCode.getText());
			String date = textDeadline.getText();
			String point = textExtrapoints.getText();
			Date d = null;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				d = format.parse(date);
				int p = Integer.parseInt(point);
				if(p<0){
					JOptionPane.showMessageDialog(null,
							"deadline and extra points willn't be added,please input a extra points > 0", "Warning",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				dao.editDeadlineAndExtraPoint(id, d, p);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, 
						"deadline and extra points willn't be added ,(e.g. 2017-11-25 12:00:00)","Warning",
						JOptionPane.INFORMATION_MESSAGE);
			}
		 }
    }
}
