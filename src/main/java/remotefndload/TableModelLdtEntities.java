package remotefndload;

import javax.swing.table.AbstractTableModel;

public class TableModelLdtEntities extends AbstractTableModel {
    
    private LdtEntities ldtEntities;
    private String[] columnNames = {"Prompt"};

    public TableModelLdtEntities(LdtEntities ldtEntities) {
        this.ldtEntities = ldtEntities;
    }

    public int getRowCount() {
        return ldtEntities.getLdtEntitiesList().size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        LdtEntity ldtEntity = ldtEntities.getLdtEntitiesList().get(rowIndex);
        return ldtEntity.getPrompt();
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        LdtEntity ldtEntity = ldtEntities.getLdtEntitiesList().get(rowIndex);
        ldtEntity.setPrompt((String)aValue);
    }

    public void addRow() {
        ldtEntities.getLdtEntitiesList().add(new LdtEntity());
    }

    public void removeRow(int row) {
        ldtEntities.getLdtEntitiesList().remove(row);
    }

    public LdtEntities getLdtEntities() {
        return ldtEntities;
    }
}
