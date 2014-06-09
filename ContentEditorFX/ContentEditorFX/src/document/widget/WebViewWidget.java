package document.widget;

import geometry.GeometryHelper;
import geometry.libgdxmath.Polygon;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import document.visual.Shape;
import storage.XmlManager;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class WebViewWidget extends Widget{
	
	@Override
	public Element saveToXmlNode(Document doc) {
		Element webViewWidgetElement = super.saveToXmlNode(doc);
		XmlManager.insertStringElement(doc, webViewWidgetElement, "Type", WidgetType.WebViewWidget.toString());
		return webViewWidgetElement;
	}

	public WebViewWidget(double x, double y, double width, double height){
		Shape shape = new Shape(GeometryHelper.getRectanglePolygon(width, height)); 
		shape.setPositionX((float) x);
		shape.setPositionY((float) y);
		setShape(shape);
	}

	public WebViewWidget(Element element) {
		loadFromXmlElement(element);
	}

	@Override
	public WidgetType getType() {
		return WidgetType.WebViewWidget;
	}

	@Override
	public void loadFromXmlElement(Element node) {
		throw new NotImplementedException();
	}


	
}
