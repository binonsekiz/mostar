package geometry;

import geometry.libgdxmath.LineSegment;
import geometry.libgdxmath.Polygon;
import geometry.libgdxmath.Vector3;

public class GeometryHelper {

	public static Polygon getRectanglePolygon(double width, double height){
		float[] vertices = new float[8];
		vertices[0] = 0;
		vertices[1] = 0;
		vertices[2] = (float) width;
		vertices[3] = 0;
		vertices[4] = (float) width;
		vertices[5] = (float) height;
		vertices[6] = 0;
		vertices[7] = (float) height;
		Polygon p = new Polygon(vertices);
		return p;		
	}
	
	public static Polygon getRegularPolygon(float centerX, float centerY, float radius, int vertexCount) {
		float[] vertices = new float[vertexCount * 2];
		
		for(int i = 0; i < vertexCount; i++) {
			vertices[2 * i] 	= (float) (centerX + radius * Math.cos(2 * Math.PI * i / vertexCount));
			vertices[2 * i+1] 	= (float) (centerY + radius * Math.sin(2 * Math.PI * i / vertexCount));
		}
		
		Polygon p = new Polygon(vertices);
		return p;
	}
	
	public static Polygon getRectanglePolygon(LineSegment segment, float height, float angle) {
		float[] vertices = new float[8];
		vertices[0] = (float) (segment.getLeftPoint().x + height * Math.sin(Math.toRadians(angle)));
		vertices[1] = (float) (segment.getLeftPoint().y + height * Math.cos(Math.toRadians(angle)));
		vertices[2] = (float) (segment.getRightPoint().x + height * Math.sin(Math.toRadians(angle)));
		vertices[3] = (float) (segment.getRightPoint().y + height * Math.cos(Math.toRadians(angle)));
		vertices[4] = segment.getRightPoint().x;
		vertices[5] = segment.getRightPoint().y;
		vertices[6] = segment.getLeftPoint().x;
		vertices[7] = segment.getLeftPoint().y;
		Polygon p = new Polygon(vertices);
		return p;
	}

	public static void changeWidthOfRectangularPolygon(Polygon shape, float width) {
		float[] vertices = shape.getVertices();
		vertices[2] = width;
		vertices[4] = width;
	}
	
	public static void changeHeightOfRectangularPolygon(Polygon shape, float height){
		float[] vertices = shape.getVertices();
		vertices[5] = height;
		vertices[7] = height;
	}

	public static Vector3 getVectorFromAngle(float angle) {
		Vector3 retVal = new Vector3((float)Math.cos(angle), (float)Math.sin(angle), 0);
		return retVal;
	}

	public static javafx.scene.shape.Polygon polygonShapeFromPolygon(Polygon shape) {
		javafx.scene.shape.Polygon retVal = new javafx.scene.shape.Polygon();
		retVal.getPoints().addAll(shape.getDoubleVertices());
		return retVal;
	}

}
