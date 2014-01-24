package control;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import gui.columnview.LineOnCanvas;
import document.Column;
import document.DocumentText;
import document.Paragraph;
import document.TextStyle;

public class Caret{
	//reference to the text
	private DocumentText documentText;
	
	//style just under the caret index
	private TextStyle style;
	
	//index relative to styled text start.
	private int caretIndex;
	private Paragraph caretParagraph;
	 
	//this is RELATIVE to caret index.
	//i.e. -2 means 2 chars are selected and caret is at the end.
	//this means this value is 0 unless a piece of text is selected
	private int anchor;
	private Paragraph anchorParagraph;
	
	//visual positions
	public float x;
	public float y;
	
	public Caret(){
		caretIndex = 0;
		anchor = 0;
	}
	
	public void setStyle(TextStyle style){
		this.style = style;
	}
	
	public void setCaretIndex(int index){
		this.caretIndex = index;
		this.style = documentText.getStyleAt(caretIndex);
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

	public Column getActiveColumn() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDocumentText(DocumentText documentText) {
		this.documentText = documentText;
	}
	
	public boolean isCaretOnParagraph(Paragraph paragraph) {
		return caretParagraph == paragraph;
	}

	public void setCaretIndex(LineOnCanvas lineOnCanvas, int index) {
		caretIndex = lineOnCanvas.getStartIndexInStyledTextProperty().get() + index;
		caretParagraph = lineOnCanvas.getParentParagraph();
	}
	
	public void drawCaret(float lineAngle, GraphicsContext context, TextStyle style) {
		context.setStroke(Color.BLACK);
		context.setLineWidth(1);
		context.strokeLine(x, y, x + style.getLineSpacingHeight() * Math.sin(Math.toRadians(lineAngle)), y + style.getLineSpacingHeight() * Math.cos(Math.toRadians(lineAngle)));
	}
	
}
