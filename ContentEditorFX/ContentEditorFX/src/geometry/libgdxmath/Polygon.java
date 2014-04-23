package geometry.libgdxmath;

import java.io.Serializable;

import javafx.scene.canvas.GraphicsContext;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import settings.GlobalAppSettings;
import storage.XmlManager;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import document.PersistentObject;

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/**
 *  Encapsulates a 2D polygon defined by it's vertices relative to an origin point (default of 0, 0). 
 */
public class Polygon implements Serializable, PersistentObject{
	private static final long serialVersionUID = 1289325039892826840L;
	private float[] localVertices;
	
	private float[] worldVertices;
	private Double[] localDoubleVertices;
	private float x, y;
	private float originX, originY;
	private float rotation;
	private float scaleX = 1, scaleY = 1;
	private boolean dirty = true;
	private Rectangle bounds;

	private short[] indexList;

	private Rectangle localBounds;
	
	public Node saveToXmlNode(Document doc) {
		Element polygonElement = doc.createElement("Polygon");
		
		XmlManager.insertNumberElement(doc, polygonElement, "X", x);
		XmlManager.insertNumberElement(doc, polygonElement, "Y", y);
		XmlManager.insertNumberElement(doc, polygonElement, "OriginX", originX);
		XmlManager.insertNumberElement(doc, polygonElement, "OriginY", originY);
		XmlManager.insertNumberElement(doc, polygonElement, "Rotation", rotation);
		XmlManager.insertNumberElement(doc, polygonElement, "ScaleX", scaleX);
		XmlManager.insertNumberElement(doc, polygonElement, "ScaleY", scaleY);
		XmlManager.insertFloatArrayElement(doc, polygonElement, "LocalVertices", localVertices);
		
		return polygonElement;
	}
	
	@Override
	public void loadFromXmlElement(Element element) {
		x = XmlManager.loadNumberFromXmlElement("X", element).floatValue();
		y = XmlManager.loadNumberFromXmlElement("Y", element).floatValue();
		originX = XmlManager.loadNumberFromXmlElement("OriginX", element).floatValue();
		originY = XmlManager.loadNumberFromXmlElement("OriginY", element).floatValue();
		scaleX = XmlManager.loadNumberFromXmlElement("ScaleX", element).floatValue();
		scaleY = XmlManager.loadNumberFromXmlElement("ScaleY", element).floatValue();
		rotation = XmlManager.loadNumberFromXmlElement("Rotation", element).floatValue();
		
		localVertices = XmlManager.loadFloatArrayFromXmlElement("LocalVertices", element);
	}
	
	/** Constructs a new polygon with no vertices. */
	public Polygon () {
		this.localVertices = new float[0];
		indexList = new short[0];
	}

	/** Constructs a new polygon from a float array of parts of vertex points.
	 * 
	 * @param vertices an array where every even element represents the horizontal part of a point, and the following element
	 *           representing the vertical part
	 * 
	 * @throws IllegalArgumentException if less than 6 elements, representing 3 points, are provided */
	public Polygon (float[] vertices) {
		if (vertices.length < 6) throw new IllegalArgumentException("polygons must contain at least 3 points.");
		this.localVertices = vertices;
		setupIndexList();
		calculateDoubleVertices();
	}
	
	public Polygon(Element element) {
		loadFromXmlElement(element);
	}

	private void setupIndexList(){
		indexList = new short[(localVertices.length/2 - 2) * 3];	
		for(int i = 0; i < indexList.length; i+= 3){
			indexList[i] = 0;
			indexList[i + 1] = (short) ((i / 3) + 1);
			indexList[i + 2] = (short) ((i / 3) + 2);
		}
	}

	/** Returns the polygon's local vertices without scaling or rotation and without being offset by the polygon position. */
	public float[] getVertices () {
		return localVertices;
	}
	
	public int getVerticeCount() {
		return localVertices.length;
	}
	
	public short[] getIndexList(){
		return indexList;
	}
	
	public LineSegment getLineSegment(int i){
		i = i % getEdgeCount();
		float[] transformedVertices = getTransformedVertices();
		return new LineSegment(
				new Vector2(transformedVertices[(i*2)], 
							transformedVertices[(i*2)+1]), 
				new Vector2(transformedVertices[((i+1)*2) % getVerticeCount()], 
							transformedVertices[((i+1)*2+1) % getVerticeCount()]));
	}
	
