package control;

import zzzzdeprecated.StyledTextDeprecated;
import document.Column;
import document.TextStyle;

public class Caret{
	//reference to the text
	private StyledTextDeprecated styledText;
	
	//style just under the caret index
	private TextStyle style;
	
	//index relative to styled text start.
	private int caretIndex;
	 
	//this is RELATIVE to caret index.
	//i.e. -2 means 2 chars are selected and caret is at the end.
	//this means this value is 0 unless a piece of text is selected
	private int anchor;
	
	//visual positions
	private float x, y;
	
	public Caret(){
		caretIndex = 0;
		anchor = 0;
	}
	
	public void setStyle(TextStyle style){
		this.style = style;
	}
	
	public void setCaretIndex(int index){
		this.caretIndex = index;
		this.style = styledText.getStyleAt(caretIndex);
	}
	
	public void setAnchor(int relativeIndex){
		this.anchor = relativeIndex;
	}
	
	public int getCaretIndex(){
		return caretIndex;
	}
	
	public int getAnchor(){
		return anchor;
	}
	
	public void setVisualPosition(float x, float y){
		this.x = x;
		this.y = y;
	}

	public TextStyle getStyle() {
		return style;
	}

	public void setRelativeCaretIndex(int index) {
		this.setCaretIndex(getCaretIndex() + index);
	}
	
	public int getSelectionStart(){
		return Math.min(caretIndex + anchor, caretIndex);
	}
	
	public int getSelectionEnd(){
		return Math.max(caretIndex + anchor, caretIndex);
	}

	public void setStyledText(StyledTextDeprecated text) {
		this.styledText = text;
	}

	public Column getActiveColumn() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
