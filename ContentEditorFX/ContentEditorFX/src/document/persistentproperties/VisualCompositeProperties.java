package document.persistentproperties;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import document.persistentproperties.interfaces.PersistentObject;
import document.visual.VisualComponent;

public class VisualCompositeProperties implements PersistentObject {

	protected ArrayList<VisualComponent> children;
	
	public VisualCompositeProperties() {
		// Default constructor
	}
	
	@Override
	public Node saveToXmlNode(Document doc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadFromXmlElement(Element element) {
		// TODO Auto-generated method stub
		
	}

}
