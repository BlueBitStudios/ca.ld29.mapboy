package ca.mapboy.tile;

import org.newdawn.slick.opengl.Texture;

import ca.mapboy.util.Colour;

public class BaseVoidTile extends Tile {
	public BaseVoidTile(int id, Colour color, Texture texture){
		super(id, color, texture, false);
	}
	
	public void render(int x, int y){}

}
