package remotefndload.wf.sort;

import java.util.TreeSet;

public class WfBlock implements Comparable<WfBlock> {

  private static final String LINE_SEPARATOR = System.getProperty("line.separator");
  private String begin;
  private String end;
	private StringBuffer bodyBuffer;
	private TreeSet<WfBlock> childWfBlocks;

	public WfBlock() {
		childWfBlocks = new TreeSet<WfBlock>();
		bodyBuffer = new StringBuffer();
	}

	public void setBegin(String begin) { this.begin = begin; }
	public String getBegin() { return begin; }

	public void setEnd(String end) { this.end = end; }
	public String getEnd() { return end; }

	public void addBodyText(String text) {
	  if (bodyBuffer.length() != 0)
  	  bodyBuffer.append(LINE_SEPARATOR);
    
    bodyBuffer.append(text);
	}

	public StringBuffer getBodyBuffer() {
	  return bodyBuffer;
	}

	public void addChildWfBlock(WfBlock wfBlock) {
		if (wfBlock == null) return;
	  childWfBlocks.add(wfBlock);
	}

	public TreeSet getChildWfBlocks() {
		return childWfBlocks;
	}

	public int compareTo(WfBlock wfBlock) {
		return begin.compareTo(wfBlock.begin);
	}

	public String toString() {
	  StringBuffer sb = new StringBuffer();
	  
	  sb.append(begin).append(LINE_SEPARATOR);
	  
	  sb.append(bodyBuffer.toString()).append(LINE_SEPARATOR);

	  for (WfBlock childWfBlock: childWfBlocks) {
  	    sb.append(LINE_SEPARATOR).append(childWfBlock.toString());
	  }
	  
	  sb.append(end).append(LINE_SEPARATOR);

	  return sb.toString();
	}

}

