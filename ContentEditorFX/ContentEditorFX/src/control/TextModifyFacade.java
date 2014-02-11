package control;

import gui.columnview.DocumentView;
import gui.columnview.LineOnCanvas;

import java.util.ArrayList;

import javafx.scene.Cursor;
import document.Document;
import document.DocumentText;
import document.Paragraph;
import document.TextStyle;

/**
 * This is a facade class that all the gui events happening on text (including cut/copy/undo etc.) has to go through.
 * It has access to the text content and the graphical representation of text.
 * @author sahin
 *
 */
public class TextModifyFacade {
	private DocumentText documentText;
	private Caret caret;
	private Document document;
	private DocumentView documentView;
	
	public TextModifyFacade() {
	}
	
	public void setDocumentAndView(Document document, DocumentView documentView) {
		this.document = document;
		this.documentView = documentView;
		this.documentText = document.getDocumentText();
		caret.setDocumentText(documentText);
	}
	
	public void setCaret(Caret caret) {
		this.caret = caret;
	}

	public Caret getCaret() {
		return caret;
	}

	public Paragraph getParagraphWithIndex(int caretIndex) {
		ArrayList<Paragraph> paragraphs =  documentText.getParagraphs();
		for(int i = 0; i < paragraphs.size(); i++) {
			if(caretIndex < paragraphs.get(i).getStartIndexInBigText())
				return paragraphs.get(i - 1);
		}
		return paragraphs.get(paragraphs.size() - 1);
	}

	public LineOnCanvas getLineViewWithIndex(int index) {
		return documentView.getLineThatIncludesIndex(index);
	}

	public void textSelectionSet(int caretIndex, int anchor) {
		int lowerIndex = Math.min(caretIndex, anchor);
		int higherIndex = Math.max(caretIndex, anchor); 
		documentView.textSelectionSet(lowerIndex, higherIndex);
	}

	public void insertSingleChar(String character) {
		caret.insertSingleChar(character);
		caret.getActiveColumnView().refresh();
	}
	
	public void addNewParagraphToEnd(TextStyle style) {
		System.out.println("\n\n~~Adding new paragraph to end, style: " + style);
		new Paragraph(style, "", documentText, documentText.getParagraphs().size());
	}
	
	/**
	 * Divide given paragraph into two parts, insert a new one in between.
	 * @param style
	 * @param caretIndex
	 */
	public void divideAtIndex(TextStyle style, int caretIndex) {
		System.out.println("\n\n~~Dividing at index: " + caretIndex + ", style: " + style);
		Paragraph paragraph = getParagraphWithIndex(caretIndex);
		paragraph.divideAtIndex(caretIndex);
		documentText.addParagraph(new Paragraph(style, "", documentText, paragraph.getIndexInParent() + 1));
	}
	
	/**
	 * Divide the start and end paragraphs into two, and set the style in between to the given style
	 * @param style
	 * @param caretIndex
	 * @param anchor
	 */
	public void setStyleAtInterval(TextStyle style, int caretIndex, int anchor) {
		System.out.println("\n\n~~Setting style in interval: " + caretIndex + "-" + anchor + " style: " + style);
		Paragraph paragraph1 = getParagraphWithIndex(caretIndex);
		Paragraph paragraph2 = paragraph1.divideAtIndex(caretIndex);
		Paragraph paragraph3 = paragraph2.divideAtIndex(anchor);
		
		paragraph2.setStyle(style);
	}

	public void backspace() {
		
	}

	public void delete() {

	}

	public void enter() {
			
	}

	public void changeMousePointer(Cursor cursorType) {
		documentView.setCursor(cursorType);
	}

	
}
