import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;


public class ArtistAudioInfo {
    ArtistAudioInfo(String artistName, String selectedAudio) {

        JFrame artistAudioScreen = new JFrame("304Project: " + selectedAudio + "'s information page"); // frame description changes depending on user selected
        JPanel artistAudioPanel = new JPanel();
        artistAudioScreen.setVisible(true);
        artistAudioScreen.setResizable(false);
        artistAudioScreen.setSize(1200, 765);
        artistAudioScreen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        artistAudioPanel.setLayout(null);

        Double avgRating = 0.0;
        Integer maxRating = 0;
        Integer minRating = 0;
        Integer countRatings = 0;
        ArrayList<Integer> ratings = new ArrayList<>();
        try {
            Statement maxFind = Gui.con.createStatement();
            ResultSet max = maxFind.executeQuery("SELECT MAX(rating) FROM rates " +
                    "WHERE sid IN " +
                    "(SELECT sid FROM audio " +
                    "WHERE audio_name = '"+ selectedAudio +"')");
            while (max.next()) {
                maxRating = max.getInt(1);
            }

            Statement minFind = Gui.con.createStatement();
            ResultSet min = minFind.executeQuery("SELECT MIN(rating) FROM rates " +
                    "WHERE sid IN " +
                    "(SELECT sid FROM audio " +
                    "WHERE audio_name = '"+ selectedAudio +"')");
            while (min.next()) {
                minRating = (min.getInt(1));
            }

            Statement countFind = Gui.con.createStatement();
            ResultSet count = countFind.executeQuery("SELECT COUNT(rating) FROM rates " +
                    "WHERE sid IN " +
                    "(SELECT sid FROM audio " +
                    "WHERE audio_name = '"+ selectedAudio +"')");
            while (count.next()) {
                countRatings = (count.getInt(1));
            }

            Statement rateFind = Gui.con.createStatement();
            ResultSet rates = rateFind.executeQuery("SELECT rating FROM rates " +
                    "WHERE sid IN " +
                    "(SELECT sid FROM audio " +
                    "WHERE audio_name = '"+ selectedAudio +"')");
            while (rates.next()) {
                ratings.add(rates.getInt(1));
            }

            int sum = 0;

            for (int i : ratings) {
                sum += i;
            }
            if (ratings.size() == 0) {
                avgRating = 0.0;
            }
            else {
                avgRating = ((double) sum) / (ratings.size());
            }

        } catch (Exception e) {
            new ErrorGui(e.getMessage());
            e.printStackTrace();
        }

        //get avg rating of selectedAudio using artistName and set rating as avgRating. (done)
        if (avgRating == 0.0) {
            JLabel avgRatingLabel = new JLabel(selectedAudio +"'s Average Rating is: No ratings found");
            avgRatingLabel.setBounds(80, 100, 1100, 50);
            artistAudioPanel.add(avgRatingLabel);
        }
        else {
            DecimalFormat df = new DecimalFormat("#.##");
            JLabel avgRatingLabel = new JLabel(selectedAudio +"'s Average Rating is: " + df.format(avgRating));
            avgRatingLabel.setBounds(80, 100, 1100, 50);
            artistAudioPanel.add(avgRatingLabel);
        }

        //get maxRating of selected audio using artistName and set rating as maxRating. (done)
        if (maxRating == 0) {
            JLabel maxRatingLabel = new JLabel(selectedAudio + "'s Max Rating is: No ratings found");
            maxRatingLabel.setBounds(80, 150, 1100, 50);
            artistAudioPanel.add(maxRatingLabel);
        }
        else {
            JLabel maxRatingLabel = new JLabel(selectedAudio + "'s Max Rating is: " + maxRating);
            maxRatingLabel.setBounds(80, 150, 1100, 50);
            artistAudioPanel.add(maxRatingLabel);
        }

        if (minRating == 0) {
            JLabel minRatingLabel = new JLabel(selectedAudio + "'s Minimum Rating is: No ratings found");
            minRatingLabel.setBounds(80, 200, 1100, 50);
            artistAudioPanel.add(minRatingLabel);
        }
        else {
            JLabel minRatingLabel = new JLabel(selectedAudio + "'s Minimum Rating is: " + minRating);
            minRatingLabel.setBounds(80, 200, 1100, 50);
            artistAudioPanel.add(minRatingLabel);
        }

        JLabel countLabel = new JLabel(selectedAudio + "'s number of Ratings is: " + countRatings);
        countLabel.setBounds(80, 250, 1100, 50);
        artistAudioPanel.add(countLabel);

        // get Album name of selectedAudio. If no album for selected audio display change msg as appropriate (done)
        String albumName = "No album found";
        try {
            Statement albumFind = Gui.con.createStatement();
            ResultSet album = albumFind.executeQuery("SELECT album_name FROM album " +
                    "WHERE aid IN " +
                    "(SELECT aid FROM audio " +
                    "WHERE audio_name = '"+ selectedAudio +"')");
            while (album.next()) {
                albumName = album.getString(1);
            }

        } catch (Exception e) {
            new ErrorGui(e.getMessage());
            e.printStackTrace();
        }

        //String albumName = "test album";
        String msg;
        msg = selectedAudio + " is in Album: " + albumName;
        JLabel albumLabel = new JLabel(msg);
        albumLabel.setBounds(80,300,1100,100);
        artistAudioPanel.add(albumLabel);

        JButton returnButton = new JButton("<-");
        returnButton.setBounds(0, 0, 50, 20);
        artistAudioPanel.add(returnButton);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ArtistManagerGui(artistName); // calls first frame for Artist section
                artistAudioScreen.dispose();
            }
        });

        artistAudioScreen.add(artistAudioPanel);



    }
}

