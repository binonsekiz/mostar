package gui.widget;


import gui.columnview.ColumnView;

import java.io.File;
import java.net.MalformedURLException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import control.WidgetModifyFacade;
import document.Column;
import document.widget.MediaWidget;
import document.widget.Widget;

public class MediaWidgetModifier extends WidgetModifier{

	private Media media;
	private MediaPlayer mediaPlayer;
	private MediaView mediaView;
	private PlayerPane playerPane;
	
//	private MediaControl mediaControl;
	private Button playButton;
	
	private MediaWidgetModifier selfReference;
	
//TODO:demo	
	public MediaWidgetModifier(WidgetModifyFacade widgetFacade,String url, Column parent, ColumnView parentPane) {
		super(widgetFacade, parent, parentPane);
		selfReference = this;
		
		isResizeAllowed = false;
		setMediaFile(url);
		initGui();
		widget = new MediaWidget(this.getLayoutX(), this.getLayoutY(), this.getWidth(), this.getHeight());
		widget.setShape(getPaneShape());
		super.initializeGui();
		super.initializeEvents();
		initEvents();	
	}
//TODO: end of demo	

	
	public MediaWidgetModifier(WidgetModifyFacade widgetFacade, Column parent, ColumnView columnViewPane) {
		super(widgetFacade, parent, columnViewPane);
		selfReference = this;
		
		isResizeAllowed = false;
		setMediaFile(null);
		initGui();
		widget = new MediaWidget(this.getLayoutX(), this.getLayoutY(), this.getWidth(), this.getHeight());
		widget.setShape(getPaneShape());
		super.initializeGui();
		super.initializeEvents();
		initEvents();
	}
	
	private void initGui() {
//		mediaView = new MediaView();
//		widgetNode = mediaView;
//		mediaControl = new MediaControl(mediaPlayer);
		
		playerPane = new PlayerPane(mediaPlayer);
		playerPane.setTranslateY(20);
		
//		mediaView.setPreserveRatio(true);
//		mediaView.setVisible(true);
		
		playButton = new Button(">");
		playButton.toFront();
		
		this.setId("single-image-widget");
		this.getChildren().add(playerPane);
	}
	
	private void initEvents() {
		super.widgetStack.widthProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
			//	playerPane.setFitWidth(widgetStack.getWidth());
				playerPane.maxWidth(widgetStack.getWidth());
			//	playerPane.setTranslateX(widgetStack.getWidth()/2 - mediaView.getBoundsInParent().getWidth()/2);
				System.out.println(media.getError());
				playerPane.toFront();
			}
		});
		
		super.widgetStack.heightProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				//playerPane.setFitHeight(widgetStack.getHeight());
				playerPane.maxHeight(widgetStack.getHeight());
			//	playerPane.setTranslateX(widgetStack.getWidth()/2 - mediaView.getBoundsInParent().getWidth()/2);
				playerPane.toFront();
			}
		});
		
		playButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
            public void handle(ActionEvent e) {
            	if(mediaPlayer.isAutoPlay()){
            		mediaPlayer.setAutoPlay(false);
            		playButton.setText("||");
            	}
            	else{
            		mediaPlayer.setAutoPlay(true);
            		playButton.setText(">");
            	}
            }
        });
		
		this.setMinSize(320, 220);
		this.setPrefSize(320, 220);
	}

	public void setMediaFile(String url) {
		if(url != null){
			media = new Media(url);
		}
		else{
			File file = new File("res\\media\\turbo.mp4");
			try {
				media = new Media(file.toURI().toURL().toString());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//media = new Media("http://download.oracle.com/otndocs/products/javafx/oow2010-2.flv");
		}
		mediaPlayer = new MediaPlayer(media);
//		mediaView.setMediaPlayer(mediaPlayer);
		mediaPlayer.setAutoPlay(true);
	}
	
	@Override
	public WidgetModifierType getType() {
		return WidgetModifierType.MediaWidgetModifier;
	}
	
	@Override
	public Widget getWidget() {
		return widget;
	}

}
