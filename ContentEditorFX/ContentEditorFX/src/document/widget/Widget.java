package document.widget;

import geometry.libgdxmath.Rectangle;

import org.w3c.dom.Element;

import document.persistentproperties.WidgetProperties;
import document.visual.Shape;

public class Widget extends WidgetProperties{

	private Shape shape;
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

	public Shape getShape() {
		return shape;
	}

	public void setShape(Shape shape) {
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
