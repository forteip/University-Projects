package zodiac.gui.user;

import zodiac.action.security.UserAction;
import zodiac.gui.login.login;

import static zodiac.util.UserMainMenuConstants.LOGOFF;
import static zodiac.util.UserMainMenuConstants.MAIN_MENU;
import static zodiac.util.UserMainMenuConstants.START_ASS;
import static zodiac.util.UserMainMenuConstants.VIEW_MARKS;
import static zodiac.util.UserMainMenuConstants.WELCOME_BANNER;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class UserMainMenu {


    private JPanel cards;

    public void setupMainMenu(JFrame frame)
    {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel banner = new JLabel(WELCOME_BANNER, SwingConstants.CENTER);

        banner.setFont(new Font("Bitstream Vera Sans", 0, 48));
        panel.add(banner, BorderLayout.PAGE_START);

        this.cards = new JPanel(new CardLayout());
        this.cards.add(this.generateMainMenu(), MAIN_MENU);
        this.cards.add(new GuiSelectAssignmentMenu().setUpMenu(), START_ASS);
        this.cards.add(new GuiViewMarks().setUpMenu(), VIEW_MARKS);

        panel.add(this.cards);

        frame.add(panel);
    }

    private JPanel generateMainMenu()
    {
        // initiate panel and panel layout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));


        JButton buttonStartAssignment = new JButton(START_ASS);
        buttonStartAssignment.addActionListener(new SwapMenuListener(START_ASS));

        JButton buttonViewMarks = new JButton(VIEW_MARKS);
        buttonViewMarks.addActionListener(new SwapMenuListener(VIEW_MARKS));

        JButton button3 = new JButton(LOGOFF);
        button3.addActionListener(new LogOffActionListener(panel));

        panel.add(buttonStartAssignment);
        panel.add(buttonViewMarks);
        panel.add(button3);

        return panel;
    }

    private class LogOffActionListener implements ActionListener
    {
        private JPanel panel;

        public LogOffActionListener(JPanel panel) {
            this.panel = panel;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            new UserAction().logoff();
            JFrame oldFrame = (JFrame) SwingUtilities.getWindowAncestor(this.panel);

            JFrame frame = new JFrame("login");
            frame.setContentPane(new login().getMainPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
            frame.setTitle("Login");
            frame.setSize(800, 400);

            oldFrame.setVisible(false);

        }
    }

    private class SwapMenuListener implements ActionListener
    {
        private String toMenu;

        public SwapMenuListener(String toMenu) {
            this.toMenu = toMenu;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            CardLayout cl = (CardLayout)(cards.getLayout());
            cl.show(cards, this.toMenu);
        }
    }

    /**
     * main function for UserMainMenu. Run this to launch the
     * standalone User version of the app.
     * @param args
     */
    public static void main(String args[])
    {
        JFrame frame = new JFrame("App");
        //Create and set up the content pane.

        UserMainMenu menu = new UserMainMenu();
        menu.setupMainMenu(frame);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);

        frame.setVisible(true);

    }
}
