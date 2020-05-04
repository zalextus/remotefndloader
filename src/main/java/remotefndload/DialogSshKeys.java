package remotefndload;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class DialogSshKeys extends JDialog {
    private JToolBar toolBar = new JToolBar();
    private BorderLayout borderLayout1 = new BorderLayout();
    private JScrollPane scrollPane = new JScrollPane();
    private JTable table = new JTable();
    private JButton buttonAdd = new JButton();
    private JButton buttonRemove = new JButton();

    public DialogSshKeys() {
        this(null, "Keys", false);
    }

    public DialogSshKeys(Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    }

    private void jbInit() throws Exception {
        this.setSize( new Dimension( 400, 300 ) );
        this.getContentPane().setLayout(borderLayout1);
        this.setModal(true);
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
        toolBar.add(buttonAdd, null);
        toolBar.add(buttonRemove, null);
        this.getContentPane().add(toolBar, BorderLayout.NORTH);
        scrollPane.getViewport().add(table, null);
        this.getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    void setSshKeys(SshKeys sshKeys) {
        TableModelSshKeys model = new TableModelSshKeys(sshKeys);
        table.setModel(model);
        table.getColumnModel().getColumn(1).setCellRenderer(new PasswordRenderer());
    }

    private void buttonAdd_actionPerformed(ActionEvent e) {
        TableModelSshKeys model = (TableModelSshKeys)table.getModel();
        model.addRow();
        model.fireTableDataChanged();
    }

    private void buttonDelete_actionPerformed(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row != -1) {
            TableModelSshKeys model = (TableModelSshKeys)table.getModel();
            row = table.convertRowIndexToModel(row);
            model.removeRow(row);
            model.fireTableDataChanged();
        }
    }
}
