package ca.mapboy.util;

import static org.lwjgl.opengl.GL11.*;

public class Colour {
	public double red, green, blue, alpha;
	
	public Colour(double red, double green, double blue, double alpha){
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}
	
	
	public void bind(){
		glColor4d(red, green, blue, alpha);
	}
}
