package remotefndload;

import javax.swing.table.DefaultTableCellRenderer;

public class PasswordRenderer extends DefaultTableCellRenderer {

    @Override
    protected void setValue(Object value) {
        if (value == null) {
            setText("");
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < ((String)value).length(); i++) {
                sb.append("*");
            }
            setText((value == null) ? "" : sb.toString());
        }
    }
}
