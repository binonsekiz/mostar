package event.input;

import gui.columnview.DocumentView;
import javafx.scene.canvas.Canvas;

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
