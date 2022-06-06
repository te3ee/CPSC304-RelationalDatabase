import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ErrorGui {
    ErrorGui(String error) {
        JFrame scGui = new JFrame("Error");
        JPanel scPanel = new JPanel();
        scGui.setVisible(true);
        scGui.setResizable(false);
        scGui.setSize(900, 200);
        scGui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        scPanel.setLayout(null);

        // Error label
        JLabel newArtistTitle = new JLabel("Error: " + error);
        newArtistTitle.setFont(new Font(Font.SERIF, Font.PLAIN, 15));
        newArtistTitle.setBounds(77, 5, 800, 100);
        scPanel.add(newArtistTitle);

        // Error close button
        JButton errCloseButton = new JButton("Close");
        errCloseButton.setBounds(140, 100, 70, 20);
        scPanel.add(errCloseButton);
        errCloseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scGui.dispose();
            }
        });

        scGui.add(scPanel);
    }
}