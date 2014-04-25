package document.persistentproperties;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import storage.XmlManager;
import document.Column;
import document.DocumentText;
import document.PageInsets;
import document.PageSpecs.Measurement;
import document.persistentproperties.interfaces.PersistentObject;

public class DocumentProperties implements PersistentObject{

	protected Measurement measurement;
	protected PageInsets pageInsets;
	
	protected DocumentText globalText;
	protected ArrayList<Column> columns;
	
	public DocumentProperties() {
		//default constructor
	}
	
	public DocumentProperties(Element element) {
		loadFromXmlElement(element);
	}
	
	@Override
	public void loadFromXmlElement(Element element) {
		if(!element.getTagName().equals("PortisInteractiveDocument")) throw new RuntimeException("Malformed XML");
		
		measurement = (Measurement) XmlManager.loadObjectFromXmlElement("Measurement", element);
		pageInsets = (PageInsets) XmlManager.loadObjectFromXmlElement("PageInsets", element);
		globalText = (DocumentText) XmlManager.loadObjectFromXmlElement("DocumentText", element);
		columns = (ArrayList<Column>) XmlManager.loadArrayListFromXmlElement("Columns", "Column", element);
	}
	
	public Node saveToXmlNode(org.w3c.dom.Document doc) {
		Element documentElement = doc.createElement("PortisInteractiveDocument");
		XmlManager.insertSingleElement(doc, documentElement, measurement);
		XmlManager.insertSingleElement(doc, documentElement, pageInsets);
		XmlManager.insertSingleElement(doc, documentElement, globalText);
		XmlManager.insertArrayListElements(doc, documentElement, "Columns", columns);
		return documentElement;
	}
	
}
