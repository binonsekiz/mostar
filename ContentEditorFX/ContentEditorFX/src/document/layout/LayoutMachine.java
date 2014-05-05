package document.layout;

import geometry.libgdxmath.LineSegment;
import geometry.libgdxmath.Polygon;
import geometry.libgdxmath.Polygon.LineSegmentIntersection;
import geometry.libgdxmath.Vector2;
import gui.columnview.ColumnView;
import gui.columnview.DocumentView;
import gui.columnview.LineOnCanvas;
import gui.columnview.ParagraphOnCanvas;

import java.util.ArrayList;
import java.util.Collections;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
//import javafx.scene.canvas.GraphicsContext;
//import javafx.scene.paint.Color;
import settings.GlobalAppSettings;
import settings.GlobalAppSettings.LineFitOption;
import control.TextModifyFacade;
import document.Column;
import document.PageInsets;
import document.Paragraph;
import document.ParagraphSet;
import document.TextLine;
import document.style.TextStyle;

/**
 * Here is how this works:
 * 
 * @author sahin
 *
 */
public class LayoutMachine{
	
	private PageInsets insets;
	private boolean textDivisionMode;
	private ParagraphSet paragraphSet;
	private Vector2 lineSegmentOffset;
	
	private LineSegment lastUsedSegment;
	private ArrayList<LineSegment> backupSegments;
	private TextStyle backupStyle;
	private Column parent;
	
	private TextModifyFacade facade;
	private boolean isOutOfSpace;
	
	public LayoutMachine(Column parent){
		this.parent = parent;
		backupSegments = new ArrayList<LineSegment>();
		insets = parent.getInsets();
	}
	
	public void initialSetup() {
		System.out.println("1.Initial Setup");
		isOutOfSpace = false;
		ArrayList<ParagraphSet> sets = parent.getParagraphSets();
		for(int i = 0; i < sets.size(); i++) {
			startTextDivision(sets.get(i));
			sets.get(i).fillWithAvailableText();
		}
	}
	
	/**
	 * Builds line on canvases out of given text lines and line segments
	 * @param parent
	 * @param paragraphOnCanvas
	 * @param textLines
	 * @param segments
	 * @param facade
	 */
	public void buildLineOnCanvases(ColumnView parent, ParagraphOnCanvas paragraphOnCanvas, ParagraphSet set, TextModifyFacade facade) {
		for(int i = 0; i < set.getParagraphCount(); i++) {
			Paragraph paragraph = set.getParagraph(i);
			ArrayList<TextLine> textLines = paragraph.getTextLines();
			Collections.sort(textLines);
			ArrayList<LineSegment> lineSegments = paragraph.getLineSegments();
			
			//At this moment, each line segment corresponds to one or more text lines.
			for(int j = 0; j < textLines.size(); j++) {
				buildSingleLineOnCanvas(parent, paragraphOnCanvas, textLines.get(i), lineSegments.get(i));
			}
		}
	}
	
	public void validateLineOnCanvases(ParagraphSet paragraphSet, ColumnView columnView) {
		ParagraphOnCanvas paragraphOnCanvas = null;
		for(int i = 0; i < columnView.getParagraphsOnCanvas().size(); i++) {
			if(columnView.getParagraphsOnCanvas().get(i).getParagraphSet() == paragraphSet){
				paragraphOnCanvas = columnView.getParagraphsOnCanvas().get(i);
				break;
			}
		}
		
		int lineOnCanvasCounter = 0;
		
		for(int j = 0; j < paragraphSet.getParagraphCount(); j++) {
			Paragraph paragraph = paragraphSet.getParagraph(j);
			
			System.out.println("\n\n\n\n\n\nvalidate call with paragraph: " + paragraph + ", columnview: " + columnView);
			System.out.println("paragraphOnCanvas.linesoncanvas1:" + paragraphOnCanvas.getLinesOnCanvas());
		
			for(int i = 0; i < paragraph.getLineSegments().size(); i++) {
				TextLine textLine = paragraph.getTextLines().get(i);
				if(lineOnCanvasCounter >= paragraphOnCanvas.getLinesOnCanvas().size()) {
					//an additional line on canvas is needed here.
					buildSingleLineOnCanvas(columnView, paragraphOnCanvas, paragraph.getTextLines().get(i), paragraph.getLineSegments().get(i));
				}
				paragraphOnCanvas.getLinesOnCanvas().get(lineOnCanvasCounter).setTextLine(textLine);
				paragraphOnCanvas.getLinesOnCanvas().get(lineOnCanvasCounter).setLineSegment(paragraph.getLineSegments().get(i));
				lineOnCanvasCounter ++;
			}
		
			System.out.println("\n\n\n\n\n\nvalidate call with paragraph: " + paragraph + ", columnview: " + columnView);
			System.out.println("paragraphOnCanvas.linesoncanvas2:" + paragraphOnCanvas.getLinesOnCanvas());
		}
		
		for(int i = paragraphOnCanvas.getLinesOnCanvas().size() - 1; i >= lineOnCanvasCounter; i--) {
			paragraphOnCanvas.getLinesOnCanvas().remove(i);
		}
	}
	
