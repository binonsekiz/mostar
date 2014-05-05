package control;

import gui.columnview.DocumentView;
import document.Document;
import document.DocumentText;

abstract class Facade {

	protected Document document;
	protected DocumentView documentView;
	protected DocumentText documentText;

	public void setDocumentAndView(Document document, DocumentView documentView) {
		this.document = document;
		this.documentView = documentView;
		this.documentText = document.getDocumentText();
	}

}
