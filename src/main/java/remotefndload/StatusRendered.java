package remotefndload;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.Serializable;

public class StatusRendered implements TableCellRenderer, Serializable {

    private JButton button = null;

    public StatusRendered() {
        button = new JButton();
        button.setFont(new Font(Font.DIALOG, Font.PLAIN ,9));
    }

//    @Override
    public Component getTableCellRendererComponent(JTable jTable, Object value, boolean b, boolean b2, int i, int i2) {
        button.setText((String)value);
        button.setToolTipText((String)value);
        return button;
    }
}
