package control;

import gui.columnview.ColumnView;
import gui.widget.ImageGalleryWidgetModifier;
import gui.widget.MediaWidgetModifier;
import gui.widget.SingleImageWidgetModifier;
import gui.widget.ThreeDViewerWidgetModifier;
import gui.widget.WebViewWidgetModifier;
import gui.widget.WidgetModifier;

import java.util.ArrayList;

import document.project.ProjectRepository;
import document.widget.Widget.TextWrapType;

public class WidgetModifyFacade extends Facade{
	
	private Caret caret;
	private WidgetFocusHandler widgetFocusHandler;
	private ArrayList<WidgetModifier> widgetModifiers;
	
	public WidgetModifyFacade(){
		this.widgetFocusHandler = new WidgetFocusHandler();
	}

	public void setCaret(Caret caret) {
		this.caret = caret;
	}

	public void changeVersatilePane(WidgetModifier widgetModifier) {
		ProjectRepository.getActiveProjectEnvironment().changeVersatilePane(widgetModifier);
	}
	
	public double getZoomFactor() {
		return ProjectRepository.getActiveProjectEnvironment().getZoomFactor();
	}

	public void addImageWidget() {
		ColumnView activeColumnView = documentView.getActiveColumnView();
		SingleImageWidgetModifier widgetModifier = new SingleImageWidgetModifier(this, caret.getActiveColumn(), activeColumnView);
		widgetModifier.setLayoutX(50);
		widgetModifier.setLayoutY(50);
		activeColumnView.addWidgetModifier(widgetModifier);
		widgetFocusHandler.addWidgetModifier(widgetModifier);
		widgetModifier.initializeShape();
	}
	
	public void addWebViewWidget() {
		ColumnView activeColumnView = documentView.getActiveColumnView();
		WebViewWidgetModifier widgetModifier = new WebViewWidgetModifier(this, caret.getActiveColumn(), activeColumnView);
		activeColumnView.addWidgetModifier(widgetModifier);
		widgetFocusHandler.addWidgetModifier(widgetModifier);
	}
	
	public void addMediaViewWidget() {
		ColumnView activeColumnView = documentView.getActiveColumnView();
		MediaWidgetModifier widgetModifier = new MediaWidgetModifier(this, caret.getActiveColumn(), activeColumnView);
		activeColumnView.addWidgetModifier(widgetModifier);
		widgetFocusHandler.addWidgetModifier(widgetModifier);
	}
	
	public void addImageGalleryWidget() {
		ColumnView activeColumnView = documentView.getActiveColumnView();
		ImageGalleryWidgetModifier widgetModifier = new ImageGalleryWidgetModifier(this, caret.getActiveColumn(), activeColumnView);
		activeColumnView.addWidgetModifier(widgetModifier);
		widgetFocusHandler.addWidgetModifier(widgetModifier);
	}
	
	public void addThreeDWidgetPressed() {
		ColumnView activeColumnView = documentView.getActiveColumnView();
		ThreeDViewerWidgetModifier widgetModifier = new ThreeDViewerWidgetModifier(this, caret.getActiveColumn(), activeColumnView);
		activeColumnView.addWidgetModifier(widgetModifier);
		widgetFocusHandler.addWidgetModifier(widgetModifier);
	}
	
	public void removeWidgetModifier(WidgetModifier widgetModifier){
		widgetModifiers.remove(widgetModifier);
		widgetFocusHandler.loseAllFocus();
	}
	
	public void gainedFocusSignal(WidgetModifier modifier){
		widgetFocusHandler.gainedFocusSignal(modifier);
	}
	
	class WidgetFocusHandler{
		protected WidgetFocusHandler() {
			widgetModifiers = new ArrayList<WidgetModifier>();
		}
		
		protected void addWidgetModifier(WidgetModifier modifier){
			widgetModifiers.add(modifier);
		}
		
		protected void removeWidgetModifier(WidgetModifier modifier){
			widgetModifiers.remove(modifier);
		}
		
		protected void clearAll(){
			widgetModifiers.clear();
		}
		
		protected void gainedFocusSignal(WidgetModifier focusedModifier){
			for(WidgetModifier modifier: widgetModifiers){
				if(modifier != focusedModifier)
					modifier.loseFocus();
			}
		}
		
		protected void loseAllFocus(){
			for(WidgetModifier modifier: widgetModifiers){
				modifier.loseFocus();
			}
			ProjectRepository.getActiveProjectEnvironment().clearVersatilePane();
		}
		
		protected void refreshWidgetTextOrder(){
			for(WidgetModifier modifier: widgetModifiers){
				if(modifier.getWidget().getTextWrap() == TextWrapType.Behind)
					modifier.toBack();
				else if(modifier.getWidget().getTextWrap() == TextWrapType.Front){
					modifier.toFront();
				}
			}
		}
	}
}
