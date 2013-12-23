package zzzzdeprecated;

import java.util.ArrayList;

import settings.Translator;
import document.Column;

@Deprecated
public class Section {

	private Chapter parent;
	private String name;
	private ArrayList<Column> columns;
	
	/**
	 * The constructor should only be called from Chapter class.
	 * @param parent
	 */
	protected Section(Chapter parent){
		this.parent = parent;
		columns = new ArrayList<Column>();
		addBlankColumn();
		this.name = Translator.get("Section") + " " + (parent.getSectionCount() + 1);
	}
	
	public void addColumn(Column newColumn){
		this.columns.add(newColumn);
	}
	
	public void addBlankColumn(){
	}
	
	public Column getColumn(int index){
		return columns.get(index);
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public Chapter getParent(){
		return parent;
	}

	public void setParent(Chapter parent) {
		this.parent = parent;
	}

	public int getColumnCount() {
		return columns.size();
	}
}
