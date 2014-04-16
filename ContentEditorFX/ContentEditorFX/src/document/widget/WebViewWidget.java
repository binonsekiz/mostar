package document.widget;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import storage.XmlManager;
import geometry.GeometryHelper;
import geometry.libgdxmath.Polygon;

public class WebViewWidget extends Widget{
	
	@Override
	public Node getXmlNode(Document doc) {
		Element webViewWidgetElement = super.getXmlAbstractNode(doc);
		XmlManager.insertStringElement(doc, webViewWidgetElement, "Type", WidgetType.WebViewWidget.toString());
		return webViewWidgetElement;
	}

	public WebViewWidget(double x, double y, double width, double height){
		Polygon shape = GeometryHelper.getRectanglePolygon(width, height); 
		shape.setPositionX((float) x);
		shape.setPositionY((float) y);
		setShape(shape);
	}

	@Override
	public WidgetType getType() {
		return WidgetType.WebViewWidget;
	}


	
}
