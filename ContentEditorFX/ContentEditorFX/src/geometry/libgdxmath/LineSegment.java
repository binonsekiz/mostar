package geometry.libgdxmath;

import settings.GlobalAppSettings;

public class LineSegment {
	private Vector2 firstPoint;
	private Vector2 secondPoint;
	private Vector2 normal;
	
	public LineSegment(Vector2 firstPoint, Vector2 secondPoint){
		this.firstPoint = firstPoint;
		this.secondPoint = secondPoint;
		normal = new Vector2(secondPoint).sub(firstPoint);
		normal.rotate(90).nor();
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
		return "LineSegment x: " + firstPoint + ", y: " + secondPoint; 
	}

	public double getLength() {
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
}
