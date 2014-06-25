package document.widget;

import geometry.GeometryHelper;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import storage.XmlManager;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import document.visual.Shape;


public class SingleImageWidget extends Widget{
	
	@Override
	public Element saveToXmlNode(Document doc) {
		Element singleImageWidgetElement = super.saveToXmlNode(doc);
		XmlManager.insertStringElement(doc, singleImageWidgetElement, "Type", WidgetType.SingleImageWidget.toString());
		return singleImageWidgetElement;
	}
	
	public SingleImageWidget(double x, double y, double width, double height){
		Shape shape = new Shape(GeometryHelper.getRectanglePolygon(width, height)); 
		shape.setPositionX((float) x);
		shape.setPositionY((float) y);
		setShape(shape);
	}

	public SingleImageWidget(Element element) {
		loadFromXmlElement(element);	
	}

	@Override
	public WidgetType getType() {
		return WidgetType.SingleImageWidget;
	}

	@Override
	public void loadFromXmlElement(Element node) {
		throw new NotImplementedException();		
	}


}
