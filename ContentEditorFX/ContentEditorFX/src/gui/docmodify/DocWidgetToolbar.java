package gui.docmodify;

import java.util.Collections;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import settings.Translator;
import control.StyleModifyFacade;
import document.Document;
import document.project.ProjectEnvironment;
import document.project.ProjectRepository;
import document.style.TextStyle;
import gui.helper.ColorGrid;

public class DocWidgetToolbar extends FlowPane{

	private static final double GAP_CONST = 5;
	
	private HBox saveLoadPane;
	private Button newButton;
	private Button saveButton;
	private Button loadButton;
	
	private HBox widgetPane;
	private Button textButton;
	private Button imageButton;
	private Button imageGalleryButton;
	private Button htmlButton;
	private Button mediaButton;
	private Button threeDButton;
	private Button backgroundButton;
	
	private HBox pagePane;
	private Button newBookButton;
	private Button newChapterButton;
	private Button newSectionButton;
	private Button newPageButton;
	
	private HBox fontPane;
	private ComboBox<String> fontBox;
	private ComboBox<String> fontSizeBox;
	private ComboBox<String> styleBox;
	
	private HBox colorPane;
	private Button frontColorPicker;
	private Rectangle frontColorRect;
	private Button highlightColorPicker;
	private Rectangle highlightColorRect;
	private Button strokeColorPicker;
	private Rectangle strokeColorRect;
	
	private HBox importPane;
	private Button importFile;
	private Button exportFile;
	
	private HBox shapePane;
	private Button drawShapeButton;
	private Button drawTableButton;
	private Button drawSignButton;
	private Button drawGraphicButton;
	private Button drawTextBoxButton;
	
	private Button lineButton;
	private StyleModifyFacade styleFacade;
	
	ObservableList<String> fontOptions;
	ObservableList<String> fontSizeOptions;
	ObservableList<String> styleOptions;

	private boolean styleChangeEventsDisabled;
	
	public DocWidgetToolbar(){
		initGui();
		initEvents();
	}

	private void initGui() {
		initSaveLoadControls();
		initWidgetControls();
		initPageControls();
		initComboBoxes();
		initColorControls();
		initImportControls();
		initShapeContols();
		
	//	deactivateControls();
		
		lineButton = new Button(Translator.get("Line"));
		
		this.setVgap(GAP_CONST);
		this.setHgap(GAP_CONST);
		
		this.setId("docwidget-toolbar");
		this.setPadding(new Insets(3));
		
		this.getChildren().addAll(saveLoadPane, pagePane, widgetPane, fontPane, colorPane, importPane, shapePane);
	}

	private void deactivateControls() {
		fontBox.setDisable(true);
		fontSizeBox.setDisable(true);
		styleBox.setDisable(true);
		frontColorPicker.setDisable(true);
		highlightColorPicker.setDisable(true);
		strokeColorPicker.setDisable(true);
		importFile.setDisable(true);
		exportFile.setDisable(true);
		drawShapeButton.setDisable(true);
		drawTextBoxButton.setDisable(true);
		drawTableButton.setDisable(true);
		drawSignButton.setDisable(true);
		drawGraphicButton.setDisable(true);
	}

	private void initShapeContols() {
		shapePane = new HBox();
		
		drawTextBoxButton = new Button(Translator.get("Draw Text Box"));
		drawTextBoxButton.getStyleClass().add("first");
		drawShapeButton = new Button(Translator.get("Draw Shape"));
		drawTableButton = new Button(Translator.get("Draw Table"));
		drawSignButton = new Button(Translator.get("Draw Sign"));
		drawGraphicButton = new Button(Translator.get("Draw Graphic"));
		drawGraphicButton.getStyleClass().add("last");
		
		shapePane.getStyleClass().add("segmented-button-bar-class");
		shapePane.getChildren().addAll(drawTextBoxButton, drawShapeButton, drawTableButton, drawSignButton, drawGraphicButton);
	}
	
	private void initSaveLoadControls() {
		saveLoadPane = new HBox();
		
		newButton = new Button(Translator.get("New"));
		saveButton = new Button(Translator.get("Save"));
		loadButton = new Button(Translator.get("Open"));
		
		saveLoadPane.getStyleClass().add("segmented-button-bar-class");
		saveLoadPane.getChildren().addAll(newButton, saveButton, loadButton);
	}