	private void buildSingleLineOnCanvas(ColumnView view, ParagraphOnCanvas paragraphOnCanvas, TextLine textLine, LineSegment lineSegment) {
		LineOnCanvas newLine = new LineOnCanvas(view, paragraphOnCanvas, facade);
		newLine.setTextLine(textLine);
		newLine.setLineSegment(lineSegment);
		paragraphOnCanvas.insertLine(newLine);
	}
	
	private void startTextDivision(ParagraphSet paragraphSet) {
		textDivisionMode = true;
		this.paragraphSet = paragraphSet;
		lineSegmentOffset = paragraphSet.getParagraphSpace().getTextDivisionStartPoint();
		backupSegments.clear();
		lastUsedSegment = null;
		isOutOfSpace = false;
		
		//TODO: erase this line
		DocumentView.getDebugContext().clearRect(0, 0, 2000, 2000);
	}

	public LineSegment getDefaultSegment(TextStyle style) {
		return getNextAvailableLineSegment(style);
	}
	
	public boolean isOutOfSpace() {
		return isOutOfSpace;
	}
	
	public LineSegment getNextAvailableLineSegment(TextStyle style) {
		debugPaintVector2(lineSegmentOffset, Color.BLUE);
		
		if(textDivisionMode == false || paragraphSet == null) {
			throw new RuntimeException("TextDiv: " + textDivisionMode + ", paragraphSet: " + paragraphSet);
		}
		if(backupStyle != style) {
			backupSegments.clear();
			backupStyle = style;
		}
		
		LineSegment retVal = null;
		
		if(lastUsedSegment != null) {
			retVal = lastUsedSegment;
		}
		else if(backupSegments.size() > 0){
			retVal  = backupSegments.get(0);
			backupSegments.remove(0);
		}
		else {
			float angle = paragraphSet.getAngle();
			
			LineSegment inputSegment = new LineSegment(lineSegmentOffset, insets.getPageWidth(), angle);
			
			ArrayList<LineSegment> intersection = buildLineSegments(inputSegment, style.getLineSpacingHeight());
			if(intersection.size() > 0) {
				retVal = intersection.get(0);
				intersection.remove(0);
				backupSegments.addAll(intersection);
			}
		}

		if(retVal != null) {
			lastUsedSegment = retVal.cpy();	
		}

		return retVal;
	}

	private void moveToNextLine() {
		float angle = paragraphSet.getAngle();
		angle = (angle + 90) % 360;
		
		lineSegmentOffset.moveWithGivenAngle(angle, backupStyle.getLineSpacingHeight());
		
		//if the offset is outside the ParagraphSpace, we are out of space.
		if(!paragraphSet.getParagraphSpace().canContain(lineSegmentOffset, angle)) {
			isOutOfSpace = true;
		}
	}

