package document;

import geometry.libgdxmath.Rectangle;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import storage.XmlManager;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import document.PageSpecs.Measurement;
import document.persistentproperties.interfaces.PersistentObject;

public class PageInsets implements PersistentObject{

	public static PageInsets Default = new PageInsets(PageSpecs.DefaultMeasurement);

	private float minX,minY,maxX,maxY,width,height;
	private Rectangle usableRectangle;

	public Node saveToXmlNode(org.w3c.dom.Document doc) {
		Element pageInsetsElement = doc.createElement("PageInsets");
		XmlManager.insertNumberElement(doc, pageInsetsElement, "MinX", minX);
		XmlManager.insertNumberElement(doc, pageInsetsElement, "MinY", minY);
		XmlManager.insertNumberElement(doc, pageInsetsElement, "MaxX", maxX);
		XmlManager.insertNumberElement(doc, pageInsetsElement, "MaxY", maxY);
		XmlManager.insertNumberElement(doc, pageInsetsElement, "Width", width);
		XmlManager.insertNumberElement(doc, pageInsetsElement, "Height", height);
		XmlManager.insertSingleElement(doc, pageInsetsElement, usableRectangle);
		return pageInsetsElement;
	}

	@Override
	public void loadFromXmlElement(Element element) {
		minX = XmlManager.loadNumberFromXmlElement("MinX", element).floatValue();
		minY = XmlManager.loadNumberFromXmlElement("MinY", element).floatValue();
		maxX = XmlManager.loadNumberFromXmlElement("MaxX", element).floatValue();
		maxY = XmlManager.loadNumberFromXmlElement("MaxY", element).floatValue();
		width = XmlManager.loadNumberFromXmlElement("Width", element).floatValue();
		height = XmlManager.loadNumberFromXmlElement("Height", element).floatValue();
		usableRectangle = (Rectangle) XmlManager.loadObjectFromXmlElement("Rectangle", element);
	}
	
	public float getMinX() {
		return minX;
	}

	public void setMinX(float x) {
		this.minX = x;
		usableRectangle.x = x;
	}

	public float getMinY() {
		return minY;
	}

	public void setMinY(float y) {
		this.minY = y;
		usableRectangle.y = y;
	}

	public float getMaxX() {
		return maxX;
	}

	public void setMaxX(float x2) {
		this.maxX = x2;
		usableRectangle.width = getActualWidth();
	}

	public float getMaxY() {
		return maxY;
	}

	public void setMaxY(float y2) {
		this.maxY = y2;
		usableRectangle.height = getActualHeight();
	}

	public PageInsets(Measurement measurement){
		minX = 20;
		maxX = 20;
		minY = 20;
		maxY = 20;
		width = measurement.getWidth();
		height = measurement.getHeight();
		usableRectangle = new Rectangle(minX, minY, getActualWidth(), getActualHeight());
	}

	public PageInsets(Element element) {
		loadFromXmlElement(element);
	}

	public float getPageWidth() {
		return width;
	}
	
	public float getActualWidth(){
		return width - minX - maxX;
	}
	
	public float getActualHeight(){
		return height - minY - maxY;
	}

	public void setPageWidth(float width) {
		this.width = width;
		usableRectangle.width = getActualWidth();
	}

	public float getPageHeight() {
		return height;
	}

	public void setPageHeight(float height) {
		this.height = height;
		usableRectangle.height = getActualHeight();
	}

	public Rectangle getUsableRectangle() {
		return usableRectangle;
	}


	
}
