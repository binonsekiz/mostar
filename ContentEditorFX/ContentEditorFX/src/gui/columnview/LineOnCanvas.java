package gui.columnview;

import zzzzdeprecated.StyledTextDeprecated;
import geometry.libgdxmath.LineSegment;
import geometry.libgdxmath.Vector2;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import document.StyleTextPair;
import document.TextStyle;

public class LineOnCanvas {

	private DoubleProperty preferredWidthProperty;
	
	private IntegerProperty startIndexInStyledText;
	private IntegerProperty endIndexInStyledText;
	
	private ColumnView parent;
	private ParagraphOnCanvas parentParagraph;
//	private StyleTextPair text;
	
	private LineOnCanvas previousLine;
	private LineOnCanvas nextLine;

	private SimpleObjectProperty<LineSegment> lineSegmentProperty;
	private SimpleObjectProperty<TextAlignment> alignmentProperty;
	
	private float layoutX;
	private float layoutY;
	private float height;
	private float width;
	private float angle;
	
	//TODO: debug variable
	private String text;
	
	public LineOnCanvas(ColumnView parent, ParagraphOnCanvas parentParagraph){
		this.parent = parent;
		this.parentParagraph = parentParagraph;
		preferredWidthProperty = new SimpleDoubleProperty();
		alignmentProperty = new SimpleObjectProperty<TextAlignment>();
		alignmentProperty.set(TextAlignment.JUSTIFY);
		startIndexInStyledText = new SimpleIntegerProperty();
		endIndexInStyledText = new SimpleIntegerProperty();
		lineSegmentProperty = new SimpleObjectProperty<LineSegment>();
		lineSegmentProperty.set(new LineSegment(new Vector2(), new Vector2()));
		initEvents();
	}
	
	public void initialPositionSetup(float layoutX, float layoutY, float width, float height, float angle){
		this.layoutX = layoutX;
		this.layoutY = layoutY;
		this.preferredWidthProperty.set(width);
		this.height = height;
		this.width = width;
		this.angle = angle;
		LineSegment lineSegment = new LineSegment(
				new Vector2(layoutX, layoutY), 
				new Vector2( (float)(layoutX + Math.cos(Math.toRadians(angle)) * width), 
						     (float)(layoutY + Math.sin(Math.toRadians(angle)) * width)));
		lineSegmentProperty.set(lineSegment);
	}
	
	public void initialTextSetup(int startIndex, int endIndex){
		startIndexInStyledText.set(startIndex);
		endIndexInStyledText.set(endIndex);
	}
	
	public void setLineSegment(LineSegment line) {
		lineSegmentProperty.set(line);
	}
	
	public void setPreferredWidth(double width){
		this.preferredWidthProperty.set(width);
	}

	public void refresh(){
		GraphicsContext context = parent.getGraphicsContext();
		//TODO: uncomment next line, lines should get texts from paragraphs.
	//	String text = parentParagraph.getText();
		context.setStroke(Color.RED);
		context.setLineWidth(2);
		LineSegment line = lineSegmentProperty.get();
		context.strokeLine(line.getFirstPoint().x, line.getFirstPoint().y, line.getSecondPoint().x, line.getSecondPoint().y);
		
		context.setStroke(Color.BLACK);
		context.setLineWidth(1);
		context.setFont(parentParagraph.getFont());
		if(text != null) {
		//	context.strokeText(text.substring(startIndexInStyledText.get(), endIndexInStyledText.get()), line.getFirstPoint().x, line.getFirstPoint().y);
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
			context.strokeText(text, startX, startY);
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
		
		endIndexInStyledText.addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				if(nextLine != null){
					nextLine.getStartIndexInStyledTextProperty().set(endIndexInStyledText.getValue());
				}
			}
		});
		
		lineSegmentProperty.addListener(new ChangeListener<LineSegment>(){
			@Override
			public void changed(ObservableValue<? extends LineSegment> arg0,
					LineSegment arg1, LineSegment arg2){
				if(arg0.getValue() != null) {
					layoutX = arg0.getValue().getLeftPoint().x;
					layoutY = arg0.getValue().getLeftPoint().y;
					setPreferredWidth(arg0.getValue().getLength());
				}
			}
		});
	}
	
	public IntegerProperty getStartIndexInStyledTextProperty(){
		return startIndexInStyledText;
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

	public void setText(String text) {
		this.text = text;
	}
}
