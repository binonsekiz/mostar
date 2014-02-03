package document;

import geometry.GeometryHelper;
import geometry.libgdxmath.Polygon;

import java.util.ArrayList;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import document.PageSpecs.Measurement;
import document.widget.Widget;

public class Column {

	private int textLineStep = 18;
	
	private Measurement pageSize;
	private SimpleObjectProperty<PageInsets> insets;
	
	private ArrayList<TextLineSpace> textLineSpaces;
	private ArrayList<Widget> widgets;
	
	private Image background;
	
	private Column previousColumn;
	private Column nextColumn;
	
	private Polygon shape;
		
	protected Column(){
		this(PageSpecs.DefaultMeasurement);
	}
	
	protected Column(Measurement measurement){
		this(measurement, PageInsets.Default);
	}
	
	protected Column(Measurement measurement, PageInsets newInsets){
		System.out.println("Document initialized");
		this.pageSize = measurement;
		textLineSpaces = new ArrayList<TextLineSpace>();
		widgets = new ArrayList<Widget>();
		
		this.insets = new SimpleObjectProperty<PageInsets>();
		this.insets.addListener(new ChangeListener<PageInsets>() {
			@Override
			public void changed(ObservableValue<? extends PageInsets> arg0,
					PageInsets arg1, PageInsets arg2) {
				shape = GeometryHelper.getRectanglePolygon(arg2.getActualWidth(), arg2.getActualHeight());
			}
		});
		this.insets.set(newInsets);
		initializeTextLineSpaces();
	}
	
	private void initializeTextLineSpaces(){
		for(double i = 0 + insets.get().getMinY() ; i < pageSize.getHeight() - insets.get().getMaxY(); i+= textLineStep){
			this.textLineSpaces.add(new TextLineSpace(insets.get(), i));
		}
	}
	
	/**
	 * Returns writable places with offsetFromTop units from the top of the document.
	 */
/*	public TextLineSpace getFittingSpaces(float offsetFromTop){
		TextLineSpace tls = new TextLineSpace(insets, offsetFromTop);
		for(int j = 0; j < widgets.size(); j++){
			Widget currentWidget = widgets.get(j);
			
			if(offsetFromTop > currentWidget.getBorderY1() - textLineStep && offsetFromTop < currentWidget.getBorderY2() + textLineStep && currentWidget.getTextWrap() == TextWrapType.Inline){
				tls.addDisallowedSlot(currentWidget.getBorderX() - textLineStep, currentWidget.getBorderX2() + textLineStep);
			}
			
			else if(offsetFromTop > currentWidget.getBorderY1() - textLineStep && offsetFromTop < currentWidget.getBorderY2() + textLineStep && currentWidget.getTextWrap() == TextWrapType.Merge){
				tls.addDisallowedSlot(this.insets.getX(), this.insets.getWidth());
			}
		}
		tls.mergeLines();
		return tls;
	}
	*/
	public void updateTextLineSpaces(){
		for(int i = 0; i < textLineSpaces.size(); i++){
			textLineSpaces.get(i).reset();
			for(int j = 0; j < widgets.size(); j++){
				if(textLineSpaces.get(i).getY() > widgets.get(j).getBorderY1() - textLineStep && textLineSpaces.get(i).getY() < widgets.get(j).getBorderY2() + textLineStep){
					textLineSpaces.get(i).addDisallowedSlot(Math.max(widgets.get(j).getBorderX() - textLineStep, 0), widgets.get(j).getBorderX2() + textLineStep);
				}
				textLineSpaces.get(i).mergeLines();
			}
		}
	}
	
	public ArrayList<Widget> getWidgets(){
		return widgets;
	}
	
	public void setBackgroundImage(Image image){
		this.background = image;
	}
	
	public Image getBackgroundImage(){
		return background;
	}
	
	public TextLineSpace getTextLineSpace(int index){
		return textLineSpaces.get(index);
	}
	
	public TextLineSpace getTextLineSpaceByHeight(double y){
		return textLineSpaces.get((int) (y / textLineStep));
	}
	
	public void addWidget(Widget widget){
		this.widgets.add(widget);
	}
	
	public void removeWidget(Widget widget){
		this.widgets.remove(widget);
	}

	public double getWidth() {
		return pageSize.getWidth();
	}

	public double getHeight() {
		return pageSize.getHeight();
	}

	public int getTextLineSpaceCount() {
		return textLineSpaces.size();
	}

	public ArrayList<TextLineSpace> getTextLineSpaces() {
		return textLineSpaces;
	}

	public PageInsets getInsets(){
		return insets.get();
	}
	
	public void setInsets(PageInsets insets){
		this.insets.set(insets);
	}
	
	public Measurement getMeasurement(){
		return pageSize;
	}

	public Polygon getPaneShape() {
		return shape;
	}

/*	public ArrayList<Node> getVisualElements() {
		return visualElements;
	}

	public void setVisualElements(ArrayList<Node> visualElements) {
		this.visualElements = visualElements;
	}

	public void removeVisual(WidgetModifier widgetModifier) {
		visualElements.remove(widgetModifier);
	}*/
}
