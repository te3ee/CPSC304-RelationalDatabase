import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

/**
 * Created by Bijan Karrobi on 6/14/2018.
 */

public class ListenerGUI {

    public static void main(String[] args) {
        new Gui();
    }

}
class Listener extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
//    private static String[] Playlists;
//    public static JComboBox<String> PlaylistCB = new JComboBox<String>(Playlists);
    public static ArrayList<String> playlists = new ArrayList<>();
    private String currentListener;

    public Listener(String name, String playlist) {
        super(name);
        playlists.removeAll(playlists);
        if (playlist != null) {
            playlists.add(playlist);
        }
        currentListener = name;
        setSize(1200, 765);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);

        try {
            Statement users = Gui.con.createStatement();
            ResultSet userID = users.executeQuery("SELECT userid FROM music_user m " +
            "WHERE m.user_name = '" + currentListener + "'");
            String currentID = "";
            while (userID.next()) {
                currentID = userID.getString(1);
            }
            ResultSet userList = users.executeQuery("SELECT list_name FROM playlist p " +
            "WHERE p.userid = " + currentID);
            while (userList.next()) {
                if (!playlists.contains(userList.getString(1))) {
                    playlists.add(userList.getString(1));
                }
            }
            for (String s : playlists) {
                //System.out.println(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
            new ErrorGui(e.getMessage());
        }

        String[] playlistArray = new String[playlists.size()];
        playlists.toArray(playlistArray);
        JComboBox<String> PlaylistCB = new JComboBox<String>(playlistArray);

        // Listen to songs button
        JPanel Listen = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.weighty = 1;
        gbc.gridx = 1;
        gbc.insets = new Insets(10, 0, 0, 0);
        add(Listen, BorderLayout.NORTH);
        JButton ListenButton = new JButton("Listen");
        ListenButton.addActionListener(this);
        Listen.add(ListenButton, gbc);

        // Create Playlist Button
        JPanel CreatePlaylist = new JPanel(new GridBagLayout());
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.weighty = 1;
        gbc.gridx = 1;
        gbc.insets = new Insets(10, 50, 0,0);
        add(CreatePlaylist, BorderLayout.CENTER);
        JButton CreatePlaylistButton = new JButton("Create Playlist");
        CreatePlaylistButton.addActionListener(this);
        CreatePlaylist.add(CreatePlaylistButton, gbc);

        // Playlist drop down menu
        JPanel Playlist = new JPanel(new GridBagLayout());
        JLabel PlaylistLabel = new JLabel("Select a playlist");
        add(Playlist, BorderLayout.SOUTH);
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.insets = new Insets(0, 0, 100, 0);
//        JButton Select = new JButton("Select");
//        Select.addActionListener(this);
        PlaylistCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = e.getActionCommand();
                JComboBox<String> Playlists = (JComboBox<String>) e.getSource();
                int Selected = Playlists.getSelectedIndex();
                for (int i = 0; i < 20; i++){
                    if (i == Selected){
                        String current = (String) Playlists.getSelectedItem();
                        System.out.println("Selected playlist " + current);
                        setVisible(false);
                        new ListenerPlaylist(currentListener, current);
                    }
                }
            }
        });
        Playlist.add(PlaylistLabel, gbc);
        Playlist.add(PlaylistCB, gbc);
//        Playlist.add(Select, gbc);

        // Back Button to get out of playlist
        JPanel Back = new JPanel(new GridBagLayout());
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weighty = 1;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(Back, BorderLayout.WEST);
        JButton BackButton = new JButton("Back");
        BackButton.addActionListener(this);
        Back.add(BackButton, gbc);

        // Delete Playlist button
        JPanel Delete = new JPanel(new GridBagLayout());
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weighty = 1;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(Delete, BorderLayout.EAST);
        JButton DeleteButton = new JButton("Delete Playlist");
        DeleteButton.addActionListener(this);
        Delete.add(DeleteButton, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name = e.getActionCommand();

        if (name.equals("Listen")) {
            System.out.println("Listen clicked");
            setVisible(false);
            new Listening(currentListener);
        } else if (name.equals("Create Playlist")) {
            System.out.println("Create Playlist clicked");
            setVisible(false);
            new NewPlaylist(currentListener);
        } else if (name.equals("Back")){
            System.out.println("Back clicked");
            setVisible(false);
            new ListenerHomePage();
        } else if (name.equals("Delete Playlist")){
            System.out.println("Delete Playlist clicked");
            setVisible(false);
            new DeletePlaylist(currentListener);
        }
    }
}

class ListenerHomePage extends JFrame implements ActionListener{
    public static JComboBox<String> ListenerCB = new JComboBox<String>();
    private static final long serialVersionUID = 1L;

