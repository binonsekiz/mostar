package document;

import gui.columnview.ParagraphOnCanvas;

import java.util.ArrayList;

public class DocumentText {

	private ArrayList<Paragraph> globalText;
	private Document document;
	
	public DocumentText (Document document) {
		this.document = document;
		globalText = new ArrayList<Paragraph>();
		debug();
	}
	
	private void debug() {
		globalText.add(new Paragraph(this));
	}
	
	public void setDebugText(String value, ParagraphOnCanvas paragraphView) {
		globalText.get(0).setText(value);
		paragraphView.setParagraph(globalText.get(0));
		
		System.out.println("DocumentText::setDebugText: global.get(0): " + globalText.get(0) + ", paragraphview: " + paragraphView);
	}

	public Paragraph getStyleTextPair(int index) {
		return globalText.get(index);
	}

	public TextStyle getStyleAt(int caretIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	public void importString(String value) {
		// TODO Auto-generated method stub
		
	}

	public Paragraph getDebugParagraph() {
		return globalText.get(0);
	}

	public ArrayList<Paragraph> getParagraphs() {
		return globalText;
	}
	
	
}
