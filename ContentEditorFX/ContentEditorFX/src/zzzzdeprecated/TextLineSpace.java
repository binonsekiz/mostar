package zzzzdeprecated;



public class TextLineSpace {

/*	private PageInsets pageInsets;
	private ArrayList<Slot> disallowedSlots;
	private ArrayList<Slot> allowedSlots;
	
	private static TextStyle defaultStyle = new TextStyle();
	
	private double y;

	public TextLineSpace(PageInsets pageInsets, double y){
		this.pageInsets = pageInsets;
		disallowedSlots = new ArrayList<Slot>();
		allowedSlots = new ArrayList<Slot>();
		this.y = y;
		
		mergeLines();
	}
	
	public void reset(){
		disallowedSlots.clear();
		allowedSlots.clear();
		mergeLines();
	}
	
	public void mergeLines(){
		Collections.sort(disallowedSlots);
		for(int i = 0; i < disallowedSlots.size() - 1; i++){
			boolean result = disallowedSlots.get(i).merge(disallowedSlots.get(i+1));
			if(result){
				disallowedSlots.remove(i+1);
			}
		}
		
		allowedSlots.clear();
		allowedSlots.add(new Slot(pageInsets.getMinX(), pageInsets.getActualWidth(),y));
		for(int i = 0; i < disallowedSlots.size(); i++){
			Slot x = allowedSlots.get(allowedSlots.size() - 1).subtract(disallowedSlots.get(i));
			if(x != null)
				allowedSlots.add(x);
		}		
	}
	
	public ArrayList<Slot> getAllowedSlots(){
		return allowedSlots;
	}
	
	public void addDisallowedSlot(double startX, double endX){
		disallowedSlots.add(new Slot(startX, endX, y));
	}
	
	public class Slot implements Comparable<Slot>{
		public double startX;
		public double endX;
		public double slotY;
		
		private String text;
		private float textWidth;
		
		private TextStyle style;
				
		protected Slot(double startX, double endX, double valY){
			this.startX = startX;
			this.endX = endX;
			this.slotY = valY;
			this.text = "";
			style = defaultStyle;
		}
		
		protected boolean merge(Slot slot){
			if(this.startX < slot.startX){
				if(this.endX < slot.startX){
					//two cannot merge.
					return false;
				}
				else if(this.endX > slot.endX){
					//this already has slot in it.
					return true;
				}
				else{
					this.endX = slot.endX;
					return true;
				}
			}
			else{
				if(slot.endX < this.startX){
					//two cannot merge
					return false;
				}
				else if(this.endX < slot.endX){
					this.startX = slot.startX;
					this.endX = slot.endX;
					return true;
				}
				else{
					this.startX = slot.startX;
					return true;
				}
			}
		}
		
		//removes the given slot from this slot
		//returns a smaller slot
		protected Slot subtract(Slot slot){
			if(this.startX <= slot.startX && this.endX > slot.endX){
				double temp = this.endX;
				this.endX = slot.startX;
				return new Slot(slot.endX, temp, this.slotY);
			}
			else if(this.startX <= slot.startX && this.endX <= slot.endX && this.endX > slot.startX){
				this.endX = slot.startX;
				return null;
			}
			else if(this.startX > slot.startX && this.endX > slot.endX){
				this.startX = slot.endX;
				return null;
			}
			else if(this.startX > slot.startX && this.endX <= slot.endX){
				this.startX = 0;
				this.endX = 0;
				return null;
			}
			else{
				return null;
			}
		}
		
		public void setText(String text){
			this.text = text;
		}
		
		public boolean fits(float wordWidth, float spaceWidth) {
			if(this.text.length() > 0){
				if(endX-startX > wordWidth + textWidth + spaceWidth){
					return true;
				}
				else return false;
			}
			else if(endX - startX > wordWidth){
				return true;
			}
			
			return false;
		}
		
		@Override
		public int compareTo(Slot o) {
			if(this.startX < o.startX)
				return -1;
			if(this.startX == o.startX)
				return 0;
			return 1;
		}
		
		public String toString(){
			return "Slot X1: " + startX + ", X2: " + endX + " Y: " + slotY;  
		}

		//this function assumes fit check is done before.
		public void addWord(String string, float computeStringWidth, float computeStringWidth2, TextStyle currentStyle) {
			if(this.text.length() > 0){
				this.text += " " + string;
				this.textWidth += computeStringWidth + computeStringWidth2;
			}
			else{
				this.text += string;
				this.textWidth = computeStringWidth;
			}
			style = currentStyle;
		}

		public String getText() {
			return text;
		}
		
		public TextStyle getTextStyle(){
			return style;
		}
		
		public double getWidth(){
			return endX - startX;
		}
	}
	
	public double getY() {
		return y;
	}

	public Slot getSlot(int slotIndex) {
		return allowedSlots.get(slotIndex);
	}
*/
	
}