	/**
	 * Divides a lineSegment with respect to the other elements on the requester.
	 * @param requester
	 * @param inputSegment
	 * @param parentParagraph
	 * @return
	 */
	private ArrayList<LineSegment> buildLineSegments(LineSegment inputSegment, float height) {
		System.out.println("\n\tLayoutMachine::BuildLineSegments started");
		
		ArrayList<LineSegment> segments = new ArrayList<LineSegment>();
		
		LineSegment initialSegment = inputSegment;
		LineSegment trimmedSegment = initialSegment.trimToFitInPolygon(paragraphSet.getParagraphSpace().getShape());
		
		System.out.println("\t\ttrimmed: " + trimmedSegment);
		if(trimmedSegment == null) {
			return segments;
		}
		
		LineSegment trimmedSegmentLower = initialSegment.buildLowerLineSegment(height);
		LineSegment segmentToUse = null;
		
		trimmedSegmentLower = trimmedSegmentLower.trimToFitInPolygon(paragraphSet.getParagraphSpace().getShape());
		
		debugPaintLineSegment(trimmedSegment, Color.GREEN);
		debugPaintLineSegment(trimmedSegmentLower, Color.BLUE);
		
		if(trimmedSegmentLower == null) {
			return segments;
		}
		
		if(GlobalAppSettings.selectedFitLineOption == LineFitOption.strictFit) {
			if(trimmedSegment.getLength() > trimmedSegmentLower.getLength()) {
				segmentToUse = trimmedSegmentLower.buildLowerLineSegment(-1 * height);
			}
			else{
				segmentToUse = trimmedSegment;
			}
		}
		else if(GlobalAppSettings.selectedFitLineOption == LineFitOption.averageFit) {
			LineSegment averageSegment = trimmedSegment.averageLineSegment(trimmedSegmentLower);
			averageSegment = averageSegment.buildLowerLineSegment(-0.5f * height);
			segmentToUse = averageSegment;
		}
		else {
			//segmentToUse = trimmedSegment;
			if(trimmedSegment.getLength() > trimmedSegmentLower.getLength()) {
				segmentToUse = trimmedSegment;
			}
			else{
				segmentToUse = trimmedSegmentLower.buildLowerLineSegment(-1 * height);
			}
		}
		
		segments.add(segmentToUse); 
		ArrayList<Polygon> shapes = parent.getShapesAndWidgetPolygons();
		
		modifySegmentsWRTShapes(segments, shapes, height);
		
		System.out.println("\t\tBuilt those line segments: ");
		
		for(int i = 0; i < segments.size(); i++) {
			System.out.print("\t\t\t");
			System.out.println(i + ": " + segments.get(i));
			//debugPaintLineSegment(segments.get(i), Color.ORCHID);
		}
		
		System.out.println("\t\t******************\n");
		
		return segments;
	}
	
	private void modifySegmentsWRTShapes(ArrayList<LineSegment> segments, ArrayList<Polygon> shapes, float height) {
		if(segments.size() != 1) throw new RuntimeException("Expecting only one segment");
		
		for(int j = 0; j < shapes.size(); j++) {
			Polygon shape = shapes.get(j);
			for(int k = 0; k < segments.size(); k++) {
				LineSegment segment = segments.get(k);
				LineSegmentIntersection intersection = shape.intersectWithHeight(segment, height);
				
				segments.remove(k);
				if(intersection == null) {
					k--;
				}
				else if(intersection.getLineSegment1() != null && intersection.getLineSegment2() == null) {
					segments.add(intersection.getLineSegment1());
				}
				else if(intersection.getLineSegment1() != null && intersection.getLineSegment2() != null) {
					segments.add(intersection.getLineSegment1());
					segments.add(intersection.getLineSegment2());
				}
				else if(intersection.getLineSegment1() == null && intersection.getLineSegment2() == null) {
					k--;
				}
			}
		}
		Collections.sort(segments);
	}

	@SuppressWarnings("unused")
	private void debugPaintPolygon(Polygon polygon) {
		GraphicsContext context = DocumentView.getDebugContext();
		context.save();
		context.setStroke(Color.BLUE);
		context.setLineWidth(2);
		polygon.draw(context);
		context.restore();
	}

	private void disregardLastSegmentForFutureUse() {
		lastUsedSegment = null;
		System.out.println("LAST SEGMENT SET TO NULL");
		if(backupSegments.size() == 0) {
			moveToNextLine();
		}
	}

	private void reuseSegmentFromOffset(float trimAmount) {
		LineSegment segment = new LineSegment(lastUsedSegment.getFirstPoint(), lastUsedSegment.getSecondPoint());
		lastUsedSegment = segment;
		lastUsedSegment.trimWithStartOffset(trimAmount);
	}
	
	private void debugPaintLineSegment(LineSegment segment, Color color) {
		if(segment == null) return;
		GraphicsContext context = DocumentView.getDebugContext();
		context.save();
		context.setStroke(color);
		segment.draw(context);
		context.restore();
	}
	
	private void debugPaintVector2(Vector2 vector, Color color) {
		if(vector == null) return;
		GraphicsContext context = DocumentView.getDebugContext();
		context.save();
		context.setStroke(color);
		context.strokeOval(vector.x-2, vector.y-2, 4, 4);
		context.restore();
	}

	public void reportLastUsedWidth(float lastCalculatedWidth) {
		System.out.println("REPORTING WIDTH: " + lastCalculatedWidth);
		if(Math.abs(lastCalculatedWidth) < GlobalAppSettings.ignoreValuesBelow) {
			disregardLastSegmentForFutureUse();
		}
		else{
			reuseSegmentFromOffset(lastCalculatedWidth);
		}
	}

	public void setTextModifyFacade(TextModifyFacade facade) {
		this.facade = facade;
	}		
}
