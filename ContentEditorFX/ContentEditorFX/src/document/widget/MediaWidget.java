package document.widget;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import storage.XmlManager;
import document.widget.Widget.WidgetType;
import geometry.GeometryHelper;
import geometry.libgdxmath.Polygon;


public class MediaWidget extends Widget{
	
	@Override
	public Node getXmlNode(Document doc) {
		Element mediaWidgetElement = super.getXmlAbstractNode(doc);
		XmlManager.insertStringElement(doc, mediaWidgetElement, "Type", WidgetType.MediaWidget.toString());
		return mediaWidgetElement;
	}

	public MediaWidget(double x, double y, double width, double height){
		Polygon shape = GeometryHelper.getRectanglePolygon(width, height); 
		shape.setPositionX((float) x);
		shape.setPositionY((float) y);
		setShape(shape);
	}
	
	@Override
	public WidgetType getType() {
		return WidgetType.MediaWidget;
	}



}
