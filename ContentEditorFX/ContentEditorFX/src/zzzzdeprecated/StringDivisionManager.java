package zzzzdeprecated;

import gui.helper.FontHelper;

import java.util.ArrayList;
import java.util.StringTokenizer;

import javafx.scene.text.Font;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;

import document.Column;
import document.TextLineSpace;
import document.TextLineSpace.Slot;
import document.TextStyle;

/**
 * Deprecated
 * @author sahin
 *
 */
@Deprecated
public class StringDivisionManager {

	private Font font;
	private FontMetrics fontMetrics;
	private Column page;
	
	private static TextStyle defaultTextStyle = new TextStyle();
	
	public StringDivisionManager(Column page){
		this(page, Font.getDefault());
	}
	
	public StringDivisionManager(Column page, Font font){
		this.font = font;
		this.page = page;
	}
		
	/**
	 * assigns the text for textlinespaces in the page.
	 */
	public void updatePageTextLines(){
		ArrayList<TextLineSpace> tls = page.getTextLineSpaces();
		
		//clone the text
		String text = title + loremIpsum + " " + loremIpsum + " " + loremIpsum + " ";
		ArrayList<String> words = new ArrayList<String>();
		
		//divide the text into words
		for(int i = 0; i < 2; i++){
			StringTokenizer tokenizer = new StringTokenizer(text);
			while(tokenizer.hasMoreTokens()){
				words.add(tokenizer.nextToken());
			}
		}
		
		int tlsIndex = 0;
		int slotIndex = 0;
		
		TextStyle currentStyle = defaultTextStyle;
		
		while(words.size() > 0 && tlsIndex < tls.size()){
			ArrayList<Slot> slots = tls.get(tlsIndex).getAllowedSlots();
			slotIndex = 0;
			while(words.size() > 0 && slotIndex < slots.size()){
				if(isStyleWord(words.get(0))){
					currentStyle = new TextStyle(words.get(0));
					fontMetrics = Toolkit.getToolkit().getFontLoader().getFontMetrics(FontHelper.getFont(currentStyle.getFontName(), currentStyle.getFontSize()));
					words.remove(0);
					tlsIndex ++;
				}
				else if(tls.size() > tlsIndex && 
						tls.get(tlsIndex).getAllowedSlots().size() > slotIndex && 
						tls.get(tlsIndex).getSlot(slotIndex).fits(fontMetrics.computeStringWidth(words.get(0)), fontMetrics.computeStringWidth(" "))){
					tls.get(tlsIndex).getSlot(slotIndex).addWord(words.get(0), fontMetrics.computeStringWidth(words.get(0)), fontMetrics.computeStringWidth(" "), currentStyle);
					words.remove(0);
				}
				else{
					slotIndex ++;
				}
			}
			tlsIndex ++;
		}
	}
	
	public boolean isStyleWord(String word){
		if(word.startsWith("<") && word.endsWith(">")) 
			return true;
		else 
			return false;
	}

	public void setPage(Column page) {
		this.page = page;
	}
	
	private String loremIpsum = "<Arial//SLATEGRAY> Dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
	private String title = "</30/BLUE> \nLorem ipsum\n";
}
