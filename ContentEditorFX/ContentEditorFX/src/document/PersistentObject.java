package document;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public interface PersistentObject {

	public Node getXmlNode(org.w3c.dom.Document doc);
	public void loadFromXmlElement(Element node);
	
}
