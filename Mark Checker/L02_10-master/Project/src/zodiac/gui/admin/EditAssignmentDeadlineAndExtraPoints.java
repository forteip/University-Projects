package zodiac.gui.admin;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import zodiac.dao.coursework.AssignmentDao;
import zodiac.definition.coursework.Assignment;

public class EditAssignmentDeadlineAndExtraPoints extends JFrame {
	/**
	 * 
	 */
	public static Integer count = 0;
	private static final long serialVersionUID = 1L;
	private Assignment assignment;
	private JTextField txDeadline;
	private JTextField txAssignmentId;
	private JTextField txExtraPoints;
	public EditAssignmentDeadlineAndExtraPoints(){
		this.setTitle("Edit Deadline and ExtraPoints");
		txDeadline = new JTextField();
		txAssignmentId = new JTextField();
		txExtraPoints = new JTextField();
		txDeadline.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		txDeadline.setMaximumSize(new Dimension(600, 70));
		txExtraPoints.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		txExtraPoints.setMaximumSize(new Dimension(600, 70));
		txAssignmentId.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		txAssignmentId.setMaximumSize(new Dimension(600, 70));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Generate JLabels for each text field
        JLabel label0 = new JLabel("Enter Assignment id: ");
        label0.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        label0.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JLabel label1 = new JLabel("Enter Assignment deadline: ");
        label1.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        label1.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel label2 = new JLabel("Enter Assignment Extra points: ");
        label2.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        label2.setBorder(new EmptyBorder(20, 0, 10, 0));
        JButton button2 = new JButton("search");
        button2.setAlignmentX(JButton.CENTER_ALIGNMENT);
        button2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = txAssignmentId.getText();
				if(id.equals("")||id == null) {
					JOptionPane.showMessageDialog(null, 
							"Please input assignment id","Warning",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				try {
					int i = Integer.parseInt(id);
					List<Assignment> list = new AssignmentDao().getAssignments(i);
					
					if(list.size() != 0) {
						assignment = list.get(0);
						txDeadline.setText(assignment.getEarlySubmissionDeadline()!=null?format.format(assignment.getEarlySubmissionDeadline()):"");
						txExtraPoints.setText(assignment.getExtraPoints()!=null?assignment.getExtraPoints().toString():"");
					}else{
						JOptionPane.showMessageDialog(null, 
								"assignment isn't exist","Warning",
								JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(null, 
							"Please input a number","Warning",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
        JButton button = new JButton("Submit");
        button.setAlignmentX(JButton.CENTER_ALIGNMENT);
        button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(assignment == null) {
					JOptionPane.showMessageDialog(null,
							"please search a assignment", "Warning",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				String date = txDeadline.getText();
				String point = txExtraPoints.getText();
				Date d = null;
				try {
					d = format.parse(date);
					int p = Integer.parseInt(point);
					if(p<0){
						JOptionPane.showMessageDialog(null,
								"deadline and extra points willn't be added,please input a extra points > 0", "Warning",
								JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					new AssignmentDao().editDeadlineAndExtraPoint(assignment.getId(), d, p);
					JOptionPane.showMessageDialog(null,
							"Edit Success", "Tip",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception a) {
					a.printStackTrace();
					JOptionPane.showMessageDialog(null, 
							"deadline and extra points willn't be added ,(e.g. 2017-11-25 12:00:00)","Warning",
							JOptionPane.INFORMATION_MESSAGE);
				}
				
			}
		});
        
        panel.add(label0);
        panel.add(txAssignmentId);
        panel.add(button2);
        panel.add(label1);
        panel.add(txDeadline);
        panel.add(label2);
        panel.add(txExtraPoints);
        panel.add(button);
        this.add(panel);
        this.setSize(700, 500);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new EditAssignmentDeadlineAndExtraPoints();
	}
}
