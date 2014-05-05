package document;

import geometry.libgdxmath.LineSegment;
import gui.columnview.ColumnView;
import gui.columnview.ParagraphOnCanvas;
import gui.helper.MathHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.IntStream;

import javafx.beans.property.SimpleIntegerProperty;

import org.w3c.dom.Element;

import com.sun.javafx.tk.FontMetrics;

import document.layout.LayoutMachine;
import document.persistentproperties.ParagraphProperties;
import document.project.ProjectRepository;
import document.style.TextStyle;

/**
 * This class resresents exactly one style and its associated text.
 * @author sahin
 *
 */
public class Paragraph extends ParagraphProperties implements CharSequence, Comparable<Paragraph>{

	private SimpleIntegerProperty startIndexInBigText;
	private int previousIndex = 0;
	private float cummulativeTextSize = 0;
	
	private int startIndexSaveOnly;
	
	private ArrayList<Float> cummulativeWordSizes;
	private HashMap<Integer, Integer> wordCountToStringIndex;

	private ParagraphOnCanvas paragraphView;
	private ParagraphSet paragraphSet;
	private boolean hasElements;
	
	public Paragraph(int index){
		this(TextStyle.defaultStyle, "Testing text", index);
	}
	
	public Paragraph(TextStyle style, String text, int index){
		this.indexInParent = index;
		System.out.println("Paragraph initialized");
		this.style = style;
		cummulativeWordSizes = new ArrayList<Float>();
		wordCountToStringIndex = new HashMap<Integer, Integer>();
		startIndexInBigText = new SimpleIntegerProperty();
		textLines = new ArrayList<TextLine>();
		lineSegments = new ArrayList<LineSegment>();
		hasElements = false;
		setText(text);
	}
	
	public Paragraph(Element element) {
		super(element);
	}

	public void setParagraphOnCanvas(ParagraphOnCanvas view) {
		this.paragraphView = view;
	}

	private void updateTextLines() {
		getParagraphSet().getColumn().getLayoutMachine().initialSetup();
	}

	public void setStartIndexInBigText(int index){
		this.startIndexInBigText.set(index);
	}
	
	public int getStartIndex(){
		return textLines.get(0).getStartIndex();
	}
	
	public void setStyle(TextStyle style){
		this.style = style;
		updateTextLines();
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
		textLines.clear();
		lineSegments.clear();
	}
	
	public TextFillReturnValue fillWithAvailableText(LayoutMachine machine, int startSegment, float startOffset) {
		System.out.println("Paragraph [" + getText() + "]::Fill With Available Text Started");
		System.out.println("\tStartSegment: " + startSegment + ", offset: " + startOffset);
		int textCounter = 0;
		int segmentCounter = startSegment;
		TextFillReturnValue retVal = null;
		float lastCalculatedWidth = 0;
		
		startTextDivision();
		
		while(textCounter < length()) {			
			//find out the available length for the current line segment
			LineSegment segment = machine.getNextAvailableLineSegment(style);
			if(segment == null) {
				//if the machine is out of space, 
				if(machine.isOutOfSpace()) {
					System.out.println("MACHINE OUT OF SPACE");
					break;
				}
				
				//if there isn't a segment returned, notify layout machine so that 
				//the last segment will not be used for the next call
				machine.reportLastUsedWidth(0);
				continue;
			}
			float availableLength = segment.getLength();
			
			TextLine newTextLine = null;
			newTextLine = new TextLine(this, 0, 0);

			float calculatedWidth = calculateNextLine(newTextLine, availableLength);
			textLines.add(newTextLine);
			
			textCounter = textCounter + newTextLine.getLength();
			segmentCounter ++;
			
			//we are going to use this line segment. Trim from the end to fit the calculated width.
			segment.adjustLength(calculatedWidth);
			lineSegments.add(segment);
			System.out.println("/***/Trimmed segment again, added to paragraphset: " + segment + ", text line: " + newTextLine);
			lastCalculatedWidth = calculatedWidth;
			machine.reportLastUsedWidth(lastCalculatedWidth);
		}
		
		Collections.sort(textLines);
		
		if(textLines.size() == 0){
			int indexOffset = 0;
			if(indexInParent > 0) {
				indexOffset = ProjectRepository.getActiveProjectEnvironment().getDocumentText().getParagraph(indexInParent - 1).getEndIndex();
				startIndexInBigText.set(indexOffset);
			}
			
			LineSegment defaultSegment = machine.getDefaultSegment(style);
			if(defaultSegment != null) {
				TextLine defaultLine = new TextLine(this, indexOffset, indexOffset);
				lineSegments.add(machine.getDefaultSegment(style));
				textLines.add(defaultLine);
			}
		}
		
		retVal = new TextFillReturnValue(textLines, Math.max(0, segmentCounter - 1), lastCalculatedWidth);
		
		return retVal;
	}
	
	/**
	 * Return a string portion that fits into the size 
	 * @param size
	 * @param breakWords: if true, words will be broken in letters.
	 * @return the pixel width of the calculated line
	 */
	private float calculateNextLine(TextLine inputLine, double inputSize){
		float lineEndSize = (float) (cummulativeTextSize + inputSize);
		
		int indexOffset = 0;
		if(indexInParent > 0) {
			indexOffset = ProjectRepository.getActiveProjectEnvironment().getDocumentText().getParagraph(indexInParent - 1).getEndIndex();
			startIndexInBigText.set(indexOffset);
		}

		inputLine.setParent(this);
		inputLine.setStartIndex(previousIndex + indexOffset);
		inputLine.setEndIndex(previousIndex + indexOffset);
		
		//TODO: use binary search instead of linear search
		int wordIndex = -1;

		//see which one of the words can fit in the given place. if none, break.
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
			System.out.println("out 1");
			return 0;
		}

