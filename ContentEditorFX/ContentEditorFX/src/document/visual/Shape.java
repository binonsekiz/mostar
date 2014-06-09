package document.visual;

import javafx.scene.canvas.GraphicsContext;
import geometry.libgdxmath.LineSegment;
import geometry.libgdxmath.Polygon;
import geometry.libgdxmath.Polygon.LineSegmentIntersection;
import geometry.libgdxmath.Rectangle;
import geometry.libgdxmath.Vector2;
import document.persistentproperties.ShapeProperties;

public class Shape extends ShapeProperties implements VisualEntity{

	public Shape(Polygon polygon) {
		this.polygon = polygon;
	}
	
	public Polygon getPolygon(){
		return polygon;
	}

	public Rectangle getBoundingRectangle() {
		return polygon.getBoundingRectangle();
	}

	public void setPositionX(float x) {
		polygon.setPositionX(x);
	}

	public void setPositionY(float y) {
		polygon.setPositionY(y);
	}

	public Vector2 getEdgeMidpoint(int i) {
		return polygon.getEdgeMidpoint(i);
	}

	@Override
	public boolean contains(float mx, float my) {
		return polygon.contains(mx, my);
	}

	@Override
	public LineSegmentIntersection intersectWithHeight(LineSegment segment, float height) {
		return polygon.intersectWithHeight(segment, height);
	}

	@Override
	public float area() {
		return polygon.area();
	}

	@Override
	public void draw(GraphicsContext context) {
		polygon.draw(context);
	}

	public int getVerticeCount() {
		return polygon.getVerticeCount();
	}

	public Vector2 getEdgeNormal(int i) {
		return polygon.getEdgeNormal(i);
	}

	public void move(float layoutX, float layoutY) {
		polygon.move(layoutX, layoutY);
	}

	public int getEdgeCount() {
		return polygon.getEdgeCount();
	}

	public double[] getTransformedXVertices() {
		return polygon.getTransformedXVertices();
	}

	public double[] getTransformedYVertices() {
		return polygon.getTransformedYVertices();
	}

	public Vector2 snapToEdge(Vector2 mousePosition, int i) {
		return polygon.snapToEdge(mousePosition, i);
	}

	public void translate(float xDiff, float yDiff) {
		polygon.translate(xDiff, yDiff);
	}
	
}
