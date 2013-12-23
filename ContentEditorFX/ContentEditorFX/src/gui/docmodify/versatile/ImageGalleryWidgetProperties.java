package gui.docmodify.versatile;

import gui.widget.ImageGalleryWidgetModifier;
import gui.widget.WidgetModifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import settings.Translator;


public class ImageGalleryWidgetProperties extends VersatileWidgetProperties{
	
	private ComboBox<Integer> countBox;
	private Text countText; 
	private GridPane imageButtons;
	
	public ImageGalleryWidgetProperties(){
		super();
		initGui();
		initEvents();
	}

	private void initEvents() {
		countBox.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0,
					Integer arg1, Integer arg2) {
				((ImageGalleryWidgetModifier)modifier).setImageCount(countBox.getValue());
				
				imageButtons.getChildren().clear();
				
				for(int i = 0; i < countBox.getValue(); i++){
					Button imageButton = new Button(Translator.get("Image") + " " + (i+1));
					imageButton.setOnAction(new ButtonEventHandler(i));
					imageButtons.add(imageButton, 0, i);
				}
			}
		});
		
	}

	private void initGui() {
		imageButtons = new GridPane();
		imageButtons.setVgap(5);
		
		countBox = new ComboBox<Integer>();
		countBox.getItems().addAll(
				new Integer(1), new Integer(2), new Integer(3),
				new Integer(4), new Integer(5), new Integer(6),
				new Integer(7), new Integer(8), new Integer(9));
		countBox.setValue(5);		
		countText = new Text(Translator.get("Image Count"));
		
		grid.add(countText, 0, cummulativeGridIndex++);
		grid.add(countBox, 0, cummulativeGridIndex++);
		grid.add(imageButtons, 0, cummulativeGridIndex++);
	}

	public void associateWithWidgetModifier(WidgetModifier modifier){
		super.associateWithWidgetModifier(modifier);
		this.countBox.setValue(((ImageGalleryWidgetModifier)modifier).getImageCount());
	}
	
	class ButtonEventHandler implements EventHandler<ActionEvent>{
		
		private int index;
		
		public ButtonEventHandler(int index){
			this.index = index;
		}
		
		@Override
		public void handle(ActionEvent arg0) {
			FileChooser chooser = new FileChooser();
			chooser.setTitle(Translator.get("Open Background Image"));
			chooser.setInitialDirectory(new File("res/icon"));
			File file = chooser.showOpenDialog(null);
			
			if(file != null){
		    	FileInputStream fis;
				try {
					fis = new FileInputStream(file);
					Image image = new Image(fis);
			    	((ImageGalleryWidgetModifier)modifier).setImage(image, index );
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
		    }
		}
	}
}