		if(wordStartIndex < previousIndex){
			hasElements = false;
			System.out.println("out 2");
			return 0;
		}
		
		String retVal = textBuffer.substring(previousIndex, wordStartIndex 	+ 1);
		startIndexSaveOnly = previousIndex + indexOffset;
		previousIndex = wordStartIndex + 1;
		
		inputLine.setStartIndex(startIndexSaveOnly);
		inputLine.setEndIndex(previousIndex + indexOffset);
		
		cummulativeTextSize = cummulativeWordSizes.get(wordIndex);
		
		FontMetrics metrics = style.getFontMetrics();
		
		return metrics.computeStringWidth(retVal);
	}
	
	/**
	 * Computes all possible string lengths for this styletextpair.
	 * Should be called only if:
	 * - style changes
	 * - text changes
	 */
	private void computeStringWidths(){
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
				MathHelper.clamp(0, arg0 - startIndexInBigText.get(), getEndIndex() - getStartIndex()), 
				MathHelper.clamp(0, arg1 - startIndexInBigText.get(), getEndIndex() - getStartIndex()));
	}
	
	public ArrayList<Float> getCummulativeWordSizes() {
		return cummulativeWordSizes;
	}

	@Override
	public IntStream chars() {
		return null;
	}

	@Override
	public IntStream codePoints() {
		return null;
	}

	public int getEndIndex() {
		if(textLines == null || textLines.size() == 0) {
			return startIndexSaveOnly + textBuffer.length();
		}
		return textLines.get(textLines.size() - 1).getEndIndex();
	}
	
	protected void setIndexInParent(int indexInParent) {
		this.indexInParent = indexInParent;
	}

	public void insertText(String text, int caretIndex) {
		this.textBuffer.insert(caretIndex - startIndexInBigText.get(), text);
		System.out.println("Text buffer becomes: " + textBuffer.toString());
		updateTextLines();	
	}

	public void insertText(String text, int caretIndex, int anchor) {
		this.textBuffer.replace(caretIndex - startIndexInBigText.get(), anchor - startIndexInBigText.get(), text);
		updateTextLines();
	}
	
	public void setText(String text) {
		this.textBuffer = new StringBuffer(text);
		if(paragraphView != null) {
			updateTextLines();
		}
	}

	public ArrayList<TextLine> getTextLines() {
		return textLines;
	}	

	public ArrayList<LineSegment> getLineSegments() {
		return lineSegments;
	}
	
	public int getIndexInParent() {
		return indexInParent;
	}

	public Paragraph divideAtIndex(int divideIndex) {
		DocumentText documentText = ProjectRepository.getActiveProjectEnvironment().getDocumentText();
		
		System.out.println("Paragraph::Divide at Index: " + divideIndex);
		divideIndex = divideIndex - startIndexInBigText.get();
		Paragraph newParagraph = new Paragraph(style, textBuffer.substring(divideIndex), this.indexInParent+1);
		this.textBuffer = textBuffer.delete(divideIndex, textBuffer.length());
		
		documentText.addParagraph(newParagraph, paragraphSet);
		
		updateTextLines();
		newParagraph.updateTextLines();
		return newParagraph;
	}
	
	/**
	 * This function assumes that this paragraph is just before p2.
	 * @param p2
	 */
	public void mergeWith(Paragraph p2) {
		this.textBuffer.append(p2.getTextBuffer());
		ProjectRepository.getActiveProjectEnvironment().getDocumentText().removeParagraph(p2.getIndexInParent());
		paragraphSet.removeParagraph(p2);
		updateTextLines();
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

	public void setStartIndexSaveOnly(int value) {
		this.startIndexSaveOnly = value;
	}

	public float getTextLineLength(TextLine newTextLine) {
		String text = subSequence(newTextLine.getStartIndex(), newTextLine.getEndIndex());
		return style.getFontMetrics().computeStringWidth(text);
	}
	
	protected class TextFillReturnValue{
		private ArrayList<TextLine> textLines;
		private float finishOffset;
		private int finishLine;
		
		protected TextFillReturnValue(ArrayList<TextLine> textLines, int finishLine, float finishOffset){
			this.textLines = textLines;
			this.finishLine = finishLine;
			this.finishOffset = finishOffset;
		}

		protected ArrayList<TextLine> getTextLines() {
			return textLines;
		}

		protected void setTextLines(ArrayList<TextLine> textLines) {
			this.textLines = textLines;
		}

		protected float getFinishOffset() {
			return finishOffset;
		}

		protected void setFinishOffset(float finishOffset) {
			this.finishOffset = finishOffset;
		}

		protected int getFinishLine() {
			return finishLine;
		}

		protected void setFinishLine(int finishLine) {
			this.finishLine = finishLine;
		}
		
		public String toString() {
			return "TextFillReturnValue, size: " + textLines.size() + ", finishLine: " + finishLine + ", finishOffset: " + finishOffset;
 		}
		
	}

	@Override
	public int compareTo(Paragraph o) {
		if(this.indexInParent < o.indexInParent){
			return -1;
		}
		else if(this.indexInParent > o.indexInParent){
			return 1;
		}
		return 0;
	}

	public void validateLineOnCanvases(ColumnView columnView) {
		paragraphSet.getColumn().getLayoutMachine().validateLineOnCanvases(this, columnView);
	}

}
