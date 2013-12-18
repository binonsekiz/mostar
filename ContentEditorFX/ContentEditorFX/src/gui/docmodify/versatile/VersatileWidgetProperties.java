package gui.docmodify.versatile;

import gui.helper.NumberSpinner;
import gui.widget.WidgetModifier;

import java.math.BigDecimal;
import java.text.NumberFormat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import settings.Translator;
import document.widget.Widget.TextWrapType;

public abstract class VersatileWidgetProperties extends Pane{

	private static final BigDecimal spinnerStepValue = new BigDecimal("10");
	
	private Text headerText;
	private TextField headerField;
	private CheckBox headerPresent;
	private Text footerText;
	private TextField footerField;
	private CheckBox footerPresent;
	
	private Text coordinateXText;
	private NumberSpinner coordinateX;
	private Text coordinateYText;
	private NumberSpinner coordinateY;
	private Text widthText;
	private NumberSpinner widthSpinner;
	private Text heightText;
	private NumberSpinner heightSpinner;
	
	private Text integrateTypeText;
	private ComboBox<String> integrateTypeBox;
	
	private Button deleteButton;
	
	protected WidgetModifier modifier;
	protected GridPane grid;
	
	protected int cummulativeGridIndex;
	
	
	public VersatileWidgetProperties(){
		preGuiInit();
		preEventInit();
	}

	private void preEventInit() {
		headerField.textProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String oldValue, String newValue) {
				modifier.setHeaderText(newValue);
			}
		});
		
		footerField.textProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String oldValue, String newValue) {
				modifier.setFooterText(newValue);
			}
		});
		
		deleteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				modifier.delete();
			}
		});
		
		headerPresent.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				modifier.setHeaderPresent(headerPresent.isSelected());
			}
		});
		
		footerPresent.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				modifier.setFooterPresent(footerPresent.isSelected());
			}
		});
		
		coordinateX.getTextField().textProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
				modifier.setLayoutX(coordinateX.getNumber().doubleValue());
			}
		});
		
		coordinateY.getTextField().textProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
				modifier.setLayoutY(coordinateY.getNumber().doubleValue());
			}
		});
		
		widthSpinner.getTextField().textProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
				modifier.setWidthFromVersatile(widthSpinner.getNumber().doubleValue());
			}
		});
		
		heightSpinner.getTextField().textProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
				modifier.setHeightFromVersatile(heightSpinner.getNumber().doubleValue());
			}
		});
		
		integrateTypeBox.valueProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
				int index = integrateTypeBox.getSelectionModel().getSelectedIndex();
				switch(index){
				case 0: modifier.setTextWrap(TextWrapType.Inline); break;
				case 1: modifier.setTextWrap(TextWrapType.Merge); break;
				case 2: modifier.setTextWrap(TextWrapType.Behind); break;
				case 3: modifier.setTextWrap(TextWrapType.Front); break;
				}
			}
		});
	}
	
	private void setupModifierAssociateEvents(){
		modifier.layoutXProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				coordinateX.setNumber(new BigDecimal(modifier.layoutXProperty().doubleValue()));
			}
		});
		
		modifier.layoutYProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				coordinateY.setNumber(new BigDecimal(modifier.layoutYProperty().doubleValue()));
			}
		});
		
		modifier.boundsInLocalProperty().addListener(new ChangeListener<Bounds>() {
			@Override
			public void changed(ObservableValue<? extends Bounds> arg0,
					Bounds arg1, Bounds arg2) {
				widthSpinner.setNumber(new BigDecimal(modifier.boundsInLocalProperty().getValue().getWidth()));
				heightSpinner.setNumber(new BigDecimal(modifier.boundsInLocalProperty().getValue().getHeight()));
			}
		});
	}

	private void preGuiInit() {
		grid = new GridPane();
		
		headerText = new Text(Translator.get("header"));
		headerField = new TextField();
		headerPresent = new CheckBox(Translator.get("Header Visible"));
		footerText = new Text(Translator.get("footer"));
		footerField = new TextField();
		footerPresent = new CheckBox(Translator.get("Footer Visible"));
		deleteButton = new Button(Translator.get("delete"));
		
		NumberFormat format = NumberFormat.getInstance();
		
		coordinateXText = new Text(Translator.get("X Coordinate"));
		coordinateX = new NumberSpinner(BigDecimal.ZERO, spinnerStepValue, format);
		coordinateYText = new Text(Translator.get("Y Coordinate"));
		coordinateY = new NumberSpinner(BigDecimal.ZERO, spinnerStepValue, format);
		widthText = new Text(Translator.get("width"));
		widthSpinner = new NumberSpinner(BigDecimal.ZERO, spinnerStepValue, format);
		heightText = new Text(Translator.get("height"));
		heightSpinner = new NumberSpinner(BigDecimal.ZERO, spinnerStepValue, format);
		
		integrateTypeText = new Text(Translator.get("Wrap Text"));
		integrateTypeBox = new ComboBox<String>();
		integrateTypeBox.getItems().addAll(
				Translator.get("Inline"),
				Translator.get("Merge"),
				Translator.get("Behind"),
				Translator.get("Front"));
		integrateTypeBox.getSelectionModel().select(0);
		
		headerPresent.setSelected(true);
		footerPresent.setSelected(true);
		integrateTypeBox.setEditable(false);
		
		cummulativeGridIndex = 0;
		
		grid.add(headerText, 0, cummulativeGridIndex++);
		grid.add(headerField, 0, cummulativeGridIndex++);
		grid.add(headerPresent, 0, cummulativeGridIndex++);
		grid.add(footerText, 0, cummulativeGridIndex++);
		grid.add(footerField, 0, cummulativeGridIndex++);
		grid.add(footerPresent, 0, cummulativeGridIndex++);
		grid.add(deleteButton, 0, cummulativeGridIndex++);
		
		grid.add(coordinateXText,0, cummulativeGridIndex++);
		grid.add(coordinateX, 0, cummulativeGridIndex++);
		grid.add(coordinateYText, 0, cummulativeGridIndex++);
		grid.add(coordinateY, 0, cummulativeGridIndex++);
		grid.add(widthText, 0, cummulativeGridIndex++);
		grid.add(widthSpinner, 0, cummulativeGridIndex++);
		grid.add(heightText, 0, cummulativeGridIndex++);
		grid.add(heightSpinner, 0, cummulativeGridIndex++);
		
		grid.add(integrateTypeText, 0, cummulativeGridIndex++);
		grid.add(integrateTypeBox, 0, cummulativeGridIndex++);
		
		GridPane.setHalignment(deleteButton, HPos.CENTER);
		
		grid.setVgap(5);
		this.getChildren().add(grid);
	}
	
	/**
	 * Pre-populates all fields with the modifier values.
	 * @param modifier
	 */
	protected void associateWithWidgetModifier(WidgetModifier modifier){
		this.modifier = modifier;
		this.headerField.setText(modifier.getHeaderText());
		this.footerField.setText(modifier.getFooterText());
		this.headerPresent.setSelected(modifier.isHeaderVisible());
		this.footerPresent.setSelected(modifier.isFooterVisible());
		this.coordinateX.setNumber(new BigDecimal(modifier.getBoundsInParent().getMinX()));
		this.coordinateY.setNumber(new BigDecimal(modifier.getBoundsInParent().getMinY()));
		this.widthSpinner.setNumber(new BigDecimal(modifier.getWidth()));
		this.heightSpinner.setNumber(new BigDecimal(modifier.getHeight()));
		setupModifierAssociateEvents();
	}
}
