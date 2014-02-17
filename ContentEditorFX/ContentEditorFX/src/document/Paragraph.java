package document;

import geometry.libgdxmath.LineSegment;
import gui.columnview.ParagraphOnCanvas;
import gui.helper.MathHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import com.sun.javafx.tk.FontMetrics;

/**
 * This class resresents exactly one style and its associated text.
 * @author sahin
 *
 */
public class Paragraph implements CharSequence{

	private TextStyle style;
	private StringBuffer textBuffer;
	private SimpleIntegerProperty startIndexInBigText;
	private ArrayList<TextLine> textLines;
	
	private SimpleFloatProperty angle;

	private int previousIndex = 0;
	private float cummulativeTextSize = 0;
	
	private int startIndexSaveOnly;
	private int endIndexSaveOnly;
	
	private ArrayList<Float> cummulativeWordSizes;
	private HashMap<Integer, Integer> wordCountToStringIndex;
	private DocumentText documentText;
	private int indexInParent;
	private ParagraphOnCanvas paragraphView;
	private ParagraphSet paragraphSet;
	private boolean hasElements;
	
	public Paragraph(DocumentText parent, int index){
		this(TextStyle.defaultStyle, "", parent, index);
	}
	
	public Paragraph(TextStyle style, String text, DocumentText parent, int index){
		this.indexInParent = index;
		System.out.println("Paragraph initialized");
		this.style = style;
		this.documentText = parent;
		cummulativeWordSizes = new ArrayList<Float>();
		wordCountToStringIndex = new HashMap<Integer, Integer>();
		startIndexInBigText = new SimpleIntegerProperty();
		angle = new SimpleFloatProperty();
		textLines = new ArrayList<TextLine>();
		hasElements = false;
		setText(text);
		computeStringWidths();
		initEvents();
		updateTextLineIndices();
	}
	
	public void setParagraphOnCanvas(ParagraphOnCanvas view) {
		this.paragraphView = view;
	}
	
