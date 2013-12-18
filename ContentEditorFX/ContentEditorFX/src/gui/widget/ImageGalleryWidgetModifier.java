package gui.widget;


import gui.columnview.ColumnView;

import java.io.FileInputStream;

import control.WidgetModifyFacade;

import zzzzdeprecated.ColumnViewPane;

import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.PaginationBuilder;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import document.Column;
import document.widget.ImageGalleryWidget;

public class ImageGalleryWidgetModifier extends WidgetModifier {
	
	private Pagination pagination;
	private ImageGalleryWidgetModifier selfReference;
		
	public ImageGalleryWidgetModifier(WidgetModifyFacade widgetFacade, Column parent, ColumnView parentPane){
		super(widgetFacade, parent, parentPane);
		selfReference = this;
		pagination = new Pagination();
		initGui();
		isResizeAllowed = false;
		widget = new ImageGalleryWidget(this.getLayoutX(), this.getLayoutY(), this.getWidth(), this.getHeight());
		super.initializeGui();
		super.initializeEvents();
		initEvents();
	}
	
	public void setImageCount(int count){
		pagination.setPageCount(count);
	}
	
	public int getImageCount(){
		return pagination.getPageCount();
	}

	private void initGui() {
		final ImageGalleryWidget widget = (ImageGalleryWidget) super.widget;
		pagination = PaginationBuilder.create().pageCount(5).minHeight(200).prefHeight(200).maxHeight(200).pageFactory(new Callback<Integer, Node>(){
			@Override
			public Node call(Integer index) {
				FileInputStream fis = null;
				Image image = null;
				try{
					if(widget.getImage(index) == null){
						fis = new FileInputStream("res/icon/Image-icon.png");
						image = new Image(fis);
					}
					else{
						image = widget.getImage(index);
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
				ImageView view = new ImageView(image);
				view.setPreserveRatio(true);
				view.setFitHeight(150);
				return view;
			}
		}).build();
		pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
	}
	
	private void initEvents() {
		//TODO: init pagination events
		
//		pagination.maxHeight(widgetStack.getHeight()-50);
		widgetStack.getChildren().add(pagination);
	}

	@Override
	public WidgetModifierType getType() {
		return WidgetModifierType.ImageGalleryWidgetModifier;
	}

	public void setImage(Image image, int i) {
		ImageGalleryWidget widget = (ImageGalleryWidget) super.widget;
		widget.setImage(i, image);
		pagination.setPageCount(pagination.getPageCount() + 1);
		pagination.setPageCount(pagination.getPageCount() - 1);
	}

}