    public ListenerHomePage(){
        super("Listeners Home Page");
        setSize(1200, 765);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
        ArrayList<String> listeners = new ArrayList<>();


        // if you want a list of listeners, here they are haha
        try {
            Statement users = Gui.con.createStatement();
            ResultSet userList = users.executeQuery("SELECT user_name FROM music_user m, listener l " +
                    "WHERE m.userid = l.userid");
            while (userList.next()) {
                listeners.add(userList.getString(1));
            }
            for (String s : listeners) {
                //System.out.println(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
            new ErrorGui(e.getMessage());
        }
        String[] listenerArray = new String[listeners.size()];
        listeners.toArray(listenerArray);
        JComboBox<String> ListenerCB_2 = new JComboBox<String>(listenerArray);
        ListenerCB = ListenerCB_2;


        // Existing Listener drop down menu
        JPanel Listener = new JPanel(new GridBagLayout());
        JLabel ListenerUsername = new JLabel("Select your username    ");
        GridBagConstraints gbc = new GridBagConstraints();
        add(Listener, BorderLayout.PAGE_START);
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(10, 0, 0, 0);
        ListenerCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = e.getActionCommand();
                JComboBox<String> Playlists = (JComboBox<String>) e.getSource();
                int Selected = Playlists.getSelectedIndex();
                for (int i = 0; i < 100; i++){
                    if (i == Selected){
                        String current = (String) Playlists.getSelectedItem();
                        System.out.println("Selected Listener " + current);
                        setVisible(false);
                        new Listener(current, null).setVisible(true);
                    }
                }
            }
        });
        Listener.add(ListenerUsername, gbc);
        Listener.add(ListenerCB, gbc);

        // New Listener Button
        JPanel ExistingListener = new JPanel(new GridBagLayout());
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weighty = 1;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 200, 0, 0);
        add(ExistingListener, BorderLayout.WEST);
        JButton NewListener = new JButton("New Listener");
        NewListener.addActionListener(this);
        ExistingListener.add(NewListener, gbc);

        // Find what Listeners are listening to
        JPanel FindListeners = new JPanel(new GridBagLayout());
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weighty = 1;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 0, 200);
        add(FindListeners, BorderLayout.EAST);
        JButton ListOfListeners = new JButton("Find what users are listening to!");
        ListOfListeners.addActionListener(this);
        FindListeners.add(ListOfListeners, gbc);

        // Back Button to get out of playlist
        JPanel Back = new JPanel(new GridBagLayout());
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.weighty = 1;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 50, 0);
        add(Back, BorderLayout.PAGE_END);
        JButton BackButton = new JButton("Back");
        BackButton.addActionListener(this);
        Back.add(BackButton, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name = e.getActionCommand();

        if (name.equals("New Listener")){
            System.out.println("New Listener clicked");
            setVisible(false);
            new CreatingListener().setVisible(true);
        } else if (name.equals("Back")){
            System.out.println("Back clicked");
            setVisible(false);
            new Gui();
        } else if (name.equals("Find what users are listening to!")){
            System.out.println("Find Listener clicked");
            setVisible(false);
            new Division().setVisible(true);
        }
    }
}

class CreatingListener extends JFrame implements ActionListener{
    private static final long serialVersionUID = 1L;
    private static JTextField Username = new JTextField(16);

    public CreatingListener() {
        super("Creating Listener");
        setSize(1200, 765);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);

        // Username text box
        JPanel CreatingUsername = new JPanel(new GridBagLayout());
        JLabel UsernameLabel = new JLabel("Username: ");
        GridBagConstraints gbc = new GridBagConstraints();
        add(CreatingUsername, BorderLayout.NORTH);
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.insets = new Insets(10, 0, 0, 0);
        CreatingUsername.add(UsernameLabel, gbc);
        CreatingUsername.add(Username, gbc);

        // Year joined text box
        JPanel YearJoined = new JPanel(new GridBagLayout());
        JTextField Year = new JTextField(16);
        JLabel YearLabel = new JLabel("Current Year: ");
        add(YearJoined, BorderLayout.CENTER);
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.insets = new Insets(0, 0, 10, 0);
        YearJoined.add(YearLabel, gbc);
        YearJoined.add(Year, gbc);

        // Submit Button for creating new Listener
        JPanel Submit = new JPanel(new GridBagLayout());
        gbc.anchor = GridBagConstraints.PAGE_END;
        gbc.weighty = 1;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        add(Submit, BorderLayout.SOUTH);
        JButton SubmitButton = new JButton("Submit");
        SubmitButton.addActionListener(this);
        Submit.add(SubmitButton, gbc);