	public LineSegment getEdge(int i){
		return getLineSegment(i);
	}
	
	public Vector2 getEdgeNormal(int i){
		LineSegment segment = getEdge(i);
		return segment.getNormal();
	}
	
	/**
	 * moves the edge by the given amount 
	 * @param edge
	 * @param amount
	 */
	public void moveEdge(int edge, Vector2 amount){
		moveVertex(edge, amount);
		moveVertex((edge + 1) % getEdgeCount(), amount);
		calculateDoubleVertices();
	}
	
	public void moveVertex(int vertex, Vector2 amount){
		localVertices[vertex*2] += amount.x;
		localVertices[vertex*2 + 1] += amount.y;
		dirty = true;
	}
	
	public double[] getTransformedXVertices() {
		double[] localXVertices = new double[localVertices.length/2];
		float[] transformedVertices = getTransformedVertices();
		
		for(int i = 0; i < localVertices.length; i+= 2){
			localXVertices[i/2] = transformedVertices[i];
 		}
		
		return localXVertices;
	}
	
	public double[] getTransformedYVertices() {
		double[] localYVertices = new double[localVertices.length/2];
		float[] transformedVertices = getTransformedVertices();
		
		for(int i = 1; i < localVertices.length; i+= 2){
			localYVertices[i/2] = transformedVertices[i];
 		}
		
		return localYVertices;
	}
	
	/**
	 * This function returns a point on the lines of the polygon that is closest
	 * to the given point. If the given point is further than maxSnapDistance to any of
	 * the edges, null is returned.
	 * @param point
	 * @return
	 */
	
	public Vector2 snapToEdge(Vector2 point, double maxSnapDistance){
		Vector2 closestPoint = null;
		double closestDistance = Double.MAX_VALUE;
		//TODO:fix
		for(int i = 0; i < getEdgeCount(); i++){
			LineSegment segment = getLineSegment(i);
		}
		
		return closestPoint;
	}

	public int getEdgeCount() {
		return getVerticeCount() / 2;
	}

	/** Calculates and returns the vertices of the polygon after scaling, rotation, and positional translations have been applied,
	 * as they are position within the world.
	 * 
	 * @return vertices scaled, rotated, and offset by the polygon position. */
	public float[] getTransformedVertices () {
		if (!dirty) return worldVertices;
		dirty = false;

		final float[] localVertices = this.localVertices;
		if (worldVertices == null || worldVertices.length != localVertices.length) worldVertices = new float[localVertices.length];

		final float[] worldVertices = this.worldVertices;
		final float positionX = x;
		final float positionY = y;
		final float originX = this.originX;
		final float originY = this.originY;
		final float scaleX = this.scaleX;
		final float scaleY = this.scaleY;
		final boolean scale = scaleX != 1 || scaleY != 1;
		final float rotation = this.rotation;
		final float cos = MathUtils.cosDeg(rotation);
		final float sin = MathUtils.sinDeg(rotation);

		for (int i = 0, n = localVertices.length; i < n; i += 2) {
			float x = localVertices[i] - originX;
			float y = localVertices[i + 1] - originY;

			// scale if needed
			if (scale) {
				x *= scaleX;
				y *= scaleY;
			}

			// rotate if needed
			if (rotation != 0) {
				float oldX = x;
				x = cos * x - sin * y;
				y = sin * oldX + cos * y;
			}

			worldVertices[i] = positionX + x + originX;
			worldVertices[i + 1] = positionY + y + originY;
		}
		return worldVertices;
	}
	
	private void calculateDoubleVertices() {
		// build the double array
		localDoubleVertices = new Double[localVertices.length];
		Rectangle rect = getLocalBoundingRectangle();
		
		for(int i = 0; i < localDoubleVertices.length; i+=2) {
			localDoubleVertices[i] = new Double(localVertices[i] - rect.x);
			localDoubleVertices[i+1] = new Double(localVertices[i+1] - rect.y);
		}
	}
	
	public Double[] getDoubleVertices() {
		// build the array if it doesnt exist
		if(localDoubleVertices == null) {
			calculateDoubleVertices();
		}
		return localDoubleVertices;
	}

	/** Sets the origin point to which all of the polygon's local vertices are relative to. */
	public void setOrigin (float originX, float originY) {
		this.originX = originX;
		this.originY = originY;
		dirty = true;
	}

