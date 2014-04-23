package document;

import geometry.GeometryHelper;
import geometry.libgdxmath.Polygon;

import java.util.ArrayList;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import storage.XmlManager;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import document.PageSpecs.Measurement;
import document.layout.LayoutMachine;
import document.widget.Widget;

public class Column implements PersistentObject{

	public static Column debugInstance;
	
	private Measurement pageSize;
	private SimpleObjectProperty<PageInsets> insets;
	private ArrayList<Widget> widgets;	
	private Image background;
	private Polygon columnShape;
	private ArrayList<ParagraphSet> paragraphSets;
	private ArrayList<Polygon> shapes;
	
	private LayoutMachine layoutMachine;
	
	@SuppressWarnings("unchecked")
	@Override
	public void loadFromXmlElement(Element element) {
		pageSize = (Measurement) XmlManager.loadObjectFromXmlElement("Measurement", element);
		PageInsets insetsProp = (PageInsets) XmlManager.loadObjectFromXmlElement("PageInsets", element);
		insets = new SimpleObjectProperty<PageInsets>(insetsProp);
		
		widgets = (ArrayList<Widget>) XmlManager.loadArrayListFromXmlElement("Widgets", "Widget", element);
		columnShape = (Polygon) XmlManager.loadObjectFromXmlElement("Polygon", element);
		ArrayList<Integer> paragraphSetIndexes = (ArrayList<Integer>) XmlManager.loadArrayListIdFromXmlElement("ParagraphSets", "ParagraphSet", element);
		
		paragraphSets = new ArrayList<ParagraphSet>();
		for(int i = 0; i < paragraphSetIndexes.size(); i++) {
			paragraphSets.add(DocumentText.instance.getParagraphSet(i));
		}
		
		shapes = (ArrayList<Polygon>) XmlManager.loadArrayListFromXmlElement("Shapes", "Polygon", element);
	}
	
	@Override
	public Node saveToXmlNode(Document doc) {
		Element columnElement = doc.createElement("Column");
		
		XmlManager.insertSingleElement(doc, columnElement, pageSize);
		XmlManager.insertSingleElement(doc, columnElement, insets.get());
		XmlManager.insertArrayListElements(doc, columnElement, "Widgets", widgets);
		XmlManager.insertSingleElement(doc, columnElement, columnShape);
		XmlManager.insertArrayListId(doc, columnElement, "ParagraphSets", "ParagraphSet", paragraphSets);
		XmlManager.insertArrayListElements(doc, columnElement, "Shapes", shapes);
		
		return columnElement;
	}
	
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
		loadFromXmlElement(element);
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
