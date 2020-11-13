package zodiac.gui.user;

import zodiac.definition.security.SecurityConstants;
import zodiac.definition.security.User;
import zodiac.util.ActiveUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static zodiac.util.UserMainMenuConstants.*;

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
        this.cards.add(new UserSelectAssignmentMenu().setUpMenu(), START_ASS);
        this.cards.add(new UserViewMarks().setUpMenu(), VIEW_MARKS);

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

        panel.add(buttonStartAssignment);
        panel.add(buttonViewMarks);
        panel.add(button3);

        return panel;
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