	/** Sets the polygon's position within the world. */
	public void setPosition (float x, float y) {
		this.x = x;
		this.y = y;
		dirty = true;
	}

	/** Sets the polygon's local vertices relative to the origin point, without any scaling, rotating or translations being applied.
	 *
	 * @param vertices float array where every even element represents the x-coordinate of a vertex, and the proceeding element
	 *           representing the y-coordinate.
	 * @throws IllegalArgumentException if less than 6 elements, representing 3 points, are provided */
	public void setVertices (float[] vertices) {
		if (vertices.length < 6) throw new IllegalArgumentException("polygons must contain at least 3 points.");

		// if the provided vertices are the same length, we can copy them into localVertices
		if (localVertices.length == vertices.length) {
			for (int i = 0; i < localVertices.length; i++) {
				localVertices[i] = vertices[i];
			}
		} else {
			localVertices = vertices;
		}
		setupIndexList();
		dirty = true;
	}
	
	public short[] getTriangleIndex(){
		
		return null;
	}

	/** Translates the polygon's position by the specified horizontal and vertical amounts. */
	public void translate (float x, float y) {
		this.x += x;
		this.y += y;
		dirty = true;
	}
	
	/** Absolute move to the given coordinates */
	public void move(float x, float y){
		this.x = x;
		this.y = y;
		dirty = true;
	}

	/** Sets the polygon to be rotated by the supplied degrees. */
	public void setRotation (float degrees) {
		this.rotation = degrees;
		dirty = true;
	}

	/** Applies additional rotation to the polygon by the supplied degrees. */
	public void rotate (float degrees) {
		rotation += degrees;
		dirty = true;
	}

	/** Sets the amount of scaling to be applied to the polygon. */
	public void setScale (float scaleX, float scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		dirty = true;
	}

	/** Applies additional scaling to the polygon by the supplied amount. */
	public void scale (float amount) {
		this.scaleX += amount;
		this.scaleY += amount;
		dirty = true;
	}

	/** Sets the polygon's world vertices to be recalculated when calling {@link #getTransformedVertices() getTransformedVertices}. */
	public void dirty () {
		dirty = true;
	}

	/** Returns the area contained within the polygon. */
	public float area () {
		float area = 0;

		float[] vertices = getTransformedVertices();
		final int numFloats = vertices.length;

		int x1, y1, x2, y2;
		for (int i = 0; i < numFloats; i += 2) {
			x1 = i;
			y1 = i + 1;
			x2 = (i + 2) % numFloats;
			y2 = (i + 3) % numFloats;

			area += vertices[x1] * vertices[y2];
			area -= vertices[x2] * vertices[y1];
		}
		area *= 0.5f;
		return Math.abs(area);
	}

	/** Returns an axis-aligned bounding box of this polygon.
	 * 
	 * Note the returned Rectangle is cached in this polygon, and will be reused if this Polygon is changed.
	 * 
	 * @return this polygon's bounding box {@link Rectangle} */
	public Rectangle getBoundingRectangle () {
		float[] vertices = getTransformedVertices();

		float minX = vertices[0];
		float minY = vertices[1];
		float maxX = vertices[0];
		float maxY = vertices[1];

		final int numFloats = vertices.length;
		for (int i = 2; i < numFloats; i += 2) {
			minX = minX > vertices[i] ? vertices[i] : minX;
			minY = minY > vertices[i + 1] ? vertices[i + 1] : minY;
			maxX = maxX < vertices[i] ? vertices[i] : maxX;
			maxY = maxY < vertices[i + 1] ? vertices[i + 1] : maxY;
		}

		if (bounds == null) bounds = new Rectangle();
		bounds.x = minX;
		bounds.y = minY;
		bounds.width = maxX - minX;
		bounds.height = maxY - minY;

		return bounds;
	}
	
	private Rectangle getLocalBoundingRectangle(){
		float[] vertices = localVertices;

		float minX = vertices[0];
		float minY = vertices[1];
		float maxX = vertices[0];
		float maxY = vertices[1];

		final int numFloats = vertices.length;
		for (int i = 2; i < numFloats; i += 2) {
			minX = minX > vertices[i] ? vertices[i] : minX;
			minY = minY > vertices[i + 1] ? vertices[i + 1] : minY;
			maxX = maxX < vertices[i] ? vertices[i] : maxX;
			maxY = maxY < vertices[i + 1] ? vertices[i + 1] : maxY;
		}

		if (localBounds == null) localBounds = new Rectangle();
		localBounds.x = minX;
		localBounds.y = minY;
		localBounds.width = maxX - minX;
		localBounds.height = maxY - minY;

		return localBounds;
	}

