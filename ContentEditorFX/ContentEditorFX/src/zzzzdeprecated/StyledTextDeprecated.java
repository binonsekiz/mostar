package zzzzdeprecated;

import gui.helper.StyleRepository;

import java.util.ArrayList;
import java.util.stream.IntStream;

import com.sun.javafx.tk.FontMetrics;

import document.TextStyle;

/**
 * Represents list of StyleTextPairs. Can be used to import or export string.
 * @author sahin
 *
 */
public class StyledTextDeprecated implements CharSequence{
	// These two *must* have one-to-one char-to-string correspondence
	// Don't reinitialize any style's here, just keep the pointers
	private StringBuffer string;
	private ArrayList<TextStyle> styles;
	
	private StyleRepository styleRepository;
	
	public StyledTextDeprecated(){
		string = new StringBuffer();
		styles = new ArrayList<TextStyle>();
		styleRepository = StyleRepository.instance;
	}

	/**
	 * returns a string that does not have any style information
	 */
	private String generateNonStyledString(){
		return string.toString();
	}
	
	public String exportString(){
		return generateNonStyledString();
	}

	public int length() {
		return string.length();
	}
	
	@Override
	public char charAt(int arg0) {
		return string.charAt(arg0);
	}

	@Override
	public CharSequence subSequence(int arg0, int arg1) {
		return string.subSequence(arg0, arg1);
	}
	
	public float getSubsequenceWidth(int startIndex, int endIndex){
		String text = substring(startIndex, endIndex);
		FontMetrics metrics = styles.get(startIndex).getFontMetrics();
		return metrics.computeStringWidth(text);
	}
	
	/**
	 * The format is as follows: 
	 * <Font1/FontSize1/ForegroundColor1>text<Font2/FontSize2/Color2>text ...
	 * @param text
	 */
	public void importString(String text){
		TextStyle tempStyle = styleRepository.getDefaultStyle();		
		string = new StringBuffer();
		
		//iterate over formatted text
		for(int i = 0; i < text.length(); i++){
			if(text.charAt(i) == '<'){
				
			}
			else if(text.charAt(i) == '/'){

			}
			else if(text.charAt(i) == '>'){

			}
			else{
				string.append(text.charAt(i));
				styles.add(tempStyle);
			}
		}
	}
	
	private enum ReadingMode{
		FontReadingMode, FontSizeReadingMode, ColorReadingMode, TextReadingMode
	}

	public TextStyle getStyleAt(int index) {
		return styles.get(index);
	}

	public String substring(int startIndex, int endIndex) {
		return string.substring(startIndex, endIndex);
	}

	public void removeString(int startIndex, int endIndex) {
		string.delete(startIndex, endIndex);
		for(int i = endIndex - 1; i >= startIndex; i--){
			styles.remove(i);
		}
	}
	
	public String toString(){
		return string.toString();
	}

	public void insertString(String text, TextStyle style, int caretStartIndex, int caretEndIndex) {
		if(caretStartIndex != caretEndIndex)
			removeString(caretStartIndex, caretEndIndex);
		string.insert(caretStartIndex, text);
		if(caretStartIndex == caretEndIndex)
		{
			styles.add(style);
		}
		else{
			for(int i = caretStartIndex; i < caretEndIndex; i++){
				styles.add(style);
			}
		}
	}

	@Override
	public IntStream chars() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntStream codePoints() {
		// TODO Auto-generated method stub
		return null;
	}

}
