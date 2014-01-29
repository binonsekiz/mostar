package document;

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
	private SimpleIntegerProperty endIndexInBigText;
	private SimpleFloatProperty angle;

	private int previousIndex = 0;
	private float cummulativeTextSize = 0;
	private boolean hasNext;
	
	private int startIndexSaveOnly;
	private int endIndexSaveOnly;
	
	private ArrayList<Float> cummulativeWordSizes;
	private HashMap<Integer, Integer> wordCountToStringIndex;
	private DocumentText documentText;
	
	public Paragraph(DocumentText parent){
		this(TextStyle.defaultStyle, "", parent);
	}
	
	public Paragraph(TextStyle style, String text, DocumentText parent){
		this.style = style;
		this.documentText = parent;
		cummulativeWordSizes = new ArrayList<Float>();
		wordCountToStringIndex = new HashMap<Integer, Integer>();
		startIndexInBigText = new SimpleIntegerProperty();
		endIndexInBigText = new SimpleIntegerProperty();
		angle = new SimpleFloatProperty();
		setText(text);
		computeStringWidths();
		initEvents();
	}
	
	private void initEvents() {
		startIndexInBigText.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number oldValue, Number arg2) {
				int wordLength = endIndexInBigText.get() - oldValue.intValue();
				endIndexInBigText.set(startIndexInBigText.getValue() + wordLength);
			}
		});
		endIndexInBigText.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				// TODO Auto-generated method stub
				
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
	
	public void setTextBuffer(StringBuffer buffer){
		this.textBuffer = buffer;
		computeStringWidths();
	}
	
	public void appendTextBuffer(StringBuffer buffer){
		this.textBuffer.append(buffer);
		//TODO: improve performance by calculating only the new portion
		computeStringWidths();
	}
	
	public void setStyle(TextStyle style){
		this.style = style;
		computeStringWidths();
	}
	
	public TextStyle getStyle(){
		return style;
	}
	
	public void setText(String text){
		this.textBuffer = new StringBuffer(text);
		computeStringWidths();
	}
	
	public String getText(){
		return textBuffer.toString();
	}
	
	public StringBuffer getStringBuffer(){
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
	 * @param breakWords: if true, words will be breaken in letters.
	 * @return
	 */
	public String getNextLine(double inputSize){
		float lineEndSize = (float) (cummulativeTextSize + inputSize);
		
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
		else return null;
		
		if(wordStartIndex < previousIndex) 
			return null;
		
		String retVal = textBuffer.substring(previousIndex, wordStartIndex 	+ 1)/*.trim()*/;
		startIndexSaveOnly = previousIndex;
		endIndexSaveOnly = wordStartIndex;
		previousIndex = wordStartIndex + 1;
		
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
	
		ArrayList<String> debugTexts = new ArrayList<String>();
		
		for(int i = 0; i < textBuffer.length(); i++){
			if(textBuffer.charAt(i) == ' ' || textBuffer.charAt(i) == '\t' || i == textBuffer.length() -1){
			//	debugTexts.add(textBuffer.substring(0, i+1));
				float stringWidth = metrics.computeStringWidth(textBuffer.substring(0, i+1));
				cummulativeWordSizes.add(stringWidth);
				wordCountToStringIndex.put(wordCount, i);
		//		DocDebugView.instance.putText("put [" + wordCount+ "] - [" + i + "] to wordcounttostringindex");
				wordCount ++;
			}
		}
		
		System.out.println("**** Calculated string widths *****");
		
		for(int i= 0; i < cummulativeWordSizes.size(); i++) {
	//		System.out.println("word: " + debugTexts.get(i) + ", size: " + cummulativeWordSizes.get(i));
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

	public void insertStringAbsoluteIndex(String string, int caretStartIndex) {
		int relativeIndex = caretStartIndex - startIndexInBigText.get();
		this.textBuffer.insert(relativeIndex, string);
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
		return textBuffer.substring(arg0, arg1);
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
}
