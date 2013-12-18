package settings;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class initializes a dictionary from a language resource xml file, and
 * provides translations for original strings on demand. No localized strings should be
 * used in other source files. Keep this class static only.
 * 
 * @author sahin
 *
 */
public class Translator {

	private static HashMap<String, String> dictionary;
	
	public static void buildDictionary(String filePath){
		dictionary = new HashMap<String, String>();
		try{
			File translationFile = new File("res/lang/" + filePath );
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(translationFile);
			
			NodeList nList = doc.getElementsByTagName("text");
			
			for(int i = 0; i < nList.getLength(); i++){
				String original;
				String translation;
				Node node = nList.item(i);
				Element element = (Element)node;
				original = element.getAttribute("id");
				translation = element.getTextContent();
				dictionary.put(original.replaceAll("\\s", "").toLowerCase(), translation);
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void buildDictionaries(ArrayList<String> languageNames, HashMap<String, String> languageFileMap){
		//TODO: add some kind of language selection mechanism. For now, just use the first language
		buildDictionary(languageFileMap.get(languageNames.get(0)));
	}
	
	public static String get(String id){
		if(dictionary.containsKey(id.replaceAll("\\s", "").toLowerCase()))
			return dictionary.get(id.replaceAll("\\s", "").toLowerCase());
		else
			return "[("+id+")]";
	}
	
}
