package gui.popup;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import settings.Translator;
import event.DocModifyScreenGuiFacade;

public class WelcomePopup extends CustomPopup{

	private GridPane pane;
	
	private Button newBookPane;
	private Button loadBookPane;
	private DocModifyScreenGuiFacade facade;
	
	public WelcomePopup(DocModifyScreenGuiFacade facade) {
		this.facade = facade;
		initGui();
		initEvent();
	}

	private void initEvent() {
		newBookPane.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				facade.createNewDocument();
				hide();
			}
		});
		
		loadBookPane.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				System.out.println("rö");
				hide();
			}
		});
	}

	private void initGui() {
		newBookPane = new Button(Translator.get("New Book"));
		loadBookPane = new Button(Translator.get("Load Book"));
		pane = new GridPane();
		
		pane.add(newBookPane, 0, 0);
		pane.add(loadBookPane, 1, 0);
		
		this.getChildren().add(pane);
	}

	@Override
	public boolean isDisposedWhenClickedOutside() {
		return false;
	}
	
}
