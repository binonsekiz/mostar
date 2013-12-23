package event.modification;

import geometry.libgdxmath.Vector2;
import gui.ShapedPane;
import javafx.scene.input.MouseEvent;
import settings.GlobalAppSettings;

public class TranslateModification extends ModificationInstance{
	
	private Vector2 mouseStartPoint;

	public TranslateModification(ShapedPane pane) {
		super(pane);
		
	}

	@Override
	public void modificationStart(MouseEvent event) {
		mouseStartPoint = new Vector2();
		mouseStartPoint.x = (float) event.getSceneX();
		mouseStartPoint.y = (float) event.getSceneY();
	}

	@Override
	public void mouseMovement(MouseEvent event) {
		float xDiff = (float) (event.getSceneX() - mouseStartPoint.x);
		float yDiff = (float) (event.getSceneY() - mouseStartPoint.y);
		mouseStartPoint.x = (float) event.getSceneX();
		mouseStartPoint.y = (float) event.getSceneY();
		
		if(Math.abs(xDiff) < GlobalAppSettings.ignoreValuesBelow) xDiff = 0;
		if(Math.abs(yDiff) < GlobalAppSettings.ignoreValuesBelow) yDiff = 0;
		
		pane.translateWithShape(xDiff, yDiff);
	}

	@Override
	public void modificationEnd(MouseEvent event) {
		// no need to do anything special
		
	}

}
