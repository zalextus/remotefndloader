package remotefndload;


import com.jcraft.jsch.*;
import com.jcraft.jsch.agentproxy.AgentProxyException;
import com.jcraft.jsch.agentproxy.Connector;
import com.jcraft.jsch.agentproxy.ConnectorFactory;
import com.jcraft.jsch.agentproxy.RemoteIdentityRepository;
import remotefndload.action.DownloadAction;
import remotefndload.action.LoaderAction;
import remotefndload.action.UploadAction;
import remotefndload.history.HistoryMenuAction;
import remotefndload.wf.sort.WfSortPanel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FrameMain extends JFrame {
    private BorderLayout layoutMain = new BorderLayout();
    private JPanel panelCenter = new JPanel();
    private JMenuBar menuBar = new JMenuBar();
    private JMenu menuFile = new JMenu();
    private JMenuItem menuFileExit = new JMenuItem();
    private JMenu menuHelp = new JMenu();
    private JMenuItem menuHelpAbout = new JMenuItem();
    private JLabel statusBar = new JLabel();
    private JMenu menuSettings = new JMenu();
    private JMenuItem menuSettingsHosts = new JMenuItem();
    private JMenuItem menuSettingsEntities = new JMenuItem();
    private JMenuItem menuSettingsSshKeys = new JMenuItem();
    private GeneralSettings generalSettings;
    private SshKeys sshKeys;
    private Hosts hosts;
    private NlsLangs nlsLangs;
    private LdtEntities ldtEntities;
    private JSplitPane splitPane = new JSplitPane();
    private BorderLayout borderLayoutHostsEntities = new BorderLayout();
    private JScrollPane scrollEntities = new JScrollPane();
    private JList listEntities = new JList();
    private JTabbedPane tabbedPane = new JTabbedPane();
    private JPanel panelLeft = new JPanel();
    private BorderLayout borderLayoutEntities = new BorderLayout();
    private JComboBox comboHosts = new JComboBox();
    private JPanel panelDownload = new JPanel();
    private JPanel panelUpload = new JPanel();
    private JPanel panelSelectUploadLdtFile = new JPanel();
    private BorderLayout borderLayout1 = new BorderLayout();
    private JButton buttonUploadBrowse = new JButton();
    private JTextField textUploadLdtFile = new JTextField();
    private JPanel panelUploadButton = new JPanel();
    private JButton buttonUpload = new JButton();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private BorderLayout borderLayout2 = new BorderLayout();
    private JPanel panelDownloadButton = new JPanel();
    private JButton buttonDownload = new JButton();
    private JPanel panelSelectDownloadLdtFile = new JPanel();
    private GridBagLayout gridBagLayout2 = new GridBagLayout();
    private JTextField textDownloadLdtFile = new JTextField();
    private JButton buttonDownloadBrowse = new JButton();
    private JPanel panelParamsLog = new JPanel();
    private GridBagLayout gridBagLayout3 = new GridBagLayout();
    private JScrollPane scrollParameters = new JScrollPane();
    private JPanel panelDownloadParameters = new JPanel();
    private JScrollPane scrollDownloadLog = new JScrollPane();
    private JTextArea textDownloadLog = new JTextArea();
    private JScrollPane scrollUploadLog = new JScrollPane();
    private JTextArea textUploadLog = new JTextArea();
    private GridBagLayout gridBagLayout4 = new GridBagLayout();
    private JTabbedPane tabbedDownloadLogs = new JTabbedPane();
    private JPanel panelDownloadLog = new JPanel();
    private BorderLayout borderLayout3 = new BorderLayout();
    private JPanel panelDownloadFndloadLog = new JPanel();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JTextArea textDownloadFndloadLog = new JTextArea();
    private BorderLayout borderLayout4 = new BorderLayout();
    private JTabbedPane tabbedUploadLogs = new JTabbedPane();
    private JPanel panelUploadLog = new JPanel();
    private JPanel panelUploadFndloadLog = new JPanel();
    private BorderLayout borderLayout5 = new BorderLayout();
    private JScrollPane scrollFndUploadLog = new JScrollPane();
    private JTextArea textUploadFndloadLog = new JTextArea();
    private BorderLayout borderLayout6 = new BorderLayout();
    private JLabel labelLanguage = new JLabel();
    private JComboBox comboLanguage = new JComboBox();
    private JLabel labelDownloadFile = new JLabel();
    private JMenuItem menuSettingsNlsLangs = new JMenuItem();
    private JCheckBox chkCustomModeForce = new JCheckBox();
    private JCheckBox chkUploadMode = new JCheckBox();
    private JComboBox cmbUploadModeParam = new JComboBox();
    private JMenu menuTools = new JMenu();
    private JMenuItem menuToolsWfSort = new JMenuItem();
    private JLabel labelLanguageUpload = new JLabel();
    private JComboBox comboLanguageUpload = new JComboBox();
    private JLabel labelUploadFile = new JLabel();
    private JPanel panelUploadModes = new JPanel();
    private GridBagLayout gridBagLayout5 = new GridBagLayout();
    private JLabel labelCustomModeForce = new JLabel();
    private JLabel labelLangUploadTip = new JLabel();
    private JMenu menuFileHistory = new JMenu();
    //private Charset charset = Charset.forName("8859_5");
    //private CharsetEncoder encoder = charset.newEncoder();
    private Pattern patternLogFilename = Pattern.compile(".*?: (L\\d+.log)", Pattern.MULTILINE);

    public FrameMain() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            generalSettings = GeneralSettings.getInstance();
            //textDownloadLdtFile.setText(generalSettings.getLastUsedDir());
            //textUploadLdtFile.setText(generalSettings.getLastUsedDir());
        } catch (Exception e) {
            e.printStackTrace();
        }        
        try {
            sshKeys = new SshKeys();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            hosts = new Hosts();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            nlsLangs = new NlsLangs();
        } catch (Exception e) {
            e.printStackTrace();
        }        
        try {
            ldtEntities = new LdtEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        LAFUtil lafUtil = new LAFUtil();
        menuSettings.add(lafUtil.createLAFMenu(this));
        
        this.setPreferredSize(new Dimension(850, 650));

        DefaultListModel listModel = new DefaultListModel();
        for (LdtEntity e : ldtEntities.getLdtEntitiesList()) {
            listModel.addElement(e);
        }
        listEntities.setModel(listModel);
        comboHosts.setModel(new DefaultComboBoxModel(hosts.getHostsList().toArray()));
        textDownloadLog.setEditable(false);
        textUploadLog.setEditable(false);
        textDownloadFndloadLog.setEditable(false);
        listEntities.getSelectionModel().addListSelectionListener(new ListEntitiesSelectionListener());
        setEnabledParams(false);
        Object[] nlsLangArray = nlsLangs.getNlsLangsList().toArray();
        comboLanguage.setModel(new DefaultComboBoxModel(nlsLangArray));
        Object[] nlsLangArrayUpload = new Object[nlsLangArray.length + 1];
        nlsLangArrayUpload[0] = new NlsLang("", ""); // ��� Upload ����� �� ��������� NLS_LANG
        for (int i = 1; i < nlsLangArrayUpload.length; i++)
            nlsLangArrayUpload[i] = nlsLangArray[i-1];

        comboLanguageUpload.setModel(new DefaultComboBoxModel(nlsLangArrayUpload));
        cmbUploadModeParam.addItem("NLS");
        cmbUploadModeParam.addItem("REPLACE");
        cmbUploadModeParam.addItem("INSTALL");
    }

    private void jbInit() throws Exception {
        this.setJMenuBar(menuBar);
        this.getContentPane().setLayout(layoutMain);
        panelCenter.setLayout(borderLayoutHostsEntities);
        this.setTitle("RemoteFndLoader, 2.7");
        this.setSize(new Dimension(594, 345));
        this.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    this_windowClosing(e);
                }
            });
        menuFile.setText("File");
        menuFileExit.setText("Exit");
        menuFileExit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    fileExit_ActionPerformed(ae);
                }
            });
        menuHelp.setText("Help");
        menuHelpAbout.setText("About...");
        menuHelpAbout.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    helpAbout_ActionPerformed(ae);
                }
            });
        statusBar.setText("");
        menuSettings.setText("Settings");
        menuSettingsHosts.setText("Hosts...");
        menuSettingsHosts.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    menuSettingsHosts_actionPerformed(e);
                }
            });
        menuSettingsEntities.setText("Entities...");
        menuSettingsEntities.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    menuSettingsEntities_actionPerformed(e);
                }
            });
        menuSettingsSshKeys.setText("Ssh keys...");
        menuSettingsSshKeys.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    menuSettingsSshKeys_actionPerformed(e);
                }
            });
        listEntities.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelLeft.setLayout(borderLayoutEntities);
        panelDownload.setLayout(borderLayout2);
        panelUpload.setLayout(borderLayout1);
        panelSelectUploadLdtFile.setLayout(gridBagLayout1);
        panelSelectUploadLdtFile.setBorder(BorderFactory.createTitledBorder("Ldt file"));
        buttonUploadBrowse.setText("Browse...");
        buttonUploadBrowse.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    buttonUploadBrowse_actionPerformed(e);
                }
            });
        buttonUpload.setText("Upload");
        buttonUpload.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    buttonUpload_actionPerformed(e);
                }
            });
        buttonDownload.setText("Download");
        buttonDownload.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    buttonDownload_actionPerformed(e);
                }
            });
        panelSelectDownloadLdtFile.setLayout(gridBagLayout2);
        DocumentListener ldtFileListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    trySelectLdtEntity(new File(e.getDocument().getText(0, e.getDocument().getLength())));
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    trySelectLdtEntity(new File(e.getDocument().getText(0, e.getDocument().getLength())));
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        };
        textUploadLdtFile.getDocument().addDocumentListener(ldtFileListener);
        textDownloadLdtFile.getDocument().addDocumentListener(ldtFileListener);
