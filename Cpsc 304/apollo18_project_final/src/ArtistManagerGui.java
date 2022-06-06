import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ArtistManagerGui {

    ArtistManagerGui(String artistName){ // artistName is the name of artist selected from previous frame.

        JFrame artistManagerScreen = new JFrame("304Project: " + artistName + "'s Management Screen"); // frame description changes depending on user selected
        JPanel artistManagerPanel = new JPanel();
        artistManagerScreen.setVisible(true);
        artistManagerScreen.setResizable(false);
        artistManagerScreen.setSize(1200, 765);
        artistManagerScreen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        artistManagerPanel.setLayout(null);

        // Frame description
        JLabel artistManagerTitle = new JLabel( "Artist's Management Screen");
        artistManagerTitle.setFont(new Font(Font.SERIF, Font.PLAIN, 40));
        artistManagerTitle.setBounds(381, 2, 500, 100);
        artistManagerPanel.add(artistManagerTitle);

        // fill artistArray with artistName's song (done)
        ArrayList<String> songs = new ArrayList<>();
        try {
            Statement songFind = Gui.con.createStatement();
            ResultSet songList = songFind.executeQuery("SELECT audio_name FROM audio " +
                    "WHERE sid IN " +
                    "(SELECT sid FROM creates c " +
                    "WHERE c.userid IN (SELECT userid FROM music_user " +
                    "WHERE user_name = '" + artistName + "'))");
            while (songList.next()) {
                songs.add(songList.getString(1));
            }

        } catch (Exception e) {
            new ErrorGui(e.getMessage());
            e.printStackTrace();
        }

        // Existing artist
        String[] artistArray = new String[songs.size()];
        songs.toArray(artistArray);
        //String artistArray[] = {"insert this user's audio","insert this user's audio1","insert this user's audio2","insert this user's audio3"};
        JComboBox<String> audioCB = new JComboBox<String>(artistArray);
        JLabel artistUsername = new JLabel("Choose audio for album and rating information: ");
        artistUsername.setFont(new Font(Font.SERIF, Font.PLAIN, 20));
        audioCB.setBounds(600,90,435,50);
        artistUsername.setBounds(200,15,600,200);
        audioCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedAudio;
                selectedAudio = audioCB.getSelectedItem().toString();
                System.out.print(selectedAudio);
                new ArtistAudioInfo(artistName,selectedAudio);
                artistManagerScreen.dispose();
            }
        });
        artistManagerScreen.add(artistUsername);
        artistManagerScreen.add(audioCB);

        //Button for creating new audio
        JButton newAudioButton = new JButton("Audio Creation");
        newAudioButton.setBounds(270, 150, 650, 150);
        artistManagerPanel.add(newAudioButton);
        newAudioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ArtistCreateAudioGui(artistName);
                artistManagerScreen.dispose();
            }
        });

        //Button for creating new album
        JButton newAlbumButton = new JButton("Album Creation");
        newAlbumButton.setBounds(270, 350, 650, 150);
        artistManagerPanel.add(newAlbumButton);
        newAlbumButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ArtistCreateAlbumGui(artistName);
                artistManagerScreen.dispose();
            }
        });

        //Button for current user
        JButton deleteCurrentArtist = new JButton("Delete Account");
        deleteCurrentArtist.setBounds(270, 550, 650, 150);
        artistManagerPanel.add(deleteCurrentArtist);
        deleteCurrentArtist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    // delete artistName (done)
                    Statement user = Gui.con.createStatement();
                    ResultSet uid = user.executeQuery("SELECT userid FROM music_user m " +
                            "WHERE m.user_name = '" + artistName + "'");
                    int id = 0;
                    while (uid.next()) {
                        id = uid.getInt(1);
                    }

                    //PreparedStatement ps = Gui.con.prepareStatement("DELETE FROM artist WHERE userid = ?");
                    //ps.setInt(1, id);

                    PreparedStatement ps2 = Gui.con.prepareStatement("DELETE FROM music_user WHERE userid = ?");
                    ps2.setInt(1, id);

                    //PreparedStatement ps3 = Gui.con.prepareStatement("DELETE FROM creates WHERE userid = ?");
                    //ps3.setInt(1, id);

                    //PreparedStatement ps4 = Gui.con.prepareStatement("DELETE FROM album WHERE userid = ?");
                    //ps4.setInt(1, id);


                    Statement albumFind = Gui.con.createStatement();
                    ResultSet albumID = albumFind.executeQuery("SELECT aid FROM album " +
                            "WHERE userid = " + id);
                    int aid = 0;
                    while (albumID.next()) {
                        aid = albumID.getInt(1);
                    }


                    PreparedStatement ps5 = Gui.con.prepareStatement("UPDATE audio SET " +
                       "aid = null WHERE aid = " + aid);

                    ps5.executeUpdate();
                    //ps4.executeUpdate();
                    //ps3.executeUpdate();
                    //ps.executeUpdate();
                    ps2.executeUpdate();
                    Gui.con.commit();

                    ps2.close();
                    new ArtistGui();
                    new SuccessfulDeletion();
                    artistManagerScreen.dispose();
                } catch (Exception sql){
                    new ErrorGui(sql.getMessage());
                    sql.printStackTrace();
                }
            }
        });


        //Button for return to previous page
        JButton returnButton = new JButton("<-");
        returnButton.setBounds(0, 0, 50, 20);
        artistManagerPanel.add(returnButton);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    new ArtistGui(); // calls first frame for Artist section
                    artistManagerScreen.dispose();
            }
        });

        artistManagerScreen.add(artistManagerPanel);

    }
}
