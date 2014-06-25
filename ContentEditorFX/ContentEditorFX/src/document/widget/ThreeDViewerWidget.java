package document.widget;

import geometry.GeometryHelper;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import storage.XmlManager;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import document.visual.Shape;

public class ThreeDViewerWidget extends Widget{

	@Override
	public Element saveToXmlNode(Document doc) {
		Element threeDWidgetElement = super.saveToXmlNode(doc);
		XmlManager.insertStringElement(doc, threeDWidgetElement, "Type", WidgetType.ThreeDViewerWidget.toString());
		return threeDWidgetElement;
	}
	
	public ThreeDViewerWidget(double x, double y, double width, double height){
		Shape shape = new Shape(GeometryHelper.getRectanglePolygon(width, height)); 
		shape.setPositionX((float) x);
		shape.setPositionY((float) y);
		setShape(shape);
	}
	
	public ThreeDViewerWidget(Element element) {
		loadFromXmlElement(element);
	}

	@Override
	public WidgetType getType() {
		return WidgetType.ThreeDViewerWidget;
	}

	@Override
	public void loadFromXmlElement(Element node) {
		throw new NotImplementedException();
	}


}
