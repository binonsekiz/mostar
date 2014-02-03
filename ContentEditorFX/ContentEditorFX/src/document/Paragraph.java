package document;

import gui.ShapedPane;
import gui.columnview.LineOnCanvas;
import gui.columnview.ParagraphOnCanvas;

import java.awt.event.TextListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import com.sun.javafx.tk.FontMetrics;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

/**
 * This class resresents exactly one style and its associated text.
 * @author sahin
 *
 */
public class Paragraph implements CharSequence{

	private TextStyle style;
	private StringBuffer textBuffer;
	private SimpleIntegerProperty startIndexInBigText;
	private SimpleIntegerProperty endIndexInBigText;
	private ArrayList<TextLine> textLines;
	
	private SimpleFloatProperty angle;

	private int previousIndex = 0;
	private float cummulativeTextSize = 0;
	private boolean hasNext;
	
	private int startIndexSaveOnly;
	private int endIndexSaveOnly;
	
	private ArrayList<Float> cummulativeWordSizes;
	private HashMap<Integer, Integer> wordCountToStringIndex;
	private DocumentText documentText;
	private int indexInParent;
	private ParagraphOnCanvas paragraphView;
	
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
		endIndexInBigText = new SimpleIntegerProperty();
		angle = new SimpleFloatProperty();
		textLines = new ArrayList<TextLine>();
		setText(text);
		computeStringWidths();
		initEvents();
	}
	
	public void setParagraphOnCanvas(ParagraphOnCanvas view) {
		this.paragraphView = view;
		initializeTextLines();
	}
	
	private void initializeTextLines() {
		textLines.clear();
		ArrayList<LineOnCanvas> lines = paragraphView.getLines();
		for(int i = 0; i < lines.size(); i++) {
			TextLine tempLine = new TextLine(this, 0, 0);
			textLines.add(tempLine);
		}
		
		if(paragraphView != null) {
			updateTextLines();
		}
	}

	private void initEvents() {
		startIndexInBigText.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number oldValue, Number arg2) {
				endIndexInBigText.set(startIndexInBigText.getValue() + textBuffer.length());
			}
		});
		endIndexInBigText.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				documentText.updateIndexAfter(indexInParent);
				System.out.println("~~~~~~~Paragraph end index updated to: " + arg2);
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

	public void setStartIndexInBigText(int index){
		this.startIndexInBigText.set(index);
	}
	
	public int getStartIndexInBigText(){
		return startIndexInBigText.get();
	}
	
	public void setStyle(TextStyle style){
		this.style = style;
		computeStringWidths();
	}
	
	public TextStyle getStyle(){
		return style;
	}

	private void updateTextLines() {
		System.out.println("\n\n****UPDATING TEXT LINES");
		System.out.println("Paragraph view: " + paragraphView);
		ArrayList<LineOnCanvas> lines = paragraphView.getLines();
		
		TextLine tempLine = new TextLine(this, 0, 0);
				
		computeStringWidths();
		startTextDivision();
		System.out.println("Calculated lines:");
		for(int i = 0; i < lines.size(); i++) {
			calculateNextLine(tempLine, lines.get(i).getWidth());
			textLines.get(i).setStartIndex(tempLine.getStartIndex());
			textLines.get(i).setEndIndex(tempLine.getEndIndex());
			System.out.println("line " + i + ": " + tempLine.getStartIndex() + ", " + tempLine.getEndIndex() );
		}
		
		this.endIndexInBigText.set(startIndexInBigText.get() + textBuffer.length());
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
		hasNext = true;
	}
	
	public boolean hasNextLine(){
		return hasNext;
	}
	
	/**
	 * Return a string portion that fits into the size 
	 * @param size
	 * @param breakWords: if true, words will be broken in letters.
	 * @return
	 */
	public String calculateNextLine(TextLine inputLine, double inputSize){
		System.out.println("\n\n\tCALCULATE NEXT LINE, input size: " + inputSize);
		
		float lineEndSize = (float) (cummulativeTextSize + inputSize);
		System.out.println("Line end size: " + lineEndSize);
		
		inputLine.setStartIndex(previousIndex);
		inputLine.setEndIndex(previousIndex);
		
		//TODO: use binary search instead of linear search
		int wordIndex = -1;

		for(int i = cummulativeWordSizes.size() - 1; i >= 0; i--) {
			if(cummulativeWordSizes.get(i) < lineEndSize) {
				wordIndex = i;
				break;
			}
		}
		
		int wordStartIndex = previousIndex;
		
		if(wordIndex >= 0 && wordCountToStringIndex.containsKey(wordIndex)){
			wordStartIndex = wordCountToStringIndex.get(wordIndex);
		}
		else {
			System.out.println("Early out 1");
			return null;
		}
				
		if(wordStartIndex < previousIndex) {
			System.out.println("Early out 2");
			return null;
		}
		
		String retVal = textBuffer.substring(previousIndex, wordStartIndex 	+ 1);
		startIndexSaveOnly = previousIndex;
		endIndexSaveOnly = wordStartIndex;
		previousIndex = wordStartIndex + 1;
		
		inputLine.setStartIndex(startIndexSaveOnly);
		inputLine.setEndIndex(previousIndex);
		
		cummulativeTextSize = cummulativeWordSizes.get(wordIndex);
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
				System.out.println("****WordCount to String index put: wordcount: " + wordCount + ", i: " + i);
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
		return textBuffer.substring(arg0 - startIndexInBigText.get(), arg1 - startIndexInBigText.get());
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
		return endIndexInBigText.get();
	}
	
	protected void setIndexInParent(int indexInParent) {
		this.indexInParent = indexInParent;
	}

	public void insertText(String text, int caretIndex) {
		this.textBuffer.insert(caretIndex, text);
		updateTextLines();	
	}

	public void insertText(String text, int caretIndex, int anchor) {
		this.textBuffer.replace(caretIndex, anchor, text);
		updateTextLines();
	}
	
	public void setText(String text){
		this.textBuffer = new StringBuffer(text);
		if(paragraphView != null) {
			updateTextLines();
		}
	}

	public ArrayList<TextLine> getTextLines() {
		return textLines;
	}
}
