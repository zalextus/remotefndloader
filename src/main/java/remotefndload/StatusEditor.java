package remotefndload;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;

public class StatusEditor extends AbstractCellEditor implements TableCellEditor {
    
    private JButton button = null;
    private String webStatus = "?", dbStatus = "?";
    private JDialog dialog = null;
    private JTable table = null;
    private int row = -1, column = -1;
    private final static String STATUS_WEB_ON = "On";
    private final static String STATUS_WEB_OFF = "Off";
    private final static String STATUS_WEB_TIMEOUT = "SSH timeout";

    public void setDialog(JDialog dialog) {
        this.dialog = dialog;
    }

    public JDialog getDialog() {
        return dialog;
    }

    public void setWebStatus(String webStatus) {
        this.webStatus = webStatus;
    }

    public String getWebStatus() {
        return webStatus;
    }

    public void setDbStatus(String dbStatus) {
        this.dbStatus = dbStatus;
    }

    public String getDbStatus() {
        return dbStatus;
    }

    public void setButton(JButton button) {
        this.button = button;
    }

    public JButton getButton() {
        return button;
    }
    
    public StatusEditor() {
        button = new JButton();
        button.setFont(new Font(Font.DIALOG, Font.PLAIN, 9));
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                button_actionPerformed(e);
            }

        });
        button.setEnabled(false);
    }
    
    private void button_actionPerformed(ActionEvent e) {
        Cursor tempCursor = dialog.getCursor();
        dialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        getHostStatus();
        button.setText((String)getCellEditorValue());
        dialog.setCursor(tempCursor);
    }
    
//    @Override
    public Object getCellEditorValue() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>Web:<font color='");
        if (STATUS_WEB_ON.equals(webStatus))
            sb.append("green");
        else
            sb.append("red");
        sb.append("'>");
        sb.append(webStatus);
        sb.append("</font>");
        
        if (!"?".equals(dbStatus)) {
            sb.append(" Db:");
            sb.append(dbStatus);
        }
        sb.append("</html>");
        
        return sb.toString();
    }

//    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        button.setText("Checking...");
        this.table = table;
        this.row = row;
        this.column = column;
        return button;
    }

    private void getHostStatus() {
        TableModel model = table.getModel();
        String host = (String) model.getValueAt(row, 1);
        String dbUser = (String) model.getValueAt(row, 2);
        String dbPassword = (String) model.getValueAt(row, 3);
        String osUser = (String) model.getValueAt(row, 4);
        String osPassword = (String) model.getValueAt(row, 5);
        String envCommand = (String) model.getValueAt(row, 7);
        
        setWebStatus(STATUS_WEB_OFF);
        setDbStatus("?");
        
        try {
            JSch jsch = new JSch();
            jsch.setKnownHosts("known_hosts");
            Session session = jsch.getSession(osUser, host);
            session.setPassword(osPassword);
            session.setUserInfo(new MyUserInfo());
            session.connect(3000);
            
            Channel channel = null;
            //int exitStatus = 0;
            InputStream in = null;
            //BufferedReader br = null;
            byte[] tmp = new byte[1024];
            StringBuffer sb = null;

            // Check login page
            channel = session.openChannel("exec");
            ((ChannelExec)channel).setCommand(envCommand + " 2> /dev/null;if [ ! -e \"$CONTEXT_FILE\" ]; then exit 0; fi;LOGIN_PAGE=`[ -e \"$CONTEXT_FILE\" ] && cat $CONTEXT_FILE|grep login_page|head -1|sed -r 's/\\s*<[^<]*login_page[^>]*>//g'`;if [ -n \"$LOGIN_PAGE\" ]; then curl -sI $LOGIN_PAGE|grep HTTP|awk -F' ' '{print $1,$2}'|sed -r 's/\\n//g'; fi");
            channel.setOutputStream(System.out);
            ((ChannelExec)channel).setErrStream(System.err);
            in = ((ChannelExec)channel).getInputStream();
            channel.connect(3000);
            
            /*br = new BufferedReader(new InputStreamReader(in));
            do {
                String line = br.readLine();
                if (line == null)
                    break;
                
                if ("HTTP/1.1 302".equals(line)) {
                    setWebStatus(STATUS_WEB_ON);
                } else {
                    setWebStatus(STATUS_WEB_OFF);
                }
            } while (true);
            exitStatus = channel.getExitStatus();
            channel.disconnect();
            if (exitStatus != 0) {
                System.out.println("!!!Exit status (check login page):" + exitStatus + "\n");
            }*/
            
            sb = new StringBuffer();
            while(true){
              while(in.available() > 0){
                int i = in.read(tmp, 0, 1024);
                if(i < 0)break;
                sb.append(new String(tmp, 0, i));
              }
              if(channel.isClosed()){
                System.out.println("exit-status (check login page): "+channel.getExitStatus());
                break;
              }
              try{Thread.sleep(1000);}catch(Exception ee){}
            }
            channel.disconnect();
            
            if ("HTTP/1.1 302".equals(sb.toString().trim())) {
                setWebStatus(STATUS_WEB_ON);
            } else {
                setWebStatus(STATUS_WEB_OFF);
            }
            
            // Check db
            if (STATUS_WEB_ON.equals(getWebStatus())) {
                channel = session.openChannel("exec");
                ((ChannelExec)channel).setCommand(envCommand + ";echo \"set echo off heading off feedback off\n" + 
                "select to_char(resetlogs_time, 'dd-Mon hh24:mi') resetlogs_time from v\\$database;\"|sqlplus -s "+dbUser+"/"+dbPassword+"|tail -1|sed -r 's/\\n//'");
                channel.setOutputStream(System.out);
                ((ChannelExec)channel).setErrStream(System.err);
                in = ((ChannelExec)channel).getInputStream();
                channel.connect(3000);
                /*br = new BufferedReader(new InputStreamReader(in));
                do {
                    String line = br.readLine();
                    if (line == null)
                        break;
                    
                    setDbStatus(line);
                } while (true);
                
                exitStatus = channel.getExitStatus();
                channel.disconnect();
                if (exitStatus != 0) {
                    System.out.println("!!!Exit status (check db):" + exitStatus + "\n");
                }*/
                sb = new StringBuffer();
                while(true){
                  while(in.available() > 0){
                    int i = in.read(tmp, 0, 1024);
                    if(i < 0)break;
                    sb.append(new String(tmp, 0, i));
                  }
                  if(channel.isClosed()){
                    System.out.println("exit-status (check db): "+channel.getExitStatus());
                    break;
                  }
                  try{Thread.sleep(1000);}catch(Exception ee){}
                }
                channel.disconnect();
                
                setDbStatus(sb.toString().trim());
            }
            session.disconnect();
            button.setToolTipText((String) getCellEditorValue());
        } catch (Exception e) {
            e.printStackTrace();
            setWebStatus(STATUS_WEB_TIMEOUT);
            button.setToolTipText(e.getMessage());
        }
        
        
        
    }
}
