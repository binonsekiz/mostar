package event.input;

import control.Caret;
import control.TextModifyFacade;
import javafx.scene.input.KeyEvent;

public class KeyboardManager {
	
	public static KeyboardManager instance;
	private TextModifyFacade textFacade;
	private Caret caret;
	
	public KeyboardManager(){
		instance = this;
	}

	public void keyPressed(KeyEvent event) {
		System.out.println("Global key pressed: " + event.getText());
		textFacade.insertSingleChar(event.getText());
	}

	public void keyReleased(KeyEvent event) {
		System.out.println("Global key released: " + event.getText());
	}

	public void keyTyped(KeyEvent event) {
		System.out.println("Global key typed: " + event.getText());
	}
	
	public void setTextFacade(TextModifyFacade facade) {
		this.textFacade = facade;
		this.caret = facade.getCaret();
	}

	public void writingModeFocus() {
		System.out.println("Writing focus...");
	}

}
