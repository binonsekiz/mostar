package zzzzdeprecated;

import java.util.ArrayList;

import settings.Translator;

@Deprecated
public class Chapter {
	
	private static int chapterCount = 0;

	private String name;
	private ArrayList<Section> sections;
	
	public Chapter(){
		this.sections = new ArrayList<Section>();
		addBlankSection();
		chapterCount ++;
		this.name = Translator.get("Chapter") + " " + chapterCount;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public Section getSection(int index){
		return sections.get(index);
	}
	
	public void addSection(Section s){
		this.sections.add(s);
	}

	public int getSectionCount() {
		return sections.size();
	}

	public void addBlankSection() {
		Section section = new Section(this);
		section.setParent(this);
		addSection(section);
	}
	
}
