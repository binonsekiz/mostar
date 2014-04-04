package gui.docmodify;

import event.DocModifyScreenGuiFacade;
import gui.GFrame.WindowType;
import gui.ScreenType;
import gui.columnview.DocumentView;
import gui.popup.WelcomePopup;
import javafx.scene.layout.BorderPane;

public class DocModifyScreen extends BorderPane implements ScreenType{

	private DocumentView documentView;
	private DocWidgetToolbar docWidgetToolbar;
	private DocOverview docOverview;
	private DocBottomToolbar docBottomToolbar;
	private DocVersatilePane docVersatilePane;
	private DocDebugView docDebugView;
	
	private WelcomePopup welcomePopup;
	
	private DocModifyScreenGuiFacade guiFacade;
	private WindowType referrer;
	
	public DocModifyScreen(){
		initGui();
		initEvents();
	}
	
	private void initGui(){
		docWidgetToolbar = new DocWidgetToolbar();
		docOverview = new DocOverview();
		
		documentView = new DocumentView();
		docBottomToolbar = new DocBottomToolbar();		
		docVersatilePane = new DocVersatilePane();
		docDebugView = new DocDebugView();
		guiFacade = new DocModifyScreenGuiFacade(documentView, docWidgetToolbar, docOverview, docBottomToolbar, docVersatilePane, docDebugView);
		
		this.setTop(docWidgetToolbar);
		this.setLeft(docDebugView);
		this.setCenter(documentView);
		this.setRight(docVersatilePane);
		this.setBottom(docBottomToolbar);

		welcomePopup = new WelcomePopup(guiFacade);
		welcomePopup.show();
	}
	
	private void initEvents(){
	/*	this.widthProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number oldValue, Number newValue) {
				docModifyPane.setPrefWidth((Double) newValue - docOverview.getWidth() - docVersatilePane.getWidth());
			}
		});
		
		this.heightProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				docModifyPane.setPrefHeight((Double) newValue);
			}
		});*/
	}
	
	public DocModifyScreenGuiFacade getGuiFacade(){
		return guiFacade;
	}

	@Override
	public WindowType getType() {
		return WindowType.DocModifyScreen;
	}

	@Override
	public void setReferrer(WindowType referrer) {
		this.referrer = referrer;
	}
	
}
