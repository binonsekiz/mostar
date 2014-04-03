package gui;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import settings.Translator;
import event.DocModifyScreenGuiFacade;
import gui.GFrame.WindowType;
import gui.docmodify.DocModifyScreen;
import gui.threed.ThreeDModifyScreen;

public class DocTabbedView extends BorderPane implements ScreenType{

	private TabPane tabPane;
	
	private DocModifyScreenGuiFacade facade;
	
	private Tab docModifyTab;
	private DocModifyScreen docModifyScreen;
	
	private Tab threeDModifyTab;
	private ThreeDModifyScreen threeDModifyScreen;
	
	private Tab templateTab;
	private TemplateModifyScreen templateModifyScreen;
	
	private WindowType referrer;
	
	public DocTabbedView() {
		tabPane = new TabPane(){
			@Override
			public void requestFocus() {
				// do nothing on focus
			}
		};
		
		docModifyScreen = new DocModifyScreen();
		docModifyTab = new Tab(Translator.get("Content Editor"));
		docModifyTab.setContent(docModifyScreen);
		docModifyTab.setClosable(false);
		facade = docModifyScreen.getGuiFacade();
		tabPane.getTabs().add(docModifyTab);
		
		threeDModifyScreen = new ThreeDModifyScreen();
		threeDModifyTab = new Tab(Translator.get("3D Editor"));
		threeDModifyTab.setContent(threeDModifyScreen);
		threeDModifyTab.setClosable(false);
		threeDModifyScreen.setGuiFacade(facade);
		tabPane.getTabs().add(threeDModifyTab);
		
		templateModifyScreen = new TemplateModifyScreen();
		templateTab = new Tab(Translator.get("Template Editor"));
		templateTab.setContent(templateModifyScreen);
		templateTab.setClosable(false);
		templateModifyScreen.setGuiFacade(facade);
		tabPane.getTabs().add(templateTab);
		
	//	tabPane.getSelectionModel().select(threeDModifyTab);
		
		this.setCenter(tabPane);
	}
	
	@Override
	public void requestFocus() {
		// do nothing... this disables keyboard events on tabs
	};
	
	@Override
	public WindowType getType() {
		return WindowType.DocTabbedView;
	}

	@Override
	public void setReferrer(WindowType referrer) {
		this.referrer = referrer;
	}

	
	
}
