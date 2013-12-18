package gui.docmodify.versatile;

import gui.widget.SingleImageWidgetModifier;
import gui.widget.WidgetModifier;

import java.io.File;

import settings.Translator;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

public class MediaWidgetProperties extends VersatileWidgetProperties{
	
	private Button mediaButton;
	private FileChooser mediaChooser;

	public MediaWidgetProperties(){
		super();
		initGui();
		initEvents();
	}
	
	private void initEvents() {
		mediaButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				File file = mediaChooser.showOpenDialog(null);
				((SingleImageWidgetModifier) modifier).setImageFile(file);
			}
		});	
	}
	
	private void initGui() {
		mediaButton = new Button(Translator.get("open media"));
		mediaChooser = new FileChooser();
		
		FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter(Translator.get("MP4 files (*.mp4)"), "*.MP4");
		FileChooser.ExtensionFilter extFilterJPEG = new FileChooser.ExtensionFilter(Translator.get("FLV files (*.flv)"), "*.FLV");
		mediaChooser.getExtensionFilters().addAll(extFilterJPG, extFilterJPEG);
		
		grid.add(mediaButton, 0, cummulativeGridIndex++);	
		GridPane.setHalignment(mediaButton, HPos.CENTER);
	}
	
	public void associateWithWidgetModifier(WidgetModifier modifier){
		super.associateWithWidgetModifier(modifier);	
	}
}
