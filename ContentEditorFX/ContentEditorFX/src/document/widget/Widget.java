package document.widget;

import geometry.libgdxmath.Polygon;

public abstract class Widget {

	private int pageNumber;
	private Border borders;
	private WidgetType type;
	private Polygon shape;
	private TextWrapType textWrap;
	
	public Widget(){
		borders = new Border();
		textWrap = TextWrapType.Inline;
	}
	
	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Border getBorders() {
		return borders;
	}
	
	public double getBorderX() {
		return borders.x;
	}
	
	public double getBorderX2() {
		return borders.x + borders.width;
	}

	public double getBorderY1() {
		return borders.y;
	}
	
	public double getBorderY2() {
		return borders.y + borders.height;
	}
	
	public double getX(){
		return borders.x;
	}
	
	public double getY(){
		return borders.y;
	}
	
	public double getWidth(){ 
		return borders.width;
	}
	
	public double getHeight(){
		return borders.height;
	}
	
	public TextWrapType getTextWrap() {
		return textWrap;
	}
	
	public void setBorders(Border borders) {
		this.borders = borders;
	}
	
	public void setBorders(double x, double y, double width, double height){
		borders.x = x; borders.y = y; borders.width = width; borders.height = height;
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
		this.shape = shape;
	}

	class Border{
		public double x, y, width, height;
	}
	
	public enum WidgetType{
		SingleImageWidget,
		ImageGalleryWidget,
		MediaWidget,
		WebViewWidget,
	}
	
	public enum TextWrapType{
		Inline,
		Merge,
		Behind,
		Front
	}
}
