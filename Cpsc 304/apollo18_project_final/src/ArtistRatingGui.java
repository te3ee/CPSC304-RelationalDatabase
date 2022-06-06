import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ArtistRatingGui {
    ArtistRatingGui(Integer artistRating){
        JFrame artistRatingScreen = new JFrame("304Project");
        JPanel artistRatingPanel = new JPanel();
        artistRatingScreen.setVisible(true);
        artistRatingScreen.setResizable(false);
        artistRatingScreen.setSize(1200, 765);
        artistRatingScreen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        artistRatingPanel.setLayout(null);

        // Frame description
        JLabel artistTitle = new JLabel("Artists with rating: " + artistRating);
        artistTitle.setFont(new Font(Font.SERIF, Font.PLAIN, 40));
        artistTitle.setBounds(440, 3, 500, 250);
        artistRatingScreen.add(artistTitle);

        // find all artists with rating artistRating and insert into artistWithRating
        ArrayList<String> artistRatings = new ArrayList<>();
        try {
            ArrayList<String> artists = new ArrayList<>();
            Statement users = Gui.con.createStatement();
            ResultSet userList = users.executeQuery("SELECT user_name FROM music_user m, artist a " +
                    "WHERE m.userid = a.userid");
            while (userList.next()) {
                artists.add(userList.getString(1));
            }

            for (String s: artists) {
                Statement user = Gui.con.createStatement();
                ResultSet uid = user.executeQuery("SELECT userid FROM music_user m " +
                        "WHERE m.user_name = '" + s + "'");
                int id = 0;
                while (uid.next()) {
                    id = uid.getInt(1);
                }

                ArrayList<String> songs = new ArrayList<>();
                Statement songFind = Gui.con.createStatement();
                ResultSet songList = songFind.executeQuery("SELECT audio_name FROM audio " +
                        "WHERE sid IN " +
                        "(SELECT sid FROM creates " +
                        "WHERE userid = '"+ id +"')");
                while (songList.next()) {
                    songs.add(songList.getString(1));
                }
                int avgArtistRating = 0;
                int sumSongRatings = 0;
                ArrayList<Integer> artistSongRatings = new ArrayList<>();

                for (String s2 : songs) {
                    Integer avgRating = 0;
                    ArrayList<Integer> ratings = new ArrayList<>();
                    Statement rateFind = Gui.con.createStatement();
                    ResultSet rates = rateFind.executeQuery("SELECT rating FROM rates " +
                            "WHERE sid IN " +
                            "(SELECT sid FROM audio " +
                            "WHERE audio_name = '"+ s2 +"')");
                    while (rates.next()) {
                        ratings.add(rates.getInt(1));
                    }
                    Integer sum = 0;
                    Integer size = ratings.size();

                    for (int i : ratings) {
                        sum += i;
                    }
                    if (size != 0) {
                        avgRating = sum/size;
                        artistSongRatings.add(avgRating);
                    }
                }
                for (int j : artistSongRatings) {
                    sumSongRatings += j;
                }
                if (artistSongRatings.size() == 0) {
                    continue;
                }
                avgArtistRating = sumSongRatings/artistSongRatings.size();
                if (avgArtistRating == artistRating) {
                    artistRatings.add(s);
                }
            }

        } catch (Exception e) {
            new ErrorGui(e.getMessage());
            e.printStackTrace();
        }
        String[] artistWithRating = new String[artistRatings.size()];
        artistRatings.toArray(artistWithRating);
        //String[] artistWithRating = {"a1","a2","a3","a4"};
        JComboBox<String> aWithRatings = new JComboBox<String>(artistWithRating);
        aWithRatings.setBounds(390,220,435,50);
        aWithRatings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sa;
                sa = aWithRatings.getSelectedItem().toString();
                new ArtistManagerGui(sa);
                artistRatingScreen.dispose();
            }
        });
        artistRatingPanel.add(aWithRatings);

        //Button for return to previous page
        JButton returnButton = new JButton("<-");
        returnButton.setBounds(0, 0, 50, 20);
        artistRatingPanel.add(returnButton);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ArtistGui(); // calls first frame for Artist section
                artistRatingScreen.dispose();
            }
        });
        artistRatingScreen.add(artistRatingPanel);
    }
}