        // Back Button to get out of playlist
        JPanel Back = new JPanel(new GridBagLayout());
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weighty = 1;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 50, 0);
        add(Back, BorderLayout.WEST);
        JButton BackButton = new JButton("Back");
        BackButton.addActionListener(this);
        Back.add(BackButton, gbc);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String name = e.getActionCommand();

        if (name.equals("Submit")){
            System.out.println("Submit clicked");
            if (Username.getText().length() != 0) {
                // creating new listener
                try {
                    Statement userCount = Gui.con.createStatement();
                    ResultSet count = userCount.executeQuery("SELECT MAX(userid) FROM music_user");
                    int id = 0;
                    while (count.next()) {
                        id = count.getInt(1);
                    }
                    //System.out.println(id);

                    PreparedStatement ps = Gui.con.prepareStatement("INSERT INTO music_user VALUES (?, ?, ?)");
                    ps.setInt(1, id + 1);
                    ps.setString(2, Username.getText());
                    ps.setInt(3, 0); // <-- if you can return an int for year you can put it here
                    ps.executeUpdate();
                    Gui.con.commit();

                    ps = Gui.con.prepareStatement("INSERT INTO listener VALUES (?, ?)");
                    ps.setInt(1, id + 1);
                    ps.setString(2, null);
                    ps.executeUpdate();
                    Gui.con.commit();
                } catch (Exception sql) {
                    sql.printStackTrace();
                    new ErrorGui(sql.getMessage());
                }

                ListenerHomePage.ListenerCB.addItem(Username.getText());
                setVisible(false);
                new ListenerHomePage().setVisible(true);
            }
        }
        else if (name.equals("Back")){
            System.out.println("Back clicked");
            setVisible(false);
            new ListenerHomePage();
        }
    }
}

