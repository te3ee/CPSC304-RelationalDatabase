import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class NewArtistGui extends JFrame {

    NewArtistGui() {
        JFrame newArtistMainFrame = new JFrame("304Project");
        JPanel newArtistMainPanel = new JPanel();
        newArtistMainFrame.setVisible(true);
        newArtistMainFrame.setResizable(false);
        newArtistMainFrame.setSize(1200, 765);
        newArtistMainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        newArtistMainPanel.setLayout(null);

        // Title label
        JLabel newArtistTitle = new JLabel("Artist Registration");
        newArtistTitle.setFont(new Font(Font.SERIF, Font.PLAIN, 40));
        newArtistTitle.setBounds(460, 3, 500, 200);
        newArtistMainPanel.add(newArtistTitle);

        // Warning label
        JLabel warningLabel = new JLabel("Note: Name, Record Label and Genre fields must not be empty and only contain alphabet or numbers");
        warningLabel.setFont(new Font(Font.SERIF, Font.PLAIN, 12));
        warningLabel.setBounds(385, 130, 550, 25);
        newArtistMainPanel.add(warningLabel);


        // Artist input for name
        JTextField nameTextField = new JTextField(25);
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(330, 200, 350, 25);
        nameTextField.setBounds(440, 200, 380, 25);
        newArtistMainPanel.add(nameTextField);
        newArtistMainPanel.add(nameLabel);

        // Artist input for Record Label
        JTextField rlTextField = new JTextField(25);
        JLabel rlLabel = new JLabel("Record Label:");
        rlLabel.setBounds(330, 260, 350, 25);
        rlTextField.setBounds(440, 260, 380, 25);
        newArtistMainPanel.add(rlTextField);
        newArtistMainPanel.add(rlLabel);

        // Artist input for Genre
        JTextField genreTextField = new JTextField(25);
        JLabel genreLabel = new JLabel("Genre:");
        genreLabel.setBounds(330, 320, 350, 25);
        genreTextField.setBounds(440, 320, 380, 25);
        newArtistMainPanel.add(genreTextField);
        newArtistMainPanel.add(genreLabel);

        // Artist input for year
        JTextField yearTextField = new JTextField();
        JLabel yearLabel = new JLabel("Registration Year:");
        yearLabel.setBounds(330, 380, 350, 25);
        yearTextField.setBounds(440, 380, 380, 25);
        newArtistMainPanel.add(yearTextField);
        newArtistMainPanel.add(yearLabel);

        // Title label
        JLabel yearDisclaimer = new JLabel("Only valid integers accepted as a year, must be 4 integers long");
        yearDisclaimer.setFont(new Font(Font.SERIF, Font.PLAIN, 12));
        yearDisclaimer.setBounds(440, 290, 500, 250);
        newArtistMainPanel.add(yearDisclaimer);

        //Button for confirming new artist registration
        JButton newArtistConfirmButton = new JButton("Confirm");
        newArtistConfirmButton.setBounds(305, 480, 600, 120);
        newArtistMainPanel.add(newArtistConfirmButton);
        newArtistConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { // Saves user input for name and convert to string
                String name = null, recordLabel = null, genre = null;
                int year = 0;
                PreparedStatement ps;
                try { // catches error when user does not enter an integer for year
                    name = nameTextField.getText(); // user input for name as string
                    recordLabel = rlTextField.getText(); // user input for record label as string
                    genre = genreTextField.getText(); // user input for genre as string

                    if (name.length() == 0 || recordLabel.length() == 0 || genre.length() == 0) { // Makes sure fields are not empty.
                        throw new InvalidFieldException();
                    }

                    // Only characters, spaces and numbers allowed in fields.
                    if (!(name.matches("[a-z|A-Z|0-9|\\s]*")) || !(recordLabel.matches("[a-z|A-Z|0-9|\\s]*")) || !(genre.matches("[a-z|A-Z|0-9|\\s]*"))) {
                        throw new InvalidFieldException();
                    }

                    year = Integer.parseInt(yearTextField.getText()); // input for year as integer
                    if (yearTextField.getText().length() != 4) { //makes sure length of year is only 4 characters long
                        throw new NumberFormatException();
                    }

                    Statement userCount = Gui.con.createStatement();
                    ResultSet count = userCount.executeQuery("SELECT MAX(userid) FROM music_user");
                    int id = 0;
                    while (count.next()) {
                        id = count.getInt(1);
                    }
                    //System.out.println(id);

                    ps = Gui.con.prepareStatement("INSERT INTO music_user VALUES (?, ?, ?)");
                    ps.setInt(1, id + 1);
                    ps.setString(2, name);
                    ps.setInt(3, year);
                    ps.executeUpdate();
                    Gui.con.commit();

                    ps = Gui.con.prepareStatement("INSERT INTO artist VALUES (?, ?, ?)");
                    ps.setInt(1, id + 1);
                    ps.setString(2, recordLabel);
                    ps.setString(3, genre);
                    ps.executeUpdate();
                    Gui.con.commit();
                    new ArtistGui(); // goes back to artist page
                    new SuccessfulConnectionGui();
                    newArtistMainFrame.dispose();
                } catch (NumberFormatException err) {
                    new IntegerErrorGui();
                } catch (InvalidFieldException e2) {
                    new InvalidFieldGui();
                    e2.printStackTrace();
                }
                catch (Exception sql) {
                    new ErrorGui(sql.getMessage());
                    sql.printStackTrace();
                }
            }
        });

        //Button for return to previous page
        JButton returnButton = new JButton("<-");
        returnButton.setBounds(0, 0, 50, 20);
        newArtistMainPanel.add(returnButton);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ArtistGui(); // calls first frame for Artist section
                newArtistMainFrame.dispose();
            }
        });

        newArtistMainFrame.add(newArtistMainPanel);
    }
}