	/** Returns whether an x, y pair is contained within the polygon. */
	public boolean contains (float x, float y) {
		final float[] vertices = getTransformedVertices();
		final int numFloats = vertices.length;
		int intersects = 0;

		for (int i = 0; i < numFloats; i += 2) {
			float x1 = vertices[i];
			float y1 = vertices[i + 1];
			float x2 = vertices[(i + 2) % numFloats];
			float y2 = vertices[(i + 3) % numFloats];
			if (((y1 <= y && y < y2) || (y2 <= y && y < y1)) && x < ((x2 - x1) / (y2 - y1) * (y - y1) + x1)) intersects++;
		}
		return (intersects & 1) == 1;
	}
	
	/** Returns the result of the intersection with this line segment */
/*	public LineSegmentIntersection intersect(LineSegment lineSegment){
		LineSegmentIntersection intersection = new LineSegmentIntersection();
		final float[] vertices = getTransformedVertices();
		final int numFloats = vertices.length;
		
		Vector2 point1 = null;
		Vector2 point2 = null;
		
	//	System.out.println("CHECKING polygon " + this + ", \nsegment: " + lineSegment);
		
		for(int i = 0; i < numFloats; i+= 2){
			Vector2 point = new Vector2();
			
			boolean result = Intersector.intersectSegments(
					new Vector2(vertices[i], vertices[i+1]), 
					new Vector2(vertices[(i+2) % numFloats], vertices[(i+3) % numFloats]), 
					lineSegment.getLeftPoint(),
					lineSegment.getRightPoint(), 
					point);
			
			if(result){
		//		System.out.println("found intersection");
				if(point1 == null){
					point1 = point;
				}
				else if(point2 == null){
					point2 = point;
				}
			}
		}
		
		//now we have the points of intersection.
		if(point1 == null && point2 == null) {
	//		System.out.print("no intersection, ");
			//no intersection. check if one of the points line in the polygon
			if(Intersector.isPointInPolygon(vertices, lineSegment.getFirstPoint())){
				//they are both inside the polygon, return the null intersection.
	//			System.out.println("full inside");
				return intersection;
			}
			else{
				//they are in a seperate area, pass in the whole line segment.
		//		System.out.println("full outside");
				intersection.segment1 = lineSegment;
				return intersection;
			}
		}
		else if(point2 == null) {
	//		System.out.println("one point inside");
			//one point is inside, the other is outside the polygon
			if(Intersector.isPointInPolygon(vertices, lineSegment.getFirstPoint())){
				intersection.segment2 = null;
				intersection.segment1 = new LineSegment(lineSegment.getSecondPoint(), point1);
				return intersection;
			}
			else{
				intersection.segment2 = null;
				intersection.segment1 = new LineSegment(lineSegment.getFirstPoint(), point1);
				return intersection;
			}
		}
		else{
	//		System.out.println("goes through kinda intersection");
			//line segment goes through the polygon, return two segments
			float dst1 = point1.dst(lineSegment.getFirstPoint());
			float dst2 = point1.dst(lineSegment.getSecondPoint());
			
			if(dst1 < dst2){
				//desired condition
				intersection.segment1 = new LineSegment(lineSegment.getFirstPoint(), point1);
				intersection.segment2 = new LineSegment(lineSegment.getSecondPoint(), point2);
			}
			else{
				intersection.segment1 = new LineSegment(lineSegment.getFirstPoint(), point2);
				intersection.segment2 = new LineSegment(lineSegment.getSecondPoint(), point1);
			}
			return intersection;
		}
	}
*/
	/** Returns the x-coordinate of the polygon's position within the world. */
	public float getX () {
		return x;
	}

	/** Returns the y-coordinate of the polygon's position within the world. */
	public float getY () {
		return y;
	}

	/** Returns the x-coordinate of the polygon's origin point. */
	public float getOriginX () {
		return originX;
	}

	/** Returns the y-coordinate of the polygon's origin point. */
	public float getOriginY () {
		return originY;
	}

	/** Returns the total rotation applied to the polygon. */
	public float getRotation () {
		return rotation;
	}