	private void initWidgetControls() {
		widgetPane = new HBox();
		
		textButton = new Button(Translator.get("Textbox"));
		imageButton = new Button(Translator.get("Image"));
		imageButton.getStyleClass().add("first");
		mediaButton = new Button(Translator.get("Media"));
		mediaButton.getStyleClass().add("last");
		htmlButton = new Button(Translator.get("Html"));
		imageGalleryButton = new Button(Translator.get("Image Gallery"));
		threeDButton = new Button(Translator.get("3D Widget"));
		backgroundButton = new Button(Translator.get("Page Background"));
		
		widgetPane.getStyleClass().add("segmented-button-bar-class");
		widgetPane.getChildren().addAll(imageButton, imageGalleryButton, backgroundButton, htmlButton, mediaButton, threeDButton);
	}

	private void initPageControls() {
		pagePane = new HBox();
		
		newBookButton = new Button(Translator.get("New Book"));
		newBookButton.getStyleClass().add("first");
		newChapterButton = new Button(Translator.get("New Chapter"));
		newSectionButton = new Button(Translator.get("New Section"));
		newPageButton = new Button(Translator.get("New Page"));
		newPageButton.getStyleClass().add("last");
		
		pagePane.getStyleClass().add("segmented-button-bar-class");
		pagePane.getChildren().addAll(newBookButton/*, newChapterButton, newSectionButton*/, newPageButton);
	}

	private void initImportControls() {
		importPane = new HBox();
		
		importFile = new Button(Translator.get("Import"));
		importFile.getStyleClass().add("first");
		exportFile = new Button(Translator.get("Export"));
		exportFile.getStyleClass().add("last");
		
		importPane.getStyleClass().add("segmented-button-bar-class");
//		importPane.getChildren().addAll(importFile, exportFile);
	}

	private void initColorControls() {
		colorPane = new HBox();
		
		frontColorPicker = new Button(Translator.get("Foreground"));
		frontColorRect = new Rectangle(ColorGrid.MIN_TILE_SIZE, ColorGrid.MIN_TILE_SIZE);
		frontColorPicker.setGraphic(frontColorRect);
		frontColorPicker.getStyleClass().add("first");
		
		highlightColorPicker = new Button(Translator.get("Background"));
		highlightColorRect = new Rectangle(ColorGrid.MIN_TILE_SIZE, ColorGrid.MIN_TILE_SIZE);
		highlightColorRect.setFill(Color.WHITE);
		highlightColorRect.setStroke(Color.BLACK);
		highlightColorPicker.setGraphic(highlightColorRect);
		
		strokeColorPicker = new Button(Translator.get("Stroke"));
		strokeColorRect = new Rectangle(ColorGrid.MIN_TILE_SIZE, ColorGrid.MIN_TILE_SIZE);
		strokeColorPicker.setGraphic(strokeColorRect);
		strokeColorPicker.getStyleClass().add("last");
		
		colorPane.getStyleClass().add("segmented-button-bar-class");
		colorPane.getChildren().addAll(frontColorPicker, highlightColorPicker, strokeColorPicker);
	}

	private void initComboBoxes() {
		fontPane = new HBox();
		
		fontOptions = 
			    FXCollections.observableArrayList(
			    	"",
			        "cafe",
			        "Coolvetica",
			        "Vera"
			    );
		fontBox = new ComboBox<String>(fontOptions);
		fontBox.setPromptText(Translator.get("Font"));
		fontBox.setEditable(false);
		
		fontSizeOptions = 
				FXCollections.observableArrayList(
					"","8", "10", "12", "14", "16", "18");
		fontSizeBox = new ComboBox<String>(fontSizeOptions);
		fontSizeBox.setPromptText(Translator.get("Font Size"));
		fontSizeBox.setEditable(true);
		fontSizeBox.getStyleClass().add("last");
		
		styleOptions = 
				FXCollections.observableArrayList(
					Translator.get("Style") + " 1",
					Translator.get("Style") + " 2",
					Translator.get("Style") + " 3");
		styleBox = new ComboBox<String>(styleOptions);
		styleBox.setPromptText(Translator.get("Style"));
		styleBox.setEditable(false);
		styleBox.getStyleClass().add("first");
		
		fontPane.getStyleClass().add("segmented-button-bar-class");
		fontPane.getChildren().addAll(styleBox, fontBox, fontSizeBox);
	}

