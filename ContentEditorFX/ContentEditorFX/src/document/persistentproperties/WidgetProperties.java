package document.persistentproperties;

import geometry.libgdxmath.Polygon;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import storage.XmlManager;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import document.persistentproperties.interfaces.PersistentObject;
import document.widget.Widget.TextWrapType;

public class WidgetProperties implements PersistentObject{

	private Polygon shape;
	private TextWrapType textWrap;
	

	public WidgetProperties() {
		
	}
	
	public WidgetProperties(Element element) {
		loadFromXmlElement(element);
	}
	
	@Override
	public void loadFromXmlElement(Element node) {
		throw new NotImplementedException();
	}
	
	@Override
	public Element saveToXmlNode(Document doc) {
		Element widgetElement = doc.createElement("Widget");
		
		XmlManager.insertSingleElement(doc, widgetElement, shape);
		XmlManager.insertStringElement(doc, widgetElement, "TextWrap", textWrap.toString());
		
		return widgetElement;
	}
	
}
