package document.widget;

import geometry.GeometryHelper;
import geometry.libgdxmath.Polygon;
import javafx.scene.image.Image;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import storage.XmlManager;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ImageGalleryWidget extends Widget{
	
	private Image[] images;
	
	@Override
	public Element getXmlNode(Document doc) {
		Element mediaWidgetElement = super.getXmlNode(doc);
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

	public ImageGalleryWidget(Element element) {
		loadFromXmlElement(element);
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

	@Override
	public void loadFromXmlElement(Element node) {
		throw new NotImplementedException();
	}

}
