package gui;

import control.Caret;
import control.TextModifyFacade;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class SecretTextField extends TextField{

	private Caret caret;
	private TextModifyFacade textFacade;
	
	public SecretTextField() {
		initEvents();
	}

	private void initEvents() {
		this.caretPositionProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				System.out.println("Caret old value: " + arg1 + ", new value: " + arg2 + ", anchor: " + caret.getAnchor());
				caret.setCaretIndex(arg2.intValue());
			}
		});
		this.anchorProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				System.out.println("Anchor old value: " + arg1 + ", new value: " + arg2 + ", actual caret: " + caret.getCaretIndex() + ", setting to value: " + caretPositionProperty().get());
				caret.setAnchorIndex(caretPositionProperty().get());
				
			}
		});
		this.textProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
				textFacade.setActiveParagraphText(arg2);
			}
		});
	}

	public void setTextModifyFacade(TextModifyFacade textModifyFacade) {
		this.textFacade = textModifyFacade;
		caret = textFacade.getCaret();
	}
}
