package zodiac.gui.admin;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.apache.commons.lang3.StringUtils;
import zodiac.action.AssignmentAction;
import zodiac.definition.MessageConstants;
import zodiac.definition.coursework.Assignment;

/**
 * Menu for editing an assignment.
 *
 * @author highzealot
 */
public class EditAssignmentMenu extends javax.swing.JFrame {

  private Assignment assignment;
  private javax.swing.JTextField closeDayField;
  private javax.swing.JTextField closeHourField;
  private javax.swing.JTextField closeMinuteField;
  private javax.swing.JTextField closeMonthField;
  private javax.swing.JLabel closeTimeLabel;
  private javax.swing.JTextField closeYearField;
  private javax.swing.JLabel dayLabel;
  private javax.swing.JLabel hourLabel;
  private javax.swing.JLabel maxLabel;
  private javax.swing.JTextField maxTextField;
  private javax.swing.JLabel minuteLabel;
  private javax.swing.JLabel monthLabel;
  private javax.swing.JLabel nameLabel;
  private javax.swing.JTextField nameTextField;
  private javax.swing.JButton okButton;
  private javax.swing.JTextField openDayField;
  private javax.swing.JTextField openHourField;
  private javax.swing.JTextField openMinuteField;
  private javax.swing.JTextField openMonthField;
  private javax.swing.JLabel openTimeLabel;
  private javax.swing.JTextField openYearField;
  private javax.swing.JButton saveCloseTimeButton;
  private javax.swing.JButton saveMaxButton;
  private javax.swing.JButton saveNameButton;
  private javax.swing.JButton saveOpenTimeButton;
  private javax.swing.JLabel titleLabel;
  private javax.swing.JLabel yearLabel;

