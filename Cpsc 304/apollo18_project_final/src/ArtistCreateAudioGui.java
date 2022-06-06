import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class ArtistCreateAudioGui {

   private JComboBox typeCB;
   private JTextField audioType;
   private  String aType;

    private JComboBox albumCB;
    private JTextField albumField;
    private  String aAlbum;



    ArtistCreateAudioGui(String artistName){
        JFrame artistAudioScreen = new JFrame("304Project: " + artistName + "'s Audio Creation Screen"); // frame description changes depending on user selected
        JPanel artistAudioPanel = new JPanel();
        artistAudioScreen.setVisible(true);
        artistAudioScreen.setResizable(false);
        artistAudioScreen.setSize(1200, 765);
        artistAudioScreen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        artistAudioPanel.setLayout(null);

        // Frame description
        JLabel artistAudioTitle = new JLabel( "Audio Creation");
        artistAudioTitle.setFont(new Font(Font.SERIF, Font.PLAIN, 40));
        artistAudioTitle.setBounds(475, 20, 300, 100);
        artistAudioPanel.add(artistAudioTitle);

        // Warning label
        JLabel warningLabel = new JLabel("Note: No empty fields allowed except for Album field. Song Name and Genre must be alphabets or numbers. Song length and year must be in integers. Year must be integer with length 4.");
        warningLabel.setFont(new Font(Font.SERIF, Font.PLAIN, 12));
        warningLabel.setBounds(215, 93, 1000, 25);
        artistAudioPanel.add(warningLabel);

        // Artist input for Song Name
        JTextField songNameTextField = new JTextField(25);
        JLabel songNameLabel = new JLabel("Song Name:");
        songNameLabel.setBounds(270, 150, 350, 25);
        songNameTextField.setBounds(440, 150, 400, 25);
        artistAudioPanel.add(songNameTextField);
        artistAudioPanel.add(songNameLabel);

        // Artist input for year created
        JTextField yearCreatedTextField = new JTextField(25);
        JLabel yearCreatedLabel = new JLabel("Year Created:");
        yearCreatedLabel.setBounds(270, 215, 350, 25);
        yearCreatedTextField.setBounds(440, 215, 400, 25);
        artistAudioPanel.add(yearCreatedTextField);
        artistAudioPanel.add(yearCreatedLabel);

        // Type of Audio
        String[] type = {"Song", "Podcast"}; // Sample data for now: need to insert name of all artists from database
        typeCB = new JComboBox<String>(type);
        audioType = new JTextField();
        JLabel typeLabel = new JLabel("Audio type: ");
        typeCB.setBounds(440,275,400,25);
        typeLabel.setBounds(270,275,400,25);
        typeCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aType = (String)typeCB.getSelectedItem();
                audioType.setText(aType);
            }

        });
        typeCB.setSelectedItem(null); // sets null to trigger ActionListener
        artistAudioPanel.add(typeLabel);
        artistAudioPanel.add(typeCB);
        artistAudioPanel.add(audioType); // saves type value

        // Artist input for song genre
        JTextField songGenreTextField = new JTextField(25);
        JLabel songGenreLabel = new JLabel("Song Genre:");
        songGenreLabel.setBounds(270, 340, 350, 25);
        songGenreTextField.setBounds(440, 340, 400, 25);
        artistAudioPanel.add(songGenreTextField);
        artistAudioPanel.add(songGenreLabel);

        // Artist input for song length
        JTextField lengthTextField = new JTextField(25);
        JLabel lengthLabel = new JLabel("Length Of Song (Seconds):");
        lengthLabel.setBounds(270, 405, 350, 25);
        lengthTextField.setBounds(440, 405, 400, 25);
        artistAudioPanel.add(lengthTextField);
        artistAudioPanel.add(lengthLabel);

        // Album
        ArrayList<String> albums = new ArrayList<>();
        try {
            Statement albumFind = Gui.con.createStatement();
            ResultSet albumList = albumFind.executeQuery("SELECT album.album_name, music_user.user_name FROM album " +
                    "INNER JOIN  music_user ON album.userid = music_user.userid");
            while (albumList.next()) {
                if (albumList.getString(2).equals(artistName)) {
                    albums.add(albumList.getString(1));
                }
            }
            for (String s : albums) {
                System.out.println(s);
            }

        } catch (Exception e) {
            new ErrorGui(e.getMessage());
            e.printStackTrace();
        }
        // Existing artist
        String[] albumArray = new String[albums.size()];
        albums.toArray(albumArray);
        //String[] albums = {"alb1", "alb2", "alb3", "alb4", "alb2", "alb3", "alb4", "alb2", "alb3", "alb4", "alb2", "alb3", "alb4"};
        albumCB = new JComboBox<String>(albumArray);
        albumField = new JTextField();
        JLabel albumLabel = new JLabel("Add Song To Album: ");
        albumCB.setBounds(440,470,400,25);
        albumLabel.setBounds(270,470,400,25);
        albumCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aAlbum = (String)albumCB.getSelectedItem();
                albumField.setText(aAlbum);
            }

        });
        albumCB.setSelectedItem(null);
        artistAudioPanel.add(albumLabel);
        artistAudioPanel.add(albumCB);
        artistAudioPanel.add(albumField);

        // Album Desc
        JLabel albumDesc = new JLabel("Empty field indicates that song belongs will not be included in any albums.");
        albumDesc.setFont(new Font(Font.SERIF, Font.PLAIN, 12));
        albumDesc.setBounds(445,490,1000,25);
        artistAudioPanel.add(albumDesc);

        //Button to create song
        JButton createButton = new JButton("Create");
        createButton.setBounds(270, 640, 585, 70);
        artistAudioPanel.add(createButton);
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nameValue = songNameTextField.getText();
                    Integer yearValue = Integer.parseInt(yearCreatedTextField.getText());
                    String typeValue = audioType.getText();
                    String genreValue = songGenreTextField.getText();
                    Integer lengthValue = Integer.parseInt(lengthTextField.getText());
                    String albumValue = albumField.getText();

                    if (albumValue.length() == 0) { // if user does not specify where to put song album will be null
                        albumValue = "null";
                    }
                    if (yearCreatedTextField.getText().length() != 4) { //makes sure length of year is only 4 characters long
                        throw new NumberFormatException();
                    }

                    if (nameValue.length() == 0 || typeValue.length() == 0 || genreValue.length()== 0 || lengthTextField.getText().length() == 0){ // Makes sure fields are not empty.
                        throw new InvalidFieldException();
                    }

                    // Only characters, spaces and numbers allowed in fields.
                    if (!(nameValue.matches("[a-z|A-Z|0-9|\\s]*")) || !(genreValue.matches("[a-z|A-Z|0-9|\\s]*"))) {
                        throw new InvalidFieldException();
                    }
                    Statement user = Gui.con.createStatement();
                    ResultSet uid = user.executeQuery("SELECT userid FROM music_user m " +
                            "WHERE m.user_name = '" + artistName + "'");
                    int id = 0;
                    while (uid.next()) {
                        id = uid.getInt(1);
                    }

                    Statement songNumber = Gui.con.createStatement();
                    ResultSet songid = songNumber.executeQuery("SELECT MAX(sid) FROM audio a");
                    int sid = 0;
                    while (songid.next()) {
                        sid = songid.getInt(1);
                    }

                    int albumid = 0;
                    if (albumValue.length() != 0) {
                        Statement albumFind = Gui.con.createStatement();
                        ResultSet album = albumFind.executeQuery("SELECT aid FROM album al " +
                                "WHERE al.album_name = '" + albumValue + "'");
                        while (album.next()) {
                            albumid = album.getInt(1);
                        }
                    }

                    PreparedStatement ps = Gui.con.prepareStatement("INSERT INTO audio VALUES (?, ?, ?, ?, ?, ?, ?)");
                    ps.setInt(1, sid + 1);
                    if (albumid == 0) {
                        ps.setNull(2, Types.INTEGER);
                    }
                    else {
                        ps.setInt(2, albumid);
                    }
                    ps.setString(3, nameValue);
                    ps.setInt(4, yearValue);
                    ps.setString(5, genreValue);
                    ps.setInt(6, lengthValue);
                    ps.setString(7, typeValue);

                    PreparedStatement ps2 = Gui.con.prepareStatement("INSERT INTO creates VALUES (?, ?)");
                    ps2.setInt(1, id);
                    ps2.setInt(2, sid + 1);

                    ps.executeUpdate();
                    ps2.executeUpdate();

                    Gui.con.commit();

                    new ArtistManagerGui(artistName);
                    new SuccessfulConnectionGui();
                    artistAudioScreen.dispose();

                }catch (NumberFormatException err){
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
        artistAudioPanel.add(returnButton);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ArtistManagerGui(artistName);
                artistAudioScreen.dispose();
            }
        });
        artistAudioScreen.add(artistAudioPanel);
    }
}
