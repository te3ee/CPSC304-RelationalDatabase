import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ArtistCreateAlbumGui {
    ArtistCreateAlbumGui(String artistName) { //artistName is the name of current artist

        JFrame artistAlbumScreen = new JFrame("304Project: " + artistName + " Album Creation Screen"); // frame description changes depending on user selected
        JPanel artistAlbumPanel = new JPanel();
        artistAlbumScreen.setVisible(true);
        artistAlbumScreen.setResizable(false);
        artistAlbumScreen.setSize(1200, 765);
        artistAlbumScreen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        artistAlbumPanel.setLayout(null);

        // Frame description
        JLabel artistAlbumTitle = new JLabel("Album Creation");
        artistAlbumTitle.setFont(new Font(Font.SERIF, Font.PLAIN, 40));
        artistAlbumTitle.setBounds(475, 40, 300, 100);
        artistAlbumPanel.add(artistAlbumTitle);

        // Warning label
        JLabel warningLabel = new JLabel("Note: Album Name and Year Created must not be empty. Album Name must only contain alphabets or numbers and Year must be valid (integer of length 4)");
        warningLabel.setFont(new Font(Font.SERIF, Font.PLAIN, 12));
        warningLabel.setBounds(270, 120, 850, 25);
        artistAlbumPanel.add(warningLabel);

        // Artist input for Album Name
        JTextField artistAlbumTextField = new JTextField(25);
        JLabel albumNameLabel = new JLabel("Album Name:");
        albumNameLabel.setBounds(270, 180, 350, 25);
        artistAlbumTextField.setBounds(420, 180, 500, 25);
        artistAlbumPanel.add(artistAlbumTextField);
        artistAlbumPanel.add(albumNameLabel);

        // Artist input for Album year
        JTextField artistAlbumYearTextField = new JTextField(25);
        JLabel albumYearLabel = new JLabel("Year Created:");
        albumYearLabel.setBounds(270, 275, 350, 25);
        artistAlbumYearTextField.setBounds(420, 275, 500, 25);
        artistAlbumPanel.add(artistAlbumYearTextField);
        artistAlbumPanel.add(albumYearLabel);

        //Button to submit
        JButton createButton = new JButton("Create");
        createButton.setBounds(270, 430, 650, 150);
        artistAlbumPanel.add(createButton);
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    String albumName = artistAlbumTextField.getText(); // input for album name
                    Integer albumyear = Integer.parseInt(artistAlbumYearTextField.getText()); // input for year as integer

                    if (artistAlbumYearTextField.getText().length() != 4) { //makes sure length of year is only 4 characters long
                        throw new NumberFormatException();
                    }

                    if (albumName.length() == 0) { // Makes sure album name is not empty.
                        throw new InvalidFieldException();
                    }

                    if (!(albumName.matches("[a-z|A-Z|0-9|\\s]*"))) {
                        throw new InvalidFieldException();
                    }

                    Statement user = Gui.con.createStatement();
                    ResultSet uid = user.executeQuery("SELECT userid FROM music_user m " +
                            "WHERE m.user_name = '" + artistName + "'");
                    int id = 0;
                    while (uid.next()) {
                        id = uid.getInt(1);
                    }

                    Statement albumNumber = Gui.con.createStatement();
                    ResultSet albumID = albumNumber.executeQuery("SELECT MAX(aid) FROM album");
                    int aid = 0;
                    while (albumID.next()) {
                        aid = albumID.getInt(1);
                    }

                    PreparedStatement ps = Gui.con.prepareStatement("INSERT INTO album VALUES (?, ?, ?, ?)");
                    ps.setInt(1, aid + 1);
                    ps.setString(2, albumName);
                    ps.setInt(3, albumyear);
                    ps.setInt(4, id);
                    ps.executeUpdate();
                    Gui.con.commit();
                    new ArtistManagerGui(artistName);
                    new SuccessfulConnectionGui();
                    artistAlbumScreen.dispose();
                } catch (NumberFormatException err) {
                    new IntegerErrorGui();
                } catch (InvalidFieldException e1) {
                    new InvalidFieldGui();
                    e1.printStackTrace();
                } catch (SQLException sql) {
                    new ErrorGui(sql.getMessage());
                    sql.printStackTrace();
                }
            }
        });


        //Button for return to previous page
        JButton returnButton = new JButton("<-");
        returnButton.setBounds(0, 0, 50, 20);
        artistAlbumPanel.add(returnButton);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ArtistManagerGui(artistName); // calls first frame for Artist section
                artistAlbumScreen.dispose();
            }
        });

        artistAlbumScreen.add(artistAlbumPanel);

    }
}
