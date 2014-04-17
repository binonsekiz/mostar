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
	}
	
	private void initGui(){
		docWidgetToolbar = new DocWidgetToolbar();
		docOverview = new DocOverview();
		
		documentView = new DocumentView();
		docBottomToolbar = new DocBottomToolbar();		
		docVersatilePane = new DocVersatilePane();
		docDebugView = new DocDebugView();
		guiFacade = new DocModifyScreenGuiFacade(this, documentView, docWidgetToolbar, docOverview, docBottomToolbar, docVersatilePane, docDebugView);
		
		this.setTop(docWidgetToolbar);
		this.setCenter(documentView);
		this.setBottom(docBottomToolbar);
		
		welcomePopup = new WelcomePopup(guiFacade);
		welcomePopup.show();
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

	public void toggleDebugPaneVisible(boolean value) {
		if(value) {
			this.setLeft(docDebugView);
		}
		else{
			this.setLeft(null);
		}
	}
	
	public void toggleVersatilePaneVisible(boolean value) {
		if(value) {
			this.setRight(docVersatilePane);
		}
		else{
			this.setRight(null);
		}
	}
}