class ListenerPlaylist extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private String currentListener;
    private String currentPlaylist;

    public ListenerPlaylist(String name, String playlist) {
        super(playlist);
        currentPlaylist = playlist;
        currentListener = name;
        setSize(1200, 765);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
        ArrayList<String> SongNames = new ArrayList<>();
        ArrayList<String> ArtistNames = new ArrayList<>();
        ArrayList<String> Lengths = new ArrayList<>();
        ArrayList<String> Years = new ArrayList<>();
        ArrayList<String> Genres = new ArrayList<>();
        ArrayList<String> Types = new ArrayList<>();



        // if you want a list of listeners, here they are haha
        try {
            Statement users = Gui.con.createStatement();
            ResultSet playlistID = users.executeQuery("SELECT lid FROM playlist p " +
                    "WHERE p.list_name = '" + currentPlaylist + "'");
            String currentID = "";
            while (playlistID.next()){
                currentID = playlistID.getString(1);
            }
            ResultSet songsN = users.executeQuery("SELECT audio_name FROM audio a, contains c " +
                    "WHERE c.lid = " + currentID + " and a.sid = c.sid");
            while (songsN.next()) {
                SongNames.add(songsN.getString(1));
            }
            for (String s : SongNames) {
                System.out.println(s);
            }
            ResultSet artists = users.executeQuery("SELECT user_name FROM audio a, contains c, music_user m, creates r " +
                    "WHERE c.lid = " + currentID + " and a.sid = c.sid and m.userid = r.userid and r.sid = a.sid");
            while (artists.next()) {
                ArtistNames.add(artists.getString(1));
            }
            for (String s : ArtistNames) {
                System.out.println(s);
            }
            ResultSet length = users.executeQuery("SELECT audio_length FROM audio a, contains c " +
                    "WHERE c.lid = " + currentID + " and a.sid = c.sid");
            while (length.next()) {
                Lengths.add(length.getString(1));
            }
            for (String s : Lengths) {
                System.out.println(s);
            }
            ResultSet year = users.executeQuery("SELECT audio_year FROM audio a, contains c " +
                    "WHERE c.lid = " + currentID + " and a.sid = c.sid");
            while (year.next()) {
                Years.add(year.getString(1));
            }
            for (String s : Years) {
                System.out.println(s);
            }
            ResultSet genre = users.executeQuery("SELECT audio_genre FROM audio a, contains c " +
                    "WHERE c.lid = " + currentID + " and a.sid = c.sid");
            while (genre.next()) {
                Genres.add(genre.getString(1));
            }
            for (String s : Genres) {
                System.out.println(s);
            }
            ResultSet type = users.executeQuery("SELECT audio_type FROM audio a, contains c " +
                    "WHERE c.lid = " + currentID + " and a.sid = c.sid");
            while (type.next()) {
                Types.add(type.getString(1));
            }
            for (String s : Types) {
                System.out.println(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
            new ErrorGui(e.getMessage());
        }
        String[] SongArray = new String[SongNames.size()];
        SongNames.toArray(SongArray);
        String[] ArtistArray = new String[ArtistNames.size()];
        ArtistNames.toArray(ArtistArray);
        String[] LengthArray = new String[Lengths.size()];
        Lengths.toArray(LengthArray);
        String[] YearArray = new String[Years.size()];
        Years.toArray(YearArray);
        String[] GenreArray = new String[Genres.size()];
        Genres.toArray(GenreArray);
        String[] TypeArray = new String[Types.size()];
        Types.toArray(TypeArray);
        System.out.println(SongNames.size());


        // Scrollable table for a playlist's songs
        JPanel Playlist = new JPanel(new GridBagLayout());
//        Playlist.setPreferredSize(new Dimension(200,200));
        GridBagConstraints gbc = new GridBagConstraints();
        add(Playlist);
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.insets = new Insets(0, 5, 0, 5);
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        String data[][] = new String[SongNames.size()][];
        for (int i = 0; i < SongNames.size(); i++){
            String[] curr = {SongNames.get(i), ArtistNames.get(i), Lengths.get(i), Years.get(i), Genres.get(i), Types.get(i)};
            data[i] = curr;
        }
        String column[]={"Name","Artist","Length (seconds)", "Year", "Genre", "Type"};
        JTable table = new JTable( data, column);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(64);
        JScrollPane pane = new JScrollPane(table);
        Playlist.add(pane, gbc);

        // Back Button to get out of playlist
        JPanel Back = new JPanel(new GridBagLayout());
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.weighty = 1;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(Back, BorderLayout.SOUTH);
        JButton BackButton = new JButton("Back");
        BackButton.addActionListener(this);
        Back.add(BackButton, gbc);

        // Add song to current playlist
        JPanel Add = new JPanel(new GridBagLayout());
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weighty = 1;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(Add, BorderLayout.EAST);
        JButton AddButton = new JButton("Add Song");
        AddButton.addActionListener(this);
        Add.add(AddButton, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name = e.getActionCommand();

        if (name.equals("Back")) {
            System.out.println("Back clicked");
            setVisible(false);
            new Listener(currentListener, null).setVisible(true);
        } else if (name.equals("Add Song")){
            System.out.println("Add Song clicked");
            setVisible(false);
            new AddToPlaylist(currentListener, currentPlaylist);
        }

    }
}

class NewPlaylist extends JFrame implements ActionListener{
    private static final long serialVersionUID = 1L;
    private static JTextField Name = new JTextField(16);
    private String CurrentName;
    private String PName;

    public NewPlaylist(String name){
        super("New Playlist");
        CurrentName = name;
        setSize(1200, 765);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
        GridBagConstraints gbc = new GridBagConstraints();

        // Playlist naming text field
        JPanel PlaylistNaming = new JPanel(new GridBagLayout());
        JLabel PlaylistLabel = new JLabel("Playlist Name (Only letters accepted and must be one word):   ");
        add(PlaylistNaming, BorderLayout.PAGE_START);
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(50, 0, 0, 0);
        PlaylistNaming.add(PlaylistLabel, gbc);
        PlaylistNaming.add(Name, gbc);

        // Submit Button for creating new Playlist
        JPanel Submit = new JPanel(new GridBagLayout());
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 1;
        gbc.gridx = 1;
        gbc.insets = new Insets(30, 0, 0, 0);
        add(Submit, BorderLayout.CENTER);
        JButton SubmitButton = new JButton("Submit");
        SubmitButton.addActionListener(this);
        Submit.add(SubmitButton, gbc);

        // Back Button to get out of playlist
        JPanel Back = new JPanel(new GridBagLayout());
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.weighty = 1;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 50, 0);
        add(Back, BorderLayout.PAGE_END);
        JButton BackButton = new JButton("Back");
        BackButton.addActionListener(this);
        Back.add(BackButton, gbc);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name = e.getActionCommand();

        if (name.equals("Submit")){
            System.out.println("Submit clicked");
            PName = Name.getText();
            if (PName.length() != 0) {
                if (PName.matches("^[a-zA-Z]+$")) {
                    setVisible(false);
                    new Listener(CurrentName, PName).setVisible(true);
                    new SuccessfulConnectionGui();
                    // add Playlist to listener's playlist database (Done)
                    try {
                        Statement user = Gui.con.createStatement();
                        ResultSet uid = user.executeQuery("SELECT userid FROM music_user m " +
                                "WHERE m.user_name = '" + CurrentName + "'");
                        int id = 0;
                        while (uid.next()) {
                            id = uid.getInt(1);
                        }

                        Statement listNumber = Gui.con.createStatement();
                        ResultSet listID = listNumber.executeQuery("SELECT MAX(lid) FROM playlist");
                        int pid = 0;
                        while (listID.next()) {
                            pid = listID.getInt(1);
                        }

                        PreparedStatement ps = Gui.con.prepareStatement("INSERT INTO playlist VALUES (?, ?, ?)");
                        ps.setInt(1, pid + 1);
                        ps.setString(2, PName);
                        ps.setInt(3, id);
                        ps.executeUpdate();
                        Gui.con.commit();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        new ErrorGui(e1.getMessage());
                    }
                }
            }
        } else if (name.equals("Back")){
            System.out.println("Back clicked");
            setVisible(false);
            new Listener(CurrentName, null);
        }
    }
}

class Listening extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private String currentListener;
    private String currentSong;

    public Listening(String name) {
        super(name);
        currentListener = name;
        setSize(1200, 765);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
        GridBagConstraints gbc = new GridBagConstraints();
        ArrayList<String> Songs = new ArrayList<>();

        try {
            Statement users = Gui.con.createStatement();
            ResultSet songs = users.executeQuery("SELECT audio_name FROM audio");
            while (songs.next()) {
                Songs.add(songs.getString(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
            new ErrorGui(e.getMessage());
        }

        String[] SongArray = new String[Songs.size()];
        Songs.toArray(SongArray);
        JComboBox<String> SongCB = new JComboBox<String>(SongArray);

        // Audio drop down menu
        JPanel Song = new JPanel(new GridBagLayout());
        JLabel SongLabel = new JLabel("Select a song to listen to:");
        add(Song, BorderLayout.WEST);
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.insets = new Insets(0, 100, 200, 0);
        Song.add(SongLabel, gbc);
        Song.add(SongCB, gbc);
        SongCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = e.getActionCommand();
                JComboBox<String> Songs = (JComboBox<String>) e.getSource();
                int Selected = Songs.getSelectedIndex();
                for (int i = 0; i < 100; i++){
                    if (i == Selected){
                        currentSong = (String) Songs.getSelectedItem();
                        System.out.println("Selected Listener " + currentSong);
//                        setVisible(false);
//                        new Listener(currentListener).setVisible(true);
                        // add current song to listener's current song they are listening to (Done)
                        try {
                            Statement user = Gui.con.createStatement();
                            ResultSet uid = user.executeQuery("SELECT userid FROM music_user m " +
                                    "WHERE m.user_name = '" + currentListener + "'");
                            int id = 0;
                            while (uid.next()) {
                                id = uid.getInt(1);
                            }

                            Statement songFind = Gui.con.createStatement();
                            ResultSet songID = songFind.executeQuery("SELECT sid FROM audio a " +
                                    "WHERE a.audio_name = '" + currentSong + "'");
                            int song = 0;
                            while (songID.next()) {
                                song = songID.getInt(1);
                            }

                            PreparedStatement ps = Gui.con.prepareStatement("UPDATE listener " +
                                    "SET listener_sid = ? WHERE userid = " + id);
                            ps.setInt(1, song);
                            ps.executeUpdate();
                            Gui.con.commit();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            new ErrorGui(e2.getMessage());
                        }
                    }
                }
            }
        });

        // Rating drop down menu
        JPanel Rate = new JPanel(new GridBagLayout());
        JLabel RateLabel = new JLabel("Select a rating (1-5):");
        String[] Ratings = {"1", "2", "3", "4", "5"};
        JComboBox<String> RateCB = new JComboBox<String>(Ratings);
        add(Rate, BorderLayout.EAST);
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.insets = new Insets(0, 0, 200, 100);
        Rate.add(RateLabel, gbc);
        Rate.add(RateCB, gbc);
        RateCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = e.getActionCommand();
                JComboBox<String> Rating = (JComboBox<String>) e.getSource();
                int Selected = Rating.getSelectedIndex();
                for (int i = 0; i < 5; i++){
                    if (i == Selected){
                        String current = (String) Rating.getSelectedItem();
                        int currentRating = Integer.parseInt(current);
                        System.out.println("Selected Listener " + current);
//                        setVisible(false);
//                        new Listener(currentListener).setVisible(true);
                        // add/update rating for user for song chosen (done)
                        try {
                            Statement user = Gui.con.createStatement();
                            ResultSet uid = user.executeQuery("SELECT userid FROM music_user m " +
                                    "WHERE m.user_name = '" + currentListener + "'");
                            int id = 0;
                            while (uid.next()) {
                                id = uid.getInt(1);
                            }

                            Statement songFind = Gui.con.createStatement();
                            ResultSet songID = songFind.executeQuery("SELECT sid FROM audio a " +
                                    "WHERE a.audio_name = '" + currentSong + "'");
                            int song = 0;
                            while (songID.next()) {
                                song = songID.getInt(1);
                            }

                            Statement rateFind = Gui.con.createStatement();
                            ResultSet rateID = rateFind.executeQuery("SELECT rating FROM rates r " +
                                    "WHERE r.userid = " + id + " AND r.sid = " + song);
                            int rating = -1;
                            while (rateID.next()) {
                                rating = rateID.getInt(1);
                            }

                            if (rating == -1) {
                                PreparedStatement ps = Gui.con.prepareStatement("INSERT INTO rates " +
                                        "VALUES (?, ?, ?)");
                                ps.setInt(1, id);
                                ps.setInt(2, song);
                                ps.setInt(3, currentRating);
                                ps.executeUpdate();
                                Gui.con.commit();
                            }
                            else {
                                PreparedStatement ps = Gui.con.prepareStatement("UPDATE rates r" +
                                        " SET rating = ? WHERE r.userid = " + id + " AND r.sid = " + song);
                                ps.setInt(1, currentRating);
                                ps.executeUpdate();
                                Gui.con.commit();
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            new ErrorGui(e2.getMessage());
                        }
                    }
                }
            }
        });

        // Back Button to get out of listening
        JPanel Back = new JPanel(new GridBagLayout());
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.weighty = 1;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 50, 0);
        add(Back, BorderLayout.PAGE_END);
        JButton BackButton = new JButton("Back");
        BackButton.addActionListener(this);
        Back.add(BackButton, gbc);

        // Stop listening to a song
        JPanel Stop = new JPanel(new GridBagLayout());
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.weighty = 1;
        gbc.gridx = 1;
        gbc.insets = new Insets(50, 0, 0, 0);
        add(Stop, BorderLayout.NORTH);
        JButton StopButton = new JButton("Stop listening");
        StopButton.addActionListener(this);
        Stop.add(StopButton, gbc);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String name = e.getActionCommand();

        if (name.equals("Back")){
            System.out.println("<- clicked");
            setVisible(false);
            new Listener(currentListener, null);
        } else if (name.equals("Stop listening")){
            System.out.println("Stop listening clicked");
            setVisible(false);

            //update listening for currentListener to null in database
            try
            {
                Statement user = Gui.con.createStatement();
                ResultSet uid = user.executeQuery("SELECT userid FROM music_user m " +
                        "WHERE m.user_name = '" + currentListener + "'");
                int id = 0;
                while (uid.next()) {
                    id = uid.getInt(1);
                }

                PreparedStatement ps = Gui.con.prepareStatement("UPDATE listener SET listener_sid = ? WHERE userid = ?");
                ps.setNull(1, Types.INTEGER);
                ps.setInt(2, id);

                ps.executeUpdate();
                Gui.con.commit();

                ps.close();
            }
            catch (Exception e1) {
                new ErrorGui(e1.getMessage());
                e1.printStackTrace();
            }

            new Listener(currentListener, null);
        }
    }

}

