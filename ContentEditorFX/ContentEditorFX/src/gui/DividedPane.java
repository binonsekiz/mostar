package gui;

import gui.GFrame.WindowType;
import gui.helper.EffectHelper;
import gui.login.SignupScreen;
import gui.start.StartScreen;

import java.util.Random;

import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class DividedPane extends Pane implements ScreenType{
	
	private double widthFactor = 0.2;
	
	private Pane leftPane;
	private Pane rightPane;
	
	private Pane leftPaneClipPane;
	private Pane rightPaneClipPane;
	private Rectangle leftPaneClip;
	private Rectangle rightPaneClip;
	
	private Line dividerLine;

	private WindowType referrer;
	
	public DividedPane(){
		initGui();
		initEvents();
						
		this.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				Random rand = new Random();
				int choice = rand.nextInt(5);
				boolean choice2 = rand.nextBoolean();
				
				if(choice2){
					if(choice == 0)
						changeRightPane(new StartScreen(), EntryAnimation.Downward);
					else if(choice == 1)
						changeRightPane(new StartScreen(), EntryAnimation.Upward);
					else if(choice == 2)
						changeRightPane(new StartScreen(), EntryAnimation.ToLeft);
					else if(choice == 3)
						changeRightPane(new StartScreen(), EntryAnimation.ToRight);
					else if(choice == 4)
						changeRightPane(new StartScreen(), EntryAnimation.NoAnimation);
				}
				else{
					if(choice == 0)
						changeLeftPane(new StartScreen(), EntryAnimation.Downward);
					else if(choice == 1)
						changeLeftPane(new StartScreen(), EntryAnimation.Upward);
					else if(choice == 2)
						changeLeftPane(new StartScreen(), EntryAnimation.ToLeft);
					else if(choice == 3)
						changeLeftPane(new StartScreen(), EntryAnimation.ToRight);
					else if(choice == 4)
						changeRightPane(new StartScreen(), EntryAnimation.NoAnimation);
				}
			}
		});
	}
	
	private void initGui() {
		leftPane = new SignupScreen();
		rightPane = new StartScreen();
		leftPaneClipPane = new Pane();
		rightPaneClipPane = new Pane();
		dividerLine = EffectHelper.getSmoothLine(this.getWidth()*widthFactor, 0, this.getWidth()*widthFactor, this.getHeight());
		recalculateClips();
			
		this.getChildren().add(dividerLine);
		this.getChildren().addAll(leftPaneClipPane, rightPaneClipPane);
		leftPaneClipPane.getChildren().addAll(leftPane);
	}
	
	private void recalculateClips(){
		rightPaneClipPane.setLayoutX(this.getWidth()*widthFactor);
		leftPaneClipPane.setPrefSize(this.getWidth() * (1.0-widthFactor), this.getHeight());
		rightPaneClipPane.setPrefSize(this.getWidth()*widthFactor, this.getHeight());
						
		leftPaneClip = new Rectangle(0,0,0,0);
		rightPaneClip = new Rectangle(0,0,0,0);
		leftPaneClip.setWidth(this.getWidth()* widthFactor);
		rightPaneClip.setWidth(this.getWidth() * (1.0 - widthFactor));
		leftPaneClip.setHeight(this.getHeight());
		rightPaneClip.setHeight(this.getHeight());
		rightPaneClipPane.setClip(rightPaneClip);
		leftPaneClipPane.setClip(leftPaneClip);
		
		dividerLine.setStartX(this.getWidth()*widthFactor);
		dividerLine.setEndX(this.getWidth()*widthFactor);
		dividerLine.setEndY(this.getHeight());
	}
	
	private void initEvents() {
		this.widthProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				recalculateClips();
			}
		});
		
		this.heightProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				recalculateClips();
			}
		});
	}
	
	private void changePane(final Pane newPane, EntryAnimation direction, final boolean isLeft){
		recalculateClips();
		if(isLeft)
			leftPaneClipPane.getChildren().add(newPane);
		else
			rightPaneClipPane.getChildren().add(newPane);
				
		TranslateTransition tt = new TranslateTransition(Duration.millis(500), newPane);
		TranslateTransition tt2 = new TranslateTransition(Duration.millis(500), isLeft ? leftPane : rightPane);

		tt.setToY(0);
		tt2.setFromY(0);
		tt.setToX(0);
		tt2.setFromX(0);
		
		switch(direction){
		case Upward:
			tt.setFromX(0);
			tt.setFromY(this.getHeight());
			tt2.setToY(-1 * this.getHeight());
			break;
		case Downward:
			tt.setFromX(0);
			tt.setFromY(-1 * this.getHeight());
			tt2.setToY(this.getHeight());
			break;
		case ToLeft:
			tt.setFromX(this.getWidth());
			tt2.setToX(-1 * this.getWidth());
			break;
		case ToRight:
			tt.setFromX(-1 * this.getWidth());
			tt2.setToX(this.getWidth());
			break;
		}
		
		tt.play();
		tt2.play();
		
		tt2.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if(isLeft){
					leftPaneClipPane.getChildren().remove(leftPane);
					leftPaneClipPane.getChildren().removeAll();
					leftPane = newPane;
				}
				else{
					rightPaneClipPane.getChildren().remove(rightPane);
					rightPaneClipPane.getChildren().removeAll();
					rightPane = newPane;
				}
			}
		});
	}
	
	public void changeLeftPane(final Pane newPane, EntryAnimation direction){
		changePane(newPane, direction, true);
	}

	public void changeRightPane(final Pane newPane, EntryAnimation direction){
		changePane(newPane, direction, false);
	}

	@Override
	public WindowType getType() {
		return WindowType.DividedPane;
	}
	
	public enum EntryAnimation{
		NoAnimation,
		Upward,
		Downward,
		ToLeft,
		ToRight;
	}

	@Override
	public void setReferrer(WindowType referrer) {
		this.referrer = referrer;
	}

}
