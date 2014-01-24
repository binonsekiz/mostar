package gui.columnview;

import geometry.libgdxmath.Rectangle;

import java.util.ArrayList;

import control.TextModifyFacade;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
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
	private Paragraph paragraph;

	private TextModifyFacade textModifyFacade;
	
	public ParagraphOnCanvas(ColumnView parent, Rectangle allowedSpace, TextStyle style, TextModifyFacade facade) {
		this.parent = parent;
		this.allowedSpace = allowedSpace;
		this.style = style;
		this.textModifyFacade = facade;
		lines = new ArrayList<LineOnCanvas>();
	}
	
	private void debug() {
		paragraph = new Paragraph();
		if(style == null)
			style = TextStyle.defaultStyle;
		paragraph.setText("DENEME DENEME DENEME DENEME DENEME DENEME DENEME DENEME DENEME DENEME DENEME DENEME DENEME DENEME DENEME DENEME DENEME DENEME ");
		paragraph.setStyle(style);
		paragraph.computeStringWidths();
	}
	
	public Rectangle getAllowedSpace() {
		return allowedSpace;
	}
	
	public TextStyle getStyle() {
		return style;
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
		paragraph.startTextDivision();
		int startIndex = 0;
		int endIndex = 0;
		for(int i = 0; i < lines.size(); i++){
			LineOnCanvas line = lines.get(i);
			float allowedWidth = line.getWidth();
			String text = paragraph.getNextLine(allowedWidth);
			if(text != null) {
				endIndex = startIndex + text.length();
			}
			line.setDebugText(text, startIndex, endIndex);
			line.refresh();
			startIndex = endIndex;
		}
	}
	
	public void refreshOverlay() {
		GraphicsContext context = parent.getOverlayContext();
		textModifyFacade.getCaret().drawCaret(0, context, style);
	}

	public String getText() {
		return paragraph.getText();
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
		this.paragraph = paragraph;
	}
	
	public String toString() {
		return paragraph.getText();
	}

	public boolean containsCoordinate(float x, float y) {
		return allowedSpace.contains(x, y);
	}

	public void mouseClick(MouseEvent event) {
		//check to see which line this is
		System.out.println("~~Clicked on paragraph");
		for(int i = 0; i < lines.size(); i++) {
			LineOnCanvas line = lines.get(i);
			if(line.containsCoordinate(event.getX(),event.getY())) {
				line.mouseClick(event);
			}
		}
	}
	
	public ArrayList<Float> getCummulativeWordSizes() {
		return paragraph.getCummulativeWordSizes();
	}

	public Paragraph getParagraph() {
		return paragraph;
	}
}
