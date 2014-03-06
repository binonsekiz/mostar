package event;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import event.ShapeDrawFacade.ShapeDrawingMode;
import geometry.libgdxmath.Vector2;
import gui.columnview.ColumnView;

public class ShapeDrawFacade {

	private ShapeDrawingMode mode;
	private ArrayList<Vector2> currentShapePoints;
	private Vector2 floatingPoint;
	
	public ShapeDrawFacade() {
		mode = ShapeDrawingMode.Off;
		currentShapePoints = new ArrayList<Vector2>();
		floatingPoint = new Vector2();
	}
	
	public void changeShapeDrawingMode(ShapeDrawingMode mode) {
		if(mode != this.mode) {
			this.mode = mode;
			currentShapePoints.clear();
		}
	}
	
	public enum ShapeDrawingMode{
		Off, PolygonDrawing, PolylineDrawing
	}

	public ShapeDrawingMode getDrawingMode() {
		return mode;
	}

	public void onMouseMoved(MouseEvent event, ColumnView source) {
		System.out.println("draw facade mouse move");
		floatingPoint.x = (float) event.getX();
		floatingPoint.y = (float) event.getY();
	}

	public void onMouseClick(MouseEvent event, ColumnView source) {
		System.out.println("draw facade mouse click");
		Vector2 newVector = new Vector2((float) event.getX(), (float) event.getY());
		currentShapePoints.add(newVector);
	}

	public void onMouseDragged(MouseEvent event, ColumnView source) {
		System.out.println("draw facade mouse drag");
		floatingPoint.x = (float) event.getX();
		floatingPoint.y = (float) event.getY();
	}
	
	public void paintCurrentShape(GraphicsContext context) {
		context.save();
		context.setStroke(Color.MEDIUMVIOLETRED);
		context.setLineWidth(0.5f);
		
		int i = 0;
		for(; i < currentShapePoints.size() - 1; i++) {
			context.strokeLine(currentShapePoints.get(i).x, currentShapePoints.get(i).y, currentShapePoints.get(i+1).x, currentShapePoints.get(i+1).y);
		}
		if(i >= 1) {
			context.strokeLine(currentShapePoints.get(i).x, currentShapePoints.get(i).y, floatingPoint.x, floatingPoint.y);
		}
		
		context.restore();
	}
	
}
