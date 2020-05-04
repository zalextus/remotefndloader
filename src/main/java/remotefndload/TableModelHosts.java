package remotefndload;

import javax.swing.table.AbstractTableModel;

public class TableModelHosts extends AbstractTableModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Hosts hosts;
    private String[] columnNames =
    { "Prompt", "Host", "DB user (or env. var)", "DB password (or env. var)", "OS user (user.name if empty)", "OS password",
      "OS dirname", "Remote temp command file name", "OEBS env. commands", "Command" };

    public TableModelHosts(Hosts hosts) {
        this.hosts = hosts;
    }

//    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

//    @Override
    public int getRowCount() {
        return hosts.getHostsList().size();
    }

//    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Host host = hosts.getHostsList().get(rowIndex);
        switch (columnIndex) {
        case 0:
            return host.getPrompt();
        case 1:
            return host.getHostname();
        case 2:
            return host.getDbUser();
        case 3:
            return host.getDbPassword();
        case 4:
            return host.getOsUser();
        case 5:
            return host.getOsPassword();
        case 6:
            return host.getOsdirname();
        case 7:
            return host.getRemoteTempCommandFileName();
        case 8:
            return host.getEnvcmd();
        case 9:
            return host.getCommand();
        default:
            return null;
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    @Override
    public void setValueAt(Object val, int rowIndex, int columnIndex) {
        Host host = hosts.getHostsList().get(rowIndex);
        switch (columnIndex) {
        case 0:
                host.setPrompt((String) val);
                break;
        case 1:
                host.setHostname((String) val);
                break;
        case 2:
                host.setDbUser((String) val);
                break;
        case 3:
                host.setDbPassword((String) val);
                break;
        case 4:
                host.setOsUser((String) val);
                break;
        case 5:
                host.setOsPassword((String) val);
                break;
        case 6:
                host.setOsdirname((String) val);
                break;
        case 7:
                host.setRemoteTempCommandFileName((String) val);
                break;
        case 8:
                host.setEnvcmd((String) val);
                break;
        case 9:
                host.setCommand((String) val);
                break;
        }

    }

    public void addRow() {
       addRow(new Host("TEST", "test.example.com", "APPS", null,
               System.getProperty("user.name"), null, "/tmp", ". /etc/oebsenv", "/bin/bash", null));
    }

    public void removeRow(int row) {
        hosts.getHostsList().remove(row);
    }

    void addRow(Host host) {
        hosts.getHostsList().add(host);
    }
}
