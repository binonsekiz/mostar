package document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.sun.javafx.tk.FontMetrics;

/**
 * This class resresents exactly one style and its associated text.
 * @author sahin
 *
 */
public class StyleTextPair implements CharSequence{

	private TextStyle style;
	private StringBuffer textBuffer;
	private int startIndexInBigText = 0;

	private int previousIndex = 0;
	private float cummulativeTextSize = 0;
	private boolean hasNext;
	
	private int startIndexSaveOnly;
	private int endIndexSaveOnly;
	
	private ArrayList<Float> cummulativeWordSizes;
	private HashMap<Integer, Integer> wordCountToStringIndex;
	
	public StyleTextPair(){
		this(new TextStyle(), "");
	}
	
	public StyleTextPair(TextStyle style){
		this(style, "");
	}
	
	public StyleTextPair(String text){
		this(new TextStyle(), text);
	}
	
	public StyleTextPair(TextStyle style, String text){
		this.style = style;
		cummulativeWordSizes = new ArrayList<Float>();
		wordCountToStringIndex = new HashMap<Integer, Integer>();
	}
	
	public void setStartIndexInBigText(int index){
		this.startIndexInBigText = index;
	}
	
	public int getStartIndexInBigText(){
		return startIndexInBigText;
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
		
		int wordIndex = Collections.binarySearch(cummulativeWordSizes, lineEndSize);
		
		System.out.println("inputSize: "+ inputSize + ", lineEndSize: " + lineEndSize + ", wordIndex: " + wordIndex);
		
		if(wordIndex < 0) wordIndex = (wordIndex + 1) * -1;
		if(wordIndex >= cummulativeWordSizes.size()){
			System.out.println("first out null");
			hasNext = false;
			return null;
		}
		while(wordIndex >= 0 && cummulativeWordSizes.get(wordIndex) > lineEndSize){
			wordIndex --;
		}
		
		//if no word fits the area
		if(wordIndex < 0) {
			System.out.println("second out null");
			return null;
		}
		
		int wordStartIndex = wordCountToStringIndex.get(wordIndex);
		String retVal = textBuffer.substring(previousIndex, wordStartIndex)/*.trim()*/;
		startIndexSaveOnly = previousIndex;
		endIndexSaveOnly = wordStartIndex;
		previousIndex = wordStartIndex;
				
		cummulativeTextSize = cummulativeWordSizes.get(wordIndex);
		System.out.println("cumulative size: " + cummulativeTextSize);
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
			if(textBuffer.charAt(i) == ' ' || textBuffer.charAt(i) == '\t'){
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

	public void insertStringAbsoluteIndex(String string, int caretStartIndex) {
		int relativeIndex = caretStartIndex - startIndexInBigText;
		this.textBuffer.insert(relativeIndex, string);
	}

	public char getCharAtAbsoluteIndex(int max) {
		return textBuffer.charAt(max - startIndexInBigText);
	}

	@Override
	public char charAt(int arg0) {
		return textBuffer.charAt(arg0);
	}

	@Override
	public String subSequence(int arg0, int arg1) {
		return textBuffer.substring(arg0, arg1);
	}
}
