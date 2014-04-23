package document;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import storage.XmlManager;
import document.PageSpecs.Measurement;

public class Document implements PersistentObject{
	//defaults for every new page
	private Measurement measurement;
	private PageInsets pageInsets;
	
	private DocumentText globalText;
	private ArrayList<Column> columns;
	
	@Override
	public void loadFromXmlElement(Element element) {
		if(!element.getTagName().equals("PortisInteractiveDocument")) throw new RuntimeException("Malformed XML");
		
		measurement = (Measurement) XmlManager.loadObjectFromXmlElement("Measurement", element);
		pageInsets = (PageInsets) XmlManager.loadObjectFromXmlElement("PageInsets", element);
		globalText = (DocumentText) XmlManager.loadObjectFromXmlElement("DocumentText", element);
		columns = (ArrayList<Column>) XmlManager.loadArrayListFromXmlElement("Columns", "Column", element);
	}
	
	public Node saveToXmlNode(org.w3c.dom.Document doc) {
		Element documentElement = doc.createElement("PortisInteractiveDocument");
		XmlManager.insertSingleElement(doc, documentElement, measurement);
		XmlManager.insertSingleElement(doc, documentElement, pageInsets);
		XmlManager.insertSingleElement(doc, documentElement, globalText);
		XmlManager.insertArrayListElements(doc, documentElement, "Columns", columns);
		return documentElement;
	}
	
	public Document(){
		System.out.println("Document initialized");
		this.measurement = PageSpecs.MiniTest;
		this.pageInsets = new PageInsets(measurement);
		
		columns = new ArrayList<Column>();
		columns.add(new Column(measurement, pageInsets));
		
		globalText = new DocumentText(this);
		
		for(int i = 0; i < columns.size(); i++) {
			columns.get(i).initialSetup();
		}
	}
	
	public Document(Element element) {
		loadFromXmlElement(element);
	}
	
	public void addColumn(Column c, int index) {
		columns.add(index, c);
	}
	
	public ArrayList<Column> getColumns(){
		return columns;
	}
	
	public PageInsets getPageInsets() {
		return pageInsets;
	}

	public void setPageInsets(PageInsets pageInsets) {
		this.pageInsets = pageInsets;
	}
	
	public void setMeasurement(Measurement measurement) {
		this.measurement = measurement;
	}

	public Measurement getMeasurement() {
		return measurement;
	}
	
	public DocumentText getDocumentText(){
		return globalText;
	}


}