class AddToPlaylist extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private String currentListener;
    private String currentPlaylist;
    private String songAdded;

    public AddToPlaylist(String name, String playlist) {
        super(name);
        currentListener = name;
        currentPlaylist = playlist;
        setSize(1200, 765);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
        GridBagConstraints gbc = new GridBagConstraints();
        ArrayList<String> Songs = new ArrayList<>();

        try {
            Statement users = Gui.con.createStatement();
            ResultSet songs = users.executeQuery("SELECT audio_name FROM audio");
            while (songs.next()) {
                Songs.add(songs.getString(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
            new ErrorGui(e.getMessage());
        }

        String[] SongArray = new String[Songs.size()];
        Songs.toArray(SongArray);
        JComboBox<String> SongCB = new JComboBox<String>(SongArray);

        // Audio drop down menu
        JPanel Song = new JPanel(new GridBagLayout());
        JLabel SongLabel = new JLabel("Select a song to listen to:      ");
        add(Song, BorderLayout.NORTH);
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.insets = new Insets(50, 0, 0, 0);
        Song.add(SongLabel, gbc);
        Song.add(SongCB, gbc);
        SongCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = e.getActionCommand();
                JComboBox<String> Songs = (JComboBox<String>) e.getSource();
                int Selected = Songs.getSelectedIndex();
                for (int i = 0; i < 10000; i++){
                    if (i == Selected){
                        String current = (String) Songs.getSelectedItem();
                        System.out.println("Selected song " + current);
                        setVisible(false);
//                        new Listener(currentListener, null).setVisible(false);
                        // Need to add the picked song (current) and add to playlist database (done)
                        try {
                            Statement user = Gui.con.createStatement();
                            ResultSet uid = user.executeQuery("SELECT userid FROM music_user m " +
                                    "WHERE m.user_name = '" + currentListener + "'");
                            int id = 0;
                            while (uid.next()) {
                                id = uid.getInt(1);
                            }

                            Statement songFind = Gui.con.createStatement();
                            ResultSet songID = songFind.executeQuery("SELECT sid FROM audio a " +
                                    "WHERE a.audio_name = '" + current + "'");
                            int song = 0;
                            while (songID.next()) {
                                song = songID.getInt(1);
                            }

                            Statement listFind = Gui.con.createStatement();
                            ResultSet listID = listFind.executeQuery("SELECT lid FROM playlist p " +
                                    "WHERE p.userid = " + id + " AND " +
                                    "p.list_name = '"+ currentPlaylist +"'");
                            int lid = 0;
                            while (listID.next()) {
                                lid = listID.getInt(1);
                            }

                            PreparedStatement ps = Gui.con.prepareStatement("INSERT INTO contains VALUES (?, ?, ?)");
                            ps.setInt(1, lid);
                            ps.setInt(2, song);
                            ps.setInt(3, id);
                            ps.executeUpdate();
                            Gui.con.commit();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            new ErrorGui(e1.getMessage());
                        }
                        new ListenerPlaylist(currentListener, currentPlaylist).setVisible(true);
                    }
                }
            }
        });

        // Back Button to get out of playlist
        JPanel Back = new JPanel(new GridBagLayout());
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.weighty = 1;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 50, 0);
        add(Back, BorderLayout.PAGE_END);
        JButton BackButton = new JButton("Back");
        BackButton.addActionListener(this);
        Back.add(BackButton, gbc);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String name = e.getActionCommand();

        if (name.equals("Back")){
            System.out.println("<- clicked");
            setVisible(false);
            new ListenerPlaylist(currentListener, currentPlaylist);
        }
    }

}

