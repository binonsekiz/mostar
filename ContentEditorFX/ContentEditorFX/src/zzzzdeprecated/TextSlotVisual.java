package zzzzdeprecated;

import gui.helper.FontHelper;
import gui.helper.StyleRepository;

import java.util.ArrayList;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

import com.sun.javafx.tk.FontMetrics;

import document.TextLineSpace;
import document.TextStyle;

public class TextSlotVisual extends Label implements Comparable<Integer>{

	private static int count = 0;
	
	private TextSlotVisual previousSlot;
	private TextSlotVisual selfReference;
	private TextSlotVisual nextSlot;
	
	private IntegerProperty startIndexInStyledText;
	private IntegerProperty endIndexInStyledText;
	
	private float maxAllowedSize;
	
	private TextStyle style;
	
	private ArrayList<Float> cummulativeWordSizes;
	private ColumnViewPane parent;
	private boolean isTextEventDisabled;
	
	public TextSlotVisual(String text, TextStyle style){
		/*
		super(text);
		this.style = style;
		if(text != null)
			cummulativeWordSizes = FontHelper.calculateFontSizeArray(text, getFont());
		selfReference = this;
		initEvents();*/
	}
	
	public TextSlotVisual(TextLineSpace.Slot currentSlot, float offsetFromTop, ColumnViewPane parent) {
		super();
		this.parent = parent;
		selfReference = this;
		count ++;
		startIndexInStyledText = new SimpleIntegerProperty();
		endIndexInStyledText = new SimpleIntegerProperty();
		
		minWidth(currentSlot.getWidth());
		maxWidth(currentSlot.getWidth());
		setMaxAllowedSize((float)currentSlot.getWidth());
		System.out.println("max allowed: " + maxAllowedSize);
		setWrapText(true);
		setTranslateX(currentSlot.startX);
		setTranslateY(offsetFromTop);
		
		setTextStyle(StyleRepository.instance.getDefaultStyle());
		
		initEvents();
	}

	public void setMaxAllowedSize(float maxSize){
		this.maxAllowedSize = maxSize;
	}
	
	public void setPreviousSlot(TextSlotVisual previousSlot){
		if(this.style != previousSlot.getTextStyle()){
			try {
				throw new Exception("Slot type mismatch.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else{
			this.previousSlot = previousSlot;
		}
	}
	
	public void setNextSlot(TextSlotVisual nextSlot){
		if(this.style != previousSlot.getTextStyle()){
			try {
				throw new Exception("Slot type mismatch.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else{
			this.nextSlot = nextSlot;
		}
	}
	
	private void initEvents(){
		this.fontProperty().addListener(new ChangeListener<Font>(){
			@Override
			public void changed(ObservableValue<? extends Font> arg0,
					Font arg1, Font arg2) {
				cummulativeWordSizes = FontHelper.calculateFontSizeArray(getText(), getFont());
			}
		});
		this.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
				//recalculate string lengths array
				//TODO: use the precalculated one to save performance.
				cummulativeWordSizes = FontHelper.calculateFontSizeArray(getText(), getFont());
			}
		});
		
		this.setOnMousePressed(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
	//			TextModifyFacade textModify = DocModifyScreenGuiFacade.instance.getTextModifyFacade();
	//			textModify.click(selfReference, event);
	//			selfReference.requestFocus();
			}
		});
		
		this.setOnMouseEntered(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
	//			DocModifyScreenGuiFacade.instance.setGuiDebugText("start: " + startIndexInStyledText.getValue() + ", end: " + endIndexInStyledText.getValue());
			}
		});
		
		this.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
	//			DocModifyScreenGuiFacade.instance.setGuiDebugText("");
			}
		});	
		
		this.startIndexInStyledText.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				selfReference.setEndIndexInStyledText(selfReference.startIndexInStyledText.getValue() + textProperty().getValue().length());
			}
		});
		
		this.endIndexInStyledText.addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				if(nextSlot != null){
					nextSlot.setStartIndexInStyledText(selfReference.endIndexInStyledText.getValue());
				}
			}
		});
		
		this.textProperty().addListener(new TextPropertyChangeListener());
	}
	
	class TextPropertyChangeListener implements ChangeListener<String>{	
		//TODO: do not use these events for the initial text set event. use a more generic divide method.
		@Override
		public void changed(ObservableValue<? extends String> arg0,
				String arg1, String newValue) {
			if(isTextEventDisabled)
				return;
			selfReference.setEndIndexInStyledText(selfReference.startIndexInStyledText.getValue() + textProperty().getValue().length());
			FontMetrics metrics = style.getFontMetrics();
			float stringWidth = metrics.computeStringWidth(newValue);			
			int counter = newValue.length();
			
			while(stringWidth > maxAllowedSize){
				counter --;
				stringWidth = metrics.computeStringWidth(newValue.substring(0, counter));
			}
			
			//continue counting backwards till a space is found, if not found return the previous counter;
			int lastIndex = newValue.substring(0, counter).lastIndexOf(" ");
			if(lastIndex > 0)
				counter = lastIndex;
			
			if(nextSlot == null) {
				nextSlot = parent.requestTextSlot();
			}
			divideText(counter, nextSlot);
			
			endIndexInStyledText.set(startIndexInStyledText.get() + getText().length());
		}
	}
	
	public void setEndIndexInStyledText(int i) {
		this.endIndexInStyledText.set(i);
	}

	public void divideText(int counter, TextSlotVisual nextSlot2) {
		nextSlot2.setText(getText().substring(counter, getText().length()));

		this.disableTextEvent(true);
		this.setText(getText().substring(0, counter));
		this.disableTextEvent(false);
	}

	private void disableTextEvent(boolean b) {
		isTextEventDisabled = b;
	}

	public void setStartIndexInStyledText(Integer value) {
		this.startIndexInStyledText.set(value);
	}

	public void setShowedText(String text){
		this.textProperty().set(text);
	}
	
	public int getStartIndexInStyledText(){
		return startIndexInStyledText.getValue();
	}
	
	public int getEndIndexInStyledText(){
		return endIndexInStyledText.getValue(); 
	}

	public ArrayList<Float> getCummulativeWordSizes() {
		return cummulativeWordSizes;
	}
	
	public void setTextStyle(TextStyle style){
		this.style = style;
		this.setFont(style.getFont());
	}

	public TextStyle getTextStyle() {
		return style;
	}

	@Override
	public int compareTo(Integer o) {
		if(this.startIndexInStyledText.get() < 0)
			return -1;
		else if(this.startIndexInStyledText.get() > 0)
			return 1;
		else
			return 0;
	}

	public void insertString(String text, int selectionStart, int selectionEnd) {
		
		System.out.println("inserting " + text + " to " + count + "th text slot");
		
		int relativeStartIndex = selectionStart - startIndexInStyledText.get();
		int relativeEndIndex = selectionEnd - startIndexInStyledText.get();
		
		StringBuffer newText = new StringBuffer(this.getText());
		if(relativeEndIndex != relativeStartIndex){
			newText.replace(selectionStart, selectionEnd, text);
		}
		
		this.setText(newText.toString());
	}
		
	
}
