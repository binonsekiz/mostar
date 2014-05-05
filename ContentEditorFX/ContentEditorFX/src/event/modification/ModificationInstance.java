package event.modification;

import gui.ShapedPane;
import javafx.scene.input.MouseEvent;

/**
 * A modification (translate, rotate etc) that is linked to a single shaped pane
 * @author sahin
 *
 */
public abstract class ModificationInstance{
	@SuppressWarnings("unused")
	private ModificationType type;
	protected ShapedPane pane;
	
	public ModificationInstance(ShapedPane pane){
		this.pane = pane;
	}

	public abstract void modificationStart(MouseEvent event);

	public abstract void mouseMovement(MouseEvent event);

	public abstract void modificationEnd(MouseEvent event);
}
