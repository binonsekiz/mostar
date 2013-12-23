package document;

import gui.docmodify.test.StyledTextTest;

import java.util.ArrayList;

import document.PageSpecs.Measurement;

public class Document {
	
	//defaults for every new page
	private Measurement measurement;
	private PageInsets pageInsets;
	
	private StyledText styledText;
	private ArrayList<Column> columns;
	
	public Document(){
		this.measurement = PageSpecs.P640x768;
		this.pageInsets = new PageInsets(measurement);
		
		columns = new ArrayList<Column>();
		columns.add(new Column(measurement, pageInsets));
		
		styledText = new StyledText();
		styledText.importString(StyledTextTest.getText());
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
	
	public StyledText getStyledText(){
		return styledText;
	}
}
