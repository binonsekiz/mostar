package event.input;

import javafx.scene.input.KeyEvent;

public class KeyboardManager {
	
	public static KeyboardManager instance;
	
	public KeyboardManager(){
		instance = this;
	}

	public void keyPressed(KeyEvent event) {
//		System.out.println("Global key pressed: " + event.getText());
	}

	public void keyReleased(KeyEvent event) {
//		System.out.println("Global key released: " + event.getText());
	}

	public void keyTyped(KeyEvent event) {
//		System.out.println("Global key typed: " + event.getText());
	}

}
