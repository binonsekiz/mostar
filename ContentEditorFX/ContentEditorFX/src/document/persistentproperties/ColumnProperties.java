package document.persistentproperties;

import geometry.libgdxmath.Polygon;

import java.util.ArrayList;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import storage.XmlManager;
import document.DocumentText;
import document.PageInsets;
import document.ParagraphSet;
import document.PageSpecs.Measurement;
import document.persistentproperties.interfaces.PersistentObject;
import document.project.ProjectRepository;
import document.widget.Widget;

public abstract class ColumnProperties implements PersistentObject {

	protected Measurement pageSize;
	protected SimpleObjectProperty<PageInsets> insets;
	protected ArrayList<Widget> widgets;
	protected Image background;
	protected Polygon columnShape;
	protected ArrayList<ParagraphSet> paragraphSets;
	protected ArrayList<Polygon> shapes;

	public ColumnProperties() {
		//default constructor, empty
	}
	
	public ColumnProperties(Element element) {
		loadFromXmlElement(element);
	}
	
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
			paragraphSets.add(ProjectRepository.getActiveProjectEnvironment().getDocumentText().getParagraphSet(i));
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
	
}
