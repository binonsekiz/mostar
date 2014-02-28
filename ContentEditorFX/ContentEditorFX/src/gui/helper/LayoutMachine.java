package gui.helper;

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
import settings.GlobalAppSettings;
import settings.GlobalAppSettings.LineFitOption;
import control.TextModifyFacade;
import document.Column;
import document.PageInsets;
import document.Paragraph;
import document.ParagraphSet;
import document.TextLine;
import document.style.TextStyle;

public class LayoutMachine {

	private ArrayList<Polygon> shapes;
	private PageInsets insets;
	private boolean textDivisionMode;
	private ParagraphSet paragraphSet;
	private Vector2 lineSegmentOffset;
	
	private LineSegment lastUsedSegment;
	private ArrayList<LineSegment> backupSegments;
	private TextStyle backupStyle;
	private Column parent;
	
	public LayoutMachine(Column parent){
		this.parent = parent;
		shapes = new ArrayList<Polygon>();
		backupSegments = new ArrayList<LineSegment>();
		insets = parent.getInsets();
	}
	
	public void initialSetup() {
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
				LineOnCanvas newLine = new LineOnCanvas(parent, paragraphOnCanvas, facade);
				newLine.setTextLine(textLines.get(j));
				newLine.setLineSegment(lineSegments.get(j));
				paragraphOnCanvas.insertLine(newLine);
			}
		}
	}
	
	public void startTextDivision(ParagraphSet paragraphSet) {
		textDivisionMode = true;
		this.paragraphSet = paragraphSet;
		lineSegmentOffset = paragraphSet.getParagraphSpace().getTextDivisionStartPoint();
		backupSegments.clear();
		lastUsedSegment = null;
		
		//TODO: erase this line
		DocumentView.getDebugContext().clearRect(0, 0, 2000, 2000);
	}
	
	public LineSegment getNextAvailableLineSegment(TextStyle style) {
		System.out.println("\n\n$$$$$$LayoutMachine::GetNextAvailableLineSegment started.");
		System.out.println("\t\tLastUsedSegment: " + lastUsedSegment);
		
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
			System.out.println("\tOption 1");
			retVal = lastUsedSegment;
		}
		else if(backupSegments.size() > 0){
			System.out.println("\tOption 2");
			retVal  = backupSegments.get(0);
			backupSegments.remove(0);
		}
		else {
			System.out.println("\tOption 3");
			float angle = paragraphSet.getAngle();
			Vector2 endPoint = new Vector2(
					(float) (lineSegmentOffset.x + insets.getPageWidth() * Math.cos(Math.toRadians(angle))), 
					(float) (lineSegmentOffset.y + insets.getPageWidth() * Math.sin(Math.toRadians(angle))));
			
			LineSegment inputSegment = new LineSegment(
					lineSegmentOffset, endPoint);
			
			ArrayList<LineSegment> intersection = buildLineSegments(inputSegment, style.getLineSpacingHeight());
			
			if(intersection.size() > 0) {
				retVal = intersection.get(0);
				intersection.remove(0);
				backupSegments.addAll(intersection);
			}
			else{
				System.out.println("----Boþ geçecek");
			}
		}

		System.out.println("\n***Line Segment will return: " + retVal + "\n");
		lastUsedSegment = retVal.cpy();	
		return retVal;
	}

	private void moveToNextLine() {
		float angle = paragraphSet.getAngle();
		angle = (angle + 270) % 360;
		
		lineSegmentOffset.moveWithGivenAngle(angle, backupStyle.getLineSpacingHeight());
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
		debugPaintLineSegment(trimmedSegment, Color.ORANGE);
		
		System.out.println("\t\ttrimmed: " + trimmedSegment);
		if(trimmedSegment == null) {
			return segments;
		}
		
		LineSegment trimmedSegmentLower = initialSegment.buildLowerLineSegment(height, inputSegment.getAngle());
		LineSegment segmentToUse = null;
		
		trimmedSegmentLower = trimmedSegmentLower.trimToFitInPolygon(paragraphSet.getParagraphSpace().getShape());
		debugPaintLineSegment(trimmedSegmentLower, Color.RED);
		
		if(trimmedSegmentLower == null) {
			return segments;
		}
		
		if(Math.abs(trimmedSegment.getLength() - trimmedSegmentLower.getLength()) < GlobalAppSettings.ignoreValuesBelow) {
			System.out.println("EQUAL LENGTH");
		}
		
		if(GlobalAppSettings.selectedFitLineOption == LineFitOption.strictFit) {
			if(trimmedSegment.getLength() > trimmedSegmentLower.getLength()) {
				segmentToUse = trimmedSegmentLower.buildLowerLineSegment(-1 * height, inputSegment.getAngle());
			}
			else{
				segmentToUse = trimmedSegment;
			}
		}
		else if(GlobalAppSettings.selectedFitLineOption == LineFitOption.averageFit) {
			LineSegment averageSegment = trimmedSegment.averageLineSegment(trimmedSegmentLower);
			averageSegment = averageSegment.buildLowerLineSegment(-0.5f * height, inputSegment.getAngle());
			segmentToUse = averageSegment;
		}
		else {
			//segmentToUse = trimmedSegment;
			if(trimmedSegment.getLength() > trimmedSegmentLower.getLength()) {
				segmentToUse = trimmedSegment;
			}
			else{
				segmentToUse = trimmedSegmentLower.buildLowerLineSegment(-1 * height, inputSegment.getAngle());
			}
		}
		
		segments.add(segmentToUse); 
		LineSegment lowerLineSegment = null;
		
		for(int i = 0; i < shapes.size(); i++) {
			Polygon shape = shapes.get(i);
			for(int j = 0; j < segments.size(); j++) {
				lowerLineSegment = segments.get(j).buildLowerLineSegment(height, inputSegment.getAngle());
				LineSegmentIntersection intersection = shape.intersect(segments.get(j));
				LineSegmentIntersection intersection2 = shape.intersect(lowerLineSegment);
				
				segments.remove(j);
				if(intersection.getLineSegment1() != null && intersection.getLineSegment2() == null){
					if(intersection2.getLineSegment1() != null && intersection2.getLineSegment2() == null){
						if(intersection.getLineSegment1().getLength() < intersection2.getLineSegment1().getLength())
							segments.add(0,intersection.getLineSegment1());
						else{
							segments.add(0, intersection2.getLineSegment1().buildLowerLineSegment(-1 * height, inputSegment.getAngle()));
						}
					}
					else if(intersection2.getLineSegment1() == null && intersection2.getLineSegment2() == null){
						
					}
					else if(intersection2.getLineSegment1().getLength() + intersection2.getLineSegment2().getLength() < intersection.getLineSegment1().getLength()){
						segments.add(0,intersection2.getLineSegment1().buildLowerLineSegment(-1 * height, inputSegment.getAngle()));
						segments.add(0,intersection2.getLineSegment2().buildLowerLineSegment(-1 * height, inputSegment.getAngle()));
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
		System.out.println("\t\tBuilt those line segments: ");
		
		for(int i = 0; i < segments.size(); i++) {
			System.out.print("\t\t\t");
			System.out.println(i + ": " + segments.get(i));
		}
		
		System.out.println("\t\t******************\n");
		
		return segments;
	}
	
	public void addSingleElement(Polygon shape){
		shapes.add(shape);
	}
	
	private void disregardLastSegmentForFutureUse() {
		lastUsedSegment = null;
		System.out.println("LAST SEGMENT SET TO NULL");
		backupSegments.clear();
		moveToNextLine();
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
}
