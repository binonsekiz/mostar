package gui.columnview;

import event.modification.ModificationType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import geometry.libgdxmath.Vector2;
import gui.ShapedPane;

public interface CanvasOwner {
	public void notifyRepaintNeeded();
	
	public void notifyModificationStart(ModificationType type, ShapedPane pane, MouseEvent event);
	public void notifyMouseMovement(ShapedPane pane, MouseEvent event);
	public void notifyModificationEnd(ShapedPane pane, MouseEvent event);

	public GraphicsContext getGraphicsContext();
}
