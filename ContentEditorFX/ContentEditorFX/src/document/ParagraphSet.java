package document;

import java.util.ArrayList;
import java.util.Collections;

import document.Paragraph.TextFillReturnValue;
import document.layout.LayoutMachine;

public class ParagraphSet {

	private ArrayList<Paragraph> paragraphs;
	private ParagraphSpace paragraphSpace;
	private Column column;
	private float angle;
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
		Collections.sort(paragraphs);
		paragraph.setParagraphSet(this);
	//	mergeWithSimilarStyles();
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
	
	public void setAngle(float angle) {
		this.angle = angle;
	}
	
	public float getAngle() {
		return angle;
	}

	public void setParagraphSpace(ParagraphSpace paragraphSpace) {
		this.paragraphSpace = paragraphSpace;
		paragraphSpace.setParagraphSet(this);
	}

	public void removeParagraph(Paragraph p2) {
		this.paragraphs.remove(p2);
	//	mergeWithSimilarStyles();
	}

/*	private void mergeWithSimilarStyles() {
		for(int i = 0; i < paragraphs.size() - 1; i++) {
			Paragraph p1 = paragraphs.get(i);
			Paragraph p2 = paragraphs.get(i+1);
			
			if(p1.getStyle().isEqual(p2.getStyle())) {
				p1.mergeWith(p2);
				i--;
			}
		}
	}*/

}
