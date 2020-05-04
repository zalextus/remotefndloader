package remotefndload;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class TableModelLdtEntityParams extends AbstractTableModel {

    private String[] columnNames = { "Prompt", "Name", "Optional", "Hint" };
    private ArrayList<LdtEntityParam> params;

    public TableModelLdtEntityParams(ArrayList<LdtEntityParam> params) {
        this.params = params;
    }

    public int getRowCount() {
        return params.size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        LdtEntityParam param = params.get(rowIndex);
        switch (columnIndex) {
        case 0:
            return param.getPrompt();
        case 1:
            return param.getName();
        case 2:
            return param.isOptional();
        case 3:
            return param.getHint();
        default:
            return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        LdtEntityParam param = params.get(rowIndex);
        switch (columnIndex) {
        case 0:
            param.setPrompt((String)aValue);
            break;
        case 1:
            param.setName((String)aValue);
            break;
        case 2:
            param.setOptional((Boolean)aValue);
            break;
        case 3:
            param.setHint((String)aValue);
            break;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
        case 2:
            return Boolean.class;
        default:
            return String.class;
        }
    }

    void addRow() {
        params.add(new LdtEntityParam());
    }

    void removeRow(int row) {
        params.remove(row);
    }
}
