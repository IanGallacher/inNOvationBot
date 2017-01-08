package Globals;

public class Utility {
	public static double distance(int x1, int y1, int x2, int y2) {
		int dx = x1 - x2; // deltaX
		int dy = y1 - y2; // deltaY
		return Math.sqrt( (dx*dx) + (dy*dy) );
	}
}
