package zzzzdeprecated;

import gui.widget.WidgetModifier;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import settings.GlobalAppSettings;
import control.TextModifyFacade;
import document.Column;

@Deprecated
public class ColumnViewPane extends Pane {

	private static int requestCount = 0;
	
	private ColumnViewPane selfReference;
	
	private Column page;
	//private ArrayList<TextSlotVisual> visualText;
	
	//this is for lines etc. that has to be updated with every refresh.
	private ArrayList<Node> tempVisualElements;
	
	private ArrayList<Node> visualElements;
	private ImageView backgroundView;
	private TextModifyFacade textFacade;
	
	//TODO: add this to page measurement
	private float paragraphDiff = 10;
	private boolean isRenderInProcess = false;
	private boolean isRefreshCalled = false;

	//private TextLineSpace currentSpace;
	private int currentSpaceSlotIndex;
	private float offsetFromTop;
	
	public ColumnViewPane(Column page){
		this.page = page;
		selfReference = this;
		visualElements = new ArrayList<Node>();
		tempVisualElements = new ArrayList<Node>();
		initGui();
		initEvents();
		this.setWidth(page.getWidth());
		this.setHeight(page.getHeight());
		this.setPrefSize(page.getWidth(), page.getHeight());
		this.setId("page-view");
		
	//	textFacade = DocModifyScreenGuiFacade.instance.getTextModifyFacade();
//		textFacade.addPage(page, this);
//		textFacade.addColumnViewPane(this);
//		visualText = new ArrayList<TextSlotVisual>();
	//	this.getChildren().add(glassPane);
		
		refreshTextShape();
	}

	private void initEvents() {
		this.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
		//		setClip(new Rectangle(0,0,getWidth(), getHeight()));				
			}
		});
		
		this.heightProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
		//		setClip(new Rectangle(0,0,getWidth(), getHeight()));
			}
		});
		
		this.setOnKeyPressed(new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent event) {
	//			TextModifyFacade textModify = DocModifyScreenGuiFacade.instance.getTextModifyFacade();
	//			textModify.keyEvent(event, selfReference);
				
				//refreshTextContentLater();
			}
		});
	}

	private void initGui() {
		backgroundView = new ImageView();
		backgroundView.toBack();
		backgroundView.setFitHeight(this.getHeight());
		backgroundView.setPreserveRatio(true);
		this.getChildren().add(backgroundView);
	}
	
	public void addVisual(Node node){
		this.getChildren().add(node);
		this.visualElements.add(node);
		refreshTextShape();
	}
	
	public void associateWithPage(Column page){
		this.page = page;
		refreshTextShape();
	}
	
	/**
	 * This function gathers up the rendertext calls to make it in one call
	 */
	public void refreshTextShapeLater(){
		if(!isRefreshCalled ){
			isRefreshCalled = true;
			Timeline timer = new Timeline(new KeyFrame(Duration.millis(GlobalAppSettings.fastDeviceFrameMillis), new EventHandler<ActionEvent>(){
				@Override
			    public void handle(ActionEvent event) {
			        refreshTextShape();
			        //DocModifyScreenGuiFacade.instance.updateOverview();
			    }
			}));
			timer.play();
		}
		isRefreshCalled = false;
	}
	
	public void refreshTextContentLater(int index){
		if(!isRefreshCalled ){
			isRefreshCalled = true;
			Timeline timer = new Timeline(new KeyFrame(Duration.millis(GlobalAppSettings.fastDeviceFrameMillis), new TextRefreshHandler(index)));
			timer.play();
		}
		isRefreshCalled = false;
	}
	
	class TextRefreshHandler implements EventHandler<ActionEvent>{
		
		int index;
		
		public TextRefreshHandler(int index){
			this.index = index;
		}
		
		@Override
	    public void handle(ActionEvent event) {
	       // refreshTextContent(index);
	        //DocModifyScreenGuiFacade.instance.updateOverview();
	    }
	}
	
	//public TextSlotVisual requestTextSlot(){
/*		requestCount ++;
		//currentSpace = page.getFittingSpaces(offsetFromTop);
		TextLineSpace.Slot slot = null;
		if(currentSpace == null){
		//	currentSpace = page.getFittingSpaces(offsetFromTop);
			offsetFromTop += 30;
			currentSpaceSlotIndex = 0;
			slot = currentSpace.getSlot(currentSpaceSlotIndex);
			currentSpaceSlotIndex ++;
		}
		else if(currentSpaceSlotIndex < currentSpace.getAllowedSlots().size()){
			slot = currentSpace.getSlot(currentSpaceSlotIndex);
			currentSpaceSlotIndex ++;
		}
		else{
			//TODO: get the largest size of previous textslotvisuals to calculate offsetfromtop
		//	currentSpace = page.getFittingSpaces(offsetFromTop);
			offsetFromTop += 30;
			currentSpaceSlotIndex = 0;
			slot = currentSpace.getSlot(currentSpaceSlotIndex);
			currentSpaceSlotIndex ++;
		}
		
		TextSlotVisual slotVisual = new TextSlotVisual(slot, offsetFromTop, this);
		this.getChildren().add(slotVisual);
		this.visualText.add(slotVisual);
		
		return slotVisual;*/
//	}
	
	private void refreshCaret(){
		
	}
	
