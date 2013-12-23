package gui.columnview;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import event.modification.ModificationType;
import gui.ShapedPane;

public interface CanvasOwner {
	public void notifyRepaintNeeded();
	
	public void notifyModificationStart(ModificationType type, ShapedPane pane, MouseEvent event);
	public void notifyMouseMovement(ShapedPane pane, MouseEvent event);
	public void notifyModificationEnd(ShapedPane pane, MouseEvent event);

	public GraphicsContext getGraphicsContext();
}
