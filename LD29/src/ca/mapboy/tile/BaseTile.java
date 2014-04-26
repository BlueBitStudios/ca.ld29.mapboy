package ca.mapboy.tile;

import org.newdawn.slick.opengl.Texture;

import ca.mapboy.util.Colour;

public class BaseTile extends Tile {
	public BaseTile(int id, Colour color, Texture texture, boolean isOpaque){
		super(id, color, texture, false, isOpaque);
	}
}
