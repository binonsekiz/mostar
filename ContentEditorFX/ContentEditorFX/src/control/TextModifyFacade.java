package control;

import gui.columnview.DocumentView;
import gui.columnview.LineOnCanvas;

import java.util.ArrayList;

import javafx.scene.Cursor;
import document.Document;
import document.Paragraph;
import document.style.TextStyle;

/**
 * This is a facade class that all the gui events happening on text (including cut/copy/undo etc.) has to go through.
 * It has access to the text content and the graphical representation of text.
 * @author sahin
 *
 */
public class TextModifyFacade extends Facade{
	private Caret caret;
	
	public TextModifyFacade() {
	}
	
	public void setDocumentAndView(Document document, DocumentView documentView) {
		super.setDocumentAndView(document, documentView);
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
			if(caretIndex < paragraphs.get(i).getStartIndex())
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

	public void insertString(String text) {
		caret.insertString(text);
	}
	
	public void addNewParagraphToEnd(TextStyle style) {
		System.out.println("\n\n~~Adding new paragraph to end, style: " + style);
		new Paragraph(style, "", documentText.getParagraphs().size());
	}
	
	/**
	 * Divide the start and end paragraphs into two, and set the style in between to the given style
	 * @param style
	 * @param caretIndex
	 * @param anchor
	 */
	public void setStyleAtInterval(TextStyle style, int caretIndex, int anchor) {
		System.out.println("\n\n~~Setting style in interval: " + caretIndex + "-" + anchor + " style: " + style);
		int lowerIndex = Math.min(caretIndex, anchor);
		int higherIndex = Math.max(caretIndex, anchor);
		TextStyle initialStyle = null;

		//get the paragraph to be divided.
		Paragraph startParagraph = getParagraphWithIndex(lowerIndex);
		Paragraph endParagraph = getParagraphWithIndex(higherIndex);
		if(startParagraph == endParagraph && style != startParagraph.getStyle()) {
			initialStyle = startParagraph.getStyle();

			//paragraph2 is the higher index result of the division. Will be divided later.
			Paragraph paragraph2;
			if(lowerIndex == startParagraph.getStartIndex()) {
				paragraph2 = startParagraph;
			}
			else {
				paragraph2 = startParagraph.divideAtIndex(lowerIndex);
			}
			paragraph2.setStyle(style);

			//divide paragraph2 
			Paragraph paragraph3;
			if(higherIndex == paragraph2.getEndIndex()) {
				paragraph3 = paragraph2;
			}
			else {
				paragraph3 = paragraph2.divideAtIndex(higherIndex);
				paragraph3.setStyle(initialStyle);
			}
		}
		else if(startParagraph != endParagraph) {
			Paragraph mergeStart = null;
			Paragraph mergeEnd = null;

			if(lowerIndex > startParagraph.getStartIndex() && startParagraph.getStyle() != style) {
				mergeStart = startParagraph.divideAtIndex(lowerIndex);
			}
			else {
				mergeStart = startParagraph;
			}
			if(higherIndex < endParagraph.getEndIndex() && endParagraph.getStyle() != style) {
				endParagraph.divideAtIndex(higherIndex);
			}
			mergeEnd = endParagraph;
			
			mergeParagraphsWithStyle(style, mergeStart, mergeEnd);
		}
		caret.getActiveColumnView().refresh();
	}

	private void mergeParagraphsWithStyle(TextStyle style, Paragraph mergeStart, Paragraph mergeEnd) {
		int mergeIndex = mergeStart.getIndexInParent();
		int loopCount = mergeEnd.getIndexInParent() - mergeIndex;
		for(int i = 0; i < loopCount; i++) {
			Paragraph p1 = documentText.getParagraph(mergeIndex);
			Paragraph p2 = documentText.getParagraph(mergeIndex + 1);
			System.out.println("merging " + p1.getText() + " with " + p2.getText());
			p1.mergeWith(p2);
		}
		mergeStart.setStyle(style);
		
		if(mergeIndex > 0) {
			Paragraph pre = documentText.getParagraph(mergeIndex - 1);
			if(pre.getStyle() == mergeStart.getStyle()) {
				pre.mergeWith(mergeStart);
			}
		}
	}

	public void backspace() {
		if(caret.getCaretIndex() == caret.getAnchor()){
			caret.setAnchorIndexRelative(-1);
			insertString("");
		}
		else{
			insertString("");
		}
	}

	public void delete() {
		if(caret.getCaretIndex() == caret.getAnchor()){
			caret.setAnchorIndexRelative(1);
			insertString("");
		}
		else{
			insertString("");
		}
	}

	public void enter() {
			
	}

	public void changeMousePointer(Cursor cursorType) {
		documentView.setCursor(cursorType);
	}

	public void startTextDivisionForAll() {
		for(int i = 0; i < documentText.getParagraphs().size(); i++) {
			documentText.getParagraph(i).startTextDivision();
		}
	}

	
	/**
	 * Divide given paragraph into two parts, insert a new one in between.
	 * @param style
	 * @param caretIndex
	 */
	/*public void divideAtIndex(TextStyle style, int caretIndex) {
		System.out.println("\n\n~~Dividing at index: " + caretIndex + ", style: " + style);
		Paragraph paragraph = getParagraphWithIndex(caretIndex);
		paragraph.divideAtIndex(caretIndex);
		documentText.addParagraph(new Paragraph(style, "", paragraph.getIndexInParent() + 1), paragraph.getParagraphSet());
	}*/
}
