package gui.columnview;

import java.util.ArrayList;

import geometry.libgdxmath.Rectangle;


/**
 * This is basically a set of LineOnCanvas'es that share the same TextStyle
 * @author sahin
 *
 */
public class ParagraphOnCanvas {

	private ColumnView parent;
	
	private Rectangle allowedSpace;
	private ArrayList<LineOnCanvas> lines; 
	
	public ParagraphOnCanvas(ColumnView parent, Rectangle allowedSpace) {
		this.parent = parent;
		this.allowedSpace = allowedSpace;
		lines = new ArrayList<LineOnCanvas>();
	}
	
	public Rectangle getAllowedSpace() {
		return allowedSpace;
	}
	
	public void setAllowedSpace(Rectangle allowedSpace) {
		this.allowedSpace = allowedSpace;
	}
	
	public void insertLine(LineOnCanvas newLine) {
		this.lines.add(newLine);
	}
	
	public ArrayList<LineOnCanvas> getLines() {
		return lines;
	}

	public void insertLines(ArrayList<LineOnCanvas> newLines) {
		this.lines.addAll(newLines);
	}
	
}
