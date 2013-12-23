package gui.start;

import gui.GFrame.WindowType;
import gui.ScreenType;
import gui.helper.EffectHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import settings.Translator;

public class StartScreen extends Pane implements ScreenType{

	private final int rightPaneOffset = 5;
	
	private Text newDocumentTabButton;
	private Text loadDocumentTabButton;
	private Line divider;
	
	private Pane templatePane;
	private Pane openPane;
	
	private RightPaneType selectedRightPane;

	private WindowType referrer;
	
	public StartScreen(){ 
		initGui();
		initEvents();
	}
	
	private void initGui(){
		newDocumentTabButton = new Text(Translator.get("New"));
		newDocumentTabButton.setTextAlignment(TextAlignment.RIGHT);
		loadDocumentTabButton = new Text(Translator.get("Open"));
		loadDocumentTabButton.setTextAlignment(TextAlignment.RIGHT);
		newDocumentTabButton.setLayoutX(20);
		loadDocumentTabButton.setLayoutX(20);
		newDocumentTabButton.setLayoutY(80);
		loadDocumentTabButton.setLayoutY(280);
		EffectHelper.setAsATitle(newDocumentTabButton);
		EffectHelper.setAsATitle(loadDocumentTabButton);
		
		double lineX = Math.max(newDocumentTabButton.getBoundsInParent().getMaxX(), loadDocumentTabButton.getBoundsInParent().getMaxX()) + 20;
		divider = EffectHelper.getSmoothLine(lineX, 0, lineX, this.getHeight());
		
		templatePane = new Pane();
		templatePane.setLayoutX(lineX + rightPaneOffset);
		openPane = new Pane();
		openPane.setLayoutX(lineX + rightPaneOffset);
		selectedRightPane = RightPaneType.templatePane;
		
		this.getChildren().addAll(newDocumentTabButton, loadDocumentTabButton, divider, templatePane);
	}
	
	private void initEvents(){
		EffectHelper.addMouseRolloverColorTransition(newDocumentTabButton);
		EffectHelper.addMouseRolloverColorTransition(loadDocumentTabButton);
		
		this.heightProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				divider.setEndY(arg0.getValue().doubleValue());
			}	
		});
		
		this.newDocumentTabButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@Override
	public WindowType getType() {
		return WindowType.StartScreen;
	}
	
	private enum RightPaneType{
		templatePane,
		openPane
	}

	@Override
	public void setReferrer(WindowType referrer) {
		this.referrer = referrer;
	}

}
