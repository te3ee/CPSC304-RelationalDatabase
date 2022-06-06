import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class Gui extends JFrame {
    public static Connection con;

    public static void main(String[] args) {
        new Gui();
    }

    public Gui() {
        connect("ora_t3a1b", "a20953162");
        JFrame titleScreen = new JFrame("304Project"); // Frame for main page
        JPanel titlePanel = new JPanel(); // Create new panel for buttons on main Screen
        titleScreen.setVisible(true);
        titleScreen.setResizable(false); // Restrict frame size to keep buttons layout consistent
        titleScreen.setSize(1200, 765); // Size of frame
        titleScreen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        titlePanel.setLayout(null);

        // App title
        JLabel appTitle = new JLabel( "Apollo 18");
        appTitle.setFont(new Font(Font.SERIF, Font.PLAIN, 40));
        appTitle.setBounds(515, 2, 200, 125);
        titlePanel.add(appTitle);

        //Button for Artist
        JButton artistButton = new JButton("Artist");
        artistButton.setBounds(225, 150, 750, 200); // Size and position of button
        titlePanel.add(artistButton);
        artistButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ArtistGui(); // calls first frame for Artist section
                titleScreen.dispose();
            }
        });

        //Button for Listeners
        JButton listenerButton = new JButton("Listener");
        listenerButton.setBounds(225, 450, 750, 200); // Size and position of button
        titlePanel.add(listenerButton);
        listenerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ListenerHomePage(); // calls first frame for Artist section
                titleScreen.dispose();
            }
        });

        titleScreen.add(titlePanel);
    }

    private boolean connect(String username, String password)
    {
        String connectURL = "jdbc:oracle:thin:@localhost:1522:ug";

        try
        {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            con = DriverManager.getConnection(connectURL,username,password);


            System.out.println("\nConnected to Oracle!");

            return true;
        }
        catch (SQLException ex)
        {
            new ErrorGui(ex.getMessage());
            System.out.println("Message: " + ex.getMessage());
            return false;
        }
    }

}
