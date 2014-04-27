package ca.mapboy.tile;

import org.newdawn.slick.opengl.Texture;

import ca.mapboy.util.Colour;
import ca.mapboy.world.World;

public class StoneTile extends Tile {

	public StoneTile(int id, Colour colour, Texture texture, boolean isSolid, boolean isOpaque) {
		super(id, colour, texture, isSolid, isOpaque);
	}
	
	public void render(int x, int y){

		x /= World.current.tileSize;
		y /= World.current.tileSize;
		
		World.current.renderTileSize(texture, x, y);
		
	}
}
