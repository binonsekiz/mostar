package gui.widget;


import gui.columnview.ColumnView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import control.WidgetModifyFacade;
import document.Column;
import document.widget.SingleImageWidget;
import document.widget.Widget;

public class SingleImageWidgetModifier extends WidgetModifier{

	private ImageView imageView;
	
	public SingleImageWidgetModifier(WidgetModifyFacade widgetFacade, Column column, ColumnView colViewPane){
		super(widgetFacade, column, colViewPane);
		initGui();
		widget = new SingleImageWidget(this.getLayoutX(), this.getLayoutY(), this.getWidth(), this.getHeight());
		widget.setShape(getPaneShape());
		
		super.initializeGui();
		super.initializeEvents();
		initEvents();
	}
	
	private void initEvents() {
		
		super.widgetStack.widthProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				imageView.setFitWidth(widgetStack.getWidth());
				imageView.setTranslateX(widgetStack.getWidth()/2 - imageView.getBoundsInParent().getWidth()/2);
			}
		});
		
		super.widgetStack.heightProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				imageView.setFitHeight(widgetStack.getHeight());
				imageView.setTranslateX(widgetStack.getWidth()/2 - imageView.getBoundsInParent().getWidth()/2);
			}
		});
	}

	private void initGui(){
		imageView = new ImageView();
		setImageFile(null);
		widgetNode = imageView;
		imageView.setPreserveRatio(true);
		
		this.setId("single-image-widget-selected");
	}

	public void setImageFile(File file) {
		try {
			FileInputStream fis = null;
			if(file == null){
				fis = new FileInputStream("res/icon/Image-icon.png");
			}
			else{
				fis = new FileInputStream(file);
			}
			imageView.setImage(new Image(fis));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public WidgetModifierType getType() {
		return WidgetModifierType.SingleImageWidgetModifier;
	}

	@Override
	public Widget getWidget() {
		return widget;
	}

}
