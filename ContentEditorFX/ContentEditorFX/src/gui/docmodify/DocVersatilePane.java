package gui.docmodify;

import gui.docmodify.versatile.HtmlWidgetProperties;
import gui.docmodify.versatile.ImageGalleryWidgetProperties;
import gui.docmodify.versatile.MediaWidgetProperties;
import gui.docmodify.versatile.SingleImageWidgetProperties;
import gui.widget.ImageGalleryWidgetModifier;
import gui.widget.MediaWidgetModifier;
import gui.widget.SingleImageWidgetModifier;
import gui.widget.WebViewWidgetModifier;
import gui.widget.WidgetModifier;
import gui.widget.WidgetModifier.WidgetModifierType;

import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 * The pane on the right side of the document modification screen.
 * @author sahin
 *
 */
public class DocVersatilePane extends StackPane {
	
	private SingleImageWidgetProperties singleImageProperties;
	private ImageGalleryWidgetProperties imageGalleryProperties;
	private MediaWidgetProperties mediaWidgetProperties;
	private HtmlWidgetProperties htmlWidgetProperties;
	
	private ArrayList<Node> propertyControls;
	
	public DocVersatilePane(){
		this.setId("versatile-pane");
		this.setMinWidth(50);
		initGui();
	}
	
	private void initGui() {
		propertyControls = new ArrayList<Node>();
		
		singleImageProperties = new SingleImageWidgetProperties();
		imageGalleryProperties = new ImageGalleryWidgetProperties();
		mediaWidgetProperties = new MediaWidgetProperties();
		htmlWidgetProperties = new HtmlWidgetProperties();
		
		propertyControls.add(singleImageProperties);
		propertyControls.add(imageGalleryProperties);
		propertyControls.add(mediaWidgetProperties);
		propertyControls.add(htmlWidgetProperties);
		
		hideAll();
		this.getChildren().addAll(singleImageProperties, imageGalleryProperties, mediaWidgetProperties, htmlWidgetProperties);
	}

	/**
	 * Switches the view to the pane, populates it with previously selected options.
	 * @param pane
	 */
	public void show(WidgetModifier widgetModifier){
		if(widgetModifier.getType() == WidgetModifierType.SingleImageWidgetModifier){
			singleImageProperties.associateWithWidgetModifier((SingleImageWidgetModifier)widgetModifier);
			hideAll();
			singleImageProperties.setOpacity(1);
			singleImageProperties.toFront();
		}
		
		else if(widgetModifier.getType() == WidgetModifierType.MediaWidgetModifier){
			mediaWidgetProperties.associateWithWidgetModifier((MediaWidgetModifier)widgetModifier);
			hideAll();
			mediaWidgetProperties.setOpacity(1);
			mediaWidgetProperties.toFront();
		}
		
		else if(widgetModifier.getType() == WidgetModifierType.ImageGalleryWidgetModifier){
			imageGalleryProperties.associateWithWidgetModifier((ImageGalleryWidgetModifier)widgetModifier);
			hideAll();
			imageGalleryProperties.setOpacity(1);
			imageGalleryProperties.toFront();
		}
		
		else if(widgetModifier.getType() == WidgetModifierType.WebViewWidgetModifier){
			htmlWidgetProperties.associateWithWidgetModifier((WebViewWidgetModifier)widgetModifier);
			hideAll();
			htmlWidgetProperties.setOpacity(1);
			htmlWidgetProperties.toFront();
		}
	}
	
	public void hideAll(){
		for(Node node:propertyControls){
			node.setOpacity(0);
		}
	}
}
