package document;

import org.w3c.dom.Node;

public interface PersistentObject {

	public Node getXmlNode(org.w3c.dom.Document doc);
	
}
