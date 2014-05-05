package document.widget;

import geometry.libgdxmath.Polygon;
import geometry.libgdxmath.Rectangle;

import org.w3c.dom.Element;

import document.persistentproperties.WidgetProperties;

public class Widget extends WidgetProperties{

	private Polygon shape;
	private TextWrapType textWrap;
	
	public Widget(){
		textWrap = TextWrapType.Inline;
	}
	
	public Widget(Element element) {
		super(element);
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
	public WidgetType getType() {
		throw new RuntimeException("Typeless Widget");
	}

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
