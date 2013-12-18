package document;


public class PageSpecs {

	public static final Measurement A4 = new Measurement(210, 297);
	public static final Measurement A4H = new Measurement(297, 210);
	public static final Measurement Letter = new Measurement(279.4f, 215.9f);
	public static final Measurement LetterH = new Measurement(215.9f, 279.4f);
	public static final Measurement P1280x768 = new Measurement(1280, 768);
	public static final Measurement P640x768 = new Measurement(640, 768);
	
	public static final Measurement DefaultMeasurement = P640x768;
	
	public static class Measurement{
		private float width;
		private float height;
		
		public Measurement(float width, float height){
			this.width = width;
			this.height = height;
		}
		
		public float getWidth(){
			return width;
		}
		
		public float getHeight(){
			return height;
		}
	}
}
