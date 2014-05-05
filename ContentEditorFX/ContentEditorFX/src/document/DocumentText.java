package document;

import geometry.libgdxmath.Polygon;

import java.util.ArrayList;

import org.w3c.dom.Element;

import document.persistentproperties.DocumentTextProperties;
import document.style.TextStyle;

public class DocumentText extends DocumentTextProperties{
		
	DocumentText (Document document) {
		System.out.println("DocumentText initialized");
		globalText = new ArrayList<Paragraph>();
		paragraphSets = new ArrayList<ParagraphSet>();
	}
	
	public DocumentText(Element element) {
		loadFromXmlElement(element);
	}

	void addParagraph(Paragraph paragraph, ParagraphSet paragraphSet) {
		if(!paragraphSets.contains(paragraphSet)){
			paragraphSets.add(paragraphSet);
		}
		
		globalText.add(paragraph.getIndexInParent(), paragraph);
		for(int i = 0; i < globalText.size(); i++) {
			globalText.get(i).setIndexInParent(i);
		}
		paragraphSet.addParagraph(paragraph);
	}
	
	void removeParagraph(int indexInParent) {
		globalText.remove(indexInParent);
		for(int i = 0; i < globalText.size(); i++) {
			globalText.get(i).setIndexInParent(i);
		}
	}
	
	private void addParagraphSet(ParagraphSet paragraphSet) {
		this.paragraphSets.add(paragraphSet);
		paragraphSet.setIndexInDocument(this.paragraphSets.size() - 1);
	}
	
	public ParagraphSet addBlankParagraphSet(Polygon finalPolygon) {
		ParagraphSet pSet = new ParagraphSet(finalPolygon);
		Paragraph paragraph = new Paragraph(0);
		pSet.addParagraph(paragraph);
		globalText.add(paragraph);
		addParagraphSet(pSet);
		return pSet;
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

	public ArrayList<ParagraphSet> getParagraphSetsInColumn(Column column) {
		ArrayList<ParagraphSet> retVal = new ArrayList<ParagraphSet>();
		for(int i = 0; i< paragraphSets.size(); i++) {
			if(paragraphSets.get(i).getColumn() == column)
				retVal.add(paragraphSets.get(i));
		}
		return retVal;
	}
	
	
// TODO Remove unused code found by UCDetector
//	 	/**
//	 	 * Counts the characters between caretIndex and the first space occurrence
//	 	 * Expected to return 0 or negative values. Will be used for ctrl+arrow
//	 	 * @param caretIndex
//	 	 * @return
//	 	 */
//	 	public int getRelativeWordStartIndexBefore(int caretIndex) {
//	 		//TODO: implement later
//	 		return caretIndex;
//	 	}
// TODO Remove unused code found by UCDetector
//	 	/**
//	 	 * Counts the characters between caretIndex and the first space occurrence
//	 	 * Expected to return 0 or positive values. 
//	 	 * @param caretIndex
//	 	 * @return
//	 	 */
//	 	public int getRelativeWordStartIndexAfter(int caretIndex) {
//	 		//TODO: implement later
//	 		return caretIndex;
//	 	}
// TODO Remove unused code found by UCDetector
// 	public ParagraphSet getParagraphSetBefore(ParagraphSet paragraphSet) {
// 		int index = paragraphSets.indexOf(paragraphSet);
// 		index --;
// 		if(index < 0)
// 			return null;
// 		else
// 			return paragraphSets.get(index);
// 	}
// TODO Remove unused code found by UCDetector
//	 	protected void updateIndexAfter(int indexInParent) {
//	 		System.out.println("\nUpdate after index: " + indexInParent);
//	 		
//	 		if(globalText.size() > indexInParent + 1) {
//	 			System.out.println("\tGot inside, setting start to: " + globalText.get(indexInParent).getEndIndex());
//	 			Paragraph paragraph = globalText.get(indexInParent + 1);
//	 			paragraph.setStartIndexInBigText(globalText.get(indexInParent).getEndIndex());
//	 		}
//	 		else{
//	 			System.out.println("Early out");
//	 		}
//	 	}

}
