

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ArtistGui extends JFrame {

    ArtistGui() {
        JFrame artistMainScreen = new JFrame("304Project");
        JPanel artistMainPanel = new JPanel();
        artistMainScreen.setVisible(true);
        artistMainScreen.setResizable(false);
        artistMainScreen.setSize(1200, 765);
        artistMainScreen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        artistMainPanel.setLayout(null);


        // Frame description
        JLabel artistTitle = new JLabel("Artist Homepage");
        artistTitle.setFont(new Font(Font.SERIF, Font.PLAIN, 40));
        artistTitle.setBounds(460, 3, 500, 250);
        artistMainScreen.add(artistTitle);

        ArrayList<String> artists = new ArrayList<>();
        // list of current artists
        try {
            Statement users = Gui.con.createStatement();
            ResultSet userList = users.executeQuery("SELECT user_name FROM music_user m, artist a " +
                    "WHERE m.userid = a.userid");
            while (userList.next()) {
                artists.add(userList.getString(1));
            }
            for (String s : artists) {
                //System.out.println(s);
            }

        } catch (Exception e) {
            new ErrorGui(e.getMessage());
            e.printStackTrace();
        }

        // Existing artist
        String[] artistArray = new String[artists.size()];
        artists.toArray(artistArray);
        //System.out.println("-----------");
        for (String s : artistArray) {
            //System.out.println(s);
        }
        JComboBox<String> artistsCB = new JComboBox<String>(artistArray);
        JLabel artistUsername = new JLabel("Select existing artist: ");
        artistUsername.setFont(new Font(Font.SERIF, Font.PLAIN, 25));
        artistsCB.setBounds(490,188,435,50);
        artistUsername.setBounds(270,65,600,300);
        artistsCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedArtist;
                selectedArtist = artistsCB.getSelectedItem().toString();
                new ArtistManagerGui(selectedArtist);
                artistMainScreen.dispose();
            }
        });
        artistMainPanel.add(artistUsername);
        artistMainPanel.add(artistsCB);


        //Button for creating new artist
        JButton newArtistButton = new JButton("New Artist Registration");
        newArtistButton.setBounds(270, 440, 650, 150);
        artistMainPanel.add(newArtistButton);
        newArtistButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new NewArtistGui(); // calls first frame for Artist section
                artistMainScreen.dispose();
            }
        });

        //Sort artist by rating
        String[] artistRating = {"1","2","3","4","5"};
        JComboBox<String> aRatings = new JComboBox<String>(artistRating);
        JLabel arLabel = new JLabel("Sort artist by rating: ");
        arLabel.setFont(new Font(Font.SERIF, Font.PLAIN, 25));
        aRatings.setBounds(490,300,435,50);
        arLabel.setBounds(270,175,600,300);
        aRatings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer selectedArtistRating;
                selectedArtistRating = Integer.parseInt(aRatings.getSelectedItem().toString());
                new ArtistRatingGui(selectedArtistRating);
                artistMainScreen.dispose();
            }
        });
        artistMainPanel.add(arLabel);
        artistMainPanel.add(aRatings);

        //Button for return to previous page
        JButton returnButton = new JButton("<-");
        returnButton.setBounds(0, 0, 50, 20);
        artistMainPanel.add(returnButton);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Gui(); // calls first frame for Artist section
                artistMainScreen.dispose();
            }
        });

        artistMainScreen.add(artistMainPanel);
    }
}

