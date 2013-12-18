package document;

import geometry.libgdxmath.Rectangle;
import document.PageSpecs.Measurement;

public class PageInsets {

	public static PageInsets Default = new PageInsets(PageSpecs.DefaultMeasurement);

	private float minX,minY,maxX,maxY,width,height;
	private Rectangle usableRectangle;
	
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
