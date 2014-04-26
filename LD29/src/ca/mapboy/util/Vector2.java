package ca.mapboy.util;

public class Vector2 {
	public int x, y;
	
	public Vector2(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public String toString(){
		return " - [Vector2] Values X/Y: " + x + ", " + y;
	}
	
	public Vector2 plus(int addFactor){
		return new Vector2(
				this.x + addFactor,
				this.y + addFactor
		);
	}
	
	public Vector2 scale(int scaleFactor){
		return new Vector2(
				this.x * scaleFactor,
				this.y * scaleFactor
		);
	}
	
	public static Vector2 add(Vector2 vec1, Vector2 vec2){
		return new Vector2(
				vec1.x + vec2.x,
				vec2.y + vec2.y
		);
	}
	
	public static Vector2 sub(Vector2 vec1, Vector2 vec2){
		return new Vector2(
				vec1.x - vec2.x,
				vec1.y - vec2.y
		);
	}
	
	public static float dot(Vector2 vec1, Vector2 vec2){
		return (vec1.x * vec2.x) + (vec1.y * vec2.y);
	}
	
	public static double distance(Vector2 vec1, Vector2 vec2){
		int dx = vec2.x - vec1.x;
		int dy = vec2.y - vec1.y;
		
		return Math.sqrt((dx * dx) + (dy * dy));
	}
}
