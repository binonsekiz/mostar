package geometry;

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

}
