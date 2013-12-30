package event.input;

import gui.columnview.DocumentView;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

public class OverlayCanvas extends Canvas{

	private DocumentView parent;
	
	public OverlayCanvas(DocumentView parent) {
		this.parent = parent;
		this.setMouseTransparent(true);
		initEvents();
	}

	private void initEvents() {
	
	}
	
}