//        textDownloadLdtFile.addKeyListener(new KeyAdapter() {
//            public void keyReleased(KeyEvent e) {
//                textDownloadLdtFile_keyReleased(e);
//            }
//        });
        buttonDownloadBrowse.setText("Browse...");
        buttonDownloadBrowse.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    buttonDownloadBrowse_actionPerformed(e);
                }
            });
        panelParamsLog.setLayout(gridBagLayout3);
        scrollParameters.setBorder(BorderFactory.createTitledBorder("Parameters"));
        panelDownloadParameters.setLayout(gridBagLayout4);
        panelDownloadLog.setLayout(borderLayout3);
        panelDownloadFndloadLog.setLayout(borderLayout4);
        panelUploadLog.setLayout(borderLayout5);
        panelUploadFndloadLog.setLayout(borderLayout6);
        labelLanguage.setText("Language");
        labelDownloadFile.setText("Ldt file");
    menuSettingsNlsLangs.setText("Nls langs...");
    menuSettingsNlsLangs.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          menuSettingsNlsLangs_actionPerformed(e);
        }
      });
    chkCustomModeForce.setText("CUSTOM_MODE");
    chkUploadMode.setText("UPLOAD_MODE");
        menuTools.setText("Tools");
        menuToolsWfSort.setText("Workflow definition sort");
        menuToolsWfSort.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menuToolsWfSort_actionPerformed(e);
            }
        });
        labelLanguageUpload.setText("Language");
        labelUploadFile.setText("Ldt file");
        panelUploadModes.setBorder(BorderFactory.createTitledBorder("Modes"));
        panelUploadModes.setLayout(gridBagLayout5);
        labelCustomModeForce.setText("FORCE");
        labelLangUploadTip.setText("Required if ldt-file encoding does not match DB encoding.");
        labelLangUploadTip.setText("Note: Use language if LDT-file encoding doesn't match DB encoding.");
        menuFileHistory.setText("History");
        menuFileHistory.setActionCommand("menuFileHistory");
        menuFile.add(menuFileHistory);
        menuFile.addSeparator();
        menuFile.add(menuFileExit);
        menuBar.add(menuFile);
        menuSettings.add(menuSettingsHosts);
        menuSettings.add(menuSettingsEntities);
        menuSettings.add(menuSettingsSshKeys);
        menuSettings.add(menuSettingsNlsLangs);
        menuBar.add(menuSettings);
        menuTools.add(menuToolsWfSort);
        menuBar.add(menuTools);
        menuHelp.add(menuHelpAbout);
        menuBar.add(menuHelp);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        scrollEntities.getViewport().add(listEntities, null);
        panelLeft.add(scrollEntities, BorderLayout.CENTER);
        panelLeft.add(comboHosts, BorderLayout.NORTH);
        panelSelectDownloadLdtFile.add(textDownloadLdtFile,
                                       new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                                                              GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0,
                                                              0));
        panelSelectDownloadLdtFile.add(buttonDownloadBrowse,
                                       new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                                                              GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        panelSelectDownloadLdtFile.add(labelLanguage,
                                       new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                                                              GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        panelSelectDownloadLdtFile.add(comboLanguage,
                                       new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                                                              GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0,
                                                              0));
        panelSelectDownloadLdtFile.add(labelDownloadFile,
                                       new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                                                              GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        panelDownloadButton.add(buttonDownload, null);
        panelDownload.add(panelDownloadButton, BorderLayout.SOUTH);
        panelDownload.add(panelSelectDownloadLdtFile, BorderLayout.NORTH);
        scrollParameters.getViewport().add(panelDownloadParameters, null);
        panelParamsLog.add(scrollParameters,
                           new GridBagConstraints(0, 0, 1, 1, 1.0, 0.5, GridBagConstraints.NORTH,
                                                  GridBagConstraints.BOTH, new Insets(5, 0, 5, 0), 0, 0));
        scrollDownloadLog.getViewport().add(textDownloadLog, null);
        panelDownloadLog.add(scrollDownloadLog);
        tabbedDownloadLogs.addTab("Download log", panelDownloadLog);
        jScrollPane1.getViewport().add(textDownloadFndloadLog, BorderLayout.CENTER);
        panelDownloadFndloadLog.add(jScrollPane1, BorderLayout.CENTER);
        tabbedDownloadLogs.addTab("FNDLOAD log", panelDownloadFndloadLog);
        panelParamsLog.add(tabbedDownloadLogs,
                           new GridBagConstraints(0, 1, 1, 1, 1.0, 0.5, GridBagConstraints.CENTER,
                                                  GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        panelDownload.add(panelParamsLog, BorderLayout.CENTER);
        panelSelectUploadLdtFile.add(buttonUploadBrowse,
                                     new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                                                            GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        panelSelectUploadLdtFile.add(textUploadLdtFile,
                                     new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                                                            GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0,
                                                            0));
        panelSelectUploadLdtFile.add(labelLanguageUpload,
                                     new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                                                            GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
        panelSelectUploadLdtFile.add(comboLanguageUpload,
                                     new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                                                            GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0,
                                                            0));
        panelSelectUploadLdtFile.add(labelUploadFile,
                                     new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                                                            GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        panelUploadModes.add(chkCustomModeForce,
                             new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                                                    GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
        panelUploadModes.add(chkUploadMode,
                             new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                                                    GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
        panelUploadModes.add(cmbUploadModeParam,
                             new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                                                    GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
        panelUploadModes.add(labelCustomModeForce,
                             new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                                                    GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
        panelSelectUploadLdtFile.add(panelUploadModes,
                                     new GridBagConstraints(0, 3, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
                                                            GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));
        panelSelectUploadLdtFile.add(labelLangUploadTip,
                                     new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
                                                            GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
        panelUpload.add(panelSelectUploadLdtFile, BorderLayout.NORTH);
        panelUploadButton.add(buttonUpload, null);
        panelUpload.add(panelUploadButton, BorderLayout.SOUTH);
        scrollUploadLog.getViewport().add(textUploadLog, null);
        panelUploadLog.add(scrollUploadLog, BorderLayout.CENTER);
        tabbedUploadLogs.addTab("Upload log", panelUploadLog);
        scrollFndUploadLog.getViewport().add(textUploadFndloadLog, null);
        panelUploadFndloadLog.add(scrollFndUploadLog, BorderLayout.CENTER);
        tabbedUploadLogs.addTab("FNDLOAD log", panelUploadFndloadLog);
        panelUpload.add(tabbedUploadLogs, BorderLayout.CENTER);
        tabbedPane.addTab("Download", panelDownload);
        tabbedPane.addTab("Upload", panelUpload);
        splitPane.add(panelLeft, JSplitPane.LEFT);
        splitPane.add(tabbedPane, JSplitPane.RIGHT);
        panelCenter.add(splitPane, BorderLayout.CENTER);
        this.getContentPane().add(panelCenter, BorderLayout.CENTER);
    }

    void fileExit_ActionPerformed(ActionEvent e) {
        exitApp();
    }

    void helpAbout_ActionPerformed(ActionEvent e) {
        FrameMain_AboutBoxPanel aboutDialog = new FrameMain_AboutBoxPanel();
        JOptionPane.showMessageDialog(this, aboutDialog,
                                      "About", JOptionPane.PLAIN_MESSAGE);
    }

    private void this_windowClosing(WindowEvent e) {
        exitApp();
    }

    private void exitApp() {
        try {
            generalSettings.save();
        } catch (Exception e) {
            e.printStackTrace();
        }        
        try {
            sshKeys.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            hosts.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ldtEntities.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
      try {
          nlsLangs.save();
      } catch (Exception e) {
          e.printStackTrace();
      }

        System.exit(0);
    }

    private void menuSettingsSshKeys_actionPerformed(ActionEvent e) {
        DialogSshKeys d = new DialogSshKeys();
        d.setSshKeys(sshKeys);
        d.setSize(780, 580);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }

    private void menuSettingsHosts_actionPerformed(ActionEvent e) {
        Host host = (Host)comboHosts.getSelectedItem();
        
        DialogHosts d = new DialogHosts();
        d.setHosts(hosts);
        d.setSize(850, 580);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
        
        // After update refill combo box with updated hosts
        comboHosts.setModel(new DefaultComboBoxModel(hosts.getHostsList().toArray()));
        comboHosts.setSelectedItem(host);
    }

    private void menuSettingsEntities_actionPerformed(ActionEvent e) {
        LdtEntity ldtEntity = (LdtEntity)listEntities.getSelectedValue();
        
        DialogLdtEntities d = new DialogLdtEntities();
        d.setLdtEntities(ldtEntities);
        d.setSize(780, 580);
        d.setLocationRelativeTo(this);
        d.setVisible(true);

        // After update refill list box with updated entities
        DefaultListModel listModel = new DefaultListModel();
        for (LdtEntity obj : ldtEntities.getLdtEntitiesList()) {
            listModel.addElement(obj);
        }
        listEntities.setModel(listModel);
        listEntities.setSelectedValue(ldtEntity, true);
    }

    private void buttonUploadBrowse_actionPerformed(ActionEvent e) {
        File file = chooseLdtFile(false);
        if (file != null) {
            textUploadLdtFile.setText(file.getAbsolutePath());
            trySelectLdtEntity(file);
        }
    }

    private void trySelectLdtEntity(File file) {
        if (file.exists()) {
            Pattern p = Pattern.compile("@\\w+:patch/\\d+/import/\\w+\\.lct");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
                String line = null;
                for (int lineNo = 0; lineNo < 100; lineNo++) {
                    line = br.readLine();
                    if (line == null) break;
                    Matcher m = p.matcher(line);
                    if (m.find()) {
                        String lctFilename = m.group();
                        ListModel model = listEntities.getModel();
                        int size = model.getSize();
                        for (int i = 0; i < size; i++) {
                            LdtEntity ldtEntity = (LdtEntity) model.getElementAt(i);
                            if (lctFilename.equals(ldtEntity.getLctfile())) {
                                listEntities.setSelectedIndex(i);
                                break;
                            }
                        }
                        break;
                    }
                }
            } catch (Exception ex) {
                System.err.println(ex);
            }
        }
    }

    private File chooseLdtFile(boolean isDownload) {
        JFileChooser chooser = new JFileChooser();
        File lastUsedDir = generalSettings.getLastUsedDir();
        if (lastUsedDir != null) chooser.setCurrentDirectory(lastUsedDir);
        chooser.setFileFilter(new FileFilter() {

                public boolean accept(File f) {
                    return f.isDirectory() ||
                        f.getName().toLowerCase().endsWith(".ldt");
                }

                public String getDescription() {
                    return "FNDLOAD ldt files";
                }
            });
        
        File selectedFile = null;
        
        if (isDownload)
            selectedFile = JFileChooser.APPROVE_OPTION == chooser.showSaveDialog(this) ? chooser.getSelectedFile() : null;
        else
            selectedFile = JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(this) ? chooser.getSelectedFile() : null;

        if (selectedFile != null)
            generalSettings.setLastUsedDir(selectedFile.getParentFile());
        return selectedFile;
    }

    private void buttonDownloadBrowse_actionPerformed(ActionEvent e) {
        File file = chooseLdtFile(true);
        if (file != null) {
            textDownloadLdtFile.setText(file.getAbsolutePath());
            appendLdtExtensionIfNeeded();
            trySelectLdtEntity(file);
        }
    }

    private void buttonDownload_actionPerformed(ActionEvent e) {
        textDownloadLog.setText("");
        textDownloadFndloadLog.setText("");
        Host host = (Host)comboHosts.getSelectedItem();
        LdtEntity ldtEntity = (LdtEntity)listEntities.getSelectedValue();
        
        String ldtfileName = textDownloadLdtFile.getText();
        if (ldtfileName == null || ldtfileName.isEmpty()) {
            try {
                ldtfileName = File.createTempFile("tmp", ".ldt", generalSettings.getLastUsedDir()).getAbsolutePath();
            } catch (IOException f) {
                ldtfileName = ldtEntity.getName() + ".ldt";
            }
            textDownloadLdtFile.setText(ldtfileName);
        }            
        File ldtFile = new File(ldtfileName);
        ldtfileName = ldtFile.getName();
        textDownloadLdtFile.setText(ldtFile.getAbsolutePath());
        ldtFile = new File(textDownloadLdtFile.getText());
        
        File parentFile = ldtFile.getParentFile();
        if (parentFile != null) generalSettings.setLastUsedDir(parentFile);

        StringBuilder command = new StringBuilder();
//        String envcmd = host.getEnvcmd();
//        if (envcmd == null || envcmd.length() == 0) {
//          envcmd = "$(grep '\\.env' .profile | sed \"s/.*='\\(.*\\)'/\\1/\")";
//        }
//        if (envcmd != null && envcmd.length() > 0) {
//            command.append(envcmd);
//            command.append("\n");
//        }
        command.append("cd " + host.getOsdirname());
        command.append("\n");
        
        NlsLang nlsLang = (NlsLang)comboLanguage.getSelectedItem();
        String executable = host.getCommand();
//        if (nlsLang != null && ((executable == null || executable.length() == 0)) ) {
//            command.append("export NLS_LANG=" + nlsLang.getOracleEncoding());
//            command.append("\n");
//            command.append("NLS_LANG=" + nlsLang.getOracleEncoding() + " ");
//        }

//        command.append(String.format("%s %s/%s 0 Y DOWNLOAD %s '%s' %s",
//                                     executable == null || executable.length() == 0 ?
//                                        "FNDLOAD" : executable + " " + "\"" + nlsLang.getOracleEncoding() + "\"",
//                                     host.getDbUser(), host.getDbPassword(),
//                                     ldtEntity.getLctfile(), ldtfileName,
//                                     ldtEntity.getName()));
        String remoteTempCommandFileName = host.getRemoteTempCommandFileName();
        if (remoteTempCommandFileName == null || remoteTempCommandFileName.trim().length() == 0) {
            try {
                remoteTempCommandFileName = File.createTempFile("fndload_wrapper", ".sh").getName();
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(this, e1.getMessage());
                return;
            }
        }

        command.append(String.format("%s %s %s '%s' %s",
                executable,
                remoteTempCommandFileName,
                ldtEntity.getLctfile(),
                ldtfileName,
                ldtEntity.getName()));

        for (Component c : panelDownloadParameters.getComponents()) {
            if (c instanceof JTextField) {
                String name = c.getName();
                String value = ((JTextField)c).getText();

                /*try {
                    ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(value));
                    value = new String(bbuf.array());
                } catch (CharacterCodingException f) {
                    JOptionPane.showMessageDialog(this, f.getMessage());
                }*/

                if (nlsLang != null && nlsLang.getJavaEncoding() != null && nlsLang.getJavaEncoding().length() > 0 &&
                  !Charset.defaultCharset().name().equalsIgnoreCase(nlsLang.getJavaEncoding())) {
                  try {
                      byte[] bytes = value.getBytes(nlsLang.getJavaEncoding());
                      value = new String(bytes);
                  } catch (UnsupportedEncodingException f) {
                      JOptionPane.showMessageDialog(this, f.getMessage());
                  }
                }

                command.append(" ");
                command.append(name);
                command.append("='");
                command.append(value);
                command.append("'");
            }
        }

        Cursor tempCursor = getCursor();
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            executeDownloadCommand(command.toString(), remoteTempCommandFileName);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }

        LoaderAction loaderAction = createActionFromUi("DOWNLOAD");
        addActionToHistoryMenu(loaderAction);
        
        setCursor(tempCursor);
    }

    private void executeDownloadCommand(String command, String remoteTempCommandFileName) throws Exception {
        textDownloadLog.append(command + "\n");

        JSch jsch = new JSch();
        for (SshKey k : sshKeys.getKeysList()) {
            jsch.addIdentity(k.getFilename(), k.getPassphrase());
        }
        jsch.setKnownHosts("known_hosts");
        Host host = (Host)comboHosts.getSelectedItem();
        String osUser = host.getOsUser() == null || host.getOsUser().trim().length() == 0 ? System.getProperty("user.name") : host.getOsUser();
        Session session = jsch.getSession(osUser, host.getHostname());
        session.setPassword(host.getOsPassword());
        session.setConfig("PreferredAuthentications", "publickey");
        Connector con = null;

        try {
            ConnectorFactory cf = ConnectorFactory.getDefault();
            con = cf.createConnector();
        }
        catch(AgentProxyException e){
            System.out.println(e);
        }

        if(con != null ){
            IdentityRepository irepo = new RemoteIdentityRepository(con);
            jsch.setIdentityRepository(irepo);
        }
//        session.setConfig("StrictHostKeyChecking", "no");
        session.setUserInfo(new MyUserInfo());
        session.connect();

        NlsLang nlsLang = (NlsLang)comboLanguage.getSelectedItem();

        String remoteCommandFileContent = host.getEnvcmd() + "\n" +
                "NLS_LANG=" + nlsLang.getOracleEncoding() + " FNDLOAD " + host.getDbUser() + "/" + host.getDbPassword() + " 0 Y DOWNLOAD $*\n";

        scpTo(session, remoteTempCommandFileName, remoteCommandFileContent.getBytes());

        Channel channel = session.openChannel("exec");
        ((ChannelExec)channel).setPty(true);
        ((ChannelExec)channel).setCommand(command);
        channel.setInputStream(null);
//        FileOutputStream fos=new FileOutputStream("c:/sasha/tmp/stderr");
//        ((ChannelExec)channel).setErrStream(fos);
        ((ChannelExec)channel).setErrStream(System.err);
        channel.setOutputStream(System.out);

        InputStream inputStream = channel.getInputStream();

        channel.connect();

//        BufferedReader brErr;
//        if (nlsLang != null && nlsLang.getJavaEncoding() != null && nlsLang.getJavaEncoding().length() > 0 &&
//            !Charset.defaultCharset().name().equalsIgnoreCase(nlsLang.getJavaEncoding())) {
//            brErr = new BufferedReader(new InputStreamReader(errorStream, nlsLang.getJavaEncoding()));
//        } else {
//            brErr = new BufferedReader(new InputStreamReader(errorStream));
//        }
//
//        do {
//            String line = brErr.readLine();
//            if (line == null) break;
//            textDownloadLog.append(line + "\n");
//        } while (true);

//        do {
//            String line = br.readLine();
//            if (line == null)
//                break;
//
//            Pattern p = Pattern.compile(".*?: (L\\d+.log)");
//            Matcher m = p.matcher(line);
//            if (m.matches()) {
//                logfilename = m.group(1);
//            }
//            textDownloadLog.append(line + "\n");
//        } while (true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int exitStatus = 0;
        byte[] tmp=new byte[1024];
        while(true){
            while(inputStream.available()>0){
                int i=inputStream.read(tmp, 0, 1024);
                if(i<0)break;

                baos.write(tmp, 0, i);
            }
            if(channel.isClosed()){
                if(inputStream.available()>0) continue;
                exitStatus = channel.getExitStatus();
                break;
            }
            try{Thread.sleep(1000);}catch(Exception ee){}
        }
        channel.disconnect();

        String osdirname = host.getOsdirname();
        if (!osdirname.endsWith("/")) osdirname = osdirname.concat("/");

        String commandOutputText = null;
        if (nlsLang != null && nlsLang.getJavaEncoding() != null && nlsLang.getJavaEncoding().length() > 0 &&
                !Charset.defaultCharset().name().equalsIgnoreCase(nlsLang.getJavaEncoding())) {
            commandOutputText = new String(baos.toByteArray(), nlsLang.getJavaEncoding());
        } else {
            commandOutputText = new String(baos.toByteArray());
        }

        textDownloadLog.append(commandOutputText + "\n");

        if (exitStatus != 0) {
            textDownloadLog.append("!!!Exit status:" + exitStatus + "\n");
        }

        String logfilename = null;
        Matcher m = patternLogFilename.matcher(commandOutputText);
        if (m.find()) {
            logfilename = m.group(1);
        }

        if (logfilename != null) {
            scpFrom(session, logfilename, logfilename);
            File fileLog = new File(logfilename);
            InputStreamReader is = null;
            if (nlsLang != null && nlsLang.getJavaEncoding() != null && nlsLang.getJavaEncoding().length() > 0 && 
             !Charset.defaultCharset().name().equalsIgnoreCase(nlsLang.getJavaEncoding())) {
              is = new InputStreamReader(new FileInputStream(fileLog), nlsLang.getJavaEncoding());
            } else {
              is = new InputStreamReader(new FileInputStream(fileLog));
            }
            char[] ca = new char[1024];
            int len;
            while ((len = is.read(ca)) != -1) {
                textDownloadFndloadLog.append(new String(ca, 0, len));
            }
            is.close();
            fileLog.delete();
            tabbedDownloadLogs.setTitleAt(1, logfilename);
            
            // Delete log file on server
//            deleteFileOnServer(session, osdirname + logfilename);
        }

        scpFrom(session, new File(textDownloadLdtFile.getText()).getName(),
                textDownloadLdtFile.getText());

        // Delete ldt file on server
//        deleteFileOnServer(session, osdirname + new File(textDownloadLdtFile.getText()).getName());

        remoteCommandFileContent = "rm " + osdirname + new File(textDownloadLdtFile.getText()).getName() + "\n";
        if (logfilename != null) {
            remoteCommandFileContent += "rm " + osdirname + logfilename;
        }

        scpTo(session, remoteTempCommandFileName, remoteCommandFileContent.getBytes());
        Channel channelDelete = session.openChannel("exec");
        ((ChannelExec)channelDelete).setCommand(host.getCommand() + " " + osdirname + remoteTempCommandFileName);
        channelDelete.connect();
        exitStatus = channelDelete.getExitStatus();
        channelDelete.disconnect();

        deleteFileOnServer(session, osdirname + remoteTempCommandFileName);

        session.disconnect();
    }

    private void deleteFileOnServer(Session session, String filePath) throws JSchException {
        Channel channel = session.openChannel("exec");
        ((ChannelExec)channel).setCommand("rm " + filePath);
        channel.connect();
        int exitStatus = channel.getExitStatus();
        channel.disconnect();
    }

    private void scpFrom(Session session, String filename,
                         String localFilename) throws Exception {
        Host host = (Host)comboHosts.getSelectedItem();
        String osdirname = host.getOsdirname();
        if (!osdirname.endsWith("/")) 
            osdirname = osdirname.concat("/");
        String scpcommand = "scp -f " + osdirname + filename;
        Channel channel = session.openChannel("exec");
        ((ChannelExec)channel).setCommand(scpcommand);
        OutputStream out = channel.getOutputStream();
        InputStream in = channel.getInputStream();

        channel.connect();

        byte[] buf = new byte[1024];

        // send '\0'
        buf[0] = 0;
        out.write(buf, 0, 1);
        out.flush();

        while (true) {
            int c = checkAck(in, textDownloadLog);
            if (c != 'C') {
                break;
            }

            // read '0644 '
            in.read(buf, 0, 5);

            long filesize = 0L;
            while (true) {
                if (in.read(buf, 0, 1) < 0) {
                    // error
                    break;
                }
                if (buf[0] == ' ')
                    break;
                filesize = filesize * 10L + (long)(buf[0] - '0');
            }

            @SuppressWarnings("unused")
            String file = null;
            for (int i = 0; ; i++) {
                in.read(buf, i, 1);
                if (buf[i] == 0x0a) {
                    file = new String(buf, 0, i);
                    break;
                }
            }

            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();

            FileOutputStream fos = new FileOutputStream(localFilename);
            int foo;
            while (true) {
                if (buf.length < filesize)
                    foo = buf.length;
                else
                    foo = (int)filesize;
                foo = in.read(buf, 0, foo);
                if (foo < 0) {
                    // error
                    break;
                }
                fos.write(buf, 0, foo);
                filesize -= foo;
                if (filesize == 0L)
                    break;
            }
            fos.close();
            fos = null;

            if (checkAck(in, textDownloadLog) != 0) {
                return;
            }

            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();
        }

        channel.disconnect();
    }

    private int checkAck(InputStream in, JTextArea ta) throws IOException {
        int b = in.read();
        // b may be 0 for success,
        // 1 for error,
        // 2 for fatal error,
        // -1
        if (b == 0)
            return b;
        if (b == -1)
            return b;

        if (b == 1 || b == 2) {
            StringBuffer sb = new StringBuffer();
            int c;
            do {
                c = in.read();
                sb.append((char)c);
            } while (c != '\n');
            if (b == 1) { // error
                ta.append("ERROR:" + sb.toString());
            }
            if (b == 2) { // fatal error
                ta.append("FATAL:" + sb.toString());
            }
        }
        return b;
    }


    private void buttonUpload_actionPerformed(ActionEvent e) {
        textUploadLog.setText("");
        textUploadFndloadLog.setText("");        
        Host host = (Host)comboHosts.getSelectedItem();
        LdtEntity ldtEntity = (LdtEntity)listEntities.getSelectedValue();
        String ldtfile = textUploadLdtFile.getText();
        if (ldtfile == null || ldtfile.isEmpty() ||
            !(new File(ldtfile).exists())) {
            JOptionPane.showMessageDialog(this, "Ldt file must be supplied.");
            return;
        }

        File file = new File(ldtfile);
        if (!file.exists() || !file.isFile()) {
            JOptionPane.showMessageDialog(this,
                                          "Ldt file must be an existent file.");
            return;
        }

        generalSettings.setLastUsedDir(file.getParentFile());

        StringBuilder command = new StringBuilder();
//        String envcmd = host.getEnvcmd();
        //        if (envcmd == null || envcmd.length() == 0) {
        //          envcmd = "$(grep '\\.env' .profile | sed \"s/.*='\\(.*\\)'/\\1/\")";
        //        }
//        if (envcmd != null && envcmd.length() > 0) {
//            command.append(envcmd);
//            command.append("\n");
//        }
        command.append("cd " + host.getOsdirname());
        command.append("\n");

        NlsLang nlsLang = (NlsLang)comboLanguageUpload.getSelectedItem();
        String executable = host.getCommand();
//        if (nlsLang != null) {
//            String oracleEncoding = nlsLang.getOracleEncoding();
//            if (oracleEncoding != null && oracleEncoding.trim().length() > 0) {
//                command.append("export NLS_LANG=" + nlsLang.getOracleEncoding());
//                command.append("\n");
//            }
//        }

        String remoteTempCommandFileName = host.getRemoteTempCommandFileName();
        if (remoteTempCommandFileName == null || remoteTempCommandFileName.trim().length() == 0) {
            try {
                remoteTempCommandFileName = File.createTempFile("fndload_wrapper", ".sh").getName();
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(this, e1.getMessage());
                return;
            }
        }

        command.append(String.format("%s %s %s '%s'",
                executable,
                remoteTempCommandFileName,
                ldtEntity.getLctfile(),
                new File(ldtfile).getName()));
        
        if (chkCustomModeForce.isSelected()) {
          command.append(" CUSTOM_MODE=FORCE");
        }
        
        if (chkUploadMode.isSelected()) {
            command.append(" UPLOAD_MODE=");
            command.append(cmbUploadModeParam.getSelectedItem());
        }

        Cursor tempCursor = getCursor();
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            executeUploadCommand(command.toString(), remoteTempCommandFileName);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }

        LoaderAction loaderAction = createActionFromUi("UPLOAD");
        addActionToHistoryMenu(loaderAction);       
        
        setCursor(tempCursor);
    }

    private void executeUploadCommand(String command, String remoteTempCommandFileName) throws Exception {
        textUploadLog.append(command + "\n");
        JSch jsch = new JSch();
        for (SshKey k : sshKeys.getKeysList()) {
            jsch.addIdentity(k.getFilename(), k.getPassphrase());
        }
        jsch.setKnownHosts("known_hosts");
        Host host = (Host)comboHosts.getSelectedItem();
        String osUser = host.getOsUser() == null || host.getOsUser().trim().length() == 0 ? System.getProperty("user.name") : host.getOsUser();
        Session session = jsch.getSession(osUser, host.getHostname());
        session.setPassword(host.getOsPassword());
        session.setConfig("PreferredAuthentications", "publickey");
        Connector con = null;

        try {
            ConnectorFactory cf = ConnectorFactory.getDefault();
            con = cf.createConnector();
        }
        catch(AgentProxyException e){
            System.out.println(e);
        }

        if(con != null ){
            IdentityRepository irepo = new RemoteIdentityRepository(con);
            jsch.setIdentityRepository(irepo);
        }

        session.setUserInfo(new MyUserInfo());
        session.connect();

        NlsLang nlsLang = (NlsLang)comboLanguage.getSelectedItem();

        String remoteCommandFileContent = host.getEnvcmd() + "\n" +
                "NLS_LANG=" + nlsLang.getOracleEncoding() + " FNDLOAD " + host.getDbUser() + "/" + host.getDbPassword() + " 0 Y UPLOAD $*\n";

        scpTo(session, remoteTempCommandFileName, remoteCommandFileContent.getBytes());

        scpTo(session, textUploadLdtFile.getText());

        Channel channel = session.openChannel("exec");
        ((ChannelExec)channel).setCommand(command);
        ((ChannelExec)channel).setPty(true);
        channel.setInputStream(null);
        ((ChannelExec)channel).setErrStream(System.err);

        InputStream inputStream = channel.getInputStream();
        channel.connect();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int exitStatus = 0;
        byte[] tmp=new byte[1024];
        while(true){
            while(inputStream.available()>0){
                int i=inputStream.read(tmp, 0, 1024);
                if(i<0)break;

                baos.write(tmp, 0, i);
            }
            if(channel.isClosed()){
                if(inputStream.available()>0) continue;
                exitStatus = channel.getExitStatus();
                break;
            }
            try{Thread.sleep(1000);}catch(Exception ee){}
        }
        channel.disconnect();

        String osdirname = host.getOsdirname();
        if (!osdirname.endsWith("/")) osdirname = osdirname.concat("/");

        // Delete ldt file on server
        deleteFileOnServer(session, osdirname + new File(textUploadLdtFile.getText()).getName());

        String commandOutputText = null;
        if (nlsLang != null && nlsLang.getJavaEncoding() != null && nlsLang.getJavaEncoding().length() > 0 &&
                !Charset.defaultCharset().name().equalsIgnoreCase(nlsLang.getJavaEncoding())) {
            commandOutputText = new String(baos.toByteArray(), nlsLang.getJavaEncoding());
        } else {
            commandOutputText = new String(baos.toByteArray());
        }

        textUploadLog.append(commandOutputText + "\n");
        if (exitStatus != 0) {
            textUploadLog.append("!!!Exit status:" + exitStatus + "\n");
        }

        String logfilename = null;
        Matcher m = patternLogFilename.matcher(commandOutputText);
        if (m.find()) {
            logfilename = m.group(1);
        }

        if (logfilename != null) {
            scpFrom(session, logfilename, logfilename);
            File fileLog = new File(logfilename);
            InputStreamReader is = null;
            if (nlsLang != null && nlsLang.getJavaEncoding() != null && nlsLang.getJavaEncoding().length() > 0 &&
                !Charset.defaultCharset().name().equalsIgnoreCase(nlsLang.getJavaEncoding())) {
              is = new InputStreamReader(new FileInputStream(fileLog), nlsLang.getJavaEncoding());
            } else {
              is = new InputStreamReader(new FileInputStream(fileLog));
            }
            char[] ca = new char[1024];
            int len;
            while ((len = is.read(ca)) != -1) {
                textUploadFndloadLog.append(new String(ca, 0, len));
            }
            is.close();
            fileLog.delete();
            tabbedUploadLogs.setTitleAt(1, logfilename);
            
            // Delete log file on server
            deleteFileOnServer(session, osdirname + logfilename);
        }

        if (logfilename != null) {
            remoteCommandFileContent = "rm " + osdirname + logfilename;
            scpTo(session, remoteTempCommandFileName, remoteCommandFileContent.getBytes());
            Channel channelDelete = session.openChannel("exec");
            ((ChannelExec)channelDelete).setCommand(host.getCommand() + " " + osdirname + remoteTempCommandFileName);
            channelDelete.connect();
            exitStatus = channelDelete.getExitStatus();
            channelDelete.disconnect();
        }

        deleteFileOnServer(session, osdirname + remoteTempCommandFileName);

        session.disconnect();
    }

    private void scpTo(Session session, String filename) throws Exception {
        File file = new File(filename);
        Host host = (Host)comboHosts.getSelectedItem();
        String osdirname = host.getOsdirname();
        if (!osdirname.endsWith("/")) 
            osdirname = osdirname.concat("/");
        String command = "scp -p -t " + osdirname + file.getName();
        Channel channel = session.openChannel("exec");
        ((ChannelExec)channel).setCommand(command);

        // get I/O streams for remote scp
        OutputStream out = channel.getOutputStream();
        InputStream in = channel.getInputStream();

        channel.connect();

        if (checkAck(in, textUploadLog) != 0) {
            throw new Exception("scpTo:checkAck(in)!=0:1");
        }

        // send "C0644 filesize filename", where filename should not include '/'

        long filesize = file.length();
        command = "C0644 " + filesize + " " + file.getName() + "\n";
        out.write(command.getBytes());
        out.flush();
        if (checkAck(in, textUploadLog) != 0) {
            throw new Exception("scpTo:checkAck(in)!=0:2");
        }

        // send a content of lfile
        FileInputStream fis = new FileInputStream(file);
        byte[] buf = new byte[1024];
        while (true) {
            int len = fis.read(buf, 0, buf.length);
            if (len <= 0)
                break;
            out.write(buf, 0, len); //out.flush();
        }
        fis.close();
        fis = null;
        // send '\0'
        buf[0] = 0;
        out.write(buf, 0, 1);
        out.flush();
        if (checkAck(in, textUploadLog) != 0) {
            throw new Exception("scpTo:checkAck(in)!=0:3");
        }
        out.close();

        channel.disconnect();
    }

    private void scpTo(Session session, String filename, byte[] fileData) throws Exception {
        Host host = (Host)comboHosts.getSelectedItem();
        String osdirname = host.getOsdirname();
        if (!osdirname.endsWith("/"))
            osdirname = osdirname.concat("/");
        String command = "scp -p -t " + osdirname + filename;
        Channel channel = session.openChannel("exec");
        ((ChannelExec)channel).setCommand(command);

        // get I/O streams for remote scp
        OutputStream out = channel.getOutputStream();
        InputStream in = channel.getInputStream();

        channel.connect();

        if (checkAck(in, textUploadLog) != 0) {
            throw new Exception("scpTo:checkAck(in)!=0:1");
        }

        // send "C0644 filesize filename", where filename should not include '/'

        int filesize = fileData.length;
        command = "C0644 " + filesize + " " + filename + "\n";
        out.write(command.getBytes());
        out.flush();
        if (checkAck(in, textUploadLog) != 0) {
            throw new Exception("scpTo:checkAck(in)!=0:2");
        }

        // send a content of lfile
        out.write(fileData); //out.flush();

        // send '\0'
        byte[] buf = new byte[] {0};
        out.write(buf, 0, 1);
        out.flush();
        if (checkAck(in, textUploadLog) != 0) {
            throw new Exception("scpTo:checkAck(in)!=0:3");
        }
        out.close();

        channel.disconnect();
    }

    private void menuSettingsNlsLangs_actionPerformed(ActionEvent e) {
        NlsLang nlsLang = (NlsLang) comboLanguage.getSelectedItem();
        DialogNlsLang d = new DialogNlsLang();
        d.setNlsLangs(nlsLangs);
        d.setSize(500, 400);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
        comboLanguage.setModel(new DefaultComboBoxModel(nlsLangs.getNlsLangsList().toArray()));
        comboLanguage.setSelectedItem(nlsLang);
    }

    private void menuToolsWfSort_actionPerformed(ActionEvent e) {
        JDialog dialog = new WfSortPanel();
        //dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);        
    }

    private void textDownloadLdtFile_keyReleased(KeyEvent e) {
        appendLdtExtensionIfNeeded();         
    }

    private void appendLdtExtensionIfNeeded() {
        String text = textDownloadLdtFile.getText();
        if (text.length() > 0) {
            if (text != null && !text.endsWith("\\") && !text.endsWith(".ldt") && !text.endsWith(".LDT")) {
                int caretPosition = textDownloadLdtFile.getCaretPosition();
                int selStart = textDownloadLdtFile.getSelectionStart();
                int selEnd = textDownloadLdtFile.getSelectionEnd();
                textDownloadLdtFile.setText(text + ".ldt");
                textDownloadLdtFile.setSelectionStart(selStart);
                textDownloadLdtFile.setSelectionEnd(selEnd);
                textDownloadLdtFile.setCaretPosition(caretPosition);
            }
        }
    }

    private LoaderAction createActionFromUi(String actionType) {
        LoaderAction loaderAction = null;
        
        if ("DOWNLOAD".equals(actionType))
            loaderAction = new DownloadAction();
        else if ("UPLOAD".equals(actionType))
            loaderAction = new UploadAction();
        else
            return null;

        Host host = (Host)comboHosts.getSelectedItem();
        loaderAction.setHostName(host.getPrompt());

        LdtEntity ldtEntity = (LdtEntity)listEntities.getSelectedValue();
        loaderAction.setEntityName(ldtEntity.getPrompt());
        
        if ("DOWNLOAD".equals(actionType)) {
            File ldtFile = new File(textDownloadLdtFile.getText());
            loaderAction.setFile(ldtFile);
            
            NlsLang nlsLang = (NlsLang)comboLanguage.getSelectedItem();
            if (nlsLang != null) loaderAction.setLanguageName(nlsLang.toString());

            HashMap params = new HashMap();
            for (Component c : panelDownloadParameters.getComponents()) {
                if (c instanceof JTextField) {
                    String name = c.getName();
                    String value = ((JTextField)c).getText();

                    Object object = params.put(name, value);
                }
            }
            
            ((DownloadAction)loaderAction).setParams(params);
        } else if ("UPLOAD".equals(actionType)) {
            File ldtFile = new File(textUploadLdtFile.getText());
            loaderAction.setFile(ldtFile);
            
            NlsLang nlsLang = (NlsLang)comboLanguageUpload.getSelectedItem();
            if (nlsLang != null) loaderAction.setLanguageName(nlsLang.toString());

            String customMode = null;
            if (chkCustomModeForce.isSelected())
              customMode = "FORCE";
            
            String uploadMode = null;
            if (chkUploadMode.isSelected())
                uploadMode = (String) cmbUploadModeParam.getSelectedItem();

            ((UploadAction)loaderAction).setCustomMode(customMode);
            ((UploadAction)loaderAction).setUploadMode(uploadMode);
        }

        return loaderAction;
    }

    private void addActionToHistoryMenu(LoaderAction historyAction) {
        if (historyAction == null) return;
        HistoryMenuAction menuAction = new HistoryMenuAction(this, historyAction);
        JMenuItem menuItem = new JMenuItem(menuAction);
        menuFileHistory.insert(menuItem, 0);
    }

    public void populateUiFromAction(LoaderAction historyAction) {
        String hostPrompt = historyAction.getHostName();
        comboHosts.setSelectedItem(hosts.getHostByPrompt(hostPrompt));

        String entityPrompt = historyAction.getEntityName();
        listEntities.setSelectedValue(ldtEntities.getLdtEntityByPrompt(entityPrompt), true /*shouldScroll*/);
        
        if ("DOWNLOAD".equals(historyAction.getActionType())) {
            textDownloadLdtFile.setText(historyAction.getFile().getAbsolutePath());
            
            String oracleEncoding = historyAction.getLanguageName();
            comboLanguage.setSelectedItem(nlsLangs.getNlsLangByOracleName(oracleEncoding));
            
            DownloadAction actionDownload = (DownloadAction) historyAction;
            
            HashMap params = actionDownload.getParams();
            
            for (Component c : panelDownloadParameters.getComponents()) {
                if (c instanceof JTextField) {
                    String name = c.getName();
                    JTextField textField = (JTextField) c;
                    textField.setText((String) params.get(name));
                }
            }            

            tabbedPane.setSelectedComponent(panelDownload);
            buttonDownload.requestFocusInWindow();
        } else if ("UPLOAD".equals(historyAction.getActionType())) {
            textUploadLdtFile.setText(historyAction.getFile().getAbsolutePath());
            
            String oracleEncoding = historyAction.getLanguageName();
            comboLanguageUpload.setSelectedItem(nlsLangs.getNlsLangByOracleName(oracleEncoding));
            
            UploadAction actionUpload = (UploadAction) historyAction;
            
            chkCustomModeForce.setSelected("FORCE".equals(actionUpload.getCustomMode()));
            String uploadMode = actionUpload.getUploadMode();
            chkUploadMode.setSelected(uploadMode != null);
            cmbUploadModeParam.setSelectedItem(actionUpload.getUploadMode());
            
            tabbedPane.setSelectedComponent(panelUpload);
            buttonUpload.requestFocusInWindow();            
        }
    }

    private class ListModelEntities extends AbstractListModel {
        private LdtEntities ldtEntities;

        private ListModelEntities(LdtEntities ldtEntities) {
            this.ldtEntities = ldtEntities;
        }

        public int getSize() {
            return ldtEntities.getLdtEntitiesList().size();
        }

        public Object getElementAt(int index) {
            LdtEntity ldtEntity = ldtEntities.getLdtEntitiesList().get(index);
            return ldtEntity.getPrompt();
        }
    }

    private void setEnabledParams(boolean flag) {
        panelParamsLog.setVisible(flag);
        buttonDownload.setEnabled(flag);
        tabbedUploadLogs.setVisible(flag);
        buttonUpload.setEnabled(flag);
    }

    private void prepareDownloadParametersPanel(LdtEntity ldtEntity) {
        panelDownloadParameters.removeAll();
        ArrayList<LdtEntityParam> params = ldtEntity.getParams();
        int y = 0;
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 5, 5, 5);
        for (LdtEntityParam p : params) {
            c.gridx = 0;
            c.gridy = y;
            c.weightx = 0;
            String lbl = (p.isOptional() ? "   " : "* ") + p.getPrompt() + ":";
            panelDownloadParameters.add(new JLabel(lbl), c);
            c.gridx = 1;
            c.weightx = 1;
            JTextField textField = new JTextField();
            textField.setName(p.getName());
            textField.setToolTipText("<html>"+p.getName() + "<br>" + p.getHint()+"</html>");
            panelDownloadParameters.add(textField, c);
            y++;
        }

        panelDownloadParameters.setVisible(false);
        panelDownloadParameters.setVisible(true);
    }

    private class ListEntitiesSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting())
                return;
            int row = listEntities.getSelectedIndex();
            if (row == -1) {
                setEnabledParams(false);
                return;
            }
            prepareDownloadParametersPanel(ldtEntities.getLdtEntitiesList().get(row));

            textUploadLog.setText("");
            setEnabledParams(true);
        }
    }
}
