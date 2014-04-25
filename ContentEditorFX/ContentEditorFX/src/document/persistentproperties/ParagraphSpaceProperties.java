package document.persistentproperties;

import geometry.libgdxmath.Polygon;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import storage.XmlManager;
import document.persistentproperties.interfaces.PersistentObject;

public class ParagraphSpaceProperties implements PersistentObject{

	protected Polygon allowedShape;
	
	public ParagraphSpaceProperties() {
		//default constructor
	}
	
	public ParagraphSpaceProperties(Element element) {
		loadFromXmlElement(element);
	}
	
	@Override
	public Node saveToXmlNode(Document doc) {
		Element paragraphSpaceElement = doc.createElement("ParagraphSpace");
		paragraphSpaceElement.appendChild(allowedShape.saveToXmlNode(doc));
		
		return paragraphSpaceElement;
	}

	@Override
	public void loadFromXmlElement(Element element) {
		allowedShape = (Polygon) XmlManager.loadObjectFromXmlElement("Polygon", element);
	}
	
}
