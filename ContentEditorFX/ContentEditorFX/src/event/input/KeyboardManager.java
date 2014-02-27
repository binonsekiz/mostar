package event.input;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import control.Caret;
import control.TextModifyFacade;

public class KeyboardManager implements EventHandler<KeyEvent>{
	
	public static KeyboardManager instance;
	private TextModifyFacade textFacade;
	private Caret caret;
	private boolean writingMode;
	
	public KeyboardManager(){
		instance = this;
	}

	public void keyPressed(KeyEvent event) {
//		System.out.println("Global key pressed (TEXT): \t" + event.getText());
//		System.out.println("Global key pressed (CODE): \t" + event.getCode());
//		System.out.println("Global key pressed (CHAR): \t" + event.getCharacter());
		System.out.println("\n\n\n\n0000000\nKeyPressed\n0000000\n\n\n");
		if(writingMode) {
			KeyCode code = event.getCode();
			if(code == KeyCode.BACK_SPACE){
				textFacade.backspace();
			}
			else if(code == KeyCode.DELETE){
				textFacade.delete();
			}
			else if(code == KeyCode.LEFT){
				caret.leftKey(false, false);
			}
			else if(code == KeyCode.RIGHT){
				caret.rightKey(false, false);
			}
		//	else if(code.isLetterKey()){
				textFacade.insertString(event.getText());
	//		}
	//		else{
		//		System.out.println("UNDEFINED KEY PRESSED");
		//	}
		}
	}

	public void keyReleased(KeyEvent event) {
//		System.out.println("Global key released (TEXT): " + event.getText());
//		System.out.println("Global key released (CODE): " + event.getCode());
//		System.out.println("Global key released (CHAR): \t" + event.getCharacter());
		
		if(event.getCode() == KeyCode.SPACE) {
			textFacade.insertString(" ");
		}
		else if(event.getCode() == KeyCode.TAB) {
			textFacade.insertString("\t");
		}
	}

	public void keyTyped(KeyEvent event) {
//		System.out.println("Global key typed (TEXT): " + event.getText());
//		System.out.println("Global key typed (CODE): " + event.getCode());
//		System.out.println("Global key typed (CHAR): \t" + event.getCharacter());
	/*	KeyCode code = event.getCode();
		
		if(code == KeyCode.SPACE) {
			System.out.println("SPACE PRESSED");
			textFacade.insertSingleChar(" ");
		}*/
	}
	
	public void setTextFacade(TextModifyFacade facade) {
		this.textFacade = facade;
		this.caret = facade.getCaret();
	}

	public void writingModeFocus() {
		writingMode = true;
	}

	@Override
	public void handle(KeyEvent event) {
		if(event.getEventType() == KeyEvent.KEY_PRESSED) {
			keyPressed(event);
		}
		else if(event.getEventType() == KeyEvent.KEY_RELEASED) {
			keyReleased(event);
		}
		else if(event.getEventType() == KeyEvent.KEY_TYPED) {
			keyTyped(event);
		}
	}

}
