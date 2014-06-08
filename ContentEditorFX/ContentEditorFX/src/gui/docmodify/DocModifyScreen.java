package gui.docmodify;

import settings.GlobalAppSettings;
import network.NetworkFacade;
import document.project.ProjectRepository;
import gui.GFrame.WindowType;
import gui.ScreenType;
import gui.columnview.DocumentView;
import gui.popup.LoginPopup;
import gui.popup.WelcomePopup;
import javafx.scene.layout.BorderPane;

public class DocModifyScreen extends BorderPane implements ScreenType{

	private DocumentView documentView;
	private DocWidgetToolbar docWidgetToolbar;
	private DocBottomToolbar docBottomToolbar;
	private DocVersatilePane docVersatilePane;
	private DocDebugView docDebugView;
	
	private WelcomePopup welcomePopup;
	private LoginPopup loginPopup;
	
	@SuppressWarnings("unused")
	private WindowType referrer;
	
	public DocModifyScreen(){
		new ProjectRepository();
		new NetworkFacade();
		initGui();
	}
	
	private void initGui(){
		docWidgetToolbar = new DocWidgetToolbar();
		
		documentView = new DocumentView();
		docBottomToolbar = new DocBottomToolbar();		
		docVersatilePane = new DocVersatilePane();
		docDebugView = new DocDebugView();
		
		this.setTop(docWidgetToolbar);
		this.setCenter(documentView);
		this.setBottom(docBottomToolbar);
		
		if(GlobalAppSettings.bypassLogin){
			welcomePopup = new WelcomePopup();
			welcomePopup.show();
		}
		else{
			loginPopup = new LoginPopup();
			loginPopup.show();
		}
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

	public DocumentView getDocumentView() {
		return documentView;
	}

	public DocWidgetToolbar getWidgetToolbar() {
		return docWidgetToolbar;
	}

	public DocBottomToolbar getDocBottomToolbar() {
		return docBottomToolbar;
	}

	public DocVersatilePane getDocVersatilePane() {
		return docVersatilePane;
	}

	public DocDebugView getDocDebugView() {
		return docDebugView;
	}
}
