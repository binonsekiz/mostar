package gui.columnview;

import geometry.GeometryHelper;
import geometry.libgdxmath.LineSegment;
import geometry.libgdxmath.Polygon;
import geometry.libgdxmath.Vector2;
import gui.docmodify.DocDebugView;
import gui.helper.DebugHelper;

import java.util.ArrayList;
import java.util.Collections;

import settings.GlobalAppSettings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import com.sun.javafx.tk.FontMetrics;

import control.Caret;
import control.TextModifyFacade;
import document.Paragraph;
import document.TextLine;

public class LineOnCanvas implements Comparable<LineOnCanvas>{

	private static int debugObjectCounter = 1;
	private int debugObjectCount;
	
	// caret start and end values, same for all lines.
	// actual indexes in line are calculated in refresh
	public static int selectedStartIndex;
	public static int selectedEndIndex;
	
	private DoubleProperty preferredWidthProperty;
	
	private ColumnView parent;
	private ParagraphOnCanvas parentParagraph;
	private ArrayList<Float> letterSizes;
	private ArrayList<Float> caretPositions;

	private SimpleObjectProperty<LineSegment> lineSegmentProperty;
	private LineSegment lowerLineSegment;
	private SimpleObjectProperty<TextAlignment> alignmentProperty;
	private Polygon shape;
	
	private float layoutX;
	private float layoutY;
	private float height;
	private float width;
	private float angle;
	
	private TextModifyFacade textModifyFacade;
	private TextLine textLine;

	private Color polygonColor;
	
	public LineOnCanvas(ColumnView parent, ParagraphOnCanvas parentParagraph, TextModifyFacade textModifyFacade) {
		debugObjectCount = debugObjectCounter;
		debugObjectCounter++;
		
		System.out.println("Line on Canvas initialized: " + debugObjectCount);
		this.parent = parent;
		this.parentParagraph = parentParagraph;
		this.textModifyFacade = textModifyFacade;
		
		preferredWidthProperty = new SimpleDoubleProperty();
		alignmentProperty = new SimpleObjectProperty<TextAlignment>();
		alignmentProperty.set(TextAlignment.JUSTIFY);
		lineSegmentProperty = new SimpleObjectProperty<LineSegment>();
		lineSegmentProperty.set(new LineSegment(new Vector2(), new Vector2()));
		polygonColor = Color.DARKRED;
		
		initEvents();
	}
	
	public void initialPositionSetup(float layoutX, float layoutY, float width, float height, float angle) {
		this.layoutX = layoutX;
		this.layoutY = layoutY;
		this.preferredWidthProperty.set(width);
		this.height = height;
		this.width = width;
		this.angle = angle;
		revalidateLineSegment();
	}
	
	public void setTextLine(TextLine line) {
		this.textLine = line;
	}
	
	private void revalidateLineSegment() {
		LineSegment lineSegment = new LineSegment(
				new Vector2(layoutX, layoutY), 
				new Vector2( (float)(layoutX + Math.cos(Math.toRadians(angle)) * width), 
						     (float)(layoutY + Math.sin(Math.toRadians(angle)) * width)));
		lineSegmentProperty.set(lineSegment);
	}
	
/*	public void initialTextSetup(int startIndex, int endIndex) {
		startIndexInStyledText.set(startIndex);
		endIndexInStyledText.set(endIndex);
	}*/
	
	public void setLineSegment(LineSegment line) {
		lineSegmentProperty.set(line);
		layoutX = line.getFirstPoint().x;
		layoutY = line.getFirstPoint().y;
		width = (float) line.getLength();
	}
	
	public void setPreferredWidth(double width) {
		this.preferredWidthProperty.set(width);
	}