class DeletePlaylist extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private String currentListener;
    public static ArrayList<String> playlists = new ArrayList<>();
    private String currentPlaylist;
//    private String songAdded;

    public DeletePlaylist(String name) {
        super(name);
        currentListener = name;
        setSize(1200, 765);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
        GridBagConstraints gbc = new GridBagConstraints();

        try {
            Statement users = Gui.con.createStatement();
            ResultSet userID = users.executeQuery("SELECT userid FROM music_user m " +
                    "WHERE m.user_name = '" + currentListener + "'");
            String currentID = "";
            while (userID.next()) {
                currentID = userID.getString(1);
            }
            ResultSet userList = users.executeQuery("SELECT list_name FROM playlist p " +
                    "WHERE p.userid = " + currentID);
            while (userList.next()) {
                if (!playlists.contains(userList.getString(1))) {
                    playlists.add(userList.getString(1));
                }
            }
            for (String s : playlists) {
                //System.out.println(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
            new ErrorGui(e.getMessage());
        }

        String[] playlistArray = new String[playlists.size()];
        playlists.toArray(playlistArray);
        JComboBox<String> PlaylistCB = new JComboBox<String>(playlistArray);

        // Playlist drop down menu
        JPanel Playlist = new JPanel(new GridBagLayout());
        JLabel PlaylistLabel = new JLabel("Select a playlist to delete:      ");
        add(Playlist, BorderLayout.NORTH);
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.insets = new Insets(50, 0, 0, 0);
        Playlist.add(PlaylistLabel, gbc);
        Playlist.add(PlaylistCB, gbc);
        PlaylistCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = e.getActionCommand();
                JComboBox<String> playlists = (JComboBox<String>) e.getSource();
                int Selected = playlists.getSelectedIndex();
                for (int i = 0; i < 10000; i++){
                    if (i == Selected){
                        String current = (String) playlists.getSelectedItem();
                        System.out.println("Selected playlist " + current);
                        setVisible(false);

                        //Add backend to deleting a Playlist (done)
                        try{
                            Statement user = Gui.con.createStatement();
                            ResultSet uid = user.executeQuery("SELECT userid FROM music_user m " +
                                    "WHERE m.user_name = '" + currentListener + "'");
                            int id = 0;
                            while (uid.next()) {
                                id = uid.getInt(1);
                            }

                            Statement listFind = Gui.con.createStatement();
                            ResultSet listID = listFind.executeQuery("SELECT userid FROM music_user m " +
                                    "WHERE m.user_name = '" + currentListener + "'");
                            int lid = 0;
                            while (listID.next()) {
                                lid = listID.getInt(1);
                            }

                            PreparedStatement ps = Gui.con.prepareStatement("DELETE FROM playlist WHERE userid = ? " +
                                    "AND list_name = ?");
                            ps.setInt(1, id);
                            ps.setString(2, current);

                            PreparedStatement ps2 = Gui.con.prepareStatement("DELETE FROM contains WHERE userid = ? " +
                                    "AND lid = ?");
                            ps2.setInt(1, lid);
                            ps2.setString(2, current);

                            ps2.executeUpdate();
                            ps.executeUpdate();
                            Gui.con.commit();
                            ps.close();
                        } catch (Exception sql){
                            sql.printStackTrace();
                            new ErrorGui(sql.getMessage());
                        }

                        new Listener(currentListener, null).setVisible(true);
                        new SuccessfulDeletion();
                    }
                }
            }
        });

        // Back Button to get out of playlist
        JPanel Back = new JPanel(new GridBagLayout());
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.weighty = 1;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 50, 0);
        add(Back, BorderLayout.PAGE_END);
        JButton BackButton = new JButton("Back");
        BackButton.addActionListener(this);
        Back.add(BackButton, gbc);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String name = e.getActionCommand();

        if (name.equals("Back")){
            System.out.println("<- clicked");
            setVisible(false);
            new Listener(currentListener, null);
        }
    }

}

