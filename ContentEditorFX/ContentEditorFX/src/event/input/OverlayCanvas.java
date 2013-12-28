package event.input;

import gui.columnview.DocumentView;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

public class OverlayCanvas extends Canvas{

	private DocumentView parent;
	
	public OverlayCanvas(DocumentView parent) {
		this.parent = parent;
		initEvents();
	}

	private void initEvents() {
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				parent.onMouseClicked(arg0);
			}
		});
		
		this.setOnMouseDragEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				parent.onMouseDragEntered(arg0);
			}
		});
		
		this.setOnMouseDragExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {	
				parent.onMouseDragExited(arg0);
			}
		});
		
		this.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {	
				parent.onMouseDragged(arg0);
			}
		});
		
		this.setOnMouseDragOver(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {	
				parent.onMouseDragOver(arg0);
			}
		});
		
		this.setOnMouseDragReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {	
				parent.onMouseDragReleased(arg0);
			}
		});

		this.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				parent.onMouseEntered(arg0);
			}
		});

		this.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				parent.onMouseExited(arg0);
			}
		});
		
		this.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {	
				parent.onMouseMoved(arg0);
			}
		});
		
		this.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {	
				parent.onMousePressed(arg0);
			}
		});
		
		this.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {	
				parent.onMouseReleased(arg0);
			}
		});
	}
	
}
