package document;

import geometry.libgdxmath.Polygon;
import gui.columnview.ColumnView;
import gui.helper.DebugHelper;

import java.util.ArrayList;
import java.util.Collections;

import org.w3c.dom.Element;

import document.Paragraph.TextFillReturnValue;
import document.layout.LayoutMachine;
import document.persistentproperties.ParagraphSetProperties;

public class ParagraphSet extends ParagraphSetProperties {

	private ArrayList<Paragraph> paragraphs;
	private Column column;
	
	public ParagraphSet() {
		System.out.println("Paragraph set initialized");
		paragraphs = new ArrayList<Paragraph>();
	}

	public ParagraphSet(Element element) {
		super(element);
	}

	public ParagraphSet(Polygon finalPolygon) {
		System.out.println("Paragraph set initialized");
		paragraphs = new ArrayList<Paragraph>();
		setParagraphSpace(new ParagraphSpace(finalPolygon));
	}
	
	private void setupInitialParagraph() {
		Paragraph paragraph = new Paragraph(0);
		paragraph.setText("");
		addParagraph(paragraph);
		paragraph.setStyle(DebugHelper.debugStyle1);
	}

	public String toString() {
		return "ParagraphSet, paragraphCount: " + paragraphs.size() + ", text: " + getText();
	}

	public String getText() {
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
		ArrayList<TextLine> retList = new ArrayList<TextLine>();
		LayoutMachine machine = column.getLayoutMachine();
		
		int startSegment = 0;
		float startOffset = 0;
		
		//get all the paragraph texts to start in filling.
		for(int i = 0; i < paragraphs.size(); i++) {
			TextFillReturnValue retValue = paragraphs.get(i).fillWithAvailableText(machine, startSegment, startOffset);
			startSegment = retValue.getFinishLine();
			startOffset = retValue.getFinishOffset();
			retList.addAll(retValue.getTextLines());
		}
		
		System.out.println("Calculated text lines");
		for(int i = 0; i < retList.size(); i++) {
			System.out.println(retList.get(i));
		}
		
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
		
		if(paragraphs.size() == 0) {
			setupInitialParagraph();
		}
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
	}

	public void setIndexInDocument(int indexInDocument) {
		this.indexInDocument = indexInDocument;
	}

	public void validateLineOnCanvases(ColumnView parent) {
		getColumn().getLayoutMachine().validateLineOnCanvases(this, parent);
	}

}
