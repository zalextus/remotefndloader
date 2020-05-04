package remotefndload.wf.sort;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class WfSortPanel extends JDialog {

    private JPanel jPanel1 = new JPanel();
    private JLabel lblOutputFile = new JLabel();
    private JButton btnSaveAs = new JButton();
    private JButton btnOpen = new JButton();
    private JTextField txtInputFile = new JTextField();
    private JTextField txtOutputFile = new JTextField();
    private JButton btnProcess = new JButton();
    private JLabel lblInputFile = new JLabel();
    private JFileChooser fileChooser = new JFileChooser();


    public WfSortPanel() {
    this(null, "Workflow definition sort", true);
  }

  public WfSortPanel(Frame parent, String title, boolean modal)
  {
    super(parent, title, modal);
    try
    {
      jbInit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    this.setSize(new Dimension(666, 172));
        this.getContentPane().setLayout(null);
        this.setTitle("Oracle workflow definiton sort");
        jPanel1.setBounds(new Rectangle(10, 10, 645, 90));
        jPanel1.setLayout(null);
        jPanel1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        lblOutputFile.setText("Output file:");
        lblOutputFile.setBounds(new Rectangle(15, 55, 60, 15));
        btnSaveAs.setText("Save As...");
        btnSaveAs.setBounds(new Rectangle(560, 55, 75, 20));
        btnSaveAs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnSaveAs_actionPerformed(e);
            }
        });
        btnOpen.setText("Open...");
        btnOpen.setBounds(new Rectangle(560, 25, 75, 20));
        btnOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnOpen_actionPerformed(e);
            }
        });
        txtInputFile.setBounds(new Rectangle(80, 20, 470, 25));
        txtInputFile.setEditable(false);
        txtOutputFile.setBounds(new Rectangle(80, 50, 470, 25));
        txtOutputFile.setEditable(false);
        btnProcess.setText("Process");
        btnProcess.setBounds(new Rectangle(290, 110, 80, 20));
        btnProcess.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnProcess_actionPerformed(e);
            }
        });
        lblInputFile.setText("Input file:");
        lblInputFile.setBounds(new Rectangle(15, 25, 60, 15));
        jPanel1.add(lblInputFile, null);
        jPanel1.add(txtOutputFile, null);
        jPanel1.add(txtInputFile, null);
        jPanel1.add(btnOpen, null);
        jPanel1.add(btnSaveAs, null);
        jPanel1.add(lblOutputFile, null);
        this.getContentPane().add(btnProcess, null);
        this.getContentPane().add(jPanel1, null);
    }

    private void btnOpen_actionPerformed(ActionEvent e) {
        String inputFileName = txtInputFile.getText();
        if (inputFileName != null && !inputFileName.isEmpty()) {
            fileChooser.setSelectedFile(new File(inputFileName));
        }
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String absolutePath = fileChooser.getSelectedFile().getAbsolutePath();
            txtInputFile.setText(absolutePath);
        }
    }

    private void btnSaveAs_actionPerformed(ActionEvent e) {
        String saveFileName = txtOutputFile.getText();
        if (saveFileName != null && !saveFileName.isEmpty()) {
            fileChooser.setSelectedFile(new File(saveFileName));
        }
        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String absolutePath = fileChooser.getSelectedFile().getAbsolutePath();
            txtOutputFile.setText(absolutePath);
        }
    }

    private void btnProcess_actionPerformed(ActionEvent e) {
        if (txtInputFile.getText() == null || txtInputFile.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Input file should not be empty", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        File file = new File(txtInputFile.getText());
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "Input file does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (txtOutputFile.getText() == null || txtOutputFile.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Output file should not be empty", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        file = new File(txtOutputFile.getText());
        if (file.exists()) {
            if (txtInputFile.getText().equals(txtOutputFile.getText())) {
                int resultVal = JOptionPane.showConfirmDialog(this, "You're going to write the output to the input file. Are you sure?", "Question", JOptionPane.YES_NO_OPTION);
                if (resultVal == JOptionPane.NO_OPTION) return;                
            } else {
                int resultVal = JOptionPane.showConfirmDialog(this, "Output file already exists. Continue?", "Question", JOptionPane.YES_NO_OPTION);
                if (resultVal == JOptionPane.NO_OPTION) return;
            }
        }
        
        Cursor tempCursor = getCursor();
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            String charset = Charset.defaultCharset().name();
            WfFileSort wfFile = new WfFileSort();
            btnProcess.setEnabled(false);
            wfFile.loadFile(txtInputFile.getText(), charset);
            wfFile.saveFile(txtOutputFile.getText(), charset);
            btnProcess.setEnabled(true);
            setCursor(tempCursor);
            JOptionPane.showMessageDialog(this, "Done.", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            btnProcess.setEnabled(true);
            setCursor(tempCursor);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
