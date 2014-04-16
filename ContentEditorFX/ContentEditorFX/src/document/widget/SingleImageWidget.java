package document.widget;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import storage.XmlManager;
import document.widget.Widget.WidgetType;
import geometry.GeometryHelper;
import geometry.libgdxmath.Polygon;


public class SingleImageWidget extends Widget{
	
	@Override
	public Node getXmlNode(Document doc) {
		Element singleImageWidgetElement = super.getXmlAbstractNode(doc);
		XmlManager.insertStringElement(doc, singleImageWidgetElement, "Type", WidgetType.SingleImageWidget.toString());
		return singleImageWidgetElement;
	}
	
	public SingleImageWidget(double x, double y, double width, double height){
		Polygon shape = GeometryHelper.getRectanglePolygon(width, height); 
		shape.setPositionX((float) x);
		shape.setPositionY((float) y);
		setShape(shape);
	}

	@Override
	public WidgetType getType() {
		return WidgetType.SingleImageWidget;
	}


}
