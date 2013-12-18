package gui.docmodify.versatile;

import gui.widget.SingleImageWidgetModifier;
import gui.widget.WidgetModifier;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import settings.Translator;

public class HtmlWidgetProperties extends VersatileWidgetProperties{

	private Button htmlButton;
	private FileChooser htmlChooser;

	public HtmlWidgetProperties(){
		super();
		initGui();
		initEvents();
	}
	
	private void initEvents() {
		htmlButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				File file = htmlChooser.showOpenDialog(null);
				((SingleImageWidgetModifier) modifier).setImageFile(file);
			}
		});	
	}
	
	private void initGui() {
		htmlButton = new Button(Translator.get("open web page"));
		htmlChooser = new FileChooser();
		
		FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter(Translator.get("HTML files (*.html)"), "*.HTML");
		FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter(Translator.get("XHTML files (*.xhtml)"), "*.XHTML");
		FileChooser.ExtensionFilter extFilterJPEG = new FileChooser.ExtensionFilter(Translator.get("HTM files (*.htm)"), "*.HTM");
		htmlChooser.getExtensionFilters().addAll(extFilterPNG, extFilterJPG, extFilterJPEG);
		
		grid.add(htmlButton, 0, cummulativeGridIndex++);	
		GridPane.setHalignment(htmlButton, HPos.CENTER);
	}
	
	public void associateWithWidgetModifier(WidgetModifier modifier){
		super.associateWithWidgetModifier(modifier);	
	}
	
}
