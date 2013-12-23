package zzzzdeprecated;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import settings.Translator;


@Deprecated
public class DemoToolbar extends ToolBar{

	private Label infoLabel;
	
	private HBox hbox1;
	private Button video1Button;
	private Button video2Button;
	
	private HBox hbox2;
	private Button interactive1Button;
	private Button interactive2Button;

	public DemoToolbar(){
		initGui();
		initEvents();
	}

	private void initGui() {
		infoLabel = new Label("Demo ->");
		infoLabel.setTextFill(Color.web("0x333333"));
	
		video1Button = new Button("Video 1");
		video2Button = new Button("Video 2");
		
		hbox1 = new HBox();
		hbox1.getChildren().addAll(video1Button, video2Button);
		
		interactive1Button = new Button(Translator.get("Int1"));
		interactive2Button = new Button(Translator.get("Int2"));
		
		hbox2 = new HBox();
		hbox2.getChildren().addAll(interactive1Button, interactive2Button);
		
		this.getItems().addAll(infoLabel, hbox1, hbox2);
	}
	
	private void initEvents() {
		video1Button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
		//		guiFacade.demoVideo1();
			}
		});
		
		video2Button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
		//		guiFacade.demoVideo2();
			}
		});
		
		interactive1Button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
		//		guiFacade.demoInteractive1();
			}
		});
		
		interactive2Button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
		//		guiFacade.demoInteractive2();
			}
		});
	}
}
