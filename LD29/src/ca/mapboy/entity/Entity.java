package ca.mapboy.entity;

import static org.lwjgl.opengl.GL11.*;

import ca.mapboy.util.Colour;
import ca.mapboy.util.Vector2;

public class Entity {
	public Vector2 position;
	protected Colour color;
	public int size;
	
	public Entity(Vector2 position, Colour color, int size){
		this.position = position;
		this.color = color;
		this.size = size;
	}
	
	public void update(){}
	
	public void render(){
		color.bind();
		
		int x = position.x;
		int y = position.y;
		
		glBegin(GL_TRIANGLES); {
			glVertex2f(x, y);
			glVertex2f(x + size, y);
			glVertex2f(x, y + size);
			
			glVertex2f(x + size, y);
			glVertex2f(x + size, y + size);
			glVertex2f(x, y + size);
		} glEnd();
	}
	
	public int getX(){
		return position.x;
	}
	
	public int getY(){
		return position.y;
	}
}
