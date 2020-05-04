package remotefndload;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DialogLdtEntities extends JDialog {
    private JSplitPane splitPane = new JSplitPane();
    private JScrollPane scrollEntities = new JScrollPane();
    private JPanel panelRight = new JPanel();
    private BorderLayout borderLayoutDetails = new BorderLayout();
    private JPanel panelGeneral = new JPanel();
    private GridBagLayout gridBagLayout = new GridBagLayout();
    private JLabel labelControlFile = new JLabel();
    private JTextField textControlFile = new JTextField();
    private JLabel labelEntityName = new JLabel();
    private JTextField textEntityName = new JTextField();
    private JTable tableEntities = new JTable();
    private JPanel panelParameters = new JPanel();
    private JScrollPane scrollParameters = new JScrollPane();
    private BorderLayout borderLayoutParameters = new BorderLayout();
    private JTable tableParameters = new JTable();
    private JPanel panelButtonsParams = new JPanel();
    private JButton buttonAddParam = new JButton();
    private JButton buttonRemoveParam = new JButton();
    private JPanel panelLeft = new JPanel();
    private BorderLayout borderLayoutEntities = new BorderLayout();
    private JPanel panelButtonsEntity = new JPanel();
    private JButton buttonAddEntity = new JButton();
    private JButton buttonRemoveEntity = new JButton();

    public DialogLdtEntities() {
        this(null, "Entities", false);
    }

    public DialogLdtEntities(Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tableEntities.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableEntities.getSelectionModel().addListSelectionListener(new TableEntitiesSelectionListener());
        tableParameters.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        textControlFile.getDocument().addDocumentListener(new DocumentListener() {

                public void insertUpdate(DocumentEvent e) {
                    updateLctfileFromTextField();
                }

                public void removeUpdate(DocumentEvent e) {
                    updateLctfileFromTextField();
                }

                private void updateLctfileFromTextField() {
                    LdtEntity ldtEntity = getSelectedLdtEntity();
                    ldtEntity.setLctfile(textControlFile.getText());
                }

                public void changedUpdate(DocumentEvent e) {
                }
            });
        textEntityName.getDocument().addDocumentListener(new DocumentListener() {

                public void insertUpdate(DocumentEvent e) {
                    updateEntityNameFromTextField();
                }

                public void removeUpdate(DocumentEvent e) {
                    updateEntityNameFromTextField();
                }

                private void updateEntityNameFromTextField() {
                    LdtEntity ldtEntity = getSelectedLdtEntity();
                    ldtEntity.setName(textEntityName.getText());
                }

                public void changedUpdate(DocumentEvent e) {
                }
            });
        setEnabledDetails(false);
    }

    private LdtEntity getSelectedLdtEntity() {
        int row = tableEntities.getSelectedRow();
        row = tableEntities.convertRowIndexToModel(row);
        TableModelLdtEntities model =
            (TableModelLdtEntities)tableEntities.getModel();
        LdtEntity ldtEntity =
            model.getLdtEntities().getLdtEntitiesList().get(row);
        return ldtEntity;
    }

    private void jbInit() throws Exception {
        this.setModal(true);

        buttonAddEntity.setText("Add");
        buttonAddEntity.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    buttonAddEntity_actionPerformed(e);
                }
            });
        buttonRemoveEntity.setText("Remove");

        buttonRemoveEntity.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    buttonRemoveEntity_actionPerformed(e);
                }
            });
        panelButtonsEntity.add(buttonAddEntity, null);
        panelButtonsEntity.add(buttonRemoveEntity, null);

        scrollEntities.getViewport().add(tableEntities, null);

        panelLeft.setLayout(borderLayoutEntities);
        panelLeft.setBorder(BorderFactory.createTitledBorder("Entities"));
        panelLeft.add(panelButtonsEntity, BorderLayout.NORTH);
        panelLeft.add(scrollEntities, BorderLayout.CENTER);

        panelRight.setLayout(borderLayoutDetails);
        panelGeneral.setLayout(gridBagLayout);
        panelGeneral.setBorder(BorderFactory.createTitledBorder("General"));
        labelControlFile.setText("Control file:");
        labelEntityName.setText("Entity name:");
        panelParameters.setLayout(borderLayoutParameters);
        panelParameters.setBorder(BorderFactory.createTitledBorder("Parameters"));
        panelButtonsParams.setLayout(null);
        panelButtonsParams.setPreferredSize(new Dimension(200, 30));
        buttonAddParam.setText("Add");
        buttonAddParam.setBounds(new Rectangle(0, 5, 95, 20));
        buttonAddParam.setSize(new Dimension(95, 23));
        buttonAddParam.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    buttonAddParam_actionPerformed(e);
                }
            });
        buttonRemoveParam.setText("Remove");
        buttonRemoveParam.setBounds(new Rectangle(100, 5, 95, 20));
        buttonRemoveParam.setSize(new Dimension(95, 23));
        buttonRemoveParam.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    buttonRemoveParam_actionPerformed(e);
                }
            });
        panelGeneral.add(labelControlFile,
                         new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                                                GridBagConstraints.WEST,
                                                GridBagConstraints.NONE,
                                                new Insets(0, 5, 0, 0), 0, 0));
        panelGeneral.add(textControlFile,
                         new GridBagConstraints(1, 0, 1, 1, 0.5, 0.0,
                                                GridBagConstraints.CENTER,
                                                GridBagConstraints.HORIZONTAL,
                                                new Insets(5, 5, 5, 5), 0, 0));
        panelGeneral.add(labelEntityName,
                         new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                                                GridBagConstraints.WEST,
                                                GridBagConstraints.NONE,
                                                new Insets(0, 5, 0, 0), 0, 0));
        panelGeneral.add(textEntityName,
                         new GridBagConstraints(1, 1, 1, 1, 0.5, 0.0,
                                                GridBagConstraints.CENTER,
                                                GridBagConstraints.HORIZONTAL,
                                                new Insets(0, 5, 5, 5), 0, 0));
        panelRight.add(panelGeneral, BorderLayout.NORTH);
        scrollParameters.getViewport().add(tableParameters, null);
        panelParameters.add(scrollParameters, BorderLayout.CENTER);
        panelButtonsParams.add(buttonAddParam,
                               new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                                                      GridBagConstraints.WEST,
                                                      GridBagConstraints.HORIZONTAL,
                                                      new Insets(0, 5, 5, 0),
                                                      0, 0));
        panelButtonsParams.add(buttonRemoveParam,
                               new GridBagConstraints(1, 0, 1, 1, 0.5, 0.0,
                                                      GridBagConstraints.CENTER,
                                                      GridBagConstraints.HORIZONTAL,
                                                      new Insets(0, 5, 5, 0),
                                                      0, 0));
        panelParameters.add(panelButtonsParams, BorderLayout.NORTH);
        panelRight.add(panelParameters, BorderLayout.CENTER);

        splitPane.add(panelLeft, JSplitPane.LEFT);
        splitPane.add(panelRight, JSplitPane.RIGHT);

        this.getContentPane().add(splitPane, BorderLayout.CENTER);
    }

    void setLdtEntities(LdtEntities ldtEntities) {
        TableModelLdtEntities model = new TableModelLdtEntities(ldtEntities);
        tableEntities.setModel(model);
    }

    private void buttonAddEntity_actionPerformed(ActionEvent e) {
        TableModelLdtEntities model =
            (TableModelLdtEntities)tableEntities.getModel();
        model.addRow();
        model.fireTableDataChanged();
    }

    private void buttonRemoveEntity_actionPerformed(ActionEvent e) {
        int row = tableEntities.getSelectedRow();
        if (row != -1) {
            TableModelLdtEntities model =
                (TableModelLdtEntities)tableEntities.getModel();
            row = tableEntities.convertRowIndexToModel(row);
            model.removeRow(row);
            model.fireTableDataChanged();
        }
    }

    private void buttonAddParam_actionPerformed(ActionEvent e) {
        TableModelLdtEntityParams model =
            (TableModelLdtEntityParams)tableParameters.getModel();
        model.addRow();
        model.fireTableDataChanged();

    }

    private void buttonRemoveParam_actionPerformed(ActionEvent e) {
        int row = tableParameters.getSelectedRow();
        if (row != -1) {
            TableModelLdtEntityParams model =
                (TableModelLdtEntityParams)tableParameters.getModel();
            row = tableParameters.convertRowIndexToModel(row);
            model.removeRow(row);
            model.fireTableDataChanged();
        }
    }

    private void setEnabledDetails(boolean flag) {
        textControlFile.setEnabled(flag);
        textEntityName.setEnabled(flag);
        buttonAddParam.setEnabled(flag);
        buttonRemoveParam.setEnabled(flag);
        tableParameters.setVisible(flag);
    }

    private class TableEntitiesSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting())
                return;

            int row = tableEntities.getSelectedRow();
            if (row == -1) {
                setEnabledDetails(false);
                return;
            }

            row = tableEntities.convertRowIndexToModel(row);
            TableModelLdtEntities model =
                (TableModelLdtEntities)tableEntities.getModel();
            LdtEntity ldtEntity =
                model.getLdtEntities().getLdtEntitiesList().get(row);
            textControlFile.setText(ldtEntity.getLctfile());
            textEntityName.setText(ldtEntity.getName());
            TableModelLdtEntityParams modelParams =
                new TableModelLdtEntityParams(ldtEntity.getParams());
            tableParameters.setModel(modelParams);

            setEnabledDetails(true);
        }

    }
}
