package ca.mapboy.util;

public class LightSource {
	public Vector2 location;
	
	public float red, green, blue;
	
	public LightSource(Vector2 position, float red, float green, float blue){
		this.location = position;
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
}
