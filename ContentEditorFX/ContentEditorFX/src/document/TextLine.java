package document;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import document.style.TextStyle;

public class TextLine implements Comparable<TextLine>, PersistentObject {

	private SimpleIntegerProperty startIndex;
	private SimpleIntegerProperty endIndex;
	
	private Paragraph parent;

	@Override
	public Node getXmlNode(Document doc) {
		Element textLineElement = doc.createElement("TextLine");
		textLineElement.setAttribute("StartIndex" , startIndex.get() + "");
		textLineElement.setAttribute("EndIndex", endIndex.get() + "");
		textLineElement.setAttribute("ParagraphIndex", parent.getIndexInParent() + "");
		return textLineElement;
	}
	
	public TextLine(Paragraph parent, int startIndex, int endIndex) {
		System.out.println("TextLine initialized, start: " + startIndex + ", end: " + endIndex);
		this.startIndex = new SimpleIntegerProperty(startIndex);
		this.endIndex = new SimpleIntegerProperty(endIndex);
		this.parent = parent;
		initEvents();
	}
	
	public TextLine(Element element) {
		loadFromXmlElement(element);
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
		return "TextLine: [" + startIndex.get() + ", " + endIndex.get() + "], parent: " + parent;
	}

	public void setParent(Paragraph paragraph) {
		this.parent = paragraph;
	}

	public boolean hasText() {
		if(startIndex.get() < endIndex.get())
			return true;
		return false;
	}

	public Paragraph getParent() {
		return parent;
	}

	@Override
	public void loadFromXmlElement(Element node) {
		throw new NotImplementedException();
	}

	
}
