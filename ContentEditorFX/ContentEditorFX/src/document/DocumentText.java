package document;

import gui.helper.DebugHelper;

import java.io.Serializable;
import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import storage.XmlManager;
import document.style.TextStyle;

public class DocumentText implements PersistentObject{

	private ArrayList<Paragraph> globalText;
	private ArrayList<ParagraphSet> paragraphSets;
	private Document document;
	
	public Node getXmlNode(org.w3c.dom.Document doc) {
		Element docTextElement = doc.createElement("DocumentText");
		
		XmlManager.insertArrayListElements(doc, docTextElement, "Paragraphs", globalText);
		XmlManager.insertArrayListElements(doc, docTextElement, "ParagraphSets", paragraphSets);

		return docTextElement;
	}

	
	public DocumentText (Document document) {
		System.out.println("DocumentText initialized");
		this.document = document;
		globalText = new ArrayList<Paragraph>();
		
		paragraphSets = new ArrayList<ParagraphSet>();
		
		//TODO: debug
		ParagraphSet set1 = new ParagraphSet(this);
		set1.setColumn(document.getColumns().get(0));
		set1.setParagraphSpace(DebugHelper.paragraphSpaces.get(3));
		set1.setAngle(0);
		
		Paragraph paragraph = new Paragraph(this, 0);
		paragraph.setText("");
		addParagraph(paragraph, set1);
		paragraph.setStyle(DebugHelper.debugStyle1);
		
//		ParagraphSet set2 = new ParagraphSet(this);
//		set2.setColumn(document.getColumns().get(0));
//		set2.setParagraphSpace(DebugHelper.paragraphSpaces.get(5));
//		set2.setAngle(0);
//
//		Paragraph paragraph2 = new Paragraph(this, 1);
//		paragraph2.setText("ccccc ddddd");
//		addParagraph(paragraph2, set2);
//		paragraph2.setStyle(DebugHelper.debugStyle1);
//
//		ParagraphSet set3 = new ParagraphSet(this);
//		set3.setColumn(document.getColumns().get(0));
//		set3.setParagraphSpace(DebugHelper.paragraphSpaces.get(0));
//		set3.setAngle(0);
//
//		Paragraph paragraph3 = new Paragraph(this, 2);
//		paragraph3.setText("ccccc ddddd");
//		addParagraph(paragraph3, set3);
//		paragraph3.setStyle(DebugHelper.debugStyle1);
	}

	/*public void addParagraph(Paragraph paragraph) {
		//add a new paragraph set as a default
		ParagraphSet newSet = new ParagraphSet(this);
		addParagraphSet(newSet);
		addParagraph(paragraph, newSet);
	}*/
	
	public void addParagraph(Paragraph paragraph, ParagraphSet paragraphSet) {
		if(!paragraphSets.contains(paragraphSet)){
			paragraphSets.add(paragraphSet);
		}
		
		globalText.add(paragraph.getIndexInParent(), paragraph);
		for(int i = 0; i < globalText.size(); i++) {
			globalText.get(i).setIndexInParent(i);
		}
		paragraphSet.addParagraph(paragraph);
	}
	
	public void removeParagraph(int indexInParent) {
		globalText.remove(indexInParent);
		for(int i = 0; i < globalText.size(); i++) {
			globalText.get(i).setIndexInParent(i);
		}
	}
	
	public void addParagraphSet(ParagraphSet paragraphSet) {
		this.paragraphSets.add(paragraphSet);
		paragraphSet.setIndexInDocument(this.paragraphSets.size() - 1);
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
	 * Expected to return 0 or negative values. Will be used for ctrl+arrow
	 * @param caretIndex
	 * @return
	 */
	public int getRelativeWordStartIndexBefore(int caretIndex) {
		//TODO: implement later
		return caretIndex;
	}
	
	/**
	 * Counts the characters between caretIndex and the first space occurrence
	 * Expected to return 0 or positive values. 
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

	public ArrayList<ParagraphSet> getParagraphSetsInColumn(Column column) {
		ArrayList<ParagraphSet> retVal = new ArrayList<ParagraphSet>();
		for(int i = 0; i< paragraphSets.size(); i++) {
			if(paragraphSets.get(i).getColumn() == column)
				retVal.add(paragraphSets.get(i));
		}
		return retVal;
	}

	public ParagraphSet getParagraphSetBefore(ParagraphSet paragraphSet) {
		int index = paragraphSets.indexOf(paragraphSet);
		index --;
		if(index < 0)
			return null;
		else
			return paragraphSets.get(index);
	}

	
	
}
