package gui.docmodify.versatile;

import java.io.File;

import gui.widget.SingleImageWidgetModifier;
import gui.widget.WidgetModifier;

import settings.Translator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

/**
 * This class is the property pane that shows up when a single image widget is selected 
 * on the DocOverview. 
 * @author sahin
 *
 */
public class SingleImageWidgetProperties extends VersatileWidgetProperties{

	private Button imageButton;
	private FileChooser imageChooser;
	
	public SingleImageWidgetProperties() {
		super();
		initGui();
		initEvents();
	}
	
	private void initEvents() {
		imageButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				File file = imageChooser.showOpenDialog(null);
				((SingleImageWidgetModifier) modifier).setImageFile(file);
			}
		});	
	}

	private void initGui() {
		imageButton = new Button(Translator.get("open image"));
		imageChooser = new FileChooser();
		
		FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter(Translator.get("PNG files (*.png)"), "*.PNG");
		FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter(Translator.get("JPG files (*.jpg)"), "*.JPG");
		FileChooser.ExtensionFilter extFilterJPEG = new FileChooser.ExtensionFilter(Translator.get("JPEG files (*.jpeg)"), "*.JPEG");
		FileChooser.ExtensionFilter extFilterGIF = new FileChooser.ExtensionFilter(Translator.get("GIF files (*.gif)"), "*.GIF");
		imageChooser.getExtensionFilters().addAll(extFilterPNG, extFilterJPG, extFilterJPEG, extFilterGIF);
		
		grid.add(imageButton, 0, cummulativeGridIndex ++);	
		GridPane.setHalignment(imageButton, HPos.CENTER);
	}
	
	public void associateWithWidgetModifier(WidgetModifier modifier){
		super.associateWithWidgetModifier(modifier);
	}
}
