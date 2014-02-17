package document;

import geometry.libgdxmath.LineSegment;
import gui.columnview.ParagraphOnCanvas;

import java.util.ArrayList;

public class ParagraphSet {

	private ArrayList<Paragraph> paragraphs;
	private ArrayList<LineSegment> lineSegments;
	private ParagraphOnCanvas paragraphOnCanvas;
	private DocumentText parent;
	
	public ParagraphSet(DocumentText parent) {
		System.out.println("Paragraph set initialized");
		this.parent = parent;
		paragraphs = new ArrayList<Paragraph>();
	}

	public String toString() {
		return "ParagraphSet, paragraphCount: " + paragraphs.size() + ", text: " + getText();
	}
	
	public void setLineSegments(ArrayList<LineSegment> lineSegments) {
		this.lineSegments = lineSegments;
	}
	
	public ArrayList<LineSegment> getLineSegments() {
		return lineSegments;
	}
	
	public String getText() {
		return getText(paragraphs.get(0).getStartIndex(), paragraphs.get(paragraphs.size()-1).getEndIndex());
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

	public void startTextDivision() {
		for(int i = 0; i < paragraphs.size(); i++) {
			paragraphs.get(i).startTextDivision();
		}
	}
	
	public void updateWithAvailableText(ArrayList<LineSegment> trimmedLines) {
		ArrayList<TextLine> retList = new ArrayList<TextLine>();
		for(int i = 0; i < paragraphs.size(); i++) {
			retList.addAll(paragraphs.get(i).getTextLines());
		}
		
		int paragraphIndex = 0;
		
		for(int i = 0; i < trimmedLines.size(); i++) {
			Paragraph paragraph = this.getParagraph(paragraphIndex);
			if(paragraph == null) throw new RuntimeException("olmamali");
			TextLine newTextLine = retList.get(i);
			
			paragraph.calculateNextLine(newTextLine, trimmedLines.get(i).getLength());
			retList.add(newTextLine);
			
			if(paragraph.hasElements() == false && paragraphIndex + 1 < paragraphs.size()) {
				paragraphIndex ++;
			}
			else{
				//do nothing. just call calculate next line again, it will fill the 
				//new text lines with latest index.
			}
		}
	}

	public ArrayList<TextLine> fillWithAvailableText(ArrayList<LineSegment> trimmedLines) {
		ArrayList<TextLine> retList = new ArrayList<TextLine>();
		int paragraphIndex = 0;
		
		for(int i = 0; i < trimmedLines.size(); i++) {
			Paragraph paragraph = this.getParagraph(paragraphIndex);
			if(paragraph == null) throw new RuntimeException("olmamali");
			TextLine newTextLine = new TextLine(paragraph, 0, 0);
			
			paragraph.calculateNextLine(newTextLine, trimmedLines.get(i).getLength());
			retList.add(newTextLine);
			
			if(paragraph.hasElements() == false && paragraphIndex + 1 < paragraphs.size()) {
				paragraphIndex ++;
			}
			else{
				//do nothing. just call calculate next line again, it will fill the 
				//new text lines with latest index.
			}
		}
		
		return retList;
	}

	private Paragraph getParagraphWithIndex(int index) {
		for(int i = 0; i < paragraphs.size(); i++) {
			if(paragraphs.get(i).includesIndex(index)) {
				return paragraphs.get(i);
			}
		}
		return null;
	}
	
}
