package ca.mapboy.item;

import static org.lwjgl.opengl.GL11.*;
import ca.mapboy.entity.Player;
import ca.mapboy.util.Vector2;
import ca.mapboy.world.World;


public class WorldItem {
	public int x, y;
	public Item sourceItem;
	
	public WorldItem(int x, int y, Item sourceItem){
		this.x = x;
		this.y = y;
		this.sourceItem = sourceItem;
	}
	
	public void render(){
		int tileSize = World.current.tileSize/3;
		glColor4d(1, 1, 1, 1);
		sourceItem.texture.bind();
		
		glBegin(GL_TRIANGLES); {
			glTexCoord2f(0, 0);
			glVertex2f(x, y);
			glTexCoord2f(1, 0);
			glVertex2f(x + tileSize, y);
			glTexCoord2f(0, 1);
			glVertex2f(x, y + tileSize);
			
			glTexCoord2f(1, 0);
			glVertex2f(x + tileSize, y);
			glTexCoord2f(1, 1);
			glVertex2f(x + tileSize, y + tileSize);
			glTexCoord2f(0, 1);
			glVertex2f(x, y + tileSize);
		} glEnd();
	}
	
	public void update(){
		Player player = World.current.getPlayers().get(0);
		
		if(isColliding(player.position.plus(player.size/2), player.size/2)){
			player.inventory.addItem(sourceItem);
			World.current.removeItem(this);
		}
		
	}
	
	private boolean isColliding(Vector2 position, int padding){
		if(position.x > (x - padding) && position.y > (y - padding) && position.x < (x + padding) + World.current.tileSize/3 && position.y < (y + padding) + World.current.tileSize/3) return true;
		
		return false;
	}
}
