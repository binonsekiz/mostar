//package zzzzdeprecated;
//
//import java.util.ArrayList;
//
//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableValue;
//import javafx.event.EventHandler;
//import javafx.scene.control.TextArea;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.Pane;
//import javafx.scene.text.Text;
//
//public class TextAreaSetDeprecated extends Pane{
//
//	private ArrayList<TextArea> textAreas;
//	private short lastUsedIndex;
//	private Text helper;
//	private static int counter = 0;
//	
//	public TextAreaSetDeprecated(){
//		textAreas = new ArrayList<TextArea>();
//		helper = new Text();
//		addTextArea("Init");
//	}
//
//	public TextAreaSetDeprecated(String text){
//		this();
//		setText(text);
//	}
//	
//	private void addTextArea(String initialText) {
//		final TextArea textArea = new TextArea(initialText);
//		textArea.relocate(0, lastUsedIndex * 20);
//		textArea.setWrapText(true);
//		textArea.setPrefRowCount(1);
//
//		textAreas.add(textArea);
//		
//		textArea.textProperty().addListener(new ChangeListener<String>() {
//			@Override
//			public void changed(ObservableValue<? extends String> arg0, String old, String recent) {
//				if(textArea.getWidth() - 15 < computeTextWidth(textArea.getText())){
//					System.out.println("Text: " + textArea.getText());
//					System.out.println("Line end" + counter);
//
//					//roll the change back
//					textArea.textProperty().removeListener(this);
//					textArea.setText(old);
//					textArea.textProperty().addListener(this);
//
//					lastUsedIndex ++;
//					addTextArea(recent.substring(old.length()));
//					
//					counter++;
//				}
//			}
//		});
//		
//		textArea.setOnMouseClicked(new EventHandler<MouseEvent>(){
//			@Override
//			public void handle(MouseEvent arg0) {
//				textAreas.get(lastUsedIndex).requestFocus();
//			}
//		});
//		
//		this.getChildren().add(textArea);
//		textArea.requestFocus();
//	}
//	
//	private void removeTextArea(){
//		
//	}
//	
//	public void setText(String text){
//		this.textAreas.get(0).setText(text);
//	}
//		
//	
//	private double computeTextWidth(String text){
//		helper.setText(text);
//	//	helper.setFont(textAreas.get(0).);
//	//default font for now. TODO: find get-font
//		helper.setWrappingWidth(0);
//		double w = helper.prefWidth(-1);
//	    helper.setWrappingWidth((int)Math.ceil(w));
//	    return Math.ceil(helper.getLayoutBounds().getWidth());
//	}
//	
//}
