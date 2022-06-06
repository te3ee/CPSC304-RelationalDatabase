import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SuccessfulDeletion {
    SuccessfulDeletion() {
        JFrame sdGui = new JFrame("Success");
        JPanel sdPanel = new JPanel();
        sdGui.setVisible(true);
        sdGui.setResizable(false);
        sdGui.setSize(350, 200);
        sdGui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        sdPanel.setLayout(null);

        // Error label
        JLabel newArtistTitle = new JLabel("Successfully deleted!");
        newArtistTitle.setFont(new Font(Font.SERIF, Font.PLAIN, 15));
        newArtistTitle.setBounds(110, 5, 300, 100);
        sdPanel.add(newArtistTitle);

        // Error close button
        JButton errCloseButton = new JButton("Close");
        errCloseButton.setBounds(140, 100, 70, 20);
        sdPanel.add(errCloseButton);
        errCloseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sdGui.dispose();
            }
        });

        sdGui.add(sdPanel);
    }
}
