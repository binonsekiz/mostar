package gui.columnview;

import java.util.ArrayList;

import geometry.libgdxmath.LineSegment;
import document.StyledText;
import document.TextStyle;
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

public class LineOnCanvas {

	private DoubleProperty preferredWidthProperty;
	
	private IntegerProperty startIndexInStyledText;
	private IntegerProperty endIndexInStyledText;
	
	private ColumnView parent;
	private ParagraphOnCanvas parentParagraph;
	
	private LineOnCanvas previousLine;
	private LineOnCanvas nextLine;
	
	private SimpleObjectProperty<StyledText> text;
	private SimpleObjectProperty<TextStyle> styleProperty;
	private SimpleObjectProperty<LineSegment> lineSegmentProperty;
	
	private SimpleObjectProperty<TextAlignment> alignmentProperty;
	
	private float layoutX;
	private float layoutY;
	private float height;
	private float angle;
	
	public LineOnCanvas(ColumnView parent, ParagraphOnCanvas parentParagraph){
		this.parent = parent;
		this.parentParagraph = parentParagraph;
		preferredWidthProperty = new SimpleDoubleProperty();
		text = new SimpleObjectProperty<StyledText>();
		alignmentProperty = new SimpleObjectProperty<TextAlignment>();
		alignmentProperty.set(TextAlignment.JUSTIFY);
		startIndexInStyledText = new SimpleIntegerProperty();
		endIndexInStyledText = new SimpleIntegerProperty();
		styleProperty = new SimpleObjectProperty<TextStyle>();
		lineSegmentProperty = new SimpleObjectProperty<LineSegment>();
		initEvents();
	}
	
	public void initialPositionSetup(float layoutX, float layoutY, float width, float height, float angle){
		this.layoutX = layoutX;
		this.layoutY = layoutY;
		this.preferredWidthProperty.set(width);
		this.height = height;
		this.angle = angle;
	}
	
	public void initialTextSetup(StyledText text, int startIndex, int endIndex){
		this.text.set(text);
		startIndexInStyledText.set(startIndex);
		endIndexInStyledText.set(endIndex);
		styleProperty.set(TextStyle.defaultStyle);
	}
	
	public void setLineSegment(LineSegment line) {
		lineSegmentProperty.set(line);
		preferredWidthProperty.set(line.getLength());
	}
	
	public void setPreferredWidth(double width){
		this.preferredWidthProperty.set(width);
	}
	
	public void setTextStyle(TextStyle style){
		this.styleProperty.set(style);
	}
	
	public void refresh(){
		GraphicsContext context = parent.getGraphicsContext();
		context.setStroke(Color.RED);
		context.setLineWidth(2);
		context.strokeLine(layoutX, layoutY, layoutX+preferredWidthProperty.get(), layoutY);
	}
	
	private void initEvents(){
		preferredWidthProperty.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				if(text.get()!= null && alignmentProperty.get() == TextAlignment.JUSTIFY) {
					calculateSpaceLength();
				}
			}
		});
		
		alignmentProperty.addListener(new ChangeListener<TextAlignment>() {
			@Override
			public void changed(ObservableValue<? extends TextAlignment> arg0,
					TextAlignment arg1, TextAlignment arg2) {
				if(arg2 == TextAlignment.JUSTIFY){
					calculateSpaceLength();
				}
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
		
		styleProperty.addListener(new ChangeListener<TextStyle>(){
			@Override
			public void changed(ObservableValue<? extends TextStyle> arg0,
					TextStyle arg1, TextStyle arg2) {
				calculateSpaceLength();
			}
		});
		
		lineSegmentProperty.addListener(new ChangeListener<LineSegment>(){
			@Override
			public void changed(ObservableValue<? extends LineSegment> arg0,
					LineSegment arg1, LineSegment arg2){
				layoutX = arg0.getValue().getLeftPoint().x;
				layoutY = arg0.getValue().getLeftPoint().y;
				setPreferredWidth(arg0.getValue().getLength());
			}
		});
	}

	protected void calculateSpaceLength() {
		// TODO Auto-generated method stub
		
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
}
