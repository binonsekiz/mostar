package document;

import java.util.ArrayList;

public class DocumentText {

	private ArrayList<Paragraph> globalText;
	private Document document;
	
	public DocumentText (Document document) {
		System.out.println("DocumentText initialized");
		this.document = document;
		globalText = new ArrayList<Paragraph>();
		
		Paragraph paragraph = new Paragraph(this, 0);
		addParagraph(paragraph);
		paragraph.setText("abcdef7");
		
	/*	Paragraph paragraph2 = new Paragraph(this, 1);
		addParagraph(paragraph2);
		paragraph2.setText("ghijklm8");*/
	}

	public void addParagraph(Paragraph paragraph) {
		globalText.add(paragraph.getIndexInParent(), paragraph);
		for(int i = paragraph.getIndexInParent(); i < globalText.size(); i++) {
			globalText.get(i).setIndexInParent(i);
		}
	}
	
	public Paragraph getStyleTextPair(int index) {
		return globalText.get(index);
	}

	public TextStyle getStyleAt(int caretIndex) {
		for(int i = 0; i < globalText.size(); i++){
			Paragraph temp = globalText.get(i);
			if(caretIndex >= temp.getStartIndexInBigText() && caretIndex <= temp.getEndIndex()){
				return temp.getStyle();
			}
		}
		return null;
	}

	public void importString(String value) {
		// TODO Auto-generated method stub
		
	}
	
	public String exportString() {
		String total = "";
		for(int i = 0; i < globalText.size(); i++) {
			total = total + "\nParagraph " + i + ":\n" + "Start: " + globalText.get(i).getStartIndexInBigText() + ", end: " + globalText.get(i).getEndIndex() + "\n" + globalText.get(i).getText();
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
		System.out.println("\nUpdate after index: " + indexInParent);
		
		if(globalText.size() > indexInParent + 1) {
			System.out.println("\tGot inside, setting start to: " + globalText.get(indexInParent).getEndIndex());
			Paragraph paragraph = globalText.get(indexInParent + 1);
			paragraph.setStartIndexInBigText(globalText.get(indexInParent).getEndIndex());
		}
		else{
			System.out.println("Early out");
		}
	}

	public int getEndIndex() {
		return globalText.get(globalText.size() - 1).getEndIndex();
	}

	public String toString(){
		return exportString();
	}
	
}
