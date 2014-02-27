package geometry.libgdxmath;

import javafx.scene.canvas.GraphicsContext;
import settings.GlobalAppSettings;

public class LineSegment implements Comparable<LineSegment>{
	private Vector2 firstPoint;
	private Vector2 secondPoint;
	private Vector2 normal;
	
	public LineSegment(Vector2 firstPoint, Vector2 secondPoint){
		this.firstPoint = firstPoint;
		this.secondPoint = secondPoint;
		normal = new Vector2(secondPoint).sub(firstPoint);
		normal.rotate(90).nor();
	}
	
	public LineSegment trimToFitInPolygon(Polygon polygon) {
		return polygon.getPortionInside(this);
	}
	
	public Vector2 getFirstPoint(){
		return firstPoint;
	}
	
	public Vector2 getSecondPoint(){
		return secondPoint;
	}
	
	public Vector2 getLeftPoint(){
		if(firstPoint.x < secondPoint.x)
			return firstPoint;
		return secondPoint;
	}
	
	public Vector2 getRightPoint(){
		if(firstPoint.x > secondPoint.x)
			return firstPoint;
		return secondPoint;
	}
	
	public Vector2 getUpperPoint(){
		if(firstPoint.y < secondPoint.y)
			return firstPoint;
		return secondPoint;
	}
	
	public Vector2 getLowerPoint(){
		if(firstPoint.y > secondPoint.y)
			return firstPoint;
		return secondPoint;
	}
	
	@Override
	public String toString(){
		return "LineSegment P1: " + firstPoint + ", P2: " + secondPoint; 
	}

	public float getLength() {
		return firstPoint.dst(secondPoint);
	}

	public Vector2 getNormal() {
		return new Vector2(normal);
	}
	
	public Vector2 getMidpoint(){
		return new Vector2((firstPoint.x + secondPoint.x)/2, (firstPoint.y + secondPoint.y)/2);
	}

	public boolean isEqual(LineSegment lineSegment1) {
		if(this.firstPoint.epsilonEquals(lineSegment1.firstPoint, GlobalAppSettings.ignoreValuesBelow) && this.secondPoint.epsilonEquals(lineSegment1.secondPoint, GlobalAppSettings.ignoreValuesBelow))
			return true;
		return false;
	}

	public int compareTo(LineSegment o) {
		if(this.getLeftPoint().x < o.getLeftPoint().x)
			return -1;
		else if(this.getLeftPoint().x > o.getLeftPoint().x)
			return 1;
		else 
			return 0;
	}

	public Vector2 closestPoint(float x, float y) {
		return Intersector.closestPointOnLineSegment(this, new Vector2(x,y));
	}

	public Vector2 getDistanceCoordinate(float distance) {
		if(this.getLength() < GlobalAppSettings.ignoreValuesBelow) {
			return this.firstPoint;
		}
		float factor = (float) (distance / this.getLength());
		Vector2 sub = this.secondPoint.cpy();
		sub = sub.sub(this.firstPoint);
		sub.scl(factor);
		return sub.add(this.firstPoint);
	}
	
	public LineSegment buildLowerLineSegment(float height, float angle){
		LineSegment lowerLineSegment = new LineSegment(
				new Vector2(
						(float)(getFirstPoint().x + height * Math.sin(Math.toRadians(angle))), 
						(float)(getFirstPoint().y + height * Math.cos(Math.toRadians(angle)))),
				new Vector2(
						(float)(getSecondPoint().x + height * Math.sin(Math.toRadians(angle))) ,
						(float)(getSecondPoint().y + height * Math.cos(Math.toRadians(angle)))));
		return lowerLineSegment;
	}

	public void fixCoordinates() {
		if(firstPoint.x > secondPoint.x) {
			Vector2 firstBackup = firstPoint.cpy();
			firstPoint = secondPoint;
			secondPoint = firstBackup;
		}
	}

	public LineSegment averageLineSegment(LineSegment trimmedSegmentLower) {
		LineSegment retVal = new LineSegment(
				new Vector2((firstPoint.x+trimmedSegmentLower.getFirstPoint().x)/2, (firstPoint.y+trimmedSegmentLower.getFirstPoint().y)/2), 
				new Vector2((secondPoint.x+trimmedSegmentLower.getSecondPoint().x)/2, (secondPoint.y+trimmedSegmentLower.getSecondPoint().y)/2));
		return retVal;
	}

	/**
	 * Returns the angle of the line segment in degrees.
	 * TODO: there is most likely a faster solution.
	 * @return
	 */
	public float getAngle() {
		Vector2 diffVector = new Vector2(this.secondPoint.x - this.firstPoint.x, this.secondPoint.y - this.firstPoint.y);
		return diffVector.angle();
	}

	public LineSegment divideUsingLength(float usedLength) {
		Vector2 midPoint = getDistanceCoordinate(usedLength);
		LineSegment retVal = new LineSegment(midPoint.cpy(), getSecondPoint());
		this.secondPoint = midPoint;
		return retVal;
	}

	/**
	 * Trims from the start callOffsetAmount.
	 * @param callOffset
	 * @return
	 */
	public LineSegment trimWithStartOffset(float callOffset) {
		Vector2 newStart = getDistanceCoordinate(callOffset);
		this.firstPoint = newStart;
		return this;
	}

	/**
	 * Trims from the end so that the length is roughly equal to desired length
	 * @param calculatedWidth
	 */
	public void adjustLength(float desiredLength) {
		Vector2 newEnd = getDistanceCoordinate(desiredLength);
		this.secondPoint = newEnd;
	}

	public void draw(GraphicsContext context) {
		context.strokeLine(firstPoint.x, firstPoint.y, secondPoint.x, secondPoint.y);
	}

	public LineSegment cpy() {
		return new LineSegment(firstPoint.cpy(), secondPoint.cpy());
	}

}
