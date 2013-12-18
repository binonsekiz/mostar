package gui.helper;

import geometry.GeometryHelper;
import geometry.libgdxmath.Intersector;
import geometry.libgdxmath.LineSegment;
import geometry.libgdxmath.Polygon;
import geometry.libgdxmath.Rectangle;
import geometry.libgdxmath.Polygon.LineSegmentIntersection;
import geometry.libgdxmath.Vector3;
import geometry.libgdxmath.collision.Ray;
import geometry.libgdxmath.Vector2;
import gui.ShapedPane;
import gui.columnview.ColumnView;
import gui.columnview.LineOnCanvas;
import gui.columnview.ParagraphOnCanvas;

import java.util.ArrayList;
import java.util.Collection;

import javafx.geometry.Point2D;
import document.PageInsets;
import document.TextStyle;

public class LayoutMachine {

	private ArrayList<ShapedPane> shapes;
	private PageInsets insets;
	private Polygon parentShape;
	
	private ArrayList<LineOnCanvas> lines;
	
	public LayoutMachine(){
		shapes = new ArrayList<ShapedPane>();
		lines = new ArrayList<LineOnCanvas>();
	}
	
	public ParagraphOnCanvas getParagraphSpace(ColumnView requester, Rectangle allowedSpace, TextStyle stlye){
		ParagraphOnCanvas paragraph = new ParagraphOnCanvas(requester, allowedSpace);
		
		//TODO: take textstyle into account
		for(int y = (int) allowedSpace.y; y <= allowedSpace.height; y+= 10){
			LineOnCanvas newLine = new LineOnCanvas(requester, paragraph);
			newLine.initialPositionSetup(allowedSpace.x, y, allowedSpace.width, allowedSpace.height, 0);
			ArrayList<LineOnCanvas> trimmedLine = trimLine(newLine);
			paragraph.insertLines(trimmedLine);
		}
		
		return paragraph;
	}
	
	private ArrayList<LineOnCanvas> trimLine(LineOnCanvas newLine) {
		//TODO: trim the lines properly
		ArrayList<LineOnCanvas> lines = new ArrayList<LineOnCanvas>();
		lines.add(newLine);

	//	for()
		
		return lines;
	}

	public LineOnCanvas getLine(float x, float y, float height, float angle){
		//see if the start position is available
		if(!parentShape.contains(x, y)){
			System.out.println("out at 1");
			return null;
		}
		for(ShapedPane shape:shapes){
			if(shape.getShape().contains(x, y)){
				System.out.println("out at 2");
				System.out.println(shape);
				return null;
			}
		}
		
		//shoot two lines to get the max possible width
		Ray topRay = new Ray(new Vector3(x,y,0), GeometryHelper.getVectorFromAngle(angle));
		Vector3 intersection = new Vector3();
		for(int i = 0; i < shapes.size(); i++){
	//		Intersector.intersectRayBounds(topRay, shapes.get(i).getShape(), intersection);
			boolean value = Intersector.intersectRayTriangles(topRay, shapes.get(i).getShape().getTransformedVertices(), shapes.get(i).getShape().getIndexList(), 2, intersection);
			System.out.println("Intersection "+ value +"  at: " + intersection);
		}
		
		
		return null;
	}
	
	public void addSingleElement(ShapedPane shape){
		shapes.add(shape);
	}
	
	public void addElements(Collection<? extends ShapedPane> collection){
		shapes.addAll(collection);
	}
	
	public void clear(){
		shapes.clear();
	}

	public void setParentShape(Polygon shape) {
		this.parentShape = shape;
	}
	
	public void setPageInsets(PageInsets insets) {
		this.insets = insets;
	}

/*	public Collection<? extends LineOnCanvas> getDebugLines(ColumnView parent) {
		ArrayList<LineOnCanvas> lines = new ArrayList<LineOnCanvas>();
		PageInsets insets = parent.getColumn().getInsets();
		for(int y = (int) insets.getMinY(); y < insets.getMinY() + insets.getActualHeight(); y+=10){
			LineOnCanvas line = new LineOnCanvas(parent);
			line.setLayout(insets.getMinX(),y);
			line.setPreferredWidth(insets.getActualWidth());
			lines.add(line);
		}
		
		return lines;
	}*/
	
	/*	@Deprecated
	public ArrayList<LineSegment> requestLine(float y){
		return requestLine(y, 0);
	}
	
	@Deprecated
	public ArrayList<LineSegment> requestLine(float y, float angle){
		//TODO: add angle into calculations
		LineSegment segment = new LineSegment(new Vector2(insets.getMinX(), y), new Vector2(insets.getPageWidth(), y));
		return getLineSegmentsDeprecated(segment);
	}
*/
	
//	@Deprecated
	/**
	 * Divides the given line segment into smaller segments, taking the measurement, insets and elements into account
	 * @param proposedLine
	 * @return
	 */
/*	public ArrayList<LineSegment> getLineSegmentsDeprecated(LineSegment proposedLine){
		ArrayList<LineSegment> segments = new ArrayList<LineSegment>();
		segments.add(proposedLine);
		
		System.out.println("Getting line segments");
		for(int i = 0; i < shapes.size(); i++){
			System.out.println("+first loop");
			//for every shape, add or remove segments to the list.
			for(int j = 0; j < segments.size(); j++){
				System.out.println("++second loop");
				//1. calculate intersection points
				LineSegmentIntersection intersection = shapes.get(i).getShape().intersect(segments.get(j));
				if(intersection.getLineSegment1() != null){
					if(segments.get(j) != intersection.getLineSegment1()){
						segments.remove(j);
					}
					segments.add(j, intersection.getLineSegment1());
				}
				if(intersection.getLineSegment2() != null){
					segments.add(j+1, intersection.getLineSegment2());
				}
			}
		}
		
		for(int i = 0; i < shapes.size(); i++){
			System.out.println("segments " + i + ": " + segments.get(i));
		}
		System.out.println("End of segments");
		
		return segments;
	}*/

}
