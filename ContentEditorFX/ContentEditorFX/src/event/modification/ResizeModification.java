package event.modification;

import geometry.libgdxmath.Polygon;
import geometry.libgdxmath.Vector2;
import gui.ShapedPane;
import javafx.scene.input.MouseEvent;
import settings.GlobalAppSettings;

public class ResizeModification extends ModificationInstance{

	private Vector2 mouseStartPoint;
	private int edgeIndex;
	private Polygon backupShape;
	private Polygon newShape;
	private Vector2 normal;
	private Vector2 diff;
	private Vector2 lastMousePosition;
	
	public ResizeModification(ShapedPane pane, int edgeIndex) {
		super(pane);
		this.edgeIndex = edgeIndex;
		newShape = pane.getPaneShape();
		backupShape = new Polygon(newShape.getVertices().clone());
		normal = backupShape.getEdgeNormal(edgeIndex);
		normal.nor();
		diff = new Vector2();
	}

	@Override
	public void modificationStart(MouseEvent event) {
		mouseStartPoint = new Vector2();
		mouseStartPoint.x = (float) event.getSceneX();
		mouseStartPoint.y = (float) event.getSceneY();
		lastMousePosition = new Vector2(mouseStartPoint);
	}

	@Override
	public void mouseMovement(MouseEvent event) {
		//if this has the same coordinates as last event, break
		if( Math.abs(lastMousePosition.x - event.getSceneX()) < GlobalAppSettings.ignoreValuesBelow && 
			Math.abs(lastMousePosition.y - event.getSceneY()) < GlobalAppSettings.ignoreValuesBelow) 
			return;
		
		diff.x = (float) (event.getSceneX() - mouseStartPoint.x);
		diff.y = (float) (event.getSceneY() - mouseStartPoint.y);
		
		float value = diff.dot(normal);
		diff.set(normal);
		diff.scl(value);
		
		//trim out the imperfections in diff
		if(Math.abs(diff.x) < GlobalAppSettings.ignoreValuesBelow) diff.x = 0;
		if(Math.abs(diff.y) < GlobalAppSettings.ignoreValuesBelow) diff.y = 0;

		newShape.setVertices(backupShape.getVertices().clone());
		newShape.moveEdge(edgeIndex, diff);
		pane.setResizeVector(diff);

		pane.setShape(newShape);
		lastMousePosition.set((float)event.getX(), (float)event.getY());
	}

	@Override
	public void modificationEnd(MouseEvent event) {
		normal = null;
		diff = null;
		pane.setShape(newShape);
		pane.setResizeVector(new Vector2());
	}

}
