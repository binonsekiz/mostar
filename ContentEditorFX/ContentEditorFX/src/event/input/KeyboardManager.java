package event.input;

import control.Caret;
import control.TextModifyFacade;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;

public class KeyboardManager implements EventHandler<KeyEvent>{
	
	public static KeyboardManager instance;
	private TextModifyFacade textFacade;
	private Caret caret;
	private boolean writingMode;
	
	public KeyboardManager(){
		instance = this;
	}

	public void keyPressed(KeyEvent event) {
		System.out.println("Global key pressed (TEXT): \t" + event.getText());
		System.out.println("Global key pressed (CODE): \t" + event.getCode());
		System.out.println("Global key pressed (CHAR): \t" + event.getCharacter());
		if(writingMode) {
			KeyCode code = event.getCode();
			if(code == KeyCode.BACK_SPACE){
				textFacade.backspace();
			}
			else if(code == KeyCode.DELETE){
				textFacade.delete();
			}
			else if(code == KeyCode.LEFT){
				caret.leftKey(event.isShiftDown(), event.isControlDown());
			}
			else if(code == KeyCode.RIGHT){
				caret.rightKey(event.isShiftDown(), event.isControlDown());
			}
		//	else if(code.isLetterKey()){
				textFacade.insertSingleChar(event.getText());
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
			textFacade.insertSingleChar(" ");
		}
		else if(event.getCode() == KeyCode.TAB) {
			textFacade.insertSingleChar("\t");
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
