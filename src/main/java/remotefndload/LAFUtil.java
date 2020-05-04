package remotefndload;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.api.SubstanceSkin;
import org.jvnet.substance.skin.SkinInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class LAFUtil {

    public static void setLAF(String className) throws InstantiationException, IllegalAccessException,
            ClassNotFoundException, UnsupportedLookAndFeelException {

        Object object = Class.forName(className).newInstance();

        if (object instanceof LookAndFeel) {
            UIManager.setLookAndFeel((LookAndFeel) object);
            Window[] windows = Window.getWindows();
            for (int i = 0; i < windows.length; i++) {
                SwingUtilities.updateComponentTreeUI(windows[i]);
            }

        } else if (object instanceof SubstanceSkin) {
            SubstanceLookAndFeel.setSkin((SubstanceSkin) object);
        }

    }

    public JMenu createLAFMenu(JFrame frame) {
        GeneralSettings generalSettings = GeneralSettings.getInstance();
        JMenu menuLAF = new JMenu("Look and feel");
        menuLAF.setMnemonic(KeyEvent.VK_L);

        // LAFs from jdk
        ButtonGroup lafRadioGroup = new ButtonGroup();
        for (UIManager.LookAndFeelInfo lafInfo : UIManager.getInstalledLookAndFeels()) {
            JRadioButtonMenuItem mi =
                new JRadioButtonMenuItem(new LAFAction(frame, lafInfo.getName(), lafInfo.getClassName()));
            if (generalSettings.getLookandfeel().equals(lafInfo.getClassName())) {
                mi.setSelected(true);
            }
            lafRadioGroup.add(mi);
            menuLAF.add(mi);
        }

        // Substance LAFs
        JMenu menuLAFSubstance = new JMenu("Substance");
        Map<String, SkinInfo> map = org.jvnet.substance.SubstanceLookAndFeel.getAllSkins();

        Collection<SkinInfo> values = map.values();
        Iterator<SkinInfo> iter = values.iterator();

        while (iter.hasNext()) {
            SkinInfo skinInfo = iter.next();
            JRadioButtonMenuItem mi =
                new JRadioButtonMenuItem(new LAFAction(frame, skinInfo.getDisplayName(), skinInfo.getClassName()));
            if (generalSettings.getLookandfeel().equals(skinInfo.getClassName())) {
                mi.setSelected(true);
            }
            lafRadioGroup.add(mi);
            menuLAFSubstance.add(mi);
        }
        menuLAF.add(menuLAFSubstance);
        return menuLAF;
    }

    private class LAFAction extends AbstractAction {

        private String className;
        private JFrame frame;

        public LAFAction(JFrame frame, String lafName, String className) {
            super(lafName);
            this.frame = frame;
            this.className = className;
        }

        protected void updateDecorationIfNeeded() {
            boolean lafSupportDecoration = UIManager.getLookAndFeel().getSupportsWindowDecorations();
            boolean frmUsesLAFDecoration = frame.isUndecorated();

            if (lafSupportDecoration != frmUsesLAFDecoration) {

                frame.setVisible(false);
                frame.dispose();

                if (lafSupportDecoration) {
                    frame.setUndecorated(true);
                    frame.getRootPane().setWindowDecorationStyle(javax.swing.JRootPane.FRAME);
                } else {
                    frame.setUndecorated(false);
                    frame.getRootPane().setWindowDecorationStyle(javax.swing.JRootPane.NONE);
                }

                frame.setVisible(true);
            }

        }

        public void actionPerformed(ActionEvent e) {
            try {
                LAFUtil.setLAF(className);
                frame.pack();
                GeneralSettings.getInstance().setLookandfeel(className);
            } catch (Exception exc) {
                exc.printStackTrace();
            }

            updateDecorationIfNeeded();
        }

    }

}
