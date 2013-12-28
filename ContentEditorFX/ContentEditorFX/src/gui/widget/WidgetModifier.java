package gui.widget;

import geometry.GeometryHelper;
import gui.ShapedPane;
import gui.columnview.ColumnView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import control.WidgetModifyFacade;
import document.Column;
import document.widget.Widget;
import document.widget.Widget.TextWrapType;
import event.input.CustomMouseHandler;

/**
 * A modifier for a widget that should be in a doc modify screen.
 * This control allows resizing and relocating the widget in a page.
 * A widget is rectangular, while a single image or text can be any shape.
 * @author sahin
 *
 */
public abstract class WidgetModifier extends ShapedPane implements CustomMouseHandler{
	
	protected boolean isResizeAllowed;
	protected boolean isRelocateAllowed;
	
	private boolean isHeaderVisible = true;
	private boolean isFooterVisible = true;
	
	private boolean hasFocus;

	private LayoutChangeListener listener;
	protected Pane widgetStack;
	protected Widget widget;
	
	private WidgetModifier selfReference;
	protected Node widgetNode;
	
	private Column parent;
	public ColumnView parentPane;
	private WidgetModifyFacade widgetModifyFacade;
	
	protected WidgetModifier(WidgetModifyFacade widgetModifyFacade, Column parent, ColumnView parentPane){
		super(parentPane);
		selfReference = this;
		isRelocateAllowed = true;
		isResizeAllowed = true;
		hasFocus = false;
		this.widgetModifyFacade = widgetModifyFacade;
		listener = new LayoutChangeListener();
		this.parent = parent;
		this.parentPane = parentPane;
	}
	
	protected void initializeGui() {
		widgetStack = new Pane();
		widgetStack.setId("red-pane");
		
		if(widgetNode!= null){
			widgetStack.getChildren().add(widgetNode);
		}
		
		this.getChildren().addAll(widgetStack);
	}

	private void recalculateClip() {
		widgetStack.setLayoutX(0);
		widgetStack.setLayoutY(0);
		widgetStack.setClip(GeometryHelper.polygonShapeFromPolygon(this.getShape()));
	}

	public void delete(){
		widgetModifyFacade.removeWidgetModifier(this);
		parentPane.removeWidgetModifier(this);
	}
	
