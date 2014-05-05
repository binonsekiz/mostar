//package zzzzdeprecated;
//
//import javafx.event.EventHandler;
//import javafx.scene.control.TextArea;
//import javafx.scene.input.KeyCode;
//import javafx.scene.input.KeyEvent;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.shape.Rectangle;
//
///**
// * Although this is technically a text area, it only represents a single line of text.
// * Text field is not used since it automatically scrolls while typing.
// * DocModifyPane has multiple of these underneath each other.
// * Previous and next text areas are used for overflow.
// * @author sahin
// *
// */
//public class DocTextDeprecated extends TextArea{
//
//	private String text;
//	private int caretStart, caretEnd;
//	
//	private Rectangle boundingRect;
//	
//	public DocTextDeprecated(String text){
//		super("TYPE HERE");
//		this.text = text;
//		initializeEvents();
//	}
//
//	private void initializeEvents() {
//		this.setOnMouseEntered(new EventHandler<MouseEvent>() {
//			@Override
//			public void handle(MouseEvent arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		
//		this.setOnMousePressed(new EventHandler<MouseEvent>(){
//			@Override
//			public void handle(MouseEvent arg0) {
//				requestFocus();
//			}
//		});
//		
//		this.setOnKeyPressed(new EventHandler<KeyEvent>(){
//			@Override
//			public void handle(KeyEvent event) {
//				System.out.println("PRESSED: " + event.getText());
//				if(event.getCode() == KeyCode.BACK_SPACE){
//					backspace();
//				}
//			}
//		});
//		
//		this.setOnKeyReleased(new EventHandler<KeyEvent>(){
//
//			@Override
//			public void handle(KeyEvent event) {
//				System.out.println("RELEASED: " + event.getCode());
//				
//			}
//			
//		});
//		
//		this.setOnKeyTyped(new EventHandler<KeyEvent>(){
//
//			@Override
//			public void handle(KeyEvent event) {
//				System.out.println("TYPED: " + event.getCharacter());
//			}
//			
//		});
//	}
//	
//	private void backspace(){
//		
//	}
//	
//}
