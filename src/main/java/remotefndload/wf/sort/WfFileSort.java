package remotefndload.wf.sort;

import java.io.*;
import java.util.TreeSet;

public class WfFileSort {

  private static final String LINE_SEPARATOR = System.getProperty("line.separator");
  private StringBuffer headerBuffer;
  private TreeSet<WfBlock> childWfBlocks;

  public static void main(String[] args) throws IOException {
    WfFileSort wfFile = new WfFileSort();
    wfFile.loadFile(args[0], args[1]);
    wfFile.saveFile(args[2], args[3]);
  }

  public WfFileSort() {
    headerBuffer = new StringBuffer();
    childWfBlocks = new TreeSet<WfBlock>();
  }

  public void loadFile(String fileName, String charset) throws IOException {
    File file = new File(fileName);
    if (!file.exists()) throw new IOException("Input file does not exist.");
    FileInputStream fis = new FileInputStream(file);
    InputStreamReader isr = new InputStreamReader(fis, charset);
    BufferedReader br = new BufferedReader(isr);

    WfBlock wfBlock = null;
    String line = null;
    while ( (line = br.readLine()) != null ) {
       if (line.matches("\\s*BEGIN\\s+.+")) {
         wfBlock = createWfBlock(br, line);
         childWfBlocks.add(wfBlock);
       } else {
         if (wfBlock == null) {
           headerBuffer.append(line).append(LINE_SEPARATOR);
         }
       }
    }

    br.close();
  }

  private WfBlock createWfBlock(BufferedReader br, String begin) throws IOException {
    //System.out.println("BLOCK " + begin);
    WfBlock wfBlock = new WfBlock();
    wfBlock.setBegin(begin);
    String line = null;
    while ( (line = br.readLine()) != null ) {
    	if (line.matches("\\s*END\\s+.+")) {
    	  wfBlock.setEnd(line);
    	  break;
    	} else if (line.matches("\\s*BEGIN\\s+.+")) {
    	  WfBlock childBlock = createWfBlock(br, line);
    	  wfBlock.addChildWfBlock(childBlock);
    	} else {
    	  if (line.trim().length() > 0)
    	    wfBlock.addBodyText(line);
    	}
    }

    return wfBlock;
  }

  public void saveFile(String fileName, String charset) throws IOException {
    File file = new File(fileName);
    FileOutputStream fos = new FileOutputStream(file);
    OutputStreamWriter osw = new OutputStreamWriter(fos, charset);
    BufferedWriter bw = new BufferedWriter(osw);
    bw.write(headerBuffer.toString());
    for (WfBlock childWfBlock: childWfBlocks) {
      bw.write(childWfBlock.toString());
      bw.write(LINE_SEPARATOR);
    }
    bw.close();
  }

}
