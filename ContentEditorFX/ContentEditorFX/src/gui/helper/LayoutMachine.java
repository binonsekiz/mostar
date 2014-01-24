package gui.helper;

import geometry.libgdxmath.LineSegment;
import geometry.libgdxmath.Polygon;
import geometry.libgdxmath.Polygon.LineSegmentIntersection;
import geometry.libgdxmath.Rectangle;
import gui.ShapedPane;
import gui.columnview.ColumnView;
import gui.columnview.LineOnCanvas;
import gui.columnview.ParagraphOnCanvas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import control.TextModifyFacade;
import document.PageInsets;
import document.Paragraph;
import document.TextStyle;

public class LayoutMachine {

	private ArrayList<ShapedPane> shapes;
	private PageInsets insets;
	private Polygon parentShape;
	
	private ArrayList<LineOnCanvas> lines;
	
	public LayoutMachine(){
		shapes = new ArrayList<ShapedPane>();
		lines = new ArrayList<LineOnCanvas>();
	}
	
	public ParagraphOnCanvas getParagraphSpace(ColumnView requester, Rectangle allowedSpace, TextStyle style, Paragraph text, TextModifyFacade facade){
		ParagraphOnCanvas paragraph = new ParagraphOnCanvas(requester, allowedSpace, style, facade);
		
		//TODO: take textstyle into account
		for(int y = (int) allowedSpace.y; y <= allowedSpace.height; y+= style.getLineSpacingHeight() + 2){
			LineOnCanvas newLine = new LineOnCanvas(requester, paragraph, facade);
			newLine.initialPositionSetup(allowedSpace.x, y, allowedSpace.width, style.getLineSpacingHeight(), 0);
			ArrayList<LineOnCanvas> trimmedLine = buildLineOnCanvas(requester, newLine, paragraph, facade);
			paragraph.insertLines(trimmedLine);
		}
		
		paragraph.setParagraph(text);
		
		return paragraph;
	}
	
	private ArrayList<LineOnCanvas> buildLineOnCanvas(ColumnView requester, LineOnCanvas newLine, ParagraphOnCanvas parentParagraph, TextModifyFacade facade) {
		ArrayList<LineOnCanvas> lines = new ArrayList<LineOnCanvas>();
		ArrayList<LineSegment> segments = new ArrayList<LineSegment>();
		
		segments.add(newLine.getLineSegment()); 
		
		for(int i = 0; i < shapes.size(); i++) {
			Polygon shape = shapes.get(i).getPaneShape();
			for(int j = 0; j < segments.size(); j++) {
				LineSegmentIntersection intersection = shape.intersect(segments.get(j), requester);
				segments.remove(j);
				if(intersection.getLineSegment1() != null && intersection.getLineSegment2() == null){
					segments.add(0,intersection.getLineSegment1());
				}
				else if(intersection.getLineSegment1() != null && intersection.getLineSegment2() != null){
					segments.add(0, intersection.getLineSegment1());
					segments.add(0, intersection.getLineSegment2());
				}
				else if(intersection.getLineSegment1() == null && intersection.getLineSegment2() == null){
					j--;
				}
			}
		}
		
		//construct the lineoncanvases.
		for(int i = 0; i < segments.size(); i++){
			LineOnCanvas tempLine = new LineOnCanvas(requester, parentParagraph, facade);
			tempLine.setLineSegment(segments.get(i));
			
			tempLine.setHeight(newLine.getHeight());
			tempLine.setAngle(newLine.getAngle());
			
			lines.add(tempLine);
		}
		
		Collections.sort(lines);
		
		return lines;
	}
	
	public void addSingleElement(ShapedPane shape){
		shapes.add(shape);
	}
	
	public void addElements(Collection<? extends ShapedPane> collection){
		shapes.addAll(collection);
	}
	
	public void clear(){
		shapes.clear();
	}

	public void setParentShape(Polygon shape) {
		this.parentShape = shape;
	}
	
	public void setPageInsets(PageInsets insets) {
		this.insets = insets;
	}

}
