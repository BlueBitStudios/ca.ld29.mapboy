package ca.mapboy.tile;

import org.newdawn.slick.opengl.Texture;

import ca.mapboy.util.Colour;

public class BaseCollidableTile extends Tile{
	public BaseCollidableTile(int id, Colour color, Texture texture){
		super(id, color, texture, true);
	}
}
