package zodiac.gui.admin;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class AssignmentManagerMenu{
    public static String TITLE_LABEL = "ASSIGNMENT MANAGER";
    public static String GET_ASSIGNMENT = "Get Assignments";
    public static String ADD_ASSIGNMENT = "Add Assignments";
    public static String EDIT_ASSIGNMENT = "Edit Assignments";
    public static String DE_ASSIGNMENT = "Edit Assignments DeadLine&ExtraPoints";
    public static String ADD_QUESTION = "Add Question";
    public static String ADD_QUESTION_TO_ASS = "Add Question to Assignment";

    private JPanel cards;
    private JPanel getAssignmentsPanel;
    private JPanel addQtoAssignmentPanel;
    
    public void generateContents(Container window)
    {
        // Create the combo box JPanel and add all the options into it
        JPanel cbPanel = new JPanel(new FlowLayout());
        String cbOptionList[] = {GET_ASSIGNMENT, ADD_ASSIGNMENT, ADD_QUESTION, ADD_QUESTION_TO_ASS,DE_ASSIGNMENT};
        JComboBox cbOptions = new JComboBox(cbOptionList);
        cbOptions.setEditable(false);
        cbOptions.addItemListener(new OptionsItemListener());
        cbPanel.add(cbOptions);

        // Create JPanel to hold the ComboBox and a title card
        JPanel topPanel = new JPanel(new BorderLayout());

        JLabel msg = new JLabel(TITLE_LABEL);
        msg.setHorizontalAlignment(JLabel.CENTER);
        msg.setVerticalAlignment(JLabel.CENTER);
        msg.setFont(new Font("Bitstream Vera Sans", 0, 48));

        topPanel.add(msg, BorderLayout.NORTH);
        topPanel.add(cbPanel, BorderLayout.PAGE_END);

        // create the various panels for each function
        getAssignmentsPanel = new GetAssignmentsMenu().generateContents();
        addQtoAssignmentPanel = new AddQuestionToAssignmentMenu().generateContents();

        // create the JPanel to hold all the cards and add each card
        cards = new JPanel(new CardLayout());
        cards.add(getAssignmentsPanel, GET_ASSIGNMENT);
        cards.add(new AddAssignmentMenu().generateContents(), ADD_ASSIGNMENT);
        cards.add(new AddQuestionMenu().generateContents(), ADD_QUESTION);
        cards.add(addQtoAssignmentPanel, ADD_QUESTION_TO_ASS);
        JPanel p = new JPanel();
        JButton jButton = new JButton("Edit DeadLine&ExtraPoints");
        jButton.setSize(20, 20);
        jButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new EditAssignmentDeadlineAndExtraPoints();
			}
		});
        p.add(jButton);
        cards.add(p, DE_ASSIGNMENT);
        // Add everything to the pane
        window.add(topPanel, BorderLayout.PAGE_START);
        window.add(cards, BorderLayout.CENTER);
    }

    private class OptionsItemListener implements ItemListener
    {
        @Override
        public void itemStateChanged(ItemEvent itemEvent) {
            CardLayout cl = (CardLayout)(cards.getLayout());
            cl.show(cards, (String)itemEvent.getItem());
        }
    }
}
