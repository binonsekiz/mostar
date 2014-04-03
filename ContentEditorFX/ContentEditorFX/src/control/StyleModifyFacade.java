package control;

import document.style.TextStyle;
import document.style.TextStyleRepository;

public class StyleModifyFacade extends Facade{
	
	private Caret caret;
	private TextModifyFacade textModifyFacade;
	private TextStyle backupStyle;
	
	public StyleModifyFacade() {
		
	}
	
	/**
	 * If the selected text is a single style, returns that. Else returns null.
	 * @return
	 */
	public TextStyle getSelectionStyle(){
		int caretStart = caret.getSelectionStart();
		int caretEnd = caret.getSelectionEnd();
		TextStyle initStyle = documentText.getStyleAt(caretStart);
		
		if(caretStart == caretEnd) {
			TextStyleRepository.setSelectedStyle(initStyle);
			return initStyle;
		}
		
		for(int i = caretStart + 1; i <= caretEnd; i++) {
			// We should have just one instance of every style, so "==" should work fine.
			if(!(documentText.getStyleAt(i) == initStyle)) {
				TextStyleRepository.setSelectedStyle(null);
				backupStyle = documentText.getStyleAt(caretStart);
				return null;
			}
		}
		TextStyleRepository.setSelectedStyle(initStyle);
		return initStyle;
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
		//		textModifyFacade.divideAtIndex(style, caret.getCaretIndex());
			}
			else{
				textModifyFacade.setStyleAtInterval(style, caret.getCaretIndex(), caret.getAnchor());
			}
		}
	}

	public void changeFontName(String value) {
		TextStyle initStyle = getInitialStyle();
		TextStyle newStyle = TextStyleRepository.deriveStyleWithFontName(initStyle, value);	
		textModifyFacade.setStyleAtInterval(newStyle, caret.getCaretIndex(), caret.getAnchor());
		System.out.println("Font name change: " + value);
	}

	public void changeFontSize(double value) {
		TextStyle initStyle = getInitialStyle();
		TextStyle newStyle = TextStyleRepository.deriveStyleWithFontSize(initStyle, value);
		textModifyFacade.setStyleAtInterval(newStyle, caret.getCaretIndex(), caret.getAnchor());
		System.out.println("Font size change: " + value);
	}
	
	private TextStyle getInitialStyle() {
		TextStyle initStyle;
		if(TextStyleRepository.getSelectedStyle() == null) {
			initStyle = backupStyle;
		}
		else{
			initStyle = TextStyleRepository.getSelectedStyle();
		}
		return initStyle;
	}
}
