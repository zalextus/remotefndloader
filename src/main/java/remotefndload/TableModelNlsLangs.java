package remotefndload;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class TableModelNlsLangs
  extends AbstractTableModel
{

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  private NlsLangs nlsLangs;
  private String[] columnNames =
  { "Oracle Encoding", "Java Encoding", "Enabled" };

  public TableModelNlsLangs(NlsLangs nlsLangs)
  {
    this.nlsLangs = nlsLangs;
  }

//  @Override
  public int getColumnCount()
  {
    return columnNames.length;
  }

//  @Override
  public int getRowCount()
  {
    return nlsLangs.getNlsLangsList().size();
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    switch (columnIndex) {
      case 2:
          return Boolean.class;
      default:
        return super.getColumnClass(columnIndex);
    }
  }

  //  @Override
  public Object getValueAt(int rowIndex, int columnIndex)
  {
    NlsLang nlsLang = nlsLangs.getNlsLangsList().get(rowIndex);
    switch (columnIndex)
    {
      case 0:
        return nlsLang.getOracleEncoding();
      case 1:
        return nlsLang.getJavaEncoding();
      case 2:
        return nlsLang.getEnabled();
      default:
        return null;
    }
  }

  @Override
  public String getColumnName(int columnIndex)
  {
    return columnNames[columnIndex];
  }

  @Override
  public boolean isCellEditable(int arg0, int arg1)
  {
    return true;
  }

  @Override
  public void setValueAt(Object val, int rowIndex, int columnIndex)
  {
    NlsLang nlsLang = nlsLangs.getNlsLangsList().get(rowIndex);
    switch (columnIndex)
    {
      case 0:
        nlsLang.setOracleEncoding((String) val);
        break;
      case 1:
        nlsLang.setJavaEncoding((String) val);
        break;
      case 2:
        nlsLang.setEnabled((Boolean) val);
        break;
    }

  }

  public void addRow()
  {
    addRow(new NlsLang());
  }

  public void removeRow(int row)
  {
    nlsLangs.getNlsLangsList().remove(row);
  }

  void addRow(NlsLang nlsLang)
  {
    nlsLangs.getNlsLangsList().add(nlsLang);
  }

  void moveRowUp(int row)
  {
    ArrayList<NlsLang> list = nlsLangs.getNlsLangsList();
    if (row > 0 && row < list.size())
    {
      NlsLang nlsLang1 = list.get(row - 1);
      NlsLang nlsLang2 = list.get(row);
      list.set(row - 1, nlsLang2);
      list.set(row, nlsLang1);
    }
  }

  void moveRowDown(int row)
  {
    ArrayList<NlsLang> list = nlsLangs.getNlsLangsList();
    if (row > -1 && row < list.size())
    {
      NlsLang nlsLang1 = list.get(row);
      NlsLang nlsLang2 = list.get(row + 1);
      list.set(row, nlsLang2);
      list.set(row + 1, nlsLang1);
    }
  }
}
