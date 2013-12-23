package gui.widget;

import gui.columnview.ColumnView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import control.WidgetModifyFacade;
import document.Column;
import document.widget.WebViewWidget;

public class WebViewWidgetModifier extends WidgetModifier{

	private WebView view;
	private WebEngine engine;
	private String url;
	
	public WebViewWidgetModifier(WidgetModifyFacade widgetFacade, Column columnViewPane, ColumnView colViewPane){
		this(widgetFacade, "http://www.google.com.tr", columnViewPane, colViewPane);
	}
	
	public WebViewWidgetModifier(WidgetModifyFacade widgetFacade,String string, Column parent, ColumnView colViewPane) {
		super(widgetFacade, parent, colViewPane);
		url = string;
		initGui();
		widget = new WebViewWidget(this.getLayoutX(), this.getLayoutY(), this.getWidth(), this.getHeight());
		
		super.initializeGui();
		super.initializeEvents();
		initEvents();
		view.toFront();
		view.maxHeight(widgetStack.getHeight()-50);
	}

	private void initEvents() {
		super.widgetStack.widthProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				view.maxWidth(widgetStack.getWidth());
			}
		});
		
		super.widgetStack.heightProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
			//	view.setFitHeight(widgetStack.getHeight());
				view.maxHeight(widgetStack.getHeight()-40);
		//		view.toBack();
			}
		});
		
		setOnMouseEntered(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent arg0) {
				view.requestFocus();
			}
			
		});
		widgetStack.getChildren().add(view);
	}

	private void initGui() {
		view = new WebView();
		engine = view.getEngine();
		engine.load(url);
	}
	
	public void setURL(String url){
		this.url = url;
		engine.load(url);
	}

	@Override
	public WidgetModifierType getType() {
		return WidgetModifierType.WebViewWidgetModifier;
	}
	
	public String getURL(){
		return url;
	}


}
