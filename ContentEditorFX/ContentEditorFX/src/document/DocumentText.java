package document;

import java.util.ArrayList;

public class DocumentText {

	private ArrayList<Paragraph> globalText;
	private Document document;
	
	public DocumentText (Document document) {
		System.out.println("DocumentText initialized");
		this.document = document;
		globalText = new ArrayList<Paragraph>();
		addParagraph();
	}
	
	public void addParagraph() {
		int index = globalText.size();
		globalText.add(new Paragraph(this, index));
	}
	
	public void addParagraph(int index) {
		globalText.add(new Paragraph(this, index));
		for(int i = index; i < globalText.size(); i++) {
			globalText.get(i).setIndexInParent(i);
		}
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
	
	public String exportString() {
		String total = "";
		for(int i = 0; i < globalText.size(); i++) {
			total = total + "\nParagraph " + i + ":\n" + globalText.get(i).getText();
		}
		return total;
	}

	public ArrayList<Paragraph> getParagraphs() {
		return globalText;
	}
	
	public Paragraph getParagraph(int index) {
		return globalText.get(index);
	}

	protected void updateIndexAfter(int indexInParent) {
		if(globalText.size() > indexInParent + 1) {
			Paragraph paragraph = globalText.get(indexInParent + 1);
			paragraph.setStartIndexInBigText(globalText.get(indexInParent).getEndIndex());
		}
	}
	
}
