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
import document.layout.LayoutMachine;


/**
 * This is the view counterpart of a ParagraphSet
 * @author sahin
 *
 */
public class ParagraphOnCanvas{

	private ColumnView parent;
	
	private ParagraphSpace allowedSpace;
	private ArrayList<LineOnCanvas> lines; 
	private ParagraphSet paragraphSet;
	
	private TextModifyFacade textModifyFacade;
	
	public ParagraphOnCanvas(ColumnView parent, ParagraphSet paragraphSet, TextModifyFacade facade) {
		System.out.println("ParagraphOnCanvas initialized");
		this.parent = parent; 
		this.paragraphSet = paragraphSet;
		allowedSpace = paragraphSet.getParagraphSpace();
		this.textModifyFacade = facade;
		lines = new ArrayList<LineOnCanvas>();
		
		getLayoutMachine().setTextModifyFacade(facade);
		getLayoutMachine().buildLineOnCanvases(parent, this, paragraphSet, facade);
	}

	public LayoutMachine getLayoutMachine() {
		return parent.getLayoutMachine();
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
	//	lines.clear();
	//	getLayoutMachine().buildLineOnCanvases(parent, this, paragraphSet, textModifyFacade);
		for(int i = 0; i < paragraphSet.getParagraphCount(); i++) {
			paragraphSet.getParagraph(i).validateLineOnCanvases(parent);
		}
		for(int i = 0; i < lines.size(); i++){
			lines.get(i).refresh();
		}
		
		allowedSpace.draw(parent.getGraphicsContext());
	}
	
	public void refreshOverlay() {
		GraphicsContext context = parent.getOverlayContext();
		textModifyFacade.getCaret().drawCaret(parent.getBoundsInParent().getMinX(), parent.getBoundsInParent().getMinY(), context);
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

	public boolean containsParagraph(Paragraph paragraph) {
		for(int i = 0; i < paragraphSet.getParagraphCount(); i++){
			if(paragraphSet.getParagraph(i) == paragraph)
				return true;
		}
		return false;
	}

}
