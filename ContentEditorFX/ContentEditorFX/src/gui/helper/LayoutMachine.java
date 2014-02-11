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

import settings.GlobalAppSettings;
import settings.GlobalAppSettings.LineFitOption;
import control.TextModifyFacade;
import document.PageInsets;
import document.Paragraph;
import document.ParagraphSpace;
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
	
	public ParagraphOnCanvas getParagraphSpace(ColumnView requester, ParagraphSpace paragraphSpace, TextStyle style, Paragraph text, TextModifyFacade facade){
		ParagraphOnCanvas paragraph = new ParagraphOnCanvas(requester, paragraphSpace, style, facade);
		paragraph.setStyle(style);
		paragraph.setParagraph(text);
		
		Rectangle allowedSpaceBounds = paragraphSpace.getShape().getBoundingRectangle();
		
		for(float y = allowedSpaceBounds.y; y <= allowedSpaceBounds.height + allowedSpaceBounds.y; y+= style.getLineSpacingHeight() + 2){
			LineOnCanvas newLine = new LineOnCanvas(requester, paragraph, facade);
			newLine.initialPositionSetup(allowedSpaceBounds.x, y, allowedSpaceBounds.width, style.getLineSpacingHeight(), 0);
			ArrayList<LineOnCanvas> trimmedLine = buildLineOnCanvas(requester, newLine, paragraph, facade);
			paragraph.insertLines(trimmedLine);
		}
		
		text.setParagraphOnCanvas(paragraph);
		
		for(int i = 0; i < paragraph.getLines().size(); i++) {
			LineOnCanvas tempLine = paragraph.getLines().get(i);
			tempLine.setTextLine(text.getTextLines().get(i));
		}
		
		return paragraph;
	}
	
	private ArrayList<LineOnCanvas> buildLineOnCanvas(ColumnView requester, LineOnCanvas newLine, ParagraphOnCanvas parentParagraph, TextModifyFacade facade) {
		ArrayList<LineOnCanvas> lines = new ArrayList<LineOnCanvas>();
		ArrayList<LineSegment> segments = new ArrayList<LineSegment>();
		
		LineSegment initialSegment = newLine.getLineSegment();
		LineSegment trimmedSegment = initialSegment.trimToFitInPolygon(parentParagraph.getAllowedSpace().getShape());
		if(trimmedSegment == null) {
			return lines;
		}
		
		LineSegment trimmedSegmentLower = initialSegment.buildLowerLineSegment(newLine.getHeight(), newLine.getAngle());
		LineSegment segmentToUse = null;
		
		trimmedSegmentLower = trimmedSegmentLower.trimToFitInPolygon(parentParagraph.getAllowedSpace().getShape());
		System.out.println("TRIMMED: " + trimmedSegment + ", LOWER: " + trimmedSegmentLower);
		
		if(trimmedSegmentLower == null) {
			return lines;
		}
		
		if(Math.abs(trimmedSegment.getLength() - trimmedSegmentLower.getLength()) < GlobalAppSettings.ignoreValuesBelow) {
			System.out.println("EQUAL LENGTH");
		}
		
		if(GlobalAppSettings.selectedFitLineOption == LineFitOption.strictFit) {
			if(trimmedSegment.getLength() - trimmedSegmentLower.getLength() > GlobalAppSettings.ignoreValuesBelowMedium){	
				//adjust back to normal height
				segmentToUse = trimmedSegmentLower.buildLowerLineSegment(-1 * newLine.getHeight(), newLine.getAngle());
			}
			else{
				segmentToUse = trimmedSegment;
			}
		}
		else if(GlobalAppSettings.selectedFitLineOption == LineFitOption.averageFit) {
			LineSegment averageSegment = trimmedSegment.averageLineSegment(trimmedSegmentLower);
			averageSegment = averageSegment.buildLowerLineSegment(-0.5f * newLine.getHeight(), newLine.getAngle());
			segmentToUse = averageSegment;
		}
		else {
			segmentToUse = trimmedSegment;
		}
		
		segments.add(segmentToUse); 
		LineSegment lowerLineSegment = null;
		
		for(int i = 0; i < shapes.size(); i++) {
			Polygon shape = shapes.get(i).getPaneShape();
			for(int j = 0; j < segments.size(); j++) {
				lowerLineSegment = segments.get(j).buildLowerLineSegment(newLine.getHeight(), newLine.getAngle());
				LineSegmentIntersection intersection = shape.intersect(segments.get(j));
				LineSegmentIntersection intersection2 = shape.intersect(lowerLineSegment);
				
				segments.remove(j);
				if(intersection.getLineSegment1() != null && intersection.getLineSegment2() == null){
					if(intersection2.getLineSegment1() != null && intersection2.getLineSegment2() == null){
						if(intersection.getLineSegment1().getLength() < intersection2.getLineSegment1().getLength())
							segments.add(0,intersection.getLineSegment1());
						else{
							segments.add(0, intersection2.getLineSegment1().buildLowerLineSegment(-1 * newLine.getHeight(), newLine.getAngle()));
						}
					}
					else if(intersection2.getLineSegment1() == null && intersection2.getLineSegment2() == null){
						
					}
					else if(intersection2.getLineSegment1().getLength() + intersection2.getLineSegment2().getLength() < intersection.getLineSegment1().getLength()){
						segments.add(0,intersection2.getLineSegment1().buildLowerLineSegment(-1 * newLine.getHeight(), newLine.getAngle()));
						segments.add(0,intersection2.getLineSegment2().buildLowerLineSegment(-1 * newLine.getHeight(), newLine.getAngle()));
					}
				}
				else if(intersection.getLineSegment1() != null && intersection.getLineSegment2() != null){
					if(intersection2.getLineSegment1() != null && intersection2.getLineSegment1().getLength() > intersection.getLineSegment1().getLength() + intersection.getLineSegment2().getLength()){
						segments.add(0, intersection.getLineSegment1());
						segments.add(0, intersection.getLineSegment2());
					}
					else if(intersection2.getLineSegment1() != null) {
						if(Math.abs(intersection.getLineSegment1().getLength() - intersection2.getLineSegment1().getLength()) < GlobalAppSettings.ignoreValuesBelowLarge )
							segments.add(0, intersection.getLineSegment1());
					}
					if(intersection2.getLineSegment2() != null) {
						if(Math.abs(intersection.getLineSegment2().getLength() - intersection2.getLineSegment2().getLength()) < GlobalAppSettings.ignoreValuesBelowLarge )
							segments.add(0, intersection.getLineSegment2());
					}
				}
				else if(intersection.getLineSegment1() == null && intersection.getLineSegment2() == null){
					j--;
				}
			}
		}
		
		//construct the lineoncanvases.
		Paragraph paragraph = parentParagraph.getParagraph();
		paragraph.startTextDivision();
		
		for(int i = 0; i < segments.size(); i++) {
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
