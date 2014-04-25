package document.persistentproperties;

import geometry.libgdxmath.LineSegment;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import storage.XmlManager;
import document.Paragraph;
import document.TextLine;
import document.persistentproperties.interfaces.PersistentIndexedObject;
import document.style.TextStyle;

public class ParagraphProperties implements PersistentIndexedObject{
	
	protected int indexInParent;
	protected ArrayList<TextLine> textLines;
	protected ArrayList<LineSegment> lineSegments;
	protected TextStyle style;
	protected StringBuffer textBuffer;
	
	public ParagraphProperties() {
		
	}

	public ParagraphProperties(Element element) {
		loadFromXmlElement(element);
	}
	
	public Node saveToXmlNode(org.w3c.dom.Document doc) {
		//init paragraph
		Element paragraphElement = doc.createElement("Paragraph");
		paragraphElement.setAttribute("index", indexInParent + "");
		
		XmlManager.insertSingleElement(doc, paragraphElement, style);
		XmlManager.insertStringElement(doc, paragraphElement, "Text", textBuffer.toString());
		XmlManager.insertArrayListElements(doc, paragraphElement, "TextLines", textLines);
		XmlManager.insertArrayListElements(doc, paragraphElement, "LineSegments", lineSegments);
		
		return paragraphElement;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadFromXmlElement(Element element) {
		indexInParent = Integer.parseInt(element.getAttribute("index"));
		
		style = (TextStyle) XmlManager.loadObjectFromXmlElement("TextStyle", element);
		textBuffer = new StringBuffer(XmlManager.loadStringFromXmlElement("Text", element));
		textLines = (ArrayList<TextLine>) XmlManager.loadArrayListFromXmlElement("TextLines", "TextLine", element);
		for(int i = 0; i < textLines.size(); i++) {
			textLines.get(i).setParent((Paragraph) this);
		}
		
		lineSegments = (ArrayList<LineSegment>) XmlManager.loadArrayListFromXmlElement("LineSegments", "LineSegment", element);
	}
	
	@Override
	public int getPersistenceId() {
		return indexInParent;
	}
	
}
