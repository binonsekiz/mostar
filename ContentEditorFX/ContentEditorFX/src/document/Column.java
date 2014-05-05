package document;

import geometry.GeometryHelper;
import geometry.libgdxmath.Polygon;

import java.util.ArrayList;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;

import org.w3c.dom.Element;

import document.PageSpecs.Measurement;
import document.layout.LayoutMachine;
import document.persistentproperties.ColumnProperties;
import document.widget.Widget;

public class Column extends ColumnProperties{

	public static Column debugInstance;
	
	private LayoutMachine layoutMachine;
	
	public Column(Measurement measurement, PageInsets newInsets){
		System.out.println("Column initialized");
		this.pageSize = measurement;
		widgets = new ArrayList<Widget>();
		paragraphSets = new ArrayList<ParagraphSet>();
		shapes = new ArrayList<Polygon>();
		debugInstance = this;
		
		this.insets = new SimpleObjectProperty<PageInsets>();
		this.insets.addListener(new ChangeListener<PageInsets>() {
			@Override
			public void changed(ObservableValue<? extends PageInsets> arg0,
					PageInsets arg1, PageInsets arg2) {
				columnShape = GeometryHelper.getRectanglePolygon(arg2.getActualWidth(), arg2.getActualHeight());
			}
		});
		this.insets.set(newInsets);
		layoutMachine = new LayoutMachine(this);
	}
	
	public Column(Element element) {
		super(element);
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
		return columnShape;
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

	public void addShape(Polygon finalPolygon) {
		shapes.add(finalPolygon);
	}

	public ArrayList<Polygon> getShapes() {
		return shapes;
	}

	public ArrayList<Polygon> getShapesAndWidgetPolygons() {
		ArrayList<Polygon> shapesAndWidgets = new ArrayList<Polygon>();
		for(int i = 0; i < widgets.size(); i++) {
			shapesAndWidgets.add(widgets.get(i).getShape());
		}
		shapesAndWidgets.addAll(shapes);
		return shapesAndWidgets;
	}

}
