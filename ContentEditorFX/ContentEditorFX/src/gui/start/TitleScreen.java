package gui.start;

import geometry.libgdxmath.Interpolation;
import gui.GFrame;
import gui.GFrame.WindowType;
import gui.helper.FontHelper;
import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import settings.GlobalAppSettings;
import settings.Translator;

public class TitleScreen extends Stage{

	private Text title;
	private Text subTitle;
	private WindowType referrer;
	private GaussianBlur titleBlur;
	private double blurFractionValue;
	private int time;
	private final int totalTime = 2000;
	
	private GFrame frame;
	private Scene scene;
	private BorderPane root;
	
	public TitleScreen(GFrame frame) {
		this.frame = frame;
		initGui();
		initStage();
		initEvents();
		
		this.centerOnScreen();
		this.show();
	}
	
	private void initStage() {
		this.initStyle(StageStyle.TRANSPARENT);
		this.setWidth(GlobalAppSettings.smallFrameWidth);
		this.setHeight(GlobalAppSettings.smallFrameHeight);
	}

	private void initGui() {
		blurFractionValue = 0;
		titleBlur = new GaussianBlur(100);
		
		root = new BorderPane();
		scene = new Scene(root, GlobalAppSettings.smallFrameWidth, GlobalAppSettings.smallFrameHeight);
		scene.setFill(null);
		
		title = new Text(Translator.get("portis"));
		subTitle = new Text(Translator.get("content editor"));
		title.setFont(FontHelper.getFont("comfortaa", 100));
		subTitle.setFont(FontHelper.getFont("coolvetica", 48));
		title.setEffect(titleBlur);
		subTitle.setEffect(titleBlur);
		title.setFill(Color.rgb(0, 0, 0, 0));
		subTitle.setFill(Color.rgb(0, 0, 0, 0));

		VBox vbox = new VBox();
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		vbox.setAlignment(Pos.CENTER);
		hbox.getChildren().add(vbox);
		vbox.getChildren().add(title);
		vbox.getChildren().add(subTitle);
		
		root.setCenter(hbox);
		this.setScene(scene);	
	}
	
	private void initEvents() {
		final FillTransition fillTransition = new FillTransition(Duration.millis(500), title, Color.rgb(0, 0, 0, 0), Color.rgb(200, 0, 0));
		final FillTransition fillTransition2 = new FillTransition(Duration.millis(500), subTitle, Color.rgb(0, 0, 0, 0), Color.GRAY);
		
		final Timeline timer = new Timeline(new KeyFrame(Duration.millis(16), new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	time += 16;
		    	blurFractionValue = Interpolation.fade.apply(1.0f - (float)time / totalTime) * 100;
		    	titleBlur.setRadius(blurFractionValue);
		    	
				if(time >= totalTime) {
					frame.mainPath();
					hide();
				}
		    }
		}));
		Timeline timer1 = new Timeline(new KeyFrame(Duration.millis(2000), new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				timer.play();
				fillTransition.play();
				fillTransition2.play();
			}
		}));
		
		timer1.play();
		timer.setCycleCount(totalTime / 16 + 1); 
	}
}
