package gui.helper;

import geometry.libgdxmath.LineSegment;
import geometry.libgdxmath.Polygon;
import geometry.libgdxmath.Polygon.LineSegmentIntersection;
import geometry.libgdxmath.Rectangle;
import geometry.libgdxmath.Vector2;
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
import document.ParagraphSet;
import document.ParagraphSpace;
import document.TextLine;

public class LayoutMachine {

	private ArrayList<ShapedPane> shapes;
	private PageInsets insets;
	
	public LayoutMachine(){
		shapes = new ArrayList<ShapedPane>();
	}
	
	/**
	 * This function is the entry point for any ParagraphSet to be fitted into a ParagraphSpace.
	 * @param requester
	 * @param paragraphSpace
	 * @param paragraphSet
	 * @param facade
	 * @return
	 */
	public ParagraphOnCanvas getParagraphSpace(ColumnView requester, ParagraphSpace paragraphSpace, ParagraphSet paragraphSet, TextModifyFacade facade){
		ParagraphOnCanvas paragraphOnCanvas = new ParagraphOnCanvas(requester, paragraphSpace, facade);
		paragraphOnCanvas.setParagraphSet(paragraphSet);
		Rectangle allowedSpaceBounds = paragraphSpace.getShape().getBoundingRectangle();
		ArrayList<TextLine> textLines = new ArrayList<TextLine>();
		ArrayList<LineSegment> lineSegments = new ArrayList<LineSegment>();
		
		facade.startTextDivisionForAll();
		
		//STEP 1: divide the shape into line segments
		for(float y = allowedSpaceBounds.y; y <= allowedSpaceBounds.height + allowedSpaceBounds.y; y+= /*style.getLineSpacingHeight() + 2TODO:fix*/40){
			//TODO: take angle into account
			LineSegment inputSegment = new LineSegment(
					new Vector2(allowedSpaceBounds.x, y), 
					new Vector2(allowedSpaceBounds.x + allowedSpaceBounds.width, y));
			
			//divide the initial line segment into further smaller segments, taking shapes into account
			ArrayList<LineSegment> trimmedLineSegments = buildLineSegments(requester, inputSegment, paragraphOnCanvas);
			lineSegments.addAll(trimmedLineSegments);	
		}
		paragraphSet.setLineSegments(lineSegments);
		
		//STEP 2: associate line segments with text lines.
		textLines.addAll(paragraphSet.fillWithAvailableText(lineSegments));
		
		//STEP 3: using text lines and line segments, construct paragraph on canvas.
		buildLineOnCanvases(requester, paragraphOnCanvas, textLines, lineSegments, facade);
		
		for(int i = 0; i < paragraphSet.getParagraphCount(); i++) {
			paragraphSet.getParagraph(i).setParagraphOnCanvas(paragraphOnCanvas);
		}

		return paragraphOnCanvas;
	}
	
	private void updateTextLines(ParagraphSet paragraphSet) {
		ArrayList<LineSegment> lineSegments = paragraphSet.getLineSegments();
		paragraphSet.startTextDivision();
		paragraphSet.fillWithAvailableText(lineSegments);
	}
	
	/**
	 * Builds line on canvases out of given text lines and line segments
	 * @param parent
	 * @param paragraphOnCanvas
	 * @param textLines
	 * @param segments
	 * @param facade
	 */
	private void buildLineOnCanvases(ColumnView parent, ParagraphOnCanvas paragraphOnCanvas, ArrayList<TextLine> textLines, ArrayList<LineSegment> segments, TextModifyFacade facade) {
		if(textLines.size() != segments.size()) {
			throw new RuntimeException("Text Line and Segment sizes don't match");
		}
		for(int i = 0; i < textLines.size(); i++) {
			LineOnCanvas newLine = new LineOnCanvas(parent, paragraphOnCanvas, facade);
			newLine.setTextLine(textLines.get(i));
			newLine.setLineSegment(segments.get(i));
			paragraphOnCanvas.insertLine(newLine);
		}
	}