	private void initEvents() {
		newBookButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ProjectRepository.getActiveProjectEnvironment().associateWithNewDocument(new Document());
			}
		});
		
		newSectionButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ProjectRepository.getActiveProjectEnvironment().addSectionPressed();
			}
		});
		
		newChapterButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ProjectRepository.getActiveProjectEnvironment().addChapterPressed();
			}
		});
		
		newPageButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				ProjectRepository.getActiveProjectEnvironment().addColumnPressed();
			}	
		});
		
		imageButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ProjectRepository.getActiveProjectEnvironment().addImageWidgetPressed();
			}
		});
		
		imageGalleryButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ProjectRepository.getActiveProjectEnvironment().addImageGalleryWidgetPressed();
			}
		});
		
		mediaButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ProjectRepository.getActiveProjectEnvironment().addMediaWidgetPressed();
			}
		});
		
		htmlButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ProjectRepository.getActiveProjectEnvironment().addHtmlWidgetPressed();				
			}
		});
		
		textButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ProjectRepository.getActiveProjectEnvironment().addTextBoxPressed();
			}
		});
		
		backgroundButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ProjectRepository.getActiveProjectEnvironment().pageBackgroundPressed();
			}
		});
		
		threeDButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ProjectRepository.getActiveProjectEnvironment().addThreeDWidgetPressed();
			}
		});
		
		fontBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
				if(styleChangeEventsDisabled) return;
				ProjectRepository.getActiveProjectEnvironment().changeFontName(arg2);
			}
		});
		
		fontSizeBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
				if(styleChangeEventsDisabled) return;
				ProjectRepository.getActiveProjectEnvironment().changeFontSize(arg2);
			}
		});
		
		drawShapeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ProjectRepository.getActiveProjectEnvironment().drawShapeButtonPressed();
			}
		});
		
		drawTextBoxButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ProjectRepository.getActiveProjectEnvironment().drawTextBoxButtonPressed();
			}
		});
		
		newButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ProjectRepository.getActiveProjectEnvironment().newButtonPressed();
			}
		});
		
		saveButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ProjectRepository.getActiveProjectEnvironment().saveButtonPressed();
			}
		});
		
		loadButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ProjectRepository.getActiveProjectEnvironment().loadButtonPressed();
			}
		});
	}
	
	public void setStyleFacade(StyleModifyFacade styleFacade) {
		this.styleFacade = styleFacade;
	}

	public void updateVisualStyleControls() {
		TextStyle style = styleFacade.getSelectionStyle();
		styleChangeEventsDisabled = true;
		updateFontName(style);
		updateFontSize(style);
		styleChangeEventsDisabled = false;
	}
	
	/**
	 * Used for updating the font name combo box
	 * @param style
	 */
	private void updateFontName(TextStyle style) {
		if(style != null) {
			//update font name
			int i;
			for(i = 0; i < fontOptions.size(); i++) {
				if(fontOptions.get(i).equals(style.getFontName())) 
					break;
			}
			if(i >= fontOptions.size()) {
				//this means the list don't have this option. add it.
				fontOptions.add(style.getFontName());
				Collections.sort(fontOptions);
			}
			fontBox.valueProperty().set(fontOptions.get(i));
		}
		else {
			//stlye == null means that the selected text has more than one style in it.
			fontBox.valueProperty().set(fontOptions.get(0));
		}
	}
	
	/**
	 * Used for updating the combo box for font sizes;
	 * @param style
	 */
	private void updateFontSize(TextStyle style) {
		if(style != null) {
			int i;
			for(i = 0; i < fontSizeOptions.size(); i++) {
				if(fontSizeOptions.get(i).equals(style.getFontSize() + ""))
					break;
			}
			if(i >= fontSizeOptions.size()) {
				//this means the list don't have this option. add it.
				fontSizeOptions.add(style.getFontSize() + "");
				Collections.sort(fontSizeOptions);
			}
			fontSizeBox.valueProperty().set(fontSizeOptions.get(i));
		}
		else {
			//stlye == null means that the selected text has more than one style in it.
			fontSizeBox.valueProperty().set(fontSizeOptions.get(0));
		}
	}
	
}
