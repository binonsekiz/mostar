package zzzzdeprecated;

import geometry.libgdxmath.LineSegment;
import geometry.libgdxmath.Polygon;
import gui.ShapedPane;
import gui.columnview.ColumnView;
import gui.columnview.WordView;

import java.util.ArrayList;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.text.TextAlignment;

import com.sun.javafx.tk.FontMetrics;

import document.StyledText;
import document.TextStyle;

/**
 * This class can only have one associated style.
 * It is equivalent to javafx Text class. It encapsulates words and whitespace.
 * @author sahin
 *
 */
public class LineViewDeprecated extends ShapedPane{

	private ArrayList<WordView> wordViews;
	private DoubleProperty preferredWidthProperty;
	
	private SimpleObjectProperty<TextAlignment> alignmentProperty;
	
	private IntegerProperty startIndexInStyledText;
	private IntegerProperty endIndexInStyledText;
	
	private ColumnView parent;
	private LineViewDeprecated previousView;
	private LineViewDeprecated nextView;
	
	private boolean isShapeDirty;
	private Polygon shape;
	
	private SimpleObjectProperty<StyledText> text;
	private SimpleObjectProperty<TextStyle> styleProperty;
	private SimpleObjectProperty<LineSegment> lineSegmentProperty;
	private float spaceWidth;
	private float tabWidth;
	
	public LineViewDeprecated(ColumnView parent){
		super(parent);
		this.parent = parent;
		wordViews = new ArrayList<WordView>();
		preferredWidthProperty = new SimpleDoubleProperty();
		text = new SimpleObjectProperty<StyledText>();
		alignmentProperty = new SimpleObjectProperty<TextAlignment>();
		alignmentProperty.set(TextAlignment.JUSTIFY);
		startIndexInStyledText = new SimpleIntegerProperty();
		endIndexInStyledText = new SimpleIntegerProperty();
		styleProperty = new SimpleObjectProperty<TextStyle>();
		lineSegmentProperty = new SimpleObjectProperty<LineSegment>();
		isShapeDirty = true;
		this.setId("LineViewTest");
		initEvents();
	}
	
	public void initialSetup(StyledText text, int startIndex, int endIndex){
		this.text.set(text);
		startIndexInStyledText.set(startIndex);
		endIndexInStyledText.set(endIndex);
		styleProperty.set(TextStyle.defaultStyle);
	}
	
	public void setLineSegment(LineSegment nextLine) {
		lineSegmentProperty.set(nextLine);
		preferredWidthProperty.set(nextLine.getLength());
	}
	
	public void divideText(){
		float cummulativeSize = 0;
		int startIndex = startIndexInStyledText.get();
		int endIndex = endIndexInStyledText.get();
		int wordEnd = 0;
		
		boolean wordReadingMode = false;
		
		for(int wordStart = startIndex; wordStart < endIndex; wordStart++){
			if(text.get().charAt(wordStart) == ' ' || text.get().charAt(wordStart) == '\t'){
				continue;
			}
			else{
				//just started reading a new word
				wordEnd = wordStart;
				while(wordEnd < text.get().length() && (text.get().charAt(wordEnd)!= ' ' && text.get().charAt(wordEnd)!= '\t')){
					wordEnd ++;
				}
				wordReadingMode = true;
				String word = text.get().substring(wordStart, wordEnd);
				WordView view = new WordView(this);
				view.setText(word);
				cummulativeSize += text.get().getSubsequenceWidth(wordStart, wordEnd);
				if(cummulativeSize >= preferredWidthProperty.get()){
					this.endIndexInStyledText.set(wordEnd);
				//	LineViewDeprecated nextLine = parent.requestLine(this.styleProperty.get().getLineSpacingHeight());
					
			//		nextLine.initialSetup(this.text.get(), this.endIndexInStyledText.get(), text.get().length());
				//	nextLine.setPreferredWidth(this.preferredWidthProperty.get());
					//nextLine.divideText();
					//nextLine.layoutWords();
					
					break;
				}
				this.wordViews.add(view);
				this.getChildren().add(view);
				wordStart = wordEnd;
			}
		}
	}
	
	public void layoutWords(){
		float cummulativeWidth = 0;
		int wordViewIndex = 0;
		calculateSpaceLength();
		
		for(int letterIndex = startIndexInStyledText.get();  letterIndex < endIndexInStyledText.get(); letterIndex++)
		{
			if(text.get().charAt(letterIndex) == ' '){
	//			cummulativeWidth += spaceWidth;
			}
			else if(text.get().charAt(letterIndex) == '\t'){
				cummulativeWidth += tabWidth;
			}
			else{
				//hit an actual letter
				WordView instance = null;
				if(wordViewIndex >= wordViews.size()){
					break;
				}
				instance = wordViews.get(wordViewIndex);
				wordViewIndex ++;
				instance.setLayoutX(cummulativeWidth);
				cummulativeWidth += instance.getWordWidth();
				letterIndex += instance.getLetterCount() - 1;
			}
		}
	}
	
	public void setTextStyle(TextStyle style){
		this.styleProperty.set(style);
	}
	
	public void addWordView(WordView view){
		this.wordViews.add(view);
		this.getChildren().add(view);
	}

	public void setPreferredWidth(double width){
		this.preferredWidthProperty.set(width);
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
				if(nextView != null){
					nextView.getStartIndexInStyledTextProperty().set(endIndexInStyledText.getValue());
				}
			}
		});
		
		styleProperty.addListener(new ChangeListener<TextStyle>(){
			@Override
			public void changed(ObservableValue<? extends TextStyle> arg0,
					TextStyle arg1, TextStyle arg2) {
				for(int i = 0; i < wordViews.size(); i++){
					wordViews.get(i).calculateWordWidth();
				}
				calculateSpaceLength();
			}
		});
		
		lineSegmentProperty.addListener(new ChangeListener<LineSegment>(){
			@Override
			public void changed(ObservableValue<? extends LineSegment> arg0,
					LineSegment arg1, LineSegment arg2){
				setLayoutX(arg0.getValue().getLeftPoint().x);
				setLayoutY(arg0.getValue().getLeftPoint().y);
			}
		});
	}
	
	public IntegerProperty getStartIndexInStyledTextProperty(){
		return startIndexInStyledText;
	}
		
	private void calculateSpaceLength(){
		FontMetrics metrics = styleProperty.get().getFontMetrics();
		spaceWidth = metrics.computeStringWidth(" ");
		tabWidth = metrics.computeStringWidth("\t");
		/*
		if(alignmentProperty.get() == TextAlignment.JUSTIFY){
			float cummulativeWordLength = 0;
			for(int i = 0; i < wordViews.size(); i++) {
				cummulativeWordLength += wordViews.get(i).getWordWidth();
			}
			
			int totalSpaceCount = wordViews.size() - 1;
			spaceWidth = (preferredWidthProperty.floatValue() - cummulativeWordLength) / totalSpaceCount;
		}
		*/
	}
	
	public void setTextAlignment(TextAlignment value){
		this.alignmentProperty.set(value);
	}

	/**
	 * Called whenever a word is modified.
	 * @param selfReference
	 * @param oldValue
	 * @param i
	 */
	public void notifyWordSizeChange(WordView selfReference, int oldValue, int i) {
		// TODO Auto-generated method stub
		
	}

	public TextStyle getTextStyle() {
		return styleProperty.get();
	}

}
