package document.widget;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import storage.XmlManager;
import geometry.libgdxmath.Polygon;
import geometry.libgdxmath.Rectangle;
import document.PersistentObject;

public abstract class Widget implements PersistentObject{

	private int pageNumber;
	private Polygon shape;
	private TextWrapType textWrap;
	
	protected Element getXmlAbstractNode(Document doc) {
		Element widgetElement = doc.createElement("Widget");
		
		XmlManager.insertNumberElement(doc, widgetElement, "PageNumber", pageNumber);
		XmlManager.insertSingleElement(doc, widgetElement, shape);
		XmlManager.insertStringElement(doc, widgetElement, "TextWrap", textWrap.toString());
		
		return widgetElement;
	}
	
	public Widget(){
		textWrap = TextWrapType.Inline;
	}
	
	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Rectangle getBorders() {
		return shape.getBoundingRectangle();
	}
	
	public double getX(){
		return shape.getBoundingRectangle().x;
	}
	
	public double getY(){
		return shape.getBoundingRectangle().y;
	}
	
	public double getWidth(){ 
		return shape.getBoundingRectangle().width;
	}
	
	public double getHeight(){
		return shape.getBoundingRectangle().height;
	}
	
	public TextWrapType getTextWrap() {
		return textWrap;
	}
	
	public void setTextWrap(TextWrapType value) {
		this.textWrap = value;
	}
	
	/**
	 * Make sure subclasses implement this correctly
	 * @return
	 */
	public abstract WidgetType getType();

	public Polygon getShape() {
		return shape;
	}

	public void setShape(Polygon shape) {
		System.out.println("@@Setting widget shape to: " + shape);
		this.shape = shape;
	}

	public enum WidgetType{
		SingleImageWidget,
		ImageGalleryWidget,
		MediaWidget,
		WebViewWidget,
		ThreeDViewerWidget,
	}
	
	public enum TextWrapType{
		Inline,
		Merge,
		Behind,
		Front
	}
}
