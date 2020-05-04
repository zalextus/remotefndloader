package remotefndload;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

public class FrameMain_AboutBoxPanel extends JPanel {
    private GridBagLayout layoutMain = new GridBagLayout();
    private Border border = BorderFactory.createEtchedBorder();
    private JLabel labelTitle2 = new JLabel();
    private JLabel labelAuthor2 = new JLabel();
    private JLabel labelCopyright2 = new JLabel();
    private JLabel jLabel1 = new JLabel();
    private JLabel jLabel2 = new JLabel();
    private JLabel jLabel3 = new JLabel();

    public FrameMain_AboutBoxPanel() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        labelAuthor2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    
    private void jbInit() throws Exception {
        this.setLayout(layoutMain);
        this.setBorder(border);
        labelTitle2.setText("RemoteFndLoader, 2.7           ");
        labelTitle2.setHorizontalAlignment(SwingConstants.LEFT);
        labelAuthor2.setText("iklesia@gmail.com");
        labelAuthor2.setForeground(new Color(33, 33, 255));
        labelAuthor2.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                labelAuthor2_mouseClicked(e);
            }
        });
        labelCopyright2.setText("Freeware, 2010-2020");
        jLabel1.setText("Title:");
        jLabel2.setText("Created:");
        jLabel3.setText("Licence:");
        this.add(labelTitle2,
                 new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                                        new Insets(0, 0, 0, 0), 0, 0));
        this.add(labelAuthor2,
                 new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                                        new Insets(0, 0, 0, 0), 0, 0));
        this.add(labelCopyright2,
                 new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                                        new Insets(0, 0, 0, 0), 0, 0));
        this.add(jLabel1,
                 new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                                        new Insets(0, 0, 0, 0), 0, 0));
        this.add(jLabel2,
                 new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                                        new Insets(0, 0, 0, 0), 0, 0));
        this.add(jLabel3,
                 new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                                        new Insets(0, 0, 0, 0), 0, 0));
    }

    private void labelAuthor2_mouseClicked(MouseEvent e) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI("mailto:iklesia@gmail.com"));
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Could not open link.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Could not open link.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame f = new JFrame();
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.getContentPane().add(new FrameMain_AboutBoxPanel());
                f.pack();
                f.setLocationRelativeTo(null);
                f.setVisible(true);
            }
        });
    }

}
