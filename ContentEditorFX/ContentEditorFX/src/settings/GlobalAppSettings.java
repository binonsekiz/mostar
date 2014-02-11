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
 * Loads app settings and initializes required values. Should only be used upon program load time
 * and if the user changes any settings. Keep this class static only.
 * @author sahin
 *
 */
public class GlobalAppSettings {
	
	public static final double fastDeviceFrameMillis = 16;
	public static final float ignoreValuesBelow = 0.001f;
	public static final float ignoreValuesBelowMedium = 0.1f;
	public static final float ignoreValuesBelowLarge = 5f;
	public static final double caretBlinkRate = 1000;
	public static final float caretMovementTime = 100f;
	public static double frameWidth = 1080;
	public static double frameHeight = 960;
	private static boolean guiDebugGuidelines;
	private static boolean isTestModeOn;
	public static boolean areLineViewCountsVisible = true;
	public static double memoryStatUpdateRate = 500f;
	public static LineFitOption selectedFitLineOption = LineFitOption.averageFit;
	
	public enum LineFitOption {
		strictFit,
		averageFit,
		looseFit
	}
	
	public static void loadAppSettings(){
		loadTestConfigSettings();
		loadLanguageSettings();
	}
	
	private static void loadTestConfigSettings() {
		//defaultValues
		guiDebugGuidelines = false;
		isTestModeOn = false;
		
		try{
			File configFile = new File("res/config/test_config.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(configFile);
			
			NodeList nList = doc.getElementsByTagName("guiDebugGuidelines");
			for(int i = 0; i < nList.getLength(); i++){
				String value;
				Node node = nList.item(i);
				Element element = (Element) node;
				value = element.getTextContent();
				System.out.println("gui debug value :" + value);
				if(value.equalsIgnoreCase("true"))
					guiDebugGuidelines = true;
			}
			
			nList = doc.getElementsByTagName("testMode");
			for(int i = 0; i < nList.getLength(); i++){
				String value;
				Node node = nList.item(i);
				Element element = (Element) node;
				value = element.getTextContent();
				System.out.println("test value :" + value);
				if(value.equalsIgnoreCase("true"))
					isTestModeOn = true;
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private static void loadLanguageSettings(){
		HashMap<String, String> languageFileMap = new HashMap<String, String>();
		ArrayList<String> availableLanguages = new ArrayList<String>();
		
		try{
			File langFile = new File("res/lang/lang.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(langFile);
			
			NodeList nList = doc.getElementsByTagName("language");
			
			for(int i = 0; i < nList.getLength(); i++){
				String langName;
				String filePath;
				Node node = nList.item(i);
				Element element = (Element)node;
				langName = element.getAttribute("id");
				filePath = element.getTextContent();
				availableLanguages.add(langName);
				languageFileMap.put(langName, filePath);
				
				if(guiDebugGuidelines)
					System.out.println("Found language: " + langName + ": " + filePath);
			}
			
			Translator.buildDictionaries(availableLanguages, languageFileMap);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static boolean areGuiDebugGuidelinesVisible(){
		return guiDebugGuidelines;
	}

	public static boolean isTestModeOn() {
		return isTestModeOn;
	}

}
