package document;

import gui.helper.LayoutMachine;

import java.util.ArrayList;

import document.Paragraph.TextFillReturnValue;

public class ParagraphSet {

	private ArrayList<Paragraph> paragraphs;
	private ParagraphSpace paragraphSpace;
	private Column column;
	private StringBuffer textBuffer;
	private DocumentText parent;
	
	public ParagraphSet(DocumentText parent) {
		System.out.println("Paragraph set initialized");
		this.parent = parent;
		paragraphs = new ArrayList<Paragraph>();
	}

	public String toString() {
		return "ParagraphSet, paragraphCount: " + paragraphs.size() + ", text: " + getText();
	}

	public String getText() {
	//	return getText(paragraphs.get(0).getStartIndex(), paragraphs.get(paragraphs.size()-1).getEndIndex());
		String retVal = "";
		for(int i = 0; i < paragraphs.size(); i++) {
			retVal = retVal + paragraphs.get(i).getText();
		}
		return retVal;
	}

	public String getText(int start, int end) {
		String cummulativeText = "";
		for(int i = 0; i < paragraphs.size(); i++) {
			cummulativeText = cummulativeText + paragraphs.get(i).subSequence(start, end);
		}	
		return cummulativeText;
	}

	public Paragraph getParagraph(int index) {
		return paragraphs.get(index);
	}

	public void addParagraph(Paragraph paragraph) {
		this.paragraphs.add(paragraph);
		paragraph.setParagraphSet(this);
	}
	
	public int getStartIndex() {
		return paragraphs.get(0).getStartIndex();
	}
	
	public int getEndIndex() {
		return paragraphs.get(paragraphs.size() - 1).getEndIndex();
	}

	public int getParagraphCount() {
		return paragraphs.size();
	}

	public ArrayList<TextLine> fillWithAvailableText() {
		System.out.println("ParagraphSet::Fill With Available Text Started");
		ArrayList<TextLine> retList = new ArrayList<TextLine>();
		LayoutMachine machine = column.getLayoutMachine();
		
		int startSegment = 0;
		float startOffset = 0;
		
		//get all the paragraph texts to start in filling.
		for(int i = 0; i < paragraphs.size(); i++) {
			TextFillReturnValue retValue = paragraphs.get(i).fillWithAvailableText(machine, startSegment, startOffset);
			System.out.println("\tGot retValue: " + retValue);
			startSegment = retValue.getFinishLine();
			startOffset = retValue.getFinishOffset();
			retList.addAll(retValue.getTextLines());
		}
		
		System.out.println("ParagraphSet::Fill With Available Text finished");
		return retList;
	}

	public Column getColumn() {
		return column;
	}

	public void setColumn(Column column) {
		if(this.column != null) {
			column.removeParagraphSet(this);
		}
		
		this.column = column;
		column.addParagraphSet(this);
	}

	public ParagraphSpace getParagraphSpace() {
		return paragraphSpace;
	}
	
	public float getAngle() {
		if(this.paragraphs.size() > 0) {
			return paragraphs.get(0).getAngle();
		}
		return 0;
	}

	public void setParagraphSpace(ParagraphSpace paragraphSpace) {
		this.paragraphSpace = paragraphSpace;
	}

}
