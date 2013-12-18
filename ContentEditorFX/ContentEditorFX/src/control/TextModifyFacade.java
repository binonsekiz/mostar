package control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import zzzzdeprecated.ColumnViewPane;
import zzzzdeprecated.TextSlotVisual;
import document.Column;
import document.Document;
import document.StyleTextPair;
import document.StyledText;
import document.TextStyle;
import event.DocModifyScreenGuiFacade;
import gui.columnview.DocumentView;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyCombination.ModifierValue;

/**
 * This is a facade class that all the gui events happening on text (including cut/copy/undo etc.) has to go through.
 * It has access to the text content and the graphical representation of text.
 * @author sahin
 *
 */
public class TextModifyFacade{
	
	private StyledText documentText;
	private Caret caret;
	private Document document;
	private DocumentView documentView;
	
	public TextModifyFacade(){
	}
	
	public void setDocumentAndView(Document document,
			DocumentView documentView) {
		this.document = document;
		this.documentView = documentView;
		this.documentText = document.getStyledText();
		caret.setStyledText(documentText);
	}

	public void setCaret(Caret caret) {
		this.caret = caret;
	}

	
	
	
	
	
	
	
	
	
	

	public void keyEvent(KeyEvent event, ColumnViewPane callbackPane) {
		KeyCode code = event.getCode();
		System.out.println("key event: " + code);
		
		if(code == KeyCode.BACK_SPACE){
			backspaceTypeRemoveString();
		}
		else if(code == KeyCode.DELETE){
			deleteTypeRemoveString();
		}
		else if(code == KeyCode.LEFT && !event.isShiftDown()){
			caret.setCaretIndex(Math.max(0, caret.getCaretIndex() - 1));
			caret.setAnchor(0);
		}
		else if(code == KeyCode.LEFT && event.isShiftDown()){
			caret.setAnchor(caret.getAnchor() - 1);
		}
		else if(code == KeyCode.RIGHT && !event.isShiftDown()){
			caret.setCaretIndex(Math.min(caret.getCaretIndex() + 1, documentText.length() - 1));
			caret.setAnchor(0);
		}
		else if(code == KeyCode.RIGHT && event.isShiftDown()){
			caret.setAnchor(caret.getAnchor() + 1);
		}
		else if(code.isLetterKey()){
			insertString(event.getText());
		}
		else if(code == KeyCode.SPACE){
			insertString(" ");
		}
		else if(code == KeyCode.TAB){
			insertString("\t");
		}
		else{
			System.out.println("UNDEFINED KEY PRESSED");
		}
		
		callbackPane.refreshTextContentLater(caret.getSelectionStart());
	}
	
	public void insertString(String text){
		
	}

	public void backspaceTypeRemoveString(){
		if(caret.getAnchor() == 0 && caret.getCaretIndex() != 0){
			documentText.removeString(caret.getCaretIndex() - 1, caret.getCaretIndex());
			caret.setRelativeCaretIndex(-1);
		}
		else{
			documentText.removeString(caret.getSelectionStart(), caret.getSelectionEnd());
			caret.setAnchor(0);
		}
	}
	
	public void deleteTypeRemoveString(){
		if(caret.getAnchor() == 0 && caret.getCaretIndex() != documentText.length()-1){
			documentText.removeString(caret.getCaretIndex(), caret.getCaretIndex() + 1);
		}
		else{
			documentText.removeString(caret.getSelectionStart(), caret.getSelectionEnd());
			caret.setAnchor(0);
		}
	}

/*	public void startTextDivision(boolean isRecording) {
		previousLineEndIndex = 0;
		hasNextLine = true;
		isRecordingDivisionLengths = isRecording;
		
		if(isRecording == true){
			stringDivisionLengths.clear();
		}
	}

	public boolean hasNextLine() {
		return hasNextLine;
	}

	public String getNextLine(double lineSize) {
		previousLineStartIndex = previousLineEndIndex;
		int endIndex =0;/* documentText.getSameStyleSubstringFitsSize(lineSize, previousLineEndIndex);*//*		
		int startIndex = previousLineEndIndex;
		previousLineEndIndex = endIndex;
		if(endIndex >= documentText.length() - 1){
			hasNextLine = false;
		}
		
		if(isRecordingDivisionLengths){
			stringDivisionLengths.add(lineSize);
		}
		
		return documentText.substring(startIndex, endIndex);
	}*/
}
