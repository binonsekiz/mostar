package document;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class TextLine implements Comparable<TextLine> {

	private SimpleIntegerProperty startIndex;
	private SimpleIntegerProperty endIndex;
	
	private Paragraph parent;
	
	public TextLine(Paragraph parent, int startIndex, int endIndex) {
		System.out.println("TextLine initialized, start: " + startIndex + ", end: " + endIndex);
		this.startIndex = new SimpleIntegerProperty(startIndex);
		this.endIndex = new SimpleIntegerProperty(endIndex);
		this.parent = parent;
		initEvents();
	}
	
	private void initEvents() {
		startIndex.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				int length = endIndex.get() - arg1.intValue();
				endIndex.set(startIndex.get() + length);
				if(parent != null) {
		//			parent.recalculateTextLineIndices();
				}
			}
		});
		
		endIndex.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
			}
		});
	}
	
	public TextStyle getStyle() {
		return parent.getStyle();
	}

	public int getStartIndex() {
		return startIndex.get();
	}
	
	public int getEndIndex() {
		return endIndex.get();
	}
	
	public void setStartIndex(int value) {
		this.startIndex.set(value);
	}
	
	public void setEndIndex(int value) {
		this.endIndex.set(value);
	}

	public int getLength() {
		return endIndex.get() - startIndex.get();
	}

	@Override
	public int compareTo(TextLine other) {
		return this.getStartIndex() - other.getStartIndex();
	}
	
	public String toString() {
		return "TextLine: [" + startIndex + ", " + endIndex + "], parent: " + parent;
	}

	public void setParent(Paragraph paragraph) {
		this.parent = paragraph;
	}
	
}
