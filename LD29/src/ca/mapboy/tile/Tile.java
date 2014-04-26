package ca.mapboy.tile;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import ca.mapboy.util.Colour;
import ca.mapboy.util.Vector2;
import ca.mapboy.world.World;

public class Tile {
	public static ArrayList<Tile> tileIds = new ArrayList<Tile>();
	
	private int id;
	public boolean isSolid;
	private Colour colour;
	private Texture texture;
	
	public Tile(int id, Colour colour, Texture texture, boolean isSolid){
		
		this.id = id;
		this.colour = colour;
		this.texture = texture;
		this.isSolid = isSolid;
	}
	
	public static Tile getTileById(int id){
		return tileIds.get(id);
	}
	
	public void render(int x, int y){
		
		glEnable(GL_TEXTURE_2D);
		
		if(texture != null){
			Color.white.bind();
			texture.bind();
		}else{
			colour.bind();
		}
		
		glBegin(GL_TRIANGLES); {
			glTexCoord2f(0, 0);
			glVertex2i(x, y);
			glTexCoord2f(1, 0);
			glVertex2i(x + World.current.tileSize, y);
			glTexCoord2f(0, 1);
			glVertex2i(x, y + World.current.tileSize);

			glTexCoord2f(1, 0);
			glVertex2i(x + World.current.tileSize, y);
			glTexCoord2f(1, 1);
			glVertex2i(x + World.current.tileSize, y + World.current.tileSize);
			glTexCoord2f(0, 1);
			glVertex2i(x, y + World.current.tileSize);
		} glEnd();
		
		glEnable(GL_TEXTURE_2D);
	}
	
	public static boolean isColliding(Vector2 position, int x, int y, int padding){
		if(position.x > (x - padding) && position.y > (y - padding) && position.x < (x + padding) + World.current.tileSize && position.y < (y + padding) + World.current.tileSize) return true;
		
		return false;
	}
}