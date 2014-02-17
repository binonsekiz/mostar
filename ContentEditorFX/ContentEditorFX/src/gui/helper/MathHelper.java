package gui.helper;

public class MathHelper {
	
	public static int clamp(int min, int value, int max) {
		return Math.max(min, Math.min(max, value));
	}
	
	public static float clamp(float min, float value, float max) {
		return Math.max(min, Math.min(max, value));
	}
	
	public static double clamp(double min, double value, double max) {
		return Math.max(min, Math.min(max, value));
	}
}