	/** Returns the total horizontal scaling applied to the polygon. */
	public float getScaleX () {
		return scaleX;
	}

	/** Returns the total vertical scaling applied to the polygon. */
	public float getScaleY () {
		return scaleY;
	}
	
	public class LineSegmentIntersection{
		LineSegment segment1;
		LineSegment segment2;
	
		public LineSegmentIntersection() {
			segment1 = null;
			segment2 = null;
		}
		
		public LineSegment getLineSegment1(){
			return segment1;
		}
		
		public LineSegment getLineSegment2(){
			return segment2;
		}
		
		public LineSegment getReversed(){
			return new LineSegment(segment1.getRightPoint(), segment2.getLeftPoint());
		}
		
		public String toString() {
			return "line segment 1: " + segment1 + ", segment 2: " + segment2;
		}

		public LineSegmentIntersection getProjection(LineSegmentIntersection lowerIntersection) {
			if(this.segment1 == null || lowerIntersection.segment1 == null)
				return null;
			LineSegmentIntersection retVal = new LineSegmentIntersection();
			
			LineSegment[] segments = new LineSegment[4];
			segments[0] = Intersector.projectOnLineSegment(segment1, lowerIntersection.segment1);
			segments[1] = Intersector.projectOnLineSegment(segment2, lowerIntersection.segment1);
			segments[2] = Intersector.projectOnLineSegment(segment1, lowerIntersection.segment2);
			segments[3] = Intersector.projectOnLineSegment(segment2, lowerIntersection.segment2);
			
			boolean isFirstUsed = false;
			for(int i = 0; i < 4; i++) {
				if(segments[i] != null && segments[i].getLength() > GlobalAppSettings.ignoreValuesBelow) {
					if(isFirstUsed == false) {
						retVal.segment1 = segments[i];
						isFirstUsed = true;
					}
					else {
						retVal.segment2 = segments[i];
					}
				}
			}
			
			return retVal;
		}
	}

	public void setPositionX(float floatValue) {
		this.x = floatValue;
		dirty = true;
	}
	
	public void setPositionY(float floatValue) {
		this.y = floatValue;
		dirty = true;
	}

	public Vector2 getEdgeMidpoint(int i) {
		return getEdge(i).getMidpoint();
	}
	
	@Override
	public String toString(){
		String retVal = "Polygon, edges: " + getEdgeCount() + "\nLocal Vertices:\t";
		for(int i = 0; i < getEdgeCount(); i++) {
			retVal += "[" + localVertices[i*2] + ":" + localVertices[(i*2)+1] +"] ";
		}
		retVal += "\nWorld Vertices:\t";
		if(worldVertices != null) { 
			for(int i = 0; i < getEdgeCount(); i++) {
				retVal += "[" + worldVertices[i*2] + ":" + worldVertices[(i*2)+1] +"] ";
			}
		}
		return retVal;
	}
	
	/**
	 * Returns a part of the line segment that is inside this polygon.
	 * @param input
	 * @return
	 */
	public LineSegment getPortionInside(LineSegment input) {
		LineSegment retVal = null;
		
		final float[] vertices = getTransformedVertices();
		final int numFloats = vertices.length;
		
		Vector2 point1 = null;
		Vector2 point2 = null;
	
		for(int i = 0; i < numFloats; i+= 2){
			Vector2 point = new Vector2();
			
			boolean result = Intersector.intersectSegments(
					new Vector2(vertices[i], vertices[i+1]), 
					new Vector2(vertices[(i+2) % numFloats], vertices[(i+3) % numFloats]), 
					input.getLeftPoint(),
					input.getRightPoint(),
					point);
			
			if(result){
				if(point1 == null){
					point1 = point;
					//if this point is very close to the initial point, disregard.
					if( point1.dst(input.getFirstPoint()) < GlobalAppSettings.ignoreValuesBelowMedium ||
						point1.dst(input.getSecondPoint()) < GlobalAppSettings.ignoreValuesBelowMedium) {	
						point1 = null;
					}
					
				}
				else if(point2 == null){
					point2 = point;
				}
			}
		}
		if(point1 == null && point2 == null){
			if(this.contains(input.getFirstPoint()) && this.contains(input.getSecondPoint())){
				retVal = new LineSegment(input.getFirstPoint(), input.getSecondPoint());
			}
		}
		else if(point1 != null && point2 == null) {
			if(this.contains(input.getFirstPoint())) {
				retVal = new LineSegment(input.getFirstPoint(), point1);
				retVal.fixCoordinates();
			}
			else if(this.contains(input.getSecondPoint())) {
				retVal = new LineSegment(input.getSecondPoint(), point1);
				retVal.fixCoordinates();
			}
			else{
				throw new RuntimeException("biseyler yanlis gitti");
			}
		}
		else if(point1 != null && point2 != null) {
			retVal = new LineSegment(point1, point2);
			retVal.fixCoordinates();
		}
				
		return retVal;
	}

