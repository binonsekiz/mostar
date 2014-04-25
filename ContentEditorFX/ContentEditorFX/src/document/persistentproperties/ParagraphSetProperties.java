package document.persistentproperties;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import storage.XmlManager;
import document.ParagraphSpace;
import document.persistentproperties.interfaces.PersistentIndexedObject;

public class ParagraphSetProperties implements PersistentIndexedObject{

	protected ParagraphSpace paragraphSpace;
	protected float angle;
	protected int indexInDocument;
	
	public ParagraphSetProperties() {
		
	}
	
	public ParagraphSetProperties(Element element) {
		loadFromXmlElement(element);
	}
	
	public Node saveToXmlNode(org.w3c.dom.Document doc) {
		Element paragraphSetElement = doc.createElement("ParagraphSet");
		
		XmlManager.insertSingleElement(doc, paragraphSetElement, paragraphSpace);
		XmlManager.insertNumberElement(doc, paragraphSetElement, "Angle", angle);
				
		return paragraphSetElement;
	}
	
	@Override
	public void loadFromXmlElement(Element element) {
		paragraphSpace = (ParagraphSpace) XmlManager.loadObjectFromXmlElement("ParagraphSpace", element);
		angle = XmlManager.loadNumberFromXmlElement("Angle", element).floatValue();
	}
	
	@Override
	public int getPersistenceId() {
		return indexInDocument;
	}
	
	
}
