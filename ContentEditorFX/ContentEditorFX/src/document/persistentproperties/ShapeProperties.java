package document.persistentproperties;

import geometry.libgdxmath.Polygon;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import document.persistentproperties.interfaces.PersistentObject;

public class ShapeProperties implements PersistentObject{

	protected Polygon polygon;
	
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
