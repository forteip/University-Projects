package zodiac.gui.admin;


import zodiac.action.QuestionAction;
import zodiac.definition.coursework.Question;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import zodiac.definition.coursework.QuestionTypeConstants;

public class AddQuestionMenu {

    JComboBox typeList;
    JCheckBox checkAutoMark;

    JTextField ansField;
    JTextField textQuestion;
    JButton ansButton;
    JButton button;
    JRadioButton Tbutton;
    JRadioButton Fbutton;
    JPanel panel;
    ArrayList<String> answers;
    ArrayList<Boolean> answersValidity;
    ButtonGroup group;
    public JPanel generateContents() {
        // Create the AddClass panel
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(600,400));

        JLabel questionTypeLabel = new JLabel("Question Type");
        questionTypeLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        questionTypeLabel.setBorder(new EmptyBorder(0,0,10,0));

        typeList = new JComboBox(Question.getTypes().toArray());
        typeList.setSelectedIndex(0);
        typeList.setEditable(false);

        checkAutoMark = new JCheckBox("Auto mark question");
        checkAutoMark.setAlignmentX(JCheckBox.CENTER_ALIGNMENT);
        checkAutoMark.setSelected(true);

        // Generate JLabels for each text field
        JLabel label1 = new JLabel("Enter your Question: ");
        label1.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        label1.setBorder(new EmptyBorder(0, 0, 10, 0));

        // Create the text fields
        textQuestion = new JTextField();
        textQuestion.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        textQuestion.setSize(600,60);
        // true false selection
        Tbutton = new JRadioButton("Valid answer");
        Tbutton.setAlignmentX(JRadioButton.CENTER_ALIGNMENT);
        Tbutton.addActionListener(new AddQuestionListener());
        Fbutton = new JRadioButton("Invalid answer");
        Fbutton.setAlignmentX(JRadioButton.CENTER_ALIGNMENT);
        Fbutton.addActionListener(new AddQuestionListener());
        group = new ButtonGroup();
        group.add(Tbutton);
        group.add(Fbutton);
        // Generate JLabels for each text field
        JLabel label2 = new JLabel("Enter your answer: ");
        label2.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        label2.setBorder(new EmptyBorder(0, 0, 10, 0));
        // answer textfield
        ansField = new JTextField();
        ansField.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        ansField.setSize(600,60);
        answers = new ArrayList<>();
        answersValidity = new ArrayList<>();
        // add answer
        ansButton = new JButton("Add answer");
        ansButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        ansButton.addActionListener(new AddQuestionListener());
        ansButton.setEnabled(false);
        // Create the submit button and link an action listener to it
        button = new JButton("Submit the question");
        button.setAlignmentX(JButton.CENTER_ALIGNMENT);

        button.addActionListener(new AddQuestionListener(textQuestion));

        // Add contents to the panel
        panel.add(questionTypeLabel);
        panel.add(typeList);
        panel.add(checkAutoMark);
        panel.add(label1);
        panel.add(textQuestion);
        panel.add(label2);
        panel.add(Tbutton);
        panel.add(Fbutton);
        panel.add(ansField);
        panel.add(ansButton);
        panel.add(button);

        return panel;
    }

    private class AddQuestionListener implements ActionListener {
        private JTextField question;

        public AddQuestionListener(JTextField question) {
            this.question = question;
        }
        public AddQuestionListener() {

        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            if(actionEvent.getActionCommand() == button.getActionCommand()){
                // add question
                Question q =  new QuestionAction().createQuestion(question.getText(), typeList.getSelectedItem().toString(), checkAutoMark.isSelected());
                if(q!=null){
                    // add answer to that question
                    panel.remove(6);
                    for(int i=0;i<answers.size();i++){
                        new QuestionAction().addAnswer(q.getQid(),answers.get(i),answersValidity.get(i));
                        // remove component from panel
                        panel.remove(6);
                    }
                    // reset this panel
                    answers.clear();
                    answersValidity.clear();
                    ansField.setText("");
                    textQuestion.setText("");
                }
                panel.updateUI();
                panel.revalidate();




            }else if(actionEvent.getActionCommand() == ansButton.getActionCommand()){


                        if(answers.size() == 0){
                            JLabel ansLabel = new JLabel("Your answers -- validity");
                            ansLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                            ansLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
                            panel.add(ansLabel,6);
                        }

                    // add validity
                    Boolean validity = Tbutton.isSelected();
                    answersValidity.add(validity);
                    group.clearSelection();

                    // add text answer
                    String answer = ansField.getText();
                    answers.add(answer);
                    ansField.setText("");
                    // dispay the answer
                    JLabel ansLabel = new JLabel(answers.size()+": "+answer +" -- " +validity);
                    ansLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                   ansLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
                    ansButton.setEnabled(false);
                    panel.add(ansLabel,6+answers.size());
                    panel.updateUI();
                    panel.revalidate();

                }else if (actionEvent.getActionCommand()==Fbutton.getActionCommand() || actionEvent.getActionCommand()==Tbutton.getActionCommand()){
                ansButton.setEnabled(true);
            }



        }
    }
}