/*	private void refreshTextContent(int index){
		textFacade.startTextDivision(false);
		for(int i = 0; i < visualText.size(); i++){
			TextSlotVisual tsl = visualText.get(i);
			if(textFacade.hasNextLine()){
				String line = textFacade.getNextLine(textFacade.getRecordedLineSize(i));
				tsl.setText(line);
			}
		}
	}*/
	
	/**
	 * Renders the column's associated styled text
	 */
	private void refreshTextShape(){
		//prevent reentry
/*		if(isRenderInProcess) 
			return;
		
		isRenderInProcess = true;
		
		//TODO: get the styled text associated with only this column.
		StyledText styledText = DocModifyScreenGuiFacade.instance.getDocument().getStyledText();
		
		textFacade.clearLabels(page);
		
		this.getChildren().removeAll(visualText);
		this.getChildren().removeAll(tempVisualElements);
		tempVisualElements.clear();
		visualText.clear();
		
		double offsetFromTop = page.getInsets().getY();
		
		//update the text
		textFacade.startTextDivision(true);
		
		while(textFacade.hasNextLine()){
			TextLineSpace spaces = page.getFittingSpaces((float) offsetFromTop);
			double greatestTopOffset = 0;
			
			for(int j = 0; j < spaces.getAllowedSlots().size(); j++){
				String line = textFacade.getNextLine(spaces.getSlot(j).endX - spaces.getSlot(j).startX);
				TextStyle style = textFacade.getCurrentStyle();
				int startIndexInStyledText = textFacade.getNextLineStartIndex();
				int endIndexInStyledText = textFacade.getNextLineEndIndex();
				
				if(GlobalAppSettings.areGuiDebugGuidelinesVisible()){
					Line tempLine = new Line(spaces.getSlot(j).startX, offsetFromTop, spaces.getSlot(j).endX, offsetFromTop);
					tempLine.setStroke(Color.CORAL);
					tempVisualElements.add(tempLine);
				}
				
				TextSlotVisual textSlotLabel = new TextSlotVisual(line, style);
				textSlotLabel.setFont(FontHelper.getFont(style.getFontName(), style.getFontSize()));
				textSlotLabel.setTextFill(style.getStrokeColor());
				textSlotLabel.minWidth(spaces.getSlot(j).endX - spaces.getSlot(j).startX);
				textSlotLabel.maxWidth(spaces.getSlot(j).endX - spaces.getSlot(j).startX);
				textSlotLabel.setWrapText(true);
				textSlotLabel.setTextAlignment(TextAlignment.JUSTIFY);
				textSlotLabel.setTranslateX(spaces.getSlot(j).startX);
				textSlotLabel.setTranslateY(offsetFromTop - textSlotLabel.getHeight());
			//	textSlotLabel.setStartEndIndexesInStyledText(startIndexInStyledText, endIndexInStyledText);
				textSlotLabel.setStartIndexInStyledText(startIndexInStyledText);
				textSlotLabel.setEndIndexInStyledText(endIndexInStyledText);
				attachEventToLabel(textSlotLabel);
				textFacade.addLabel(textSlotLabel, page);
				visualText.add(textSlotLabel);
				
				double currentTopOffset = textFacade.getCurrentStyle().getFontMetrics().getLineHeight() + paragraphDiff;
				if(currentTopOffset > greatestTopOffset)
					greatestTopOffset = currentTopOffset;
			}
			offsetFromTop += greatestTopOffset;
		}
		
		this.getChildren().addAll(visualText);
		this.getChildren().addAll(tempVisualElements);
		
		WidgetFocusHandler.refreshWidgetTextOrder();
		backgroundView.toBack();
		
		isRenderInProcess = false;*/
	
		System.out.println("REFRESH CALL ");
		
	}
	
	
	private void addWidgetModifier(WidgetModifier widget){
		addVisual(widget);
		page.addWidget(widget.getWidget());
	}
	
	public void addEmptyImage() {
//		SingleImageWidgetModifier imageWidget = new SingleImageWidgetModifier(page, this);
//		addWidgetModifier(imageWidget);
	}
	
	public void addWebView() {
//		WebViewWidgetModifier webWidget = new WebViewWidgetModifier(page, this);
//		addWidgetModifier(webWidget);
	}
	
	public void addMediaView() {
//		MediaWidgetModifier mediaWidget = new MediaWidgetModifier(page, this);
//		addWidgetModifier(mediaWidget);
	}
	
	public void addLine() {
//		ShapeModifier shapeModifier = new ShapeModifier(PresetShape.LineString);
//		addShape(shapeModifier);
	}

	public void addDemoMediaView(String string) {
//		MediaWidgetModifier mediaWidget = new MediaWidgetModifier(string, page, this);
//		addWidgetModifier(mediaWidget);
	}

	public void addDemoWebView(String string) {
//		WebViewWidgetModifier webWidget = new WebViewWidgetModifier(string, page, this);
//		webWidget.setPrefSize(503, 348);
//		addWidgetModifier(webWidget);
	}

	public void addImageGallery() {
//		ImageGalleryWidgetModifier imageGalleryWidget = new ImageGalleryWidgetModifier(page, this);
//		addWidgetModifier(imageGalleryWidget);
	}

	public void setBackground(Image image) {
		page.setBackgroundImage(image);
		backgroundView.setImage(image);
		if(image.getWidth()<image.getHeight()){
			backgroundView.setFitWidth(this.getWidth());
		}
		else{
			backgroundView.setFitHeight(this.getHeight());
		}
		backgroundView.toBack();
	}

	public void removeWidgetModifier(WidgetModifier widgetModifier) {
		visualElements.remove(widgetModifier);
		this.getChildren().remove(widgetModifier);
		page.removeWidget(widgetModifier.getWidget());
		refreshTextShape();
	}

/*	public void clearText() {
		this.getChildren().removeAll(visualText);
		visualText.clear();
		offsetFromTop = page.getInsets().getMinY();
	}*/
	
}