class CurrentListeners extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private String currentSong;


    public CurrentListeners(String song) {
        super("Listeners Rating Home Page:");
        currentSong = song;
        setSize(1200, 765);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
        ArrayList<String> Names = new ArrayList<>();
        String sid = "";


        try {
            Statement users = Gui.con.createStatement();
            ResultSet SongName = users.executeQuery("SELECT sid FROM audio WHERE audio_name = '" + currentSong + "'");
            while (SongName.next()) {
                sid = SongName.getString(1);
                System.out.println("current sid: " + sid);
            }

            ArrayList<Integer> listeners = new ArrayList<>();
            Statement listenerFind = Gui.con.createStatement();
            ResultSet listenerList = listenerFind.executeQuery("SELECT userid FROM listener");
            while (listenerList.next()) {
                listeners.add(listenerList.getInt(1));
            }

            //TODO Relational Algebra
            ResultSet Usernames = users.executeQuery("SELECT m.user_name FROM music_user m, listener l " +
                    "WHERE m.userid = l.userid AND NOT EXISTS ((SELECT * FROM rates r WHERE r.userid = m.userid) MINUS "+
                    "(SELECT * FROM rates r WHERE r.userid = m.userid AND r.sid = "+ sid +"))");
            while (Usernames.next()) {
                Names.add(Usernames.getString(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
            new ErrorGui(e.getMessage());
        }
        String[] NameArray = new String[Names.size()];
        Names.toArray(NameArray);

        // Scrollable table for a user listening list
        JPanel Listening = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        add(Listening);
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.insets = new Insets(0, 5, 0, 5);
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        String data[][] = new String[Names.size()][];
        for (int i = 0; i < Names.size(); i++){
            String[] curr = {Names.get(i)};
            data[i] = curr;
        }
        String column[]={"Name"};
        JTable table = new JTable( data, column);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(64);
        JScrollPane pane = new JScrollPane(table);
        Listening.add(pane, gbc);

        // Back Button to get out of CurrentListener
        JPanel Back = new JPanel(new GridBagLayout());
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.weighty = 1;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(Back, BorderLayout.SOUTH);
        JButton BackButton = new JButton("Back");
        BackButton.addActionListener(this);
        Back.add(BackButton, gbc);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name = e.getActionCommand();

        if (name.equals("Back")) {
            System.out.println("Back clicked");
            setVisible(false);
            new Division().setVisible(true);
        }
    }
}

class Division extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    public Division() {
        super("Find all listeners who've rated a song");
        setSize(1200, 765);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
        GridBagConstraints gbc = new GridBagConstraints();
        ArrayList<String> Songs = new ArrayList<>();

        try {
            Statement users = Gui.con.createStatement();
            ResultSet songs = users.executeQuery("SELECT audio_name FROM audio");
            while (songs.next()) {
                Songs.add(songs.getString(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
            new ErrorGui(e.getMessage());
        }

        String[] SongArray = new String[Songs.size()];
        Songs.toArray(SongArray);
        JComboBox<String> SongCB = new JComboBox<String>(SongArray);

        // Audio drop down menu
        JPanel Song = new JPanel(new GridBagLayout());
        JLabel SongLabel = new JLabel("Select a song:      ");
        add(Song, BorderLayout.NORTH);
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.insets = new Insets(50, 0, 0, 0);
        Song.add(SongLabel, gbc);
        Song.add(SongCB, gbc);
        SongCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = e.getActionCommand();
                JComboBox<String> Songs = (JComboBox<String>) e.getSource();
                int Selected = Songs.getSelectedIndex();
                for (int i = 0; i < 10000; i++){
                    if (i == Selected){
                        String current = (String) Songs.getSelectedItem();
                        System.out.println("Selected song " + current);
                        setVisible(false);
                        new CurrentListeners(current).setVisible(true);
                    }
                }
            }
        });

        // Back Button to get out of playlist
        JPanel Back = new JPanel(new GridBagLayout());
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.weighty = 1;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 50, 0);
        add(Back, BorderLayout.PAGE_END);
        JButton BackButton = new JButton("Back");
        BackButton.addActionListener(this);
        Back.add(BackButton, gbc);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String name = e.getActionCommand();

        if (name.equals("Back")){
            System.out.println("<- clicked");
            setVisible(false);
            new ListenerHomePage().setVisible(true);
        }
    }

}