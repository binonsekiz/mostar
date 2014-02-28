package control;

import gui.columnview.DocumentView;
import document.Document;
import document.DocumentText;
import document.style.TextStyle;

public class StyleModifyFacade {
	
	private DocumentText documentText;
	private Caret caret;
	private Document document;
	private DocumentView documentView;
	private TextModifyFacade textModifyFacade;
	
	public StyleModifyFacade() {
		
	}
	
	public void setDocumentAndView(Document document, DocumentView documentView) {
		this.document = document;
		this.documentView = documentView;
		this.documentText = document.getDocumentText();
	}
	
	public void setCaret(Caret caret) {
		this.caret = caret;
	}
	
	public void setTextModifyFacade(TextModifyFacade facade) {
		this.textModifyFacade = facade;
	}
	
	public void changeStyleForSelection(TextStyle style) {
		if(!caret.isSelectionStyleEquals(style)) {
			if(caret.isAtEnd()){
				textModifyFacade.addNewParagraphToEnd(style);
			}
			else if(caret.getCaretIndex() == caret.getAnchor()) {
				//divide the paragraph in two, add a third in between.
				textModifyFacade.divideAtIndex(style, caret.getCaretIndex());
			}
			else{
				textModifyFacade.setStyleAtInterval(style, caret.getCaretIndex(), caret.getAnchor());
			}
		}
	}

	public void changeFontName(String value) {
		System.out.println("Font name change: " + value);
	}

	public void changeFontSize(double value) {
		System.out.println("Font size change: " + value);
	}
}
