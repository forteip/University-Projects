package zodiac.gui.login;

import java.awt.Dialog.ModalityType;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import zodiac.action.security.UserAction;
import zodiac.dao.security.UserDao;


public class register {

  private JTextField usernameField;
  private JPanel panel1;
  private JPasswordField passwordField1;
  private JButton registerButton;
  private String username;
  private String password;
  private JDialog result;

  public register() {
    registerButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        username = usernameField.getText();
        char[] workingPassword = passwordField1.getPassword();
        password = new String(workingPassword);
        UserAction potentialUser = new UserAction();
        result = new JDialog();
        result.setSize(new Dimension(400, 400));
        JLabel prompt = new JLabel(potentialUser.register(username, password));
        result.add(prompt);
        result.setModalityType(ModalityType.APPLICATION_MODAL);
        result.setVisible(true);

      }
    });
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame("register");
    frame.setContentPane(new register().panel1);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
    frame.setTitle("Registration");
    frame.setSize(800, 400);
  }

  {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
    $$$setupUI$$$();
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR
   * call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    panel1 = new JPanel();
    panel1.setLayout(
        new com.intellij.uiDesigner.core.GridLayoutManager(7, 5, new Insets(0, 0, 0, 0), -1, -1));
    usernameField = new JTextField();
    usernameField.setText("Username");
    panel1.add(usernameField, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 3, 3,
        com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
        com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
        com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW,
        com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1),
        null, 0, false));
    passwordField1 = new JPasswordField();
    passwordField1.setText("Password");
    panel1.add(passwordField1, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 3, 3,
        com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
        com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
        com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW,
        com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1),
        null, 0, false));
    registerButton = new JButton();
    registerButton.setText("Register");
    panel1.add(registerButton, new com.intellij.uiDesigner.core.GridConstraints(6, 2, 1, 1,
        com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
        com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
        com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK
            | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
        com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
    panel1.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(6, 3, 1, 1,
        com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
        com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
        com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0,
        false));
    final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
    panel1.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1,
        com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
        com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
        com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0,
        false));
    final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
    panel1.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(3, 4, 3, 1,
        com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
        com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
        com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0,
        false));
    final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
    panel1.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 3, 1,
        com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
        com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
        com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0,
        false));
    final JLabel label1 = new JLabel();
    label1.setText("Username");
    panel1.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1,
        com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
        com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
        com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
        com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label2 = new JLabel();
    label2.setText("Password");
    panel1.add(label2, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1,
        com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
        com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
        com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
        com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return panel1;
  }
}
