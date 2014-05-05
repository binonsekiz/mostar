package event;

import geometry.libgdxmath.Polygon;
import geometry.libgdxmath.Vector2;
import gui.columnview.ColumnView;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import settings.GlobalAppSettings;
import document.project.ProjectRepository;

public class ShapeDrawFacade {

	private ShapeDrawingMode mode;
	private ArrayList<Vector2> currentShapePoints;
	private Vector2 floatingPoint;
	
	private boolean isWaitingForCaller;
	private ColumnView caller;
	
	public ShapeDrawFacade() {
		mode = ShapeDrawingMode.Off;
		isWaitingForCaller = false;
		currentShapePoints = new ArrayList<Vector2>();
		floatingPoint = new Vector2();
	}
	
	public void changeShapeDrawingMode(ShapeDrawingMode mode) {
		if(mode != this.mode) {
			if(this.mode == ShapeDrawingMode.Off) {
				//the next event from a columnview is the one we are going to send the drawn polygon to.
				isWaitingForCaller = true;
			}
			this.mode = mode;
			currentShapePoints.clear();
		}
	}
	
	public boolean isCallerColumnView(ColumnView caller2) {
		return caller == caller2;
	}
	
	public enum ShapeDrawingMode{
		Off, PolygonDrawing, PolylineDrawing, TextboxDrawing
	}

	public ShapeDrawingMode getDrawingMode() {
		return mode;
	}

	private void mouseClicked(MouseEvent event, ColumnView source) {
		if(isWaitingForCaller) {
			caller = source;
			isWaitingForCaller = false;
		}
		if(source != caller) {
			return;
		}
		Vector2 newVector = new Vector2((float) event.getX(), (float) event.getY());
		if(isCloseToInitialPoint()) {
			if(mode == ShapeDrawingMode.PolygonDrawing) {
				finalizePolygon();
			}
			else if(mode == ShapeDrawingMode.TextboxDrawing) {
				finalizeTextBox();
			}
		}
		else{
			currentShapePoints.add(newVector);
		}
	}

	private void finalizeTextBox() {
		float[] vertices = new float[currentShapePoints.size() * 2];
		for(int i = 0; i < currentShapePoints.size(); i++) {
			vertices[2 * i] = currentShapePoints.get(i).x;
			vertices[2 * i + 1] = currentShapePoints.get(i).y;
		}
		Polygon finalPolygon = new Polygon(vertices);
		
		//ParagraphSet pSet = new ParagraphSet(finalPolygon);
		//caller.insertParagraphSet(pSet);
		
		
		ProjectRepository.getActiveProjectEnvironment().addBlankParagraphSet(finalPolygon, caller);
		
		mode = ShapeDrawingMode.Off;
		caller.refresh();
		caller = null;
		isWaitingForCaller = false;
	}

	private void finalizePolygon() {
		float[] vertices = new float[currentShapePoints.size() * 2];
		for(int i = 0; i < currentShapePoints.size(); i++) {
			vertices[2 * i] = currentShapePoints.get(i).x;
			vertices[2 * i + 1] = currentShapePoints.get(i).y;
		}
		Polygon finalPolygon = new Polygon(vertices);
		caller.getColumn().addShape(finalPolygon);
		mode = ShapeDrawingMode.Off;
		caller.refresh();
		caller = null;
		isWaitingForCaller = false;
	}

	private void mouseMovedOrDragged(MouseEvent event, ColumnView source) {
		if(isWaitingForCaller) {
			caller = source;
			isWaitingForCaller = false;
		}
		if(source != caller) {
			return;
		}
		floatingPoint.x = (float) event.getX();
		floatingPoint.y = (float) event.getY();
	}
	
	private boolean isCloseToInitialPoint() {
		if(currentShapePoints.size() <= 2)
			return false;
		if(floatingPoint.dst(currentShapePoints.get(0)) < GlobalAppSettings.pointerJumpSize)
			return true;
		return false;
	}

	public void paintCurrentShape(GraphicsContext context) {
		context.save();
		context.setStroke(Color.BLUE);
		context.setLineWidth(0.5f);
		
		int i = 0;
		for(; i < currentShapePoints.size() - 1; i++) {
			context.strokeLine(currentShapePoints.get(i).x, currentShapePoints.get(i).y, currentShapePoints.get(i+1).x, currentShapePoints.get(i+1).y);
		}
		if(currentShapePoints.size() > 0 && i >= 0) {
			context.setStroke(Color.DARKGREEN);
			context.strokeLine(currentShapePoints.get(i).x, currentShapePoints.get(i).y, floatingPoint.x, floatingPoint.y);
		}
		
		if(isCloseToInitialPoint()) {
			//draw the finish box
			context.setLineWidth(1.5f);
			context.setStroke(Color.ORANGERED);
			Vector2 p1 = currentShapePoints.get(0);
			context.strokeRect(p1.x-3, p1.y-3, 6, 6);
		}
		
		context.restore();
	}

	public void onMouseEvent(MouseEvent event, ColumnView source) {
		if(event.getEventType() == MouseEvent.MOUSE_MOVED || event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
			mouseMovedOrDragged(event, source);
		}
		else if(event.getEventType() == MouseEvent.MOUSE_PRESSED) {
			mouseClicked(event, source);
		}
	}
}