	private boolean contains(Vector2 firstPoint) {
		return contains(firstPoint.x, firstPoint.y);
	}

	public LineSegmentIntersection intersect(LineSegment lineSegment) {
		LineSegmentIntersection intersection = new LineSegmentIntersection();
		final float[] vertices = getTransformedVertices();
		final int numFloats = vertices.length;
		
		Vector2 point1 = null;
		Vector2 point2 = null;
	
		for(int i = 0; i < numFloats; i+= 2){
			Vector2 point = new Vector2();
			
			boolean result = Intersector.intersectSegments(
					new Vector2(vertices[i], vertices[i+1]), 
					new Vector2(vertices[(i+2) % numFloats], vertices[(i+3) % numFloats]), 
					lineSegment.getLeftPoint(),
					lineSegment.getRightPoint(),
					point);
			
			if(result){
				if(point1 == null){
					point1 = point;
				}
				else if(point2 == null){
					point2 = point;
				}
			}
		}
		
		//now we have the points of intersection.
		if(point1 == null && point2 == null) {
			//no intersection. check if one of the points line in the polygon
			if(Intersector.isPointInPolygon(vertices, lineSegment.getFirstPoint())){
				//they are both inside the polygon, return the null intersection.
				return intersection;
			}
			else{
				//they are in a seperate area, pass in the whole line segment.
				intersection.segment1 = lineSegment;
				return intersection;
			}
		}
		else if(point2 == null) {
			//one point is inside, the other is outside the polygon
			if(Intersector.isPointInPolygon(vertices, lineSegment.getFirstPoint())){
				intersection.segment2 = null;
				intersection.segment1 = new LineSegment(point1, lineSegment.getSecondPoint());
				return intersection;
			}
			else{
				intersection.segment2 = null;
				intersection.segment1 = new LineSegment(lineSegment.getFirstPoint(), point1);
				return intersection;
			}
		}
		else{
			//line segment goes through the polygon, return two segments
			float dst1 = point1.dst(lineSegment.getFirstPoint());
			float dst2 = point2.dst(lineSegment.getFirstPoint());
			
			if(dst1 < dst2){
				//point 1 is closer, desired condition
				intersection.segment1 = new LineSegment(lineSegment.getFirstPoint(), point1);
				intersection.segment2 = new LineSegment(point2, lineSegment.getSecondPoint());
			}
			else{
				intersection.segment1 = new LineSegment(lineSegment.getFirstPoint(), point2);
				intersection.segment2 = new LineSegment(point1, lineSegment.getSecondPoint());
			}
			return intersection;
		}
	}

	public void draw(GraphicsContext context) {
		context.strokePolygon(getTransformedXVertices(), getTransformedYVertices(), getVertices().length / 2);
	}

	/**
	 * Just like intersect with line segment, however this intersects a rectangular area with this polygon
	 * @param segment
	 * @param height
	 * @return
	 */
	public LineSegmentIntersection intersectWithHeight(LineSegment segment, float height) {
		LineSegment lowerSegment = segment.buildLowerLineSegment(height);
		LineSegmentIntersection intersection = intersect(segment);
		LineSegmentIntersection lowerIntersection = intersect(lowerSegment);
		
		LineSegmentIntersection retVal = intersection.getProjection(lowerIntersection);
				
		return retVal;
	}

	public Polygon copy() {
		float[] vertices2 = this.localVertices.clone();
		Polygon retVal = new Polygon(vertices2);
		retVal.x = x;
		retVal.y = y;
		retVal.originX = originX;
		retVal.originY = originY;
		retVal.rotation = rotation;
		retVal.scaleX = scaleX;
		retVal.scaleY = scaleY;
		retVal.indexList = this.indexList.clone();
		
		return retVal;
	}

}