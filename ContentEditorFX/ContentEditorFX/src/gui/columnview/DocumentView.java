package gui.columnview;

import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import document.Column;
import document.Document;
import event.DocModifyScreenGuiFacade;

/**
 * This is a pane that has a corresponding document attached.
 * It is a collection of column views.
 * @author sahin
 *
 */
public class DocumentView extends Pane{
	
	private DocModifyScreenGuiFacade guiFacade;
	
	private ScrollPane scrollPane;
	private Group scrollContent;
	private GridPane gridPane;
	
	private ArrayList<ColumnView> columnViews;
	private double zoomFactor;
	
	private Document document;
	
	public DocumentView(){
		columnViews = new ArrayList<ColumnView>();
		initGui();
		initEvents();

		zoomFactor = 1;		
		
		debug();
	}

	private void debug(){
		
	}
	
	private void initGui() {
		this.setClip(new Rectangle(0,0,this.getWidth(), this.getHeight()));
		this.setId("docmodify-pane");
		
		scrollPane = new ScrollPane();
		scrollContent = new Group();
		scrollPane.setContent(scrollContent);
		
		scrollPane.setId("scrollpane-custom");
		scrollContent.setId("scrollpane-content");
		
		gridPane = new GridPane();
		gridPane.setVgap(20);
		gridPane.setHgap(20);
		
		this.setId("docmodify-pane");
		scrollContent.getChildren().add(gridPane);
		this.getChildren().add(scrollPane);
	}
	
	private void initEvents(){
		this.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				setClip(new Rectangle(0,0,getWidth(), getHeight()));
				scrollPane.setPrefSize(getWidth(), getHeight());
			}
		});
		
		this.heightProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				setClip(new Rectangle(0,0,getWidth(), getHeight()));
				scrollPane.setPrefSize(getWidth(), getHeight());
			}
		});
	}

	public void associateWithDocument(Document document) {
		this.document = document;
		initialPopulate();
	}

	private void initialPopulate() {
		for(int i = 0; i < document.getColumns().size(); i++){
			Column tempColumn = document.getColumns().get(i);
			ColumnView tempColumnView = new ColumnView(this);
			tempColumnView.associateWithColumn(tempColumn);
			tempColumnView.setStyledText(document.getStyledText());
			columnViews.add(tempColumnView);
			gridPane.add(tempColumnView, i, 0);
		}
		refresh();
	}

	public void refresh() {
		//TODO: increase perf by not refreshing everything
		for(int i = 0; i < columnViews.size(); i++){
			columnViews.get(i).refresh();
		}
	}

	public void changeZoom(double zoomFactor) {
		this.zoomFactor = zoomFactor;
	}

	public double getZoomFactor() {
		return zoomFactor;
	}

	public void setGuiFacade(DocModifyScreenGuiFacade docModifyScreenGuiFacade) {
		this.guiFacade = docModifyScreenGuiFacade;
	}

	public ColumnView getActiveColumnView() {
		//TODO: implement properly
		return columnViews.get(0);
	}

}
