package document;

import java.util.ArrayList;

import document.PageSpecs.Measurement;

public class Document {
	public static Document instance;
	
	//defaults for every new page
	private Measurement measurement;
	private PageInsets pageInsets;
	
	private DocumentText globalText;
	private ArrayList<Column> columns;
	
	public Document(){
		System.out.println("Document initialized");
		instance = this;
		this.measurement = PageSpecs.MiniTest;
		this.pageInsets = new PageInsets(measurement);
		
		columns = new ArrayList<Column>();
		columns.add(new Column(measurement, pageInsets));
		
		globalText = new DocumentText(this);
		
		for(int i = 0; i < columns.size(); i++) {
			columns.get(i).initialSetup();
		}
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
