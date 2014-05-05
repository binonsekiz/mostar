package document;

import java.util.ArrayList;

import org.w3c.dom.Element;

import document.PageSpecs.Measurement;
import document.persistentproperties.DocumentProperties;

public class Document extends DocumentProperties{

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
		super(element);
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
