package zzzzdeprecated;


import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;

/**
 * The text inside the text area set.
 * @author sahin
 *
 */
public class TextAreaSetModel {
	private String text;
	
	private ArrayList<TextSubStyle> styles;
	
	private int caretStart;
	private int caretEnd;
	
//	private TextAreaSet textAreaSet;
	
	private float[] fontWidths;
	
	FontMetrics metrics;

	Font font;
	
/*	protected TextAreaSetModel(TextAreaSet textAreaSet){
		text = "";
	//	this.textAreaSet = textAreaSet;
		caretStart = 0;
		caretEnd = 0;
		styles = new ArrayList<TextSubStyle>();
		setupMetrics();
	}*/
	
	protected void setupMetrics(){
		font = Font.font("Tahoma", 14f);
		metrics = Toolkit.getToolkit().getFontLoader().getFontMetrics(font);
	}
	
	protected void divideText(float[] widths){
		fontWidths = new float[text.length()];
		int widthCount = 0;
		int[] retVal = new int[widths.length];
		float cumulative = 0;
		int newLineStart = 0;
		
		for(int i = 0; i < widths.length; i++){
			fontWidths[i] = metrics.computeStringWidth(text.substring(newLineStart, i));
			if(fontWidths[i] - cumulative > widths[widthCount]){
				//roll back to the next delimiter
				int j = i;
				for(; j >= 0; j--){
					if(text.charAt(j) == ' ')
						break;
				}
				cumulative += widths[widthCount];
				retVal[widthCount] = j;
				newLineStart = j;
				widthCount ++;
			}
		}
	}
	
	protected void moveCaret(int offset){
		this.caretStart += offset;
		this.caretEnd = caretStart;
	}
	
	protected void moveCaretStart(int offset){
		this.caretStart += offset;
	}
	
	protected void moveCaretEnd(int offset){
		this.caretEnd += offset;
	}
	
	protected int getCaretStart(){
		return caretStart;
	}
	
	protected int getCaretEnd(){
		return caretEnd;
	}
	
	protected void setText(String text){
		this.text = text;
	}
	
	protected String getText(){
		return text;
	}
	
	protected String substring(int start, int end){
		return text.substring(start,end);
	}
	
	protected String substringWithLength(int start, int length){
		return text.substring(start, start+length);
	}
	
	protected String substring(int start){
		return text.substring(start);
	}
	
	protected int length(){
		return text.length();
	}
	
	protected char charAt(int index){
		return text.charAt(index);
	}
	
	/**
	 * Inner class that defines a continuous area of a single style
	 */
	class TextSubStyle{
		Font font;
		int startIndex;
		Color color;
		Color highlight;
		
		public TextSubStyle(int startIndex) {
			this.startIndex = startIndex;
		}
		
		public Font getFont() {
			return font;
		}

		public void setFont(Font font) {
			this.font = font;
		}

		public int getStartIndex() {
			return startIndex;
		}

		public void setStartIndex(int startIndex) {
			this.startIndex = startIndex;
		}

		public Color getColor() {
			return color;
		}

		public void setColor(Color color) {
			this.color = color;
		}

		public Color getHighlight() {
			return highlight;
		}

		public void setHighlight(Color highlight) {
			this.highlight = highlight;
		}	
	}
	
	
}
