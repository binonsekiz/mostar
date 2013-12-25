package gui.docmodify;

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
import event.DocModifyScreenGuiFacade;
import gui.helper.ColorGrid;

public class DocWidgetToolbar extends FlowPane{

	private static final double GAP_CONST = 5;
	
	private HBox widgetPane;
	private Button textButton;
	private Button imageButton;
	private Button imageGalleryButton;
	private Button htmlButton;
	private Button mediaButton;
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
	
	private Button lineButton;
	private Button debugButton;
	private DocModifyScreenGuiFacade guiFacade;
	
	public DocWidgetToolbar(){
		initGui();
		initEvents();
	}

	private void initGui() {
		initWidgetControls();
		initPageControls();
		initComboBoxes();
		initColorControls();
		initImportControls();
		initShapeContols();
		
		deactivateControls();
		
		debugButton = new Button(Translator.get("Debug"));
		lineButton = new Button(Translator.get("Line"));
		
		this.setVgap(GAP_CONST);
		this.setHgap(GAP_CONST);
		
		this.setId("docwidget-toolbar");
		this.setPadding(new Insets(3));
		
		this.getChildren().addAll(pagePane, widgetPane, fontPane, colorPane, importPane, shapePane);
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
		drawTableButton.setDisable(true);
		drawSignButton.setDisable(true);
		drawGraphicButton.setDisable(true);
	}

	private void initShapeContols() {
		shapePane = new HBox();
		
		drawShapeButton = new Button(Translator.get("Draw Shape"));
		drawShapeButton.getStyleClass().add("first");
		drawTableButton = new Button(Translator.get("Draw Table"));
		drawSignButton = new Button(Translator.get("Draw Sign"));
		drawGraphicButton = new Button(Translator.get("Draw Graphic"));
		drawGraphicButton.getStyleClass().add("last");
		
		shapePane.getStyleClass().add("segmented-button-bar-class");
//		shapePane.getChildren().addAll(drawShapeButton, drawTableButton, drawSignButton, drawGraphicButton);
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
		backgroundButton = new Button(Translator.get("Page Background"));
		
		widgetPane.getStyleClass().add("segmented-button-bar-class");
		widgetPane.getChildren().addAll(imageButton, imageGalleryButton, backgroundButton, htmlButton, mediaButton);
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
	//	colorPane.getChildren().addAll(frontColorPicker, highlightColorPicker, strokeColorPicker);
	}

	private void initComboBoxes() {
		fontPane = new HBox();
		
		ObservableList<String> fontOptions = 
			    FXCollections.observableArrayList(
			        "Cafe",
			        "Coolvetica",
			        "Vera"
			    );
		fontBox = new ComboBox<>(fontOptions);
		fontBox.setPromptText(Translator.get("Font"));
		fontBox.setEditable(false);
		
		ObservableList<String> fontSizeOptions = 
				FXCollections.observableArrayList(
					"8", "10", "12", "14", "16", "18");
		fontSizeBox = new ComboBox<>(fontSizeOptions);
		fontSizeBox.setPromptText(Translator.get("Font Size"));
		fontSizeBox.setEditable(true);
		fontSizeBox.getStyleClass().add("last");
		
		ObservableList<String> styleOptions = 
				FXCollections.observableArrayList(
					Translator.get("Style") + " 1",
					Translator.get("Style") + " 2",
					Translator.get("Style") + " 3");
		styleBox = new ComboBox<>(styleOptions);
		styleBox.setPromptText(Translator.get("Style"));
		styleBox.setEditable(false);
		styleBox.getStyleClass().add("first");
		
		fontPane.getStyleClass().add("segmented-button-bar-class");
//		fontPane.getChildren().addAll(styleBox, fontBox, fontSizeBox);
	}

	private void initEvents() {
		newBookButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				guiFacade.createNewDocument();
			}
		});
		
		newSectionButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				guiFacade.addSectionPressed();
			}
		});
		
		newChapterButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				guiFacade.addChapterPressed();
			}
		});
		
		newPageButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				guiFacade.addColumnPressed();
			}	
		});
		
		imageButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				guiFacade.addImageWidgetPressed();
			}
		});
		
		imageGalleryButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				guiFacade.addImageGalleryWidgetPressed();
			}
		});
		
		mediaButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				guiFacade.addMediaWidgetPressed();
			}
		});
		
		htmlButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				guiFacade.addHtmlWidgetPressed();				
			}
		});
		
		textButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				guiFacade.addTextBoxPressed();
			}
		});
		
		backgroundButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				guiFacade.pageBackgroundPressed();
			}
		});
		
		debugButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				//TODO: handle debug events
			}
		});
		
	}

	public void setGuiFacade(DocModifyScreenGuiFacade guiFacade) {
		this.guiFacade = guiFacade;
	}
	
	
}
