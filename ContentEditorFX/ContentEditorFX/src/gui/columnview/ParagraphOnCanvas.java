package gui.columnview;

import geometry.libgdxmath.Rectangle;

import java.util.ArrayList;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.text.Font;
import document.Paragraph;
import document.TextStyle;


/**
 * This is basically a set of LineOnCanvas'es that share the same TextStyle
 * @author sahin
 *
 */
public class ParagraphOnCanvas {

	private ColumnView parent;
	
	private Rectangle allowedSpace;
	private ArrayList<LineOnCanvas> lines; 
	private TextStyle style;
	private Paragraph textPair;
	
	public ParagraphOnCanvas(ColumnView parent, Rectangle allowedSpace, TextStyle style) {
		this.parent = parent;
		this.allowedSpace = allowedSpace;
		this.style = style;
		lines = new ArrayList<LineOnCanvas>();
	}
	
	private void debug() {
		textPair = new Paragraph();
		if(style == null)
			style = TextStyle.defaultStyle;
		textPair.setText("DENEME DENEME DENEME DENEME DENEME DENEME DENEME DENEME DENEME DENEME DENEME DENEME DENEME DENEME DENEME DENEME DENEME DENEME ");
		textPair.setStyle(style);
		textPair.computeStringWidths();
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
	
	public void refresh() {
		textPair.startTextDivision();
		for(int i = 0; i < lines.size(); i++){
			LineOnCanvas line = lines.get(i);
			float allowedWidth = line.getWidth();
			String text = textPair.getNextLine(allowedWidth);
			System.out.println("Paragraph refresh, got line " + text +" for length: " + allowedWidth);
			line.setDebugText(text);
			line.refresh();
		}
	}

	public String getText() {
		return textPair.getText();
	}

	public Font getFont() {
		return style.getFont();
	}

	/**
	 * Forces refresh on the column this paragraph is in
	 */
	public void updateColumn() {
		parent.refresh();
	}

	public void setParagraph(Paragraph paragraph) {
		System.out.println("Set paragraph to: " + paragraph);
		textPair = paragraph;
	}
	
	public String toString() {
		return textPair.getText();
	}
	
}
