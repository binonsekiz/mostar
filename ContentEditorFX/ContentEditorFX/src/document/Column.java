package document;

import geometry.GeometryHelper;
import geometry.libgdxmath.Polygon;
import gui.helper.LayoutMachine;

import java.util.ArrayList;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import document.PageSpecs.Measurement;
import document.widget.Widget;

public class Column {

	public static Column debugInstance;
	private Measurement pageSize;
	private SimpleObjectProperty<PageInsets> insets;
	private ArrayList<Widget> widgets;	
	private Image background;
	private Polygon shape;
	private ArrayList<ParagraphSet> paragraphSets;
	
	private LayoutMachine layoutMachine;
		
	protected Column(){
		this(PageSpecs.DefaultMeasurement);
	}
	
	protected Column(Measurement measurement){
		this(measurement, PageInsets.Default);
	}
	
	protected Column(Measurement measurement, PageInsets newInsets){
		System.out.println("Column initialized");
		debugInstance = this;
		this.pageSize = measurement;
		widgets = new ArrayList<Widget>();
		paragraphSets = new ArrayList<ParagraphSet>();
		
		this.insets = new SimpleObjectProperty<PageInsets>();
		this.insets.addListener(new ChangeListener<PageInsets>() {
			@Override
			public void changed(ObservableValue<? extends PageInsets> arg0,
					PageInsets arg1, PageInsets arg2) {
				shape = GeometryHelper.getRectanglePolygon(arg2.getActualWidth(), arg2.getActualHeight());
			}
		});
		this.insets.set(newInsets);
		layoutMachine = new LayoutMachine(this);
	}
	
	protected void initialSetup() {
		layoutMachine.initialSetup();
	}
	
	public void addParagraphSet(ParagraphSet newSpace) {
		this.paragraphSets.add(newSpace);
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
	
	public void addWidget(Widget widget){
		this.widgets.add(widget);
		layoutMachine.addSingleElement(widget.getShape());
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

	public LayoutMachine getLayoutMachine() {
		return layoutMachine;
	}

	public ArrayList<ParagraphSet> getParagraphSets() {
		return paragraphSets;
	}

	public void removeParagraphSet(ParagraphSet paragraphSet) {
		paragraphSets.remove(paragraphSet);
	}
}
