package document.widget;

import geometry.GeometryHelper;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import storage.XmlManager;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import document.visual.Shape;


public class MediaWidget extends Widget{
	
	@Override
	public Element saveToXmlNode(Document doc) {
		Element mediaWidgetElement = super.saveToXmlNode(doc);
		XmlManager.insertStringElement(doc, mediaWidgetElement, "Type", WidgetType.MediaWidget.toString());
		return mediaWidgetElement;
	}

	public MediaWidget(double x, double y, double width, double height){
		Shape shape = new Shape(GeometryHelper.getRectanglePolygon(width, height)); 
		shape.setPositionX((float) x);
		shape.setPositionY((float) y);
		setShape(shape);
	}

	public MediaWidget(Element element) {
		loadFromXmlElement(element);
	}

	@Override
	public WidgetType getType() {
		return WidgetType.MediaWidget;
	}

	@Override
	public void loadFromXmlElement(Element node) {
		throw new NotImplementedException();
	}



}
