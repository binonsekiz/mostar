package gui.columnview;

import geometry.libgdxmath.Rectangle;
import gui.docmodify.DocDebugView;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import control.TextModifyFacade;
import document.Document;
import document.Paragraph;
import document.ParagraphSpace;
import document.TextLine;
import document.TextStyle;


/**
 * This is basically a set of LineOnCanvas'es that share the same TextStyle
 * @author sahin
 *
 */
public class ParagraphOnCanvas {

	private ColumnView parent;
	
	private ParagraphSpace allowedSpace;
	private ArrayList<LineOnCanvas> lines; 
	private TextStyle style;
	private Paragraph paragraph;

	private TextModifyFacade textModifyFacade;
	
	public ParagraphOnCanvas(ColumnView parent, ParagraphSpace allowedSpace, TextStyle style, TextModifyFacade facade) {
		System.out.println("ParagraphView initialized");
		this.parent = parent; 
		this.allowedSpace = allowedSpace;
		this.style = style;
		this.textModifyFacade = facade;
		lines = new ArrayList<LineOnCanvas>();
	}

	public ParagraphSpace getAllowedSpace() {
		return allowedSpace;
	}
	
	public TextStyle getStyle() {
		return style;
	}
	
	public void setAllowedSpace(ParagraphSpace allowedSpace) {
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
		for(int i = 0; i < lines.size(); i++){
			lines.get(i).refresh();
		}
		
		allowedSpace.draw(parent.getGraphicsContext());
		
		DocDebugView.instance.debugRefreshTotalDocument(Document.instance);
	}
	
	public void refreshOverlay() {
		GraphicsContext context = parent.getOverlayContext();
		textModifyFacade.getCaret().drawCaret(context);
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
		paragraph.setStyle(style);
	}
	
	public String toString() {
		return paragraph.getText();
	}

	public boolean containsCoordinate(float x, float y) {
		return allowedSpace.getShape().contains(x, y);
	}

	public void mouseClick(MouseEvent event) {
		for(int i = 0; i < lines.size(); i++) {
			LineOnCanvas line = lines.get(i);
			if(line.containsCoordinate(event.getX(),event.getY())) {
				line.mouseClick(event);
			}
		}
	}
	
	public void mouseDrag(MouseEvent event) {
		for(int i = 0; i < lines.size(); i++) {
			LineOnCanvas line = lines.get(i);
			if(line.containsCoordinate(event.getX(),event.getY())) {
				line.mouseDrag(event);
			}
		}
	}
	
	public void mouseMoved(MouseEvent event) {
		for(int i = 0; i < lines.size(); i++) {
			LineOnCanvas line = lines.get(i);
			if(line.containsCoordinate(event.getX(),event.getY())) {
				line.mouseMoved(event);
			}
		}
	}
	
	public ArrayList<Float> getCummulativeWordSizes() {
		return paragraph.getCummulativeWordSizes();
	}

	public Paragraph getParagraph() {
		return paragraph;
	}

	public ArrayList<LineOnCanvas> getLinesOnCanvas() {
		return lines;
	}

	public int getStartIndex() {
		return lines.get(0).getStartIndex();
	}

	public int getEndIndex() {
		return lines.get(lines.size()-1).getEndIndex();
	}

	public void setStyle(TextStyle style) {
		this.style = style;
	}

	public String getText(int i, int j) {
		return paragraph.subSequence(i, j);
	}

	public String getText(TextLine textLine) {
		return getText(textLine.getStartIndex(), textLine.getEndIndex());
	}

}