	/**
	 * Divides a lineSegment with respect to the other elements on the requester.
	 * @param requester
	 * @param inputSegment
	 * @param parentParagraph
	 * @return
	 */
	private ArrayList<LineSegment> buildLineSegments(ColumnView requester, LineSegment inputSegment, ParagraphOnCanvas parentParagraph) {
		ArrayList<LineSegment> segments = new ArrayList<LineSegment>();
		
		LineSegment initialSegment = inputSegment;
		LineSegment trimmedSegment = initialSegment.trimToFitInPolygon(parentParagraph.getAllowedSpace().getShape());
		if(trimmedSegment == null) {
			return segments;
		}
		
		LineSegment trimmedSegmentLower = initialSegment.buildLowerLineSegment(/*TODO:fix*/40, inputSegment.getAngle());
		LineSegment segmentToUse = null;
		
		trimmedSegmentLower = trimmedSegmentLower.trimToFitInPolygon(parentParagraph.getAllowedSpace().getShape());
		
		if(trimmedSegmentLower == null) {
			return segments;
		}
		
		if(Math.abs(trimmedSegment.getLength() - trimmedSegmentLower.getLength()) < GlobalAppSettings.ignoreValuesBelow) {
			System.out.println("EQUAL LENGTH");
		}
		
		if(GlobalAppSettings.selectedFitLineOption == LineFitOption.strictFit) {
			if(trimmedSegment.getLength() - trimmedSegmentLower.getLength() > GlobalAppSettings.ignoreValuesBelowMedium){	
				//adjust back to normal height
				segmentToUse = trimmedSegmentLower.buildLowerLineSegment(/*-1 * newLine.getHeight()TODO:fix*/-40,  inputSegment.getAngle());
			}
			else{
				segmentToUse = trimmedSegment;
			}
		}
		else if(GlobalAppSettings.selectedFitLineOption == LineFitOption.averageFit) {
			LineSegment averageSegment = trimmedSegment.averageLineSegment(trimmedSegmentLower);
			averageSegment = averageSegment.buildLowerLineSegment(/*-0.5f * newLine.getHeight()TODO: fix*/ -20, inputSegment.getAngle());
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
				lowerLineSegment = segments.get(j).buildLowerLineSegment(/*newLine.getHeight()TODO:fix*/40, inputSegment.getAngle());
				LineSegmentIntersection intersection = shape.intersect(segments.get(j));
				LineSegmentIntersection intersection2 = shape.intersect(lowerLineSegment);
				
				segments.remove(j);
				if(intersection.getLineSegment1() != null && intersection.getLineSegment2() == null){
					if(intersection2.getLineSegment1() != null && intersection2.getLineSegment2() == null){
						if(intersection.getLineSegment1().getLength() < intersection2.getLineSegment1().getLength())
							segments.add(0,intersection.getLineSegment1());
						else{
							segments.add(0, intersection2.getLineSegment1().buildLowerLineSegment(/*-1 * newLine.getHeight()TODO:fix*/-40, inputSegment.getAngle()));
						}
					}
					else if(intersection2.getLineSegment1() == null && intersection2.getLineSegment2() == null){
						
					}
					else if(intersection2.getLineSegment1().getLength() + intersection2.getLineSegment2().getLength() < intersection.getLineSegment1().getLength()){
						segments.add(0,intersection2.getLineSegment1().buildLowerLineSegment(/*-1 * newLine.getHeight()TODO:fix*/-40, inputSegment.getAngle()));
						segments.add(0,intersection2.getLineSegment2().buildLowerLineSegment(/*-1 * newLine.getHeight()TODO:fix*/-40, inputSegment.getAngle()));
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

		
		Collections.sort(segments);
		
		System.out.println("\tBuilt those line segments: ");
		
		for(int i = 0; i < segments.size(); i++) {
			System.out.print("\t\t");
			System.out.println(i + ": " + segments.get(i));
		}
		
		System.out.println("\t******************\n");
		
		return segments;
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

/*	public void setParentShape(Polygon shape) {
		this.parentShape = shape;
	}*/
	
	public void setPageInsets(PageInsets insets) {
		this.insets = insets;
	}

}
