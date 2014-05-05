package document.persistentproperties;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import storage.XmlManager;
import document.Paragraph;
import document.ParagraphSet;
import document.persistentproperties.interfaces.PersistentObject;

public class DocumentTextProperties implements PersistentObject{

	protected ArrayList<Paragraph> globalText;
	protected ArrayList<ParagraphSet> paragraphSets;
	
	public DocumentTextProperties() {
		
	}

	public DocumentTextProperties(Element element) {
		loadFromXmlElement(element);
	}
	
	public Node saveToXmlNode(org.w3c.dom.Document doc) {
		Element docTextElement = doc.createElement("DocumentText");
		
		XmlManager.insertArrayListElements(doc, docTextElement, "Paragraphs", globalText);
		XmlManager.insertArrayListElements(doc, docTextElement, "ParagraphSets", paragraphSets);

		return docTextElement;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadFromXmlElement(Element element) {
		paragraphSets = (ArrayList<ParagraphSet>) XmlManager.loadArrayListFromXmlElement("ParagraphSets", "ParagraphSet", element);
		globalText = (ArrayList<Paragraph>) XmlManager.loadArrayListFromXmlElement("Paragraphs", "Paragraph", element);
	}
		
}
