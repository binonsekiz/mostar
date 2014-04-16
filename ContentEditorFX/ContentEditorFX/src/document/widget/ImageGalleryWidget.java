package document.widget;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import storage.XmlManager;
import document.widget.Widget.WidgetType;
import geometry.GeometryHelper;
import geometry.libgdxmath.Polygon;
import javafx.scene.image.Image;

public class ImageGalleryWidget extends Widget{
	
	private Image[] images;
	
	@Override
	public Node getXmlNode(Document doc) {
		Element mediaWidgetElement = super.getXmlAbstractNode(doc);
		XmlManager.insertStringElement(doc, mediaWidgetElement, "Type", WidgetType.MediaWidget.toString());
		//TODO: insert image names
		return mediaWidgetElement;
	}
	
	public ImageGalleryWidget(double x, double y, double width, double height){
		Polygon shape = GeometryHelper.getRectanglePolygon(width, height); 
		shape.setPositionX((float) x);
		shape.setPositionY((float) y);
		setShape(shape);
		images = new Image[9];
	}
	
	@Override
	public WidgetType getType() {
		return WidgetType.ImageGalleryWidget;
	}

	public Image getImage(int index) {
		return images[index];
	}

	public void setImage(int i, Image image) {
		this.images[i] = image;
	}

}
