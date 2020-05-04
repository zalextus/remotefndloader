package remotefndload;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DialogNlsLang
  extends JDialog
{
  private JToolBar toolBar = new JToolBar();
  private BorderLayout borderLayout1 = new BorderLayout();
  private JButton buttonAdd = new JButton();
  private JButton buttonRemove = new JButton();
  private JScrollPane scrollPane = new JScrollPane();
  private JTable table = new JTable();
  private JButton buttonUp = new JButton();
  private JSeparator jSeparator1 = new JSeparator();
  private JButton buttonDown = new JButton();

  public DialogNlsLang()
  {
    this(null, "NLS_LANG", false);
  }

  public DialogNlsLang(Frame parent, String title, boolean modal)
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
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
  }

  private void jbInit()
    throws Exception
  {
    this.setSize( new Dimension( 400, 300 ) );
    this.getContentPane().setLayout(borderLayout1);
    this.setModal(true);
    buttonAdd.setText("Add");
    buttonAdd.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          buttonAdd_actionPerformed(e);
        }
      });
    buttonRemove.setText("Remove");
    buttonRemove.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          buttonRemove_actionPerformed(e);
        }
      });
    buttonUp.setText("Move up");
    buttonUp.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          buttonUp_actionPerformed(e);
        }
      });
    jSeparator1.setOrientation(SwingConstants.VERTICAL);
    buttonDown.setText("Move down");
    buttonDown.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          buttonDown_actionPerformed(e);
        }
      });
    toolBar.add(buttonAdd, null);
    toolBar.add(buttonRemove, null);
    toolBar.add(jSeparator1, null);
    toolBar.add(buttonUp, null);
    toolBar.add(buttonDown, null);
    this.getContentPane().add(toolBar, BorderLayout.NORTH);
    scrollPane.getViewport().add(table, null);
    this.getContentPane().add(scrollPane, BorderLayout.CENTER);
  }

  void setNlsLangs(NlsLangs nlsLangs)
  {
    TableModelNlsLangs model = new TableModelNlsLangs(nlsLangs);
    table.setModel(model);
  }  

  private void buttonAdd_actionPerformed(ActionEvent e)
  {
    TableModelNlsLangs model = (TableModelNlsLangs) table.getModel();
    model.addRow();
    model.fireTableDataChanged();    
  }

  private void buttonRemove_actionPerformed(ActionEvent e)
  {
    int row = table.getSelectedRow();
    if (row != -1)
    {
      TableModelNlsLangs model = (TableModelNlsLangs) table.getModel();
      row = table.convertRowIndexToModel(row);
      model.removeRow(row);
      model.fireTableDataChanged();
    }    
  }

  private void buttonUp_actionPerformed(ActionEvent e)
  {
    int row = table.getSelectedRow();
    if (row > 0)
    {
      TableModelNlsLangs model = (TableModelNlsLangs) table.getModel();
      row = table.convertRowIndexToModel(row);
      model.moveRowUp(row);
      model.fireTableDataChanged();
    }
  }

  private void buttonDown_actionPerformed(ActionEvent e)
  {
    int row = table.getSelectedRow();
    if (row != -1 && row < table.getModel().getRowCount())
    {
      TableModelNlsLangs model = (TableModelNlsLangs) table.getModel();
      row = table.convertRowIndexToModel(row);
      model.moveRowDown(row);
      model.fireTableDataChanged();      
    }  
  }
}
