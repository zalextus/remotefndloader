package remotefndload;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.prefs.Preferences;


public class DialogHosts
        extends JDialog {
    private JToolBar toolBar = new JToolBar();
    private BorderLayout borderLayout1 = new BorderLayout();
    private JScrollPane scrollPane = new JScrollPane();
    private JTable table = new JTable();
    private JButton buttonAdd = new JButton();
    private JButton buttonRemove = new JButton();
    private JButton buttonImportPuttySessions = new JButton();

    public DialogHosts() {
        this(null, "Hosts", false);
    }

    public DialogHosts(Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void jbInit()
            throws Exception {
        this.setSize(new Dimension(400, 300));
        this.getContentPane().setLayout(borderLayout1);
        this.setModal(true);
        table.setFont(new Font("Tahoma", 0, 12));
        buttonAdd.setText("Add");
        buttonAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonAdd_actionPerformed(e);
            }
        });
        buttonRemove.setText("Remove");
        buttonRemove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonDelete_actionPerformed(e);
            }
        });
        buttonImportPuttySessions.setText("Import putty sessions");
        buttonImportPuttySessions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonImportPuttySessions_actionPerformed(e);
            }
        });
        toolBar.add(buttonAdd, null);
        toolBar.add(buttonRemove, null);
        toolBar.addSeparator();
        toolBar.add(buttonImportPuttySessions, null);
        this.getContentPane().add(toolBar, BorderLayout.NORTH);
        scrollPane.getViewport().add(table, null);
        this.getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    void setHosts(Hosts hosts) {
        TableModelHosts model = new TableModelHosts(hosts);
        table.setModel(model);
        PasswordRenderer passwordRenderer = new PasswordRenderer();

        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(2).setPreferredWidth(25);
        table.getColumnModel().getColumn(3).setPreferredWidth(30);
        table.getColumnModel().getColumn(4).setPreferredWidth(25);
        table.getColumnModel().getColumn(5).setPreferredWidth(30);
        //table.getColumnModel().getColumn(8).setPreferredWidth(100);

        table.getColumnModel().getColumn(3).setCellRenderer(passwordRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(passwordRenderer);
        //table.getColumnModel().getColumn(8).setCellRenderer(new StatusRendered());

        //StatusEditor statusEditor = new StatusEditor();
        //statusEditor.setDialog(this);
        //table.getColumnModel().getColumn(8).setCellEditor(statusEditor);
    }

    private void buttonAdd_actionPerformed(ActionEvent e) {
        TableModelHosts model = (TableModelHosts) table.getModel();
        model.addRow();
        model.fireTableDataChanged();
    }

    private void buttonDelete_actionPerformed(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row != -1) {
            TableModelHosts model = (TableModelHosts) table.getModel();
            row = table.convertRowIndexToModel(row);
            model.removeRow(row);
            model.fireTableDataChanged();
        }
    }

    private void buttonImportPuttySessions_actionPerformed(ActionEvent e) {
        try {
            importPuttySavedSessions();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    /**
     * Returns this java string as a null-terminated byte array
     */
    private static byte[] stringToByteArray(String str) {
        byte[] result = new byte[str.length() + 1];
        for (int i = 0; i < str.length(); i++) {
            result[i] = (byte) str.charAt(i);
        }
        result[str.length()] = 0;
        return result;
    }

    /**
     * Converts a null-terminated byte array to java string
     */
    private static String byteArrayToString(byte[] array) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < array.length - 1; i++) {
            result.append((char) array[i]);
        }
        return result.toString();
    }

    private void importPuttySavedSessions()
            throws Exception {
        int HKEY_CURRENT_USER = 0x80000001;
        int KEY_READ = 0x20019;
        int NATIVE_HANDLE = 0;
        int ERROR_CODE = 1;
        int ERROR_SUCCESS = 0;
        int SUBKEYS_NUMBER = 0;
        int MAX_KEY_LENGTH = 3;

        Preferences userPref = Preferences.userRoot();
        Class<? extends Preferences> clz = Preferences.userRoot().getClass();

        Method regOpenKey =
                clz.getDeclaredMethod("WindowsRegOpenKey", int.class, byte[].class,
                        int.class);
        regOpenKey.setAccessible(true);
        Method regCloseKey =
                clz.getDeclaredMethod("WindowsRegCloseKey", int.class);
        regCloseKey.setAccessible(true);
        Method regQueryInfoKey =
                clz.getDeclaredMethod("WindowsRegQueryInfoKey", int.class);
        regQueryInfoKey.setAccessible(true);
        Method regEnumKeyEx =
                clz.getDeclaredMethod("WindowsRegEnumKeyEx", int.class, int.class,
                        int.class);
        regEnumKeyEx.setAccessible(true);
        Method regQueryValueEx =
                clz.getDeclaredMethod("WindowsRegQueryValueEx", int.class,
                        byte[].class);
        regQueryValueEx.setAccessible(true);

        String keyName = "Software\\SimonTatham\\PuTTY\\Sessions";
        int[] ret =
                (int[]) regOpenKey.invoke(userPref, HKEY_CURRENT_USER, stringToByteArray(keyName),
                        KEY_READ);
        int handle = ret[NATIVE_HANDLE];
        if (ret[ERROR_CODE] == ERROR_SUCCESS) {
            int[] keyInfo = (int[]) regQueryInfoKey.invoke(userPref, handle);
            int maxKeyLength = keyInfo[MAX_KEY_LENGTH];
            int subKeysNumber = keyInfo[SUBKEYS_NUMBER];
            TableModelHosts model = (TableModelHosts) table.getModel();
            for (int i = 0; i < subKeysNumber; i++) {
                byte[] key =
                        (byte[]) regEnumKeyEx.invoke(userPref, handle, i, maxKeyLength +
                                1);
                String keyStr = byteArrayToString(key);
                String subKeyName =
                        "Software\\SimonTatham\\PuTTY\\Sessions\\" + keyStr;
                ret =
                        (int[]) regOpenKey.invoke(userPref, HKEY_CURRENT_USER, stringToByteArray(subKeyName),
                                KEY_READ);
                if (ret[ERROR_CODE] == ERROR_SUCCESS) {
                    int subHandle = ret[NATIVE_HANDLE];
                    byte[] hostname =
                            (byte[]) regQueryValueEx.invoke(userPref, subHandle,
                                    stringToByteArray("HostName"));
                    String hostnameStr = byteArrayToString(hostname);
                    byte[] username =
                            (byte[]) regQueryValueEx.invoke(userPref, subHandle,
                                    stringToByteArray("UserName"));
                    String usernameStr = byteArrayToString(username);
                    Host host =
                            new Host(keyStr, hostnameStr, "APPS", "", usernameStr, "", "/tmp",
                                    "", "FNDLOAD", "");
                    model.addRow(host);
                }
            }
            regCloseKey.invoke(userPref, handle);
            model.fireTableDataChanged();
        }

    }
}
