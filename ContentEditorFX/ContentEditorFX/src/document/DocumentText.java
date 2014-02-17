package document;

import java.util.ArrayList;

public class DocumentText {

	private ArrayList<Paragraph> globalText;
	private ArrayList<ParagraphSet> paragraphSets;
	private Document document;
	
	public DocumentText (Document document) {
		System.out.println("DocumentText initialized");
		this.document = document;
		globalText = new ArrayList<Paragraph>();
		
		paragraphSets = new ArrayList<ParagraphSet>();
		
		ParagraphSet set1 = new ParagraphSet(this);
		
		Paragraph paragraph = new Paragraph(this, 0);
		addParagraph(paragraph, set1);
		paragraph.setText("abcdef7");
		
		Paragraph paragraph2 = new Paragraph(this, 1);
		addParagraph(paragraph2, set1);
		paragraph2.setText("ghijklm8");
	}

	public void addParagraph(Paragraph paragraph) {
		//add a new paragraph set as a default
		ParagraphSet newSet = new ParagraphSet(this);
		addParagraphSet(newSet);
		addParagraph(paragraph, newSet);
	}
	
	public void addParagraph(Paragraph paragraph, ParagraphSet paragraphSet) {
		if(!paragraphSets.contains(paragraphSet)){
			paragraphSets.add(paragraphSet);
		}
		
		paragraphSet.addParagraph(paragraph);
		globalText.add(paragraph.getIndexInParent(), paragraph);
		for(int i = paragraph.getIndexInParent(); i < globalText.size(); i++) {
			globalText.get(i).setIndexInParent(i);
		}
	}
	
	public void addParagraphSet(ParagraphSet paragraphSet) {
		this.paragraphSets.add(paragraphSet);
	}
	
	public TextStyle getStyleAt(int caretIndex) {
		for(int i = 0; i < globalText.size(); i++){
			Paragraph temp = globalText.get(i);
			if(caretIndex >= temp.getStartIndex() && caretIndex <= temp.getEndIndex()){
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
			total = total + "\nParagraph " + i + ":\n" + "Start: " + globalText.get(i).getStartIndex() + ", end: " + globalText.get(i).getEndIndex() + "\n" + globalText.get(i).getText();
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

	public void debugValidateAllTextLines() {
		int value = 0;
		for(int i = 0; i < globalText.size(); i++) {
			globalText.get(i).setStartIndexInBigText(value);
			value = value + globalText.get(i).getText().length();
		}
	}
	
	/**
	 * Counts the characters between caretIndex and the first space occurrence
	 * Expected to return 0 or negative values.
	 * @param caretIndex
	 * @return
	 */
	public int getRelativeWordStartIndexBefore(int caretIndex) {
		//TODO: implement later
		return caretIndex;
	}
	
	/**
	 * Counts the characters between caretIndex and the first space occurrence
	 * Expected to return 0 or positive values. okürýns
	 * @param caretIndex
	 * @return
	 */
	public int getRelativeWordStartIndexAfter(int caretIndex) {
		//TODO: implement later
		return caretIndex;
	}

	public ArrayList<ParagraphSet> getParagraphSets() {
		return paragraphSets;
	}

	public ParagraphSet getParagraphSet(int i) {
		return paragraphSets.get(i);
	}

	public Paragraph getParagraphThatIncludesIndex(int index) {
		for(int i = 0; i < globalText.size(); i++) {
			if(globalText.get(i).includesIndex(index))
				return globalText.get(i);
		}
		return null;
	}
	
}