	public void refresh(){
		GraphicsContext context = parent.getGraphicsContext();
		String text = parentParagraph.getText(textLine);
		
		context.setLineWidth(0.3);
		LineSegment line = lineSegmentProperty.get();

		if(this.getColumnView().getDocumentView().getLinePolygonsVisible()) {
			context.setStroke(polygonColor);
			context.strokePolygon(shape.getTransformedXVertices(), shape.getTransformedYVertices(), shape.getEdgeCount());
		}
		
		if(GlobalAppSettings.areLineViewCountsVisible) {
			DebugHelper.helperStyle1.prepareContext(context);
			context.setFill(Color.MAGENTA);
			context.fillText(textLine.getStartIndex() + "", line.getLeftPoint().x - 20, line.getLeftPoint().y + 15);
			context.fillText(textLine.getEndIndex() + "", line.getRightPoint().x + 5, line.getRightPoint().y + 15);
		}
		
		context.setFill(parentParagraph.getStyle().getStrokeColor());
		context.setLineWidth(1f);
		context.setFont(parentParagraph.getFont());
	
		if(text != null) {
			float startX = 0;
			float startY = 0;
			if(isTextUpsideDown()){
				startX = line.getRightPoint().x;
				startY = line.getRightPoint().y;
			}	
			else{
				startX = line.getLeftPoint().x;
				startY = line.getLeftPoint().y;
			}
			
			if((selectedEndIndex == 0 && selectedStartIndex == 0) || selectedEndIndex == selectedStartIndex) {
				context.fillText(text, startX, startY + this.height);
			}
			else{
				if(caretPositions == null) {
					initializeLetterSizes();
				}
				
				int startIndexAdjusted = selectedStartIndex - textLine.getStartIndex();
				int endIndexAdjusted = selectedEndIndex - textLine.getStartIndex();
				
				startIndexAdjusted = Math.max(0, Math.min(textLine.getLength(), startIndexAdjusted));
				endIndexAdjusted = Math.max(0, Math.min(textLine.getLength(), endIndexAdjusted));
				
				System.out.println("Selected Start Index: " + selectedStartIndex + ", text start: " + textLine.getStartIndex());
				System.out.println("Selected End Index: " + selectedEndIndex + ", text end: " + textLine.getEndIndex());
				
				context.fillText(text.substring(0, startIndexAdjusted), startX, startY + this.height);
				context.save();
				context.setFill(parentParagraph.getStyle().getSelectionColor());
				
				context.fillRect(startX + caretPositions.get(startIndexAdjusted), startY, caretPositions.get(endIndexAdjusted) - caretPositions.get(startIndexAdjusted), this.height);
				context.setFill(parentParagraph.getStyle().getInvertedStrokeColor());
				context.fillText(text.substring(startIndexAdjusted, endIndexAdjusted), startX + caretPositions.get(startIndexAdjusted), startY + this.height);
				
				context.restore();
				context.fillText(text.substring(endIndexAdjusted), startX + caretPositions.get(endIndexAdjusted), startY + this.height);
			}
		}
	}
	
	private boolean isTextUpsideDown() {
		float angleTemp = ((angle % 360) + 360) % 360;
		if(angleTemp > 90 && angleTemp < 270){
			return true;
		}
		return false;
	}
	
