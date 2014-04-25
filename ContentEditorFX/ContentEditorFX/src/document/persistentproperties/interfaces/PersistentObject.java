package document.persistentproperties.interfaces;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public interface PersistentObject {

	public Node saveToXmlNode(org.w3c.dom.Document doc);
	public void loadFromXmlElement(Element element);
	
}
