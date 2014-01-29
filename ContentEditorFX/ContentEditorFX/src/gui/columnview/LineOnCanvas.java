package gui.columnview;

import java.util.ArrayList;
import java.util.Collections;

import com.sun.javafx.tk.FontMetrics;

import control.Caret;
import control.TextModifyFacade;
import document.Paragraph;
import geometry.GeometryHelper;
import geometry.libgdxmath.LineSegment;
import geometry.libgdxmath.Polygon;
import geometry.libgdxmath.Vector2;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class LineOnCanvas implements Comparable<LineOnCanvas>{

	private DoubleProperty preferredWidthProperty;
	
	private IntegerProperty startIndexInStyledText;
	private IntegerProperty endIndexInStyledText;
	
	private ColumnView parent;
	private ParagraphOnCanvas parentParagraph;
	private ArrayList<Float> letterSizes;
	private ArrayList<Float> caretPositions;
//	private StyleTextPair text;
	
	private LineOnCanvas previousLine;
	private LineOnCanvas nextLine;

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
	//TODO: debug variable
	private String text;

	private boolean isDebugColorOn;

	private int selectedStartIndex;

	private int selectedEndIndex;
	
	public LineOnCanvas(ColumnView parent, ParagraphOnCanvas parentParagraph, TextModifyFacade textModifyFacade) {
		this.parent = parent;
		this.parentParagraph = parentParagraph;
		this.textModifyFacade = textModifyFacade;
		
		preferredWidthProperty = new SimpleDoubleProperty();
		alignmentProperty = new SimpleObjectProperty<TextAlignment>();
		alignmentProperty.set(TextAlignment.JUSTIFY);
		startIndexInStyledText = new SimpleIntegerProperty();
		endIndexInStyledText = new SimpleIntegerProperty();
		lineSegmentProperty = new SimpleObjectProperty<LineSegment>();
		lineSegmentProperty.set(new LineSegment(new Vector2(), new Vector2()));
		
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
	
	private void revalidateLineSegment() {
		LineSegment lineSegment = new LineSegment(
				new Vector2(layoutX, layoutY), 
				new Vector2( (float)(layoutX + Math.cos(Math.toRadians(angle)) * width), 
						     (float)(layoutY + Math.sin(Math.toRadians(angle)) * width)));
		lineSegmentProperty.set(lineSegment);
	}
	
	public void initialTextSetup(int startIndex, int endIndex) {
		startIndexInStyledText.set(startIndex);
		endIndexInStyledText.set(endIndex);
	}
	
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
		//TODO: uncomment next line, lines should get texts from paragraphs.
//		String text = parentParagraph.getText(startIndexInStyledText.get(), endIndexInStyledText.get());
		
		if(isDebugColorOn)
			context.setStroke(Color.GREEN);
		else
			context.setStroke(Color.RED);
		context.setLineWidth(2);
		LineSegment line = lineSegmentProperty.get();
	//	context.strokeLine(line.getFirstPoint().x, line.getFirstPoint().y, line.getSecondPoint().x, line.getSecondPoint().y);
		
		if(this.getColumnView().getDocumentView().getLinePolygonsVisible()) {
			context.strokePolygon(shape.getTransformedXVertices(), shape.getTransformedYVertices(), shape.getEdgeCount());
		}
		
		context.setFill(parentParagraph.getStyle().getStrokeColor());
		context.setLineWidth(1.5f);
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
				System.out.println("YOYOYOYstart: " + startIndexInStyledText.get() + ", end: " + endIndexInStyledText.get() + ", caretStart: " + selectedStartIndex + ", caretEnd: " + selectedEndIndex);
				context.fillText(text, startX, startY + this.height);
			}
			else{
				if(caretPositions == null) {
					initializeLetterSizes();
				}
				
				System.out.println("ü~ start: " + startIndexInStyledText.get() + ", end: " + endIndexInStyledText.get() + ", caretStart: " + selectedStartIndex + ", caretEnd: " + selectedEndIndex);
				
				int startIndexAdjusted = selectedStartIndex - startIndexInStyledText.get();
				int endIndexAdjusted = selectedEndIndex - endIndexInStyledText.get();
				
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
		
		startIndexInStyledText.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number oldValue, Number newValue) {
				int wordLength = endIndexInStyledText.get() - oldValue.intValue();
				endIndexInStyledText.set(startIndexInStyledText.getValue() + wordLength);
			}
		});
		
		endIndexInStyledText.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				if(nextLine != null){
					nextLine.getStartIndexInStyledTextProperty().set(endIndexInStyledText.getValue());
				}
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
	
	public IntegerProperty getStartIndexInStyledTextProperty() {
		return startIndexInStyledText;
	}
	
	public IntegerProperty getEndIndexInStyledTextProperty() {
		return endIndexInStyledText;
	}
	
	public int getStartIndex() {
		return startIndexInStyledText.get();
	}
	
	public int getEndIndex() {
		return endIndexInStyledText.get();
	}
	
	private void setStartIndex(int index) {
		this.startIndexInStyledText.set(index);
	}
	
	private void setEndIndex(int index) {
		this.endIndexInStyledText.set(index);
	}

	public void setLayout(float x, float y) {
		this.layoutX = x;
		this.layoutY = y;
	}
	
	@Override
	public String toString(){
		return "x: " + layoutX + ", y: " + layoutY + " width: " + preferredWidthProperty.get();
	}

	public LineSegment getLineSegment() {
		return lineSegmentProperty.get();
	}

	public float getWidth() {
		return (float) lineSegmentProperty.get().getLength();
	}

/*	public void setText(String text) {
		this.text = text;
	}*/

	@Override
	public int compareTo(LineOnCanvas o) {
		int value = this.getLineSegment().compareTo(o.getLineSegment());
		if(isTextUpsideDown())
			return -1 * value;
		else
			return value;
	}

	public void setDebugText(String text, int startIndex, int endIndex) {
		if(text!= null)
			System.out.println("Line received text: " + text);
		this.setStartIndex(startIndex);
		this.setEndIndex(endIndex);
		if(text != null)
			this.text = text/*.trim()*/;
		else
			this.text = null;
		letterSizes = null;
		caretPositions = null;
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
		int index = findClickedIndex(event);
		Caret caret = textModifyFacade.getCaret();
		caret.setCaretIndex(index);
		System.out.println("CARET INDEX: " + index);
		
		for(int i = 0; i < letterSizes.size(); i++) {
			System.out.println("\tLetter[" + i + "]= " + letterSizes.get(i) + ", Caret[" + i + "]= " + caretPositions.get(i));
		}

//		Vector2 caretPos = getLetterPosition(index);
//		caret.setVisualPosition(caretPos.x, caretPos.y);
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
		parent.refreshOverlayCanvas();
	}

	public Paragraph getParentParagraph() {
		return parentParagraph.getParagraph();
	}

	public LineSegment getLowerLineSegment() {
		return lowerLineSegment;
	}
	
	public void setSelectedIndex(int startIndex, int endIndex) {
		this.selectedStartIndex = startIndex;
		this.selectedEndIndex = endIndex;
	}

	public ColumnView getColumnView() {
		return parent;
	}

}