	protected void initializeEvents() {
		this.setMinHeight(100);
		this.setMinWidth(100);
		this.setPrefWidth(250);
		this.setPrefHeight(250);
		
		this.addEventHandler(MouseEvent.MOUSE_PRESSED,new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if(!hasFocus){
					hasFocus = true;
					gainFocus();
				}
			}
		});
			
		this.widthProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				recalculateClip();
			}
		});
		
		this.heightProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				recalculateClip();
			}
		});
		
		this.layoutXProperty().addListener(listener);
		this.layoutYProperty().addListener(listener);
		this.widthProperty().addListener(listener);
		this.heightProperty().addListener(listener);
		
		recalculateClip();
		
	}
	
	protected class LayoutChangeListener implements ChangeListener<Number>{
		@Override
		public void changed(ObservableValue<? extends Number> arg0,
				Number arg1, Number arg2) {
			widget.setBorders(getLayoutX(), getLayoutY(), getWidth(), getHeight());
		}
	}
	
	public void setWidthFromVersatile(double value){
		setMinWidth(value);
		setPrefWidth(value);
		setMaxWidth(value);
	}
	
	public void setHeightFromVersatile(double value){
		setMinHeight(value);
		setPrefHeight(value);
		setMaxHeight(value);
	}
	
	/**
	 * Returns a renderable, interactable widget object to be put in the document
	 */
	public Widget getWidget(){
		return widget;
	}
	
	/**
	 * Events to be performed when this widgetmodifier gains focus
	 * e.g.: show the resize handlers etc.
	 */
	protected void gainFocus(){
		widgetModifyFacade.changeVersatilePane(this);
		widgetModifyFacade.gainedFocusSignal(this);
		this.setId("single-image-widget-selected");
	}
	
	/**
	 * Events to be performed when this widgetmodifier loses focus
	 * e.g.: hide the resize handlers etc.
	 */
	public void loseFocus(){
		this.setId("single-image-widget");
//		((ColumnViewPane) this.getParent()).refresh();
		hasFocus = false;
	}
	
	public abstract WidgetModifierType getType();
 
	public void setFocus(boolean value){
		if(value != hasFocus){
			if(value) gainFocus();
			else loseFocus();
			this.hasFocus = value;
		}
	}
	
	public boolean hasFocus(){
		return hasFocus;
	}
	
	public enum WidgetModifierType{
		SingleImageWidgetModifier,
		WebViewWidgetModifier, 
		MediaWidgetModifier, 
		ImageGalleryWidgetModifier
	}

	public void setFooterPresent(boolean selected) {
		if(selected != isFooterVisible){
			//state changed, update
		/*	if(selected){
				layout.setBottom(footer);
			}
			else{
				layout.setBottom(null);
			}*/
		}
		this.isFooterVisible = selected;
	}

	public void setHeaderPresent(boolean selected) {
		if(selected != isHeaderVisible){
			//state changed, update
	/*		if(selected){
				layout.setTop(header);
			}
			else{
				layout.setTop(null);
			}*/
		}
		this.isHeaderVisible = selected;
	}

	public boolean isHeaderVisible() {
		return isHeaderVisible;
	}
	
	public boolean isFooterVisible() {
		return isFooterVisible;
	}

	public void setTextWrap(TextWrapType value) {
		if(widget.getTextWrap() != value){
			widget.setTextWrap(value);
			parentPane.refresh();
		}
	}
	
	public void setHeaderText(String text){
	//	header.setText(text);
	}
	
	public void setFooterText(String text){
	//	footer.setText(text);
	}
	
	public String getHeaderText() {
	//	return header.getText();
		return "";
	}
	
	public String getFooterText() {
	//	return footer.getText();
		return "";
	}
}
/*	setOnMousePressed(new EventHandler<MouseEvent>() {
@Override 
public void handle(MouseEvent mouseEvent) {
	if(!hasFocus){
		hasFocus = true;
		gainFocus();
	}
	if(isRelocateAllowed && !isInDraggableZone(mouseEvent)){
		// record a delta distance for the drag and drop operation.
		dragDelta.x = getLayoutX() - mouseEvent.getSceneX();
		dragDelta.y = getLayoutY() - mouseEvent.getSceneY();
		setCursor(Cursor.MOVE);
		isRelocating = true;
		isResizing = false;
	}
	if(isResizeAllowed && isInDraggableZone(mouseEvent)){
		dragging = true;
		if (!initMinHeight) {
			setMinWidth(getWidth());
			setMinHeight(getHeight());
	    	initMinHeight = true;
	    }
		resizeStartDelta.x = mouseEvent.getX();
	    resizeStartDelta.y = mouseEvent.getY();
	    isResizing = true;
	    isRelocating = false;
	}
}
});
setOnMouseReleased(new EventHandler<MouseEvent>() {
@Override 
public void handle(MouseEvent mouseEvent) {
	if(isRelocateAllowed){
		setCursor(Cursor.HAND);
	}
	if(isResizeAllowed){
		setMinWidth(getWidth());
		setMinHeight(getHeight());
	}
	dragging = false;
	isRelocating = false;
	isResizing = false;
}
});
setOnMouseDragged(new EventHandler<MouseEvent>() {
@Override 
public void handle(MouseEvent mouseEvent) {
	if(isRelocateAllowed && isRelocating){
		setLayoutX((mouseEvent.getSceneX() + dragDelta.x) / widgetModifyFacade.getZoomFactor());
		setLayoutY((mouseEvent.getSceneY() + dragDelta.y) / widgetModifyFacade.getZoomFactor());
	}
	if(isResizeAllowed && dragging && isResizing && !isResizingVertical){
		double newWidth = getWidth()+ (mouseEvent.getX() - resizeStartDelta.x);
		setMinWidth(newWidth);
		setMaxWidth(newWidth);
		resizeStartDelta.x = mouseEvent.getX();
	}
	else if(isResizeAllowed && dragging && isResizing && isResizingVertical){
		double newHeight = getHeight() + (mouseEvent.getY() - resizeStartDelta.y);
        setMinHeight(newHeight);
        setMaxHeight(newHeight);
        resizeStartDelta.y = mouseEvent.getY();
    }
}
});
setOnMouseMoved(new EventHandler<MouseEvent>(){
@Override
public void handle(MouseEvent mouseEvent) {
	if(isResizeAllowed){
		if((isInDraggableZone(mouseEvent) && isResizingVertical) || (isResizingVertical && dragging)) {
			setCursor(Cursor.S_RESIZE);
		}
		else if((isInDraggableZone(mouseEvent) && !isResizingVertical) || (!isResizingVertical && dragging)){
			setCursor(Cursor.E_RESIZE);
		}
		else{
			setCursor(Cursor.DEFAULT);
		}
	}
	else if(isRelocateAllowed) {
		setCursor(Cursor.HAND);
	}
	else 
		setCursor(Cursor.DEFAULT);
}
});
*/