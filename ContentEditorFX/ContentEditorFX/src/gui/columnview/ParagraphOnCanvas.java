package gui.columnview;

import gui.docmodify.DocDebugView;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import control.TextModifyFacade;
import document.Document;
import document.Paragraph;
import document.ParagraphSet;
import document.ParagraphSpace;
import document.TextLine;


/**
 * This is basically a set of LineOnCanvas'es that share the same TextStyle
 * @author sahin
 *
 */
public class ParagraphOnCanvas {

	private ColumnView parent;
	
	private ParagraphSpace allowedSpace;
	private ArrayList<LineOnCanvas> lines; 
	private ParagraphSet paragraphSet;
	
	private TextModifyFacade textModifyFacade;
	
	public ParagraphOnCanvas(ColumnView parent, ParagraphSpace allowedSpace, TextModifyFacade facade) {
		System.out.println("ParagraphView initialized");
		this.parent = parent; 
		this.allowedSpace = allowedSpace;
		this.textModifyFacade = facade;
		lines = new ArrayList<LineOnCanvas>();
	}

	public ParagraphSpace getAllowedSpace() {
		return allowedSpace;
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
		return paragraphSet.getText();
	}

	/**
	 * Forces refresh on the column this paragraph is in
	 */
	public void updateColumn() {
		parent.refresh();
	}

	public void setParagraphSet(ParagraphSet paragraph) {
		this.paragraphSet = paragraph;
	}
	
	public String toString() {
		return paragraphSet.toString();
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
	
	/*public ArrayList<Float> getCummulativeWordSizes() {
		return paragraph.getCummulativeWordSizes();
	}*/

	public Paragraph getParagraph(int index) {
		return paragraphSet.getParagraph(index);
	}
	
	public ParagraphSet getParagraphSet() {
		return paragraphSet;
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

	public String getText(int i, int j) {
		return paragraphSet.getText(i, j);
	}

	public String getText(TextLine textLine) {
		return getText(textLine.getStartIndex(), textLine.getEndIndex());
	}

}