  /**
   * Creates new form EditAssignmentMenu.
   */
  public EditAssignmentMenu(Assignment assignment) {
    initComponents();
    this.assignment = assignment;
    nameTextField.setText(assignment.getName());
    maxTextField.setText(assignment.getMaxAttempt() + "");

    if (assignment.getOpenDate() != null) {
      Calendar openDate = Calendar.getInstance();
      openDate.setTime(assignment.getOpenDate());
      openYearField.setText(openDate.get(Calendar.YEAR) + "");
      // Since months start at 0, add 1 so that 1=January,12=December
      openMonthField.setText((openDate.get(Calendar.MONTH) + 1) + "");
      openDayField.setText(openDate.get(Calendar.DAY_OF_MONTH) + "");
      openHourField.setText(openDate.get(Calendar.HOUR_OF_DAY) + "");
      openMinuteField.setText(openDate.get(Calendar.MINUTE) + "");
    } else {
      openYearField.setText(null);
      openMonthField.setText(null);
      openDayField.setText(null);
      openHourField.setText(null);
      openMinuteField.setText(null);
    }

    if (assignment.getCloseDate() != null) {
      Calendar closeDate = Calendar.getInstance();
      closeDate.setTime(assignment.getCloseDate());
      closeYearField.setText(closeDate.get(Calendar.YEAR) + "");
      // Since months start at 0, add 1 so that 1=January,12=December
      closeMonthField.setText((closeDate.get(Calendar.MONTH) + 1) + "");
      closeDayField.setText(closeDate.get(Calendar.DAY_OF_MONTH) + "");
      closeHourField.setText(closeDate.get(Calendar.HOUR_OF_DAY) + "");
      closeMinuteField.setText(closeDate.get(Calendar.MINUTE) + "");
    } else {
      closeYearField.setText(null);
      closeMonthField.setText(null);
      closeDayField.setText(null);
      closeHourField.setText(null);
      closeMinuteField.setText(null);
    }

    saveNameButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        JOptionPane.showMessageDialog(new JFrame(), "Nothing yet");
      }
    });

    saveMaxButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        String input = StringUtils.trimToEmpty(maxTextField.getText());
        try {
          JOptionPane.showMessageDialog(new JFrame(), editMax(Integer.parseInt(input)));
        } catch (NumberFormatException e) {
          JOptionPane.showMessageDialog(new JFrame(), "Invalid input");
        }
      }
    });

    okButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        close();
      }
    });

    saveOpenTimeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Trim inputs
        openYearField.setText(StringUtils.trimToEmpty(openYearField.getText()));
        openMonthField.setText(StringUtils.trimToEmpty(openMonthField.getText()));
        openDayField.setText(StringUtils.trimToEmpty(openDayField.getText()));
        openHourField.setText(StringUtils.trimToEmpty(openHourField.getText()));
        openMinuteField.setText(StringUtils.trimToEmpty(openMinuteField.getText()));
        
        JOptionPane.showMessageDialog(new JFrame(), saveOpenTime());
      }
    });

    saveCloseTimeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Trim inputs
        closeYearField.setText(StringUtils.trimToEmpty(closeYearField.getText()));
        closeMonthField.setText(StringUtils.trimToEmpty(closeMonthField.getText()));
        closeDayField.setText(StringUtils.trimToEmpty(closeDayField.getText()));
        closeHourField.setText(StringUtils.trimToEmpty(closeHourField.getText()));
        closeMinuteField.setText(StringUtils.trimToEmpty(closeMinuteField.getText()));
        
        JOptionPane.showMessageDialog(new JFrame(), saveCloseTime());
      }
    });

  }

  private String editMax(int max) {
    return new AssignmentAction().setAssignmentMaxAttempt(assignment, max);
  }

  private void close() {
    this.dispose();
  }

  private String saveOpenTime() {
    if (new AssignmentAction()
        .validateTime(openYearField.getText(), openMonthField.getText(), openDayField.getText(),
            openHourField.getText(), openMinuteField.getText())) {
      return new AssignmentAction()
          .setAssignmentOpenTime(assignment.getId(), Integer.parseInt(openYearField.getText()),
              Integer.parseInt(openMonthField.getText()), Integer.parseInt(openDayField.getText()),
              Integer.parseInt(openHourField.getText()),
              Integer.parseInt(openMinuteField.getText()));
    } else {
      return MessageConstants.INVALID_INPUT;
    }
  }

  private String saveCloseTime() {
    if (new AssignmentAction()
        .validateTime(closeYearField.getText(), closeMonthField.getText(), closeDayField.getText(),
            closeHourField.getText(), closeMinuteField.getText())) {
      return new AssignmentAction()
          .setAssignmentCloseTime(assignment.getId(), Integer.parseInt(closeYearField.getText()),
              Integer.parseInt(closeMonthField.getText()),
              Integer.parseInt(closeDayField.getText()), Integer.parseInt(closeHourField.getText()),
              Integer.parseInt(closeMinuteField.getText()));
    } else {
      return MessageConstants.INVALID_INPUT;
    }
  }

  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT
   * modify this code. The content of this method is always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">
  private void initComponents() {

    nameTextField = new javax.swing.JTextField();
    maxTextField = new javax.swing.JTextField();
    saveNameButton = new javax.swing.JButton();
    saveMaxButton = new javax.swing.JButton();
    okButton = new javax.swing.JButton();
    titleLabel = new javax.swing.JLabel();
    nameLabel = new javax.swing.JLabel();
    maxLabel = new javax.swing.JLabel();
    openTimeLabel = new javax.swing.JLabel();
    closeTimeLabel = new javax.swing.JLabel();
    openYearField = new javax.swing.JTextField();
    openMonthField = new javax.swing.JTextField();
    openDayField = new javax.swing.JTextField();
    openHourField = new javax.swing.JTextField();
    openMinuteField = new javax.swing.JTextField();
    yearLabel = new javax.swing.JLabel();
    monthLabel = new javax.swing.JLabel();
    dayLabel = new javax.swing.JLabel();
    hourLabel = new javax.swing.JLabel();
    minuteLabel = new javax.swing.JLabel();
    closeYearField = new javax.swing.JTextField();
    closeMonthField = new javax.swing.JTextField();
    closeDayField = new javax.swing.JTextField();
    closeHourField = new javax.swing.JTextField();
    closeMinuteField = new javax.swing.JTextField();
    saveOpenTimeButton = new javax.swing.JButton();
    saveCloseTimeButton = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setBackground(null);

    nameTextField.setText("jTextField1");

    maxTextField.setText("jTextField1");

    saveNameButton.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
    saveNameButton.setText("Save Name");

    saveMaxButton.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
    saveMaxButton.setText("Save Max");

    okButton.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
    okButton.setText("Close");

    titleLabel.setFont(new java.awt.Font("Arial", 0, 48)); // NOI18N
    titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    titleLabel.setText("Edit Assignment");

    nameLabel.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
    nameLabel.setText("Name: ");

    maxLabel.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
    maxLabel.setText("Max Attempts:");

    openTimeLabel.setText("Open Time:");

    closeTimeLabel.setText("Close Time:");

    openYearField.setText("jTextField1");

    openMonthField.setText("jTextField1");

    openDayField.setText("jTextField1");

    openHourField.setText("jTextField1");

    openMinuteField.setText("jTextField1");

    yearLabel.setText("Year");

    monthLabel.setText("Month");

    dayLabel.setText("Day");

    hourLabel.setText("Hour");

    minuteLabel.setText("Minute");

    closeYearField.setText("jTextField1");

    closeMonthField.setText("jTextField1");

    closeDayField.setText("jTextField1");

    closeHourField.setText("jTextField1");

    closeMinuteField.setText("jTextField1");

    saveOpenTimeButton.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
    saveOpenTimeButton.setText("Save Open Time");

    saveCloseTimeButton.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
    saveCloseTimeButton.setText("Save Close Time");

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(
                            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(nameLabel)
                                .addComponent(maxLabel)
                                .addComponent(openTimeLabel)
                                .addComponent(closeTimeLabel)
                                .addComponent(okButton))
                        .addGap(6, 6, 6)
                        .addGroup(
                            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout
                                    .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                        false)
                                    .addComponent(maxTextField,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                                    .addComponent(nameTextField))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(openYearField,
                                        javax.swing.GroupLayout.PREFERRED_SIZE, 59,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(openMonthField,
                                        javax.swing.GroupLayout.PREFERRED_SIZE, 42,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(openDayField,
                                        javax.swing.GroupLayout.PREFERRED_SIZE, 36,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(openHourField,
                                        javax.swing.GroupLayout.PREFERRED_SIZE, 36,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(openMinuteField,
                                        javax.swing.GroupLayout.PREFERRED_SIZE, 36,
                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(12, 12, 12)
                                    .addComponent(yearLabel)
                                    .addGap(18, 18, 18)
                                    .addComponent(monthLabel)
                                    .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(dayLabel)
                                    .addGap(18, 18, 18)
                                    .addComponent(hourLabel)
                                    .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(minuteLabel))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(closeYearField,
                                        javax.swing.GroupLayout.PREFERRED_SIZE, 59,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(closeMonthField,
                                        javax.swing.GroupLayout.PREFERRED_SIZE, 42,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(closeDayField,
                                        javax.swing.GroupLayout.PREFERRED_SIZE, 36,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(closeHourField,
                                        javax.swing.GroupLayout.PREFERRED_SIZE, 36,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(closeMinuteField,
                                        javax.swing.GroupLayout.PREFERRED_SIZE, 36,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(11, 11, 11)
                        .addGroup(
                            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(saveMaxButton, javax.swing.GroupLayout.DEFAULT_SIZE,
                                    javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(saveOpenTimeButton,
                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                    javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(saveCloseTimeButton,
                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                    javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(saveNameButton, javax.swing.GroupLayout.DEFAULT_SIZE,
                                    javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(12, 12, 12)))
                .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 54,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveNameButton)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(maxTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveMaxButton)
                    .addComponent(maxLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(yearLabel)
                    .addComponent(monthLabel)
                    .addComponent(dayLabel)
                    .addComponent(hourLabel)
                    .addComponent(minuteLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(openTimeLabel)
                    .addComponent(openYearField, javax.swing.GroupLayout.PREFERRED_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(openMonthField, javax.swing.GroupLayout.PREFERRED_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(openDayField, javax.swing.GroupLayout.PREFERRED_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(openHourField, javax.swing.GroupLayout.PREFERRED_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(openMinuteField, javax.swing.GroupLayout.PREFERRED_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveOpenTimeButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(closeTimeLabel)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(closeYearField, javax.swing.GroupLayout.PREFERRED_SIZE,
                            javax.swing.GroupLayout.DEFAULT_SIZE,
                            javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(closeMonthField, javax.swing.GroupLayout.PREFERRED_SIZE,
                            javax.swing.GroupLayout.DEFAULT_SIZE,
                            javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(closeDayField, javax.swing.GroupLayout.PREFERRED_SIZE,
                            javax.swing.GroupLayout.DEFAULT_SIZE,
                            javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(closeHourField, javax.swing.GroupLayout.PREFERRED_SIZE,
                            javax.swing.GroupLayout.DEFAULT_SIZE,
                            javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(closeMinuteField, javax.swing.GroupLayout.PREFERRED_SIZE,
                            javax.swing.GroupLayout.DEFAULT_SIZE,
                            javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(saveCloseTimeButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(okButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    pack();
  } // </editor-fold>

}
