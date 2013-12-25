package gui.columnview;

import geometry.libgdxmath.Rectangle;

import java.util.ArrayList;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.text.Font;
import document.StyleTextPair;
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
	private StyleTextPair textPair;
	
	public ParagraphOnCanvas(ColumnView parent, Rectangle allowedSpace, TextStyle style) {
		this.parent = parent;
		this.allowedSpace = allowedSpace;
		this.style = style;
		lines = new ArrayList<LineOnCanvas>();
		debug();
	}
	
	private void debug() {
		textPair = new StyleTextPair();
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
			line.setText("Line #" + i);
			line.refresh();
		}
	}

	public String getText() {
		return textPair.getText();
	}

	public Font getFont() {
		return style.getFont();
	}
	
}
