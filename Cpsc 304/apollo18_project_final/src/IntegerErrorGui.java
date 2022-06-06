import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IntegerErrorGui extends JFrame {
    IntegerErrorGui() {
        JFrame integerErrorMainFrame = new JFrame("Error");
        JPanel integerErrorMainPanel = new JPanel();
        integerErrorMainFrame.setVisible(true);
        integerErrorMainFrame.setResizable(false);
        integerErrorMainFrame.setSize(350, 200);
        integerErrorMainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        integerErrorMainPanel.setLayout(null);

        // Error label
        JLabel newArtistTitle = new JLabel("Error: Please input a valid year or a valid song length");
        newArtistTitle.setFont(new Font(Font.SERIF, Font.PLAIN, 13));
        newArtistTitle.setBounds(25, 5, 350, 100);
        integerErrorMainPanel.add(newArtistTitle);

        // Error close button
        JButton errCloseButton = new JButton("Close");
        errCloseButton.setBounds(140, 100, 70, 20);
        integerErrorMainPanel.add(errCloseButton);
        errCloseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                integerErrorMainFrame.dispose();
            }
        });

        integerErrorMainFrame.add(integerErrorMainPanel);
    }
}

