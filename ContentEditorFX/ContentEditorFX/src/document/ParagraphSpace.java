package document;

import geometry.libgdxmath.LineSegment;
import geometry.libgdxmath.Polygon;
import geometry.libgdxmath.Rectangle;
import geometry.libgdxmath.Vector2;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * This is a small set of ParagraphWithStyle's bundled in a single shape.
 * @author sahin
 *
 */
public class ParagraphSpace {

	private ArrayList<Paragraph> paragraphs;
	private Polygon allowedShape;
	private Column parent;
	
	public ParagraphSpace(Column parent, Polygon shape) {
		this.parent = parent;
		paragraphs = new ArrayList<Paragraph>();
		this.allowedShape = shape;
	}

	public Polygon getShape() {
		return allowedShape;
	}

	public void setShape(Polygon allowedShape) {
		this.allowedShape = allowedShape;
	}

	public void draw(GraphicsContext graphicsContext) {
		graphicsContext.setStroke(Color.GAINSBORO);
		graphicsContext.setLineWidth(2);
		allowedShape.draw(graphicsContext);
	}

	/**
	 * This returns the top left point of the text division start point.
	 * If angle is 0 and the shape is rectangle, 0,0 is returned.
	 * @return
	 */
	public Vector2 getTextDivisionStartPoint() {
		float angle = 0;
		if(paragraphs.size() > 0) {
			angle = paragraphs.get(0).getAngle();
		}
		
		Rectangle boundingRectangle = allowedShape.getBoundingRectangle();
		
		System.out.println("Offset point calculation started, rect: " + boundingRectangle);
		
		Vector2 rectMidPoint = new Vector2();
		boundingRectangle.getCenter(rectMidPoint);
		System.out.println("\tCenter: " + rectMidPoint);
		
		Vector2 topLeft = new Vector2(boundingRectangle.x, boundingRectangle.y);
		System.out.println("\tTop Left: " + topLeft);
		
		//using the mid point of the bounding rectangle, 
		//rotate the up-left point as much as the angle
		float currentAngle;
		float length;
		LineSegment tempSegment = new LineSegment(rectMidPoint, topLeft);
		currentAngle = tempSegment.getAngle();
		length = tempSegment.getLength();
		
		System.out.println("\tTemp segment: " + tempSegment);
		float angleOffset =  currentAngle - angle;
		
		System.out.println("#######Temp angle: " + currentAngle + ", desired angle: " + angle + ", offset: " + angleOffset);
		
		//multiplication by 0.99 is to ensure the line segment that will be initialized 
		//will reside in the polygon while trying to layout the words
		float xPoint = (float) (rectMidPoint.x + length * Math.cos(Math.toRadians(angleOffset)) * 0.999f);
		float yPoint = (float) (rectMidPoint.y + length * Math.sin(Math.toRadians(angleOffset)) * 0.999f);
		
		Vector2 retVal =  new Vector2(xPoint, yPoint);
		
		System.out.println("\nRetVal: " + retVal);
		
		return retVal;
	}
	
	public String toString() {
		return "Paragraph Space: " + allowedShape + ", paragraph count: " + paragraphs.size();
	}
	
	
}
