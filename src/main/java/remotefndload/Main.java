package remotefndload;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);

        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                try {
                    LAFUtil.setLAF(GeneralSettings.getInstance().getLookandfeel());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                int height = gd.getDisplayMode().getHeight();
                FrameMain main = new FrameMain();
                main.setSize(900, height - 100);
                main.setLocationRelativeTo(null);
                main.setVisible(true);
            }
        });
    }
}
