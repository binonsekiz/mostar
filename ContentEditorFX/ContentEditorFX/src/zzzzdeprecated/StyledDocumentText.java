package zzzzdeprecated;


import java.util.ArrayList;

import document.Document;
import document.TextStyle;


/**
 * 
 * @author sahin
 *
 */
@Deprecated
public class StyledDocumentText {
	
	private Document document;
	private ArrayList<TextStyle> textStyleLibrary;
	
	public StyledDocumentText(Document document){
		this.document = document;
		textStyleLibrary = new ArrayList<TextStyle>();
	}
	

}
