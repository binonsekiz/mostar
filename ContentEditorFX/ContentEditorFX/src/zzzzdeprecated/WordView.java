//package zzzzdeprecated;
//
//import gui.columnview.VisualView;
//import javafx.beans.property.IntegerProperty;
//import javafx.beans.property.SimpleIntegerProperty;
//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableValue;
//import javafx.scene.control.Label;
//
//import com.sun.javafx.tk.FontMetrics;
//
///**
// * This is a label only suitable for showing a word.
// * A word is defined as the word itself and not any whitespace.
// * @author sahin
// *
// */
//public class WordView extends Label implements VisualView{
//
//	private WordView selfReference;
//	private LineViewDeprecated parent;
//	
//	private IntegerProperty stringLength;
//	private float wordWidth;
//	
//	public WordView(LineViewDeprecated parent){
//		System.out.println("new word view");
//		this.parent = parent;
//		this.selfReference = this;
//		stringLength = new SimpleIntegerProperty();
//		initEvents();
//	}
//	
//	public void initEvents(){
//		textProperty().addListener(new ChangeListener<String>(){
//			@Override
//			public void changed(ObservableValue<? extends String> arg0,
//					String arg1, String arg2) {
//				calculateWordWidth();
//				
//				int oldValue = stringLength.get();
//				stringLength.set(arg2.length());
//				parent.notifyWordSizeChange(selfReference, oldValue, stringLength.get());
//			}
//		});
//	}
//	
//	public void calculateWordWidth() {
//		FontMetrics metrics = parent.getTextStyle().getFontMetrics();
//		wordWidth = metrics.computeStringWidth(getText());
//	}
//
//	public float getWordWidth(){
//		return wordWidth;
//	}
//	
//	public int getLetterCount(){
//		return getText().length();
//	}
//}
