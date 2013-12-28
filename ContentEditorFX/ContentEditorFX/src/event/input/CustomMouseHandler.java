package event.input;

import javafx.scene.input.MouseEvent;

public interface CustomMouseHandler {

	public void onMouseClicked(MouseEvent event);
	
	public void onMouseEntered(MouseEvent event);
	
	public void onMouseExited(MouseEvent event);
	
	public void onMouseMoved(MouseEvent event);
	
	public void onMousePressed(MouseEvent event);
	
	public void onMouseDragEntered(MouseEvent event);
	
	public void onMouseDragExited(MouseEvent event);
	
	public void onMouseDragOver(MouseEvent event);
	
	public void onMouseDragReleased(MouseEvent event);
	
	public void onMouseDragged(MouseEvent event);
	
	public void onMouseReleased(MouseEvent event);
	
	enum MouseEventType {
		MouseClicked,
		MouseEntered,
		MouseExited,
		MouseMoved,
		MousePressed,
		MouseDragEntered,
		MouseDragExited,
		MouseDragOver,
		MouseDragReleased,
		MouseDragged,
		MouseReleased,
	}
}
