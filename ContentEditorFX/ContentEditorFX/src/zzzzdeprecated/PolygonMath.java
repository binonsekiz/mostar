package zzzzdeprecated;

import java.util.ArrayList;
import java.util.Collections;

import javafx.scene.shape.Polyline;

public class PolygonMath {
	
	private Polyline poly;
	
	private ArrayList<Point> points;
	private boolean isLeft;
	
	public PolygonMath(){
		poly = new Polyline();
		points = new ArrayList<Point>();
		isLeft = true;
	}

	public void setLeft(boolean value){
		this.isLeft = value;
	}
	
	public Polyline getPolyline(){
		return poly;
	}
	
	public void addPoint(float x, float y){
		points.add(new Point(x,y));
		Collections.sort(points);
	}
	
	public float getX(float y){
		//find the interior where y lies
		Point tempPoint = new Point(0,y);
		int index = Collections.binarySearch(points, tempPoint);
		if(index > 0){
			//key found, return
			return points.get(index).x;
		}
		else{
			//calculate x
			index *= -1;
			int index2 = index - 1;
		
			float retVal;
			float val = points.get(index).x;
			float val2 = points.get(index2).x;
			
			float percentage = (y - points.get(index2).y)/ (points.get(index).y - points.get(index2).y) ;
			
			retVal = percentage * (val - val2) + val2;
			
			return retVal;
		}
	}
	
	class Point implements Comparable<Point>{
		float x, y;
		public Point(float x, float y){
			this.x = x;
			this.y = y;
		}
		
		@Override
		public int compareTo(Point o) {
			if(this.y > o.y)
				return 1;
			else if(this.y < o.y)
				return -1;
			return 0;
		}
	}
	
}