	private void initEvents(){
		preferredWidthProperty.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
			}
		});
		
		alignmentProperty.addListener(new ChangeListener<TextAlignment>() {
			@Override
			public void changed(ObservableValue<? extends TextAlignment> arg0,
					TextAlignment arg1, TextAlignment arg2) {
			}
		});
		
		lineSegmentProperty.addListener(new ChangeListener<LineSegment>() {
			@Override
			public void changed(ObservableValue<? extends LineSegment> arg0,
					LineSegment arg1, LineSegment arg2){
				if(arg0.getValue() != null) {
					layoutX = arg0.getValue().getLeftPoint().x;
					layoutY = arg0.getValue().getLeftPoint().y;
					setPreferredWidth(arg0.getValue().getLength());
					shape = GeometryHelper.getRectanglePolygon(lineSegmentProperty.get(), height, angle);
					lowerLineSegment = arg0.getValue().buildLowerLineSegment(height, angle);
				}
			}
		});
	}
	
	public int getStartIndex() {
		return textLine.getStartIndex();
	}
	
	public int getEndIndex() {
		return textLine.getEndIndex();
	}

	public void setLayout(float x, float y) {
		this.layoutX = x;
		this.layoutY = y;
	}
	
	@Override
	public String toString() {
		return "x: " + layoutX + ", y: " + layoutY + " width: " + preferredWidthProperty.get();
	}

	public LineSegment getLineSegment() {
		return lineSegmentProperty.get();
	}

	public float getWidth() {
		return (float) lineSegmentProperty.get().getLength();
	}

	@Override
	public int compareTo(LineOnCanvas o) {
		int value = this.getLineSegment().compareTo(o.getLineSegment());
		if(isTextUpsideDown())
			return -1 * value;
		else
			return value;
	}

	public boolean containsCoordinate(double x, double y) {
		return shape.contains((float)x, (float)y);
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
		revalidateLineSegment();
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
		revalidateLineSegment();
	}
	
	private float calculateDistanceFromStart(float x, float y) {
		LineSegment segment = lineSegmentProperty.get();
		Vector2 closestPoint = segment.closestPoint(x,y);
		return segment.getFirstPoint().dst(closestPoint);
	}
	
	private void initializeLetterSizes() {
		FontMetrics metrics = parentParagraph.getStyle().getFontMetrics();
		letterSizes = new ArrayList<Float>();
		caretPositions = new ArrayList<Float>();
		String text = parentParagraph.getText(textLine);
		if(text != null){
			System.out.println("text: " + text  + ", size: " + text.length());
			float currentWidth = 0;
			caretPositions.add(0f);
			for(int i = 1; i <= text.length(); i++) {
				float nextWidth = metrics.computeStringWidth(text.substring(0, i));
				letterSizes.add((currentWidth + nextWidth ) /2);
				caretPositions.add(nextWidth);
				currentWidth = nextWidth;
			}
			
			System.out.println("CALCULATED with size: " + letterSizes.size() + ", caretPosSize: " + caretPositions.size());
		}
	}
	
	private int findClickedIndex(MouseEvent event) {
		float distance = calculateDistanceFromStart((float) event.getX(), (float) event.getY());
		if(letterSizes == null){
			initializeLetterSizes();
		}
		int index = Collections.binarySearch(letterSizes, distance);
		if(index < 0) index = index * -1 - 1;
		
		return index;
	}
	
	private void updateCaret(MouseEvent event) {
		int index = findClickedIndex(event) + this.textLine.getStartIndex();
		Caret caret = textModifyFacade.getCaret();
		
		if(event.getEventType() == MouseEvent.MOUSE_PRESSED) {
			caret.setCaretIndex(index);
		}

		else if(event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
			caret.setAnchorIndex(index);
		}
	}

	public Vector2 getLetterPosition(int index) {
		System.out.println("\n##GetLetterPosition, index: " + index);
		Vector2 pos;
		initializeLetterSizes();
		if(caretPositions == null || caretPositions.size() == 0)
			pos = lineSegmentProperty.get().getDistanceCoordinate(0);
		else
			pos = lineSegmentProperty.get().getDistanceCoordinate(caretPositions.get(Math.min(index,letterSizes.size())));
		return pos;
	}

	public void mouseClick(MouseEvent event) {
		updateCaret(event);	
	}
	
	public void mouseDrag(MouseEvent event) {
		updateCaret(event);
	}
	
	public void mouseMoved(MouseEvent evet) {
		DocDebugView.instance.setDebugText("LineOnCanvas, x: "  + this.layoutX + ", y: " + this.layoutY + ", width: " + this.width, 0);
	}

	public Paragraph getParentParagraph() {
		return parentParagraph.getParagraph();
	}

	public LineSegment getLowerLineSegment() {
		return lowerLineSegment;
	}

	public ColumnView getColumnView() {
		return parent;
	}

	public void setPolygonColor(Color polygonColor) {
		this.polygonColor = polygonColor;
	}
}