	private void initEvents() {
		startIndexInBigText.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number oldValue, Number arg2) {
				System.out.println("Start index set to: " + arg2); 
				updateTextLineIndices();
			}
		});
		angle.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	protected void updateTextLineIndices() {
		System.out.println("\n////Update text line indices");
		if(textLines.size() > 0) {
			if(indexInParent > 0) {
				textLines.get(0).setStartIndex(documentText.getParagraph(indexInParent - 1).getEndIndex());
				System.out.println("\tSet start to: " + documentText.getParagraph(indexInParent - 1).getEndIndex());
			}
			else{
				textLines.get(0).setStartIndex(0);
			}
		}
		else{
			System.out.println("Early out...");
		}
		
		for(int i = 1; i < textLines.size(); i++) {
			TextLine temp = textLines.get(i);
			temp.setStartIndex(textLines.get(i-1).getEndIndex());
			System.out.println("\tSetting "+ i + "th value to: " + textLines.get(i-1).getEndIndex());
		}
		documentText.updateIndexAfter(indexInParent);
	}

	private void updateTextLines() {
		ArrayList<LineSegment> lineSegments = paragraphSet.getLineSegments();
		paragraphSet.startTextDivision();
		paragraphSet.updateWithAvailableText(lineSegments);
	}

	public void setStartIndexInBigText(int index){
		this.startIndexInBigText.set(index);
	}
	
	public int getStartIndex(){
		return textLines.get(0).getStartIndex();
	}
	
	public void setStyle(TextStyle style){
		this.style = style;
		computeStringWidths();
	}
	
	public TextStyle getStyle(){
		return style;
	}

	public String getText(){
		return textBuffer.toString();
	}
	
	public StringBuffer getTextBuffer(){
		return textBuffer;
	}
	
	public void startTextDivision(){
		previousIndex = 0;
		cummulativeTextSize = 0;
		computeStringWidths();
		hasElements = true;
	}
	
	/**
	 * Return a string portion that fits into the size 
	 * @param size
	 * @param breakWords: if true, words will be broken in letters.
	 * @return
	 */
	public String calculateNextLine(TextLine inputLine, double inputSize){
		float lineEndSize = (float) (cummulativeTextSize + inputSize);

System.out.println("1, text: " + this.getText());
		inputLine.setParent(this);
		inputLine.setStartIndex(previousIndex);
		inputLine.setEndIndex(previousIndex);
System.out.println("2");		
		
		//TODO: use binary search instead of linear search
		int wordIndex = -1;
		
		textLines.add(inputLine);

		for(int i = cummulativeWordSizes.size() - 1; i >= 0; i--) {
			if(cummulativeWordSizes.get(i) < lineEndSize) {
				wordIndex = i;
				break;
			}
		}
System.out.println("3");		
		int wordStartIndex = previousIndex;
		
		if(wordIndex >= 0 && wordCountToStringIndex.containsKey(wordIndex)){
			wordStartIndex = wordCountToStringIndex.get(wordIndex);
		}
		else {
			hasElements = false;
			System.out.println("out 1");
			return null;
		}
System.out.println("4");
				
		if(wordStartIndex < previousIndex) {
			hasElements = false;
			System.out.println("out 2");
			return null;
		}
System.out.println("5");		
		String retVal = textBuffer.substring(previousIndex, wordStartIndex 	+ 1);
		startIndexSaveOnly = previousIndex;
		endIndexSaveOnly = wordStartIndex;
		previousIndex = wordStartIndex + 1;
System.out.println("6");		
		inputLine.setStartIndex(startIndexSaveOnly);
		inputLine.setEndIndex(previousIndex);
System.out.println("7");		
		cummulativeTextSize = cummulativeWordSizes.get(wordIndex);
		
		System.out.println("Added " + inputLine + " to " + this);
		
		return retVal;
	}
	
	/**
	 * Computes all possible string lengths for this styletextpair.
	 * Should be called only if:
	 * - style changes
	 * - text changes
	 */
	public void computeStringWidths(){
		FontMetrics metrics = style.getFontMetrics();
		cummulativeWordSizes.clear();
		int wordCount = 0;
		
		for(int i = 0; i < textBuffer.length(); i++){
			if(textBuffer.charAt(i) == ' ' || textBuffer.charAt(i) == '\t' || i == textBuffer.length() -1){
				float stringWidth = metrics.computeStringWidth(textBuffer.substring(0, i+1));
				cummulativeWordSizes.add(stringWidth);
				wordCountToStringIndex.put(wordCount, i);
				wordCount ++;
			}
		}
	}

	public int getNextLineStartIndex() {
		return startIndexSaveOnly;
	}
	
	public int getNextLineEndIndex() {
		return endIndexSaveOnly;
	}

	public int length(){
		return textBuffer.length();
	}

	public char getCharAtAbsoluteIndex(int max) {
		return textBuffer.charAt(max - startIndexInBigText.get());
	}

	@Override
	public char charAt(int arg0) {
		return textBuffer.charAt(arg0);
	}

	@Override
	public String subSequence(int arg0, int arg1) {
		return textBuffer.substring(
				MathHelper.clamp(0 ,arg0 - startIndexInBigText.get(), getEndIndex() - getStartIndex()), 
				MathHelper.clamp(0 ,arg1 - startIndexInBigText.get(), getEndIndex() - getStartIndex()));
	}
	
	public ArrayList<Float> getCummulativeWordSizes() {
		return cummulativeWordSizes;
	}

	@Override
	public IntStream chars() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntStream codePoints() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public float getAngle() {
		return angle.get();
	}
	
	public void setAngle(float angle) {
		this.angle.set(angle);
	}

	public int getEndIndex() {
		return textLines.get(textLines.size() - 1).getEndIndex();
	}
	
	protected void setIndexInParent(int indexInParent) {
		this.indexInParent = indexInParent;
	}

	public void insertText(String text, int caretIndex) {
		this.textBuffer.insert(caretIndex - startIndexInBigText.get(), text);
		updateTextLines();	
	}

	public void insertText(String text, int caretIndex, int anchor) {
		this.textBuffer.replace(caretIndex - startIndexInBigText.get(), anchor - startIndexInBigText.get(), text);
		updateTextLines();
	}
	
	public void setText(String text) {
		this.textBuffer = new StringBuffer(text);
		updateTextLineIndices();
		if(paragraphView != null) {
			updateTextLines();
		}
	}

	public ArrayList<TextLine> getTextLines() {
		return textLines;
	}	

	public int getIndexInParent() {
		return indexInParent;
	}

	public Paragraph divideAtIndex(int caretIndex) {
		Paragraph newParagraph = new Paragraph(style, textBuffer.substring(caretIndex), documentText, this.indexInParent);
		this.textBuffer = textBuffer.delete(caretIndex, textBuffer.length());
		documentText.addParagraph(newParagraph);
		
		return newParagraph;
	}

	protected void tryLowestStartValue(int value) {
		if(this.startIndexInBigText.get() > value) {
			startIndexInBigText.set(value);
			if(textLines.size() > 0) {
				textLines.get(0).setStartIndex(startIndexInBigText.get());
			}
		}
	}

	/**
	 * This should only be called from paragraph set.
	 * @param paragraphSet
	 */
	protected void setParagraphSet(ParagraphSet paragraphSet) {
		this.paragraphSet = paragraphSet;
	}
	
	public ParagraphSet getParagraphSet() {
		return paragraphSet;
	}

	public boolean includesIndex(int index) {
		if(textLines.size() == 0) 
			return false;
		if(index == getStartIndex() && index == getEndIndex())
			return true;
		if(index < getEndIndex() && index >= getStartIndex())
			return true; 
		return false;
	}

	public boolean hasElements() {
		return hasElements;
	}
	
	public String toString() {
		return "Paragraph: " + getText() + ", is being divided: " + hasElements;
	}
}
