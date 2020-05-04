package remotefndload;

import javax.swing.table.AbstractTableModel;

public class TableModelSshKeys extends AbstractTableModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private SshKeys sshkeys;
    private String[] columnNames = { "Path to ssh key file", "Passphrase (if exists)" };

    public TableModelSshKeys(SshKeys sshkeys) {
        this.sshkeys = sshkeys;
    }

//    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

//    @Override
    public int getRowCount() {
        return sshkeys.getKeysList().size();
    }

//    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SshKey sshkey = sshkeys.getKeysList().get(rowIndex);
        switch (columnIndex) {
        case 0:
            return sshkey.getFilename();
        case 1:
            return sshkey.getPassphrase();
        }
        return null;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public boolean isCellEditable(int arg0, int arg1) {
        return true;
    }

    @Override
    public void setValueAt(Object val, int rowIndex, int columnIndex) {
        SshKey sshkey = sshkeys.getKeysList().get(rowIndex);
        switch (columnIndex) {
        case 0:
            sshkey.setFilename((String)val);
            break;
        case 1:
            sshkey.setPassphrase((String)val);
            break;
        }
    }

    public void addRow() {
        sshkeys.getKeysList().add(new SshKey());
    }

    public void removeRow(int row) {
        sshkeys.getKeysList().remove(row);
    }
}
