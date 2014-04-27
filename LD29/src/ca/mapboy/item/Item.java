package ca.mapboy.item;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.util.ArrayList;

import org.newdawn.slick.opengl.Texture;

import ca.mapboy.entity.Player;
import ca.mapboy.world.World;

public class Item {
	public static ArrayList<Item> items = new ArrayList<Item>();
	
	ItemType type;
	Texture texture;
	private int actionFactor;
	
	public Item(ItemType type, Texture texture, int actionFactor){
		this.type = type;
		this.texture = texture;
		this.actionFactor = actionFactor;
		items.add(this);
	}
	
	public void render(float x, float y, int size){
		texture.bind();
		
		glBegin(GL_TRIANGLES); {
			glTexCoord2f(0, 0);
			glVertex2f(x, y);
			glTexCoord2f(1, 0);
			glVertex2f(x + size, y);
			glTexCoord2f(0, 1);
			glVertex2f(x, y + size);

			glTexCoord2f(1, 0);
			glVertex2f(x + size, y);
			glTexCoord2f(1, 1);
			glVertex2f(x + size, y + size);
			glTexCoord2f(0, 1);
			glVertex2f(x, y + size);
		} glEnd();
	}
	
	public void action(){
		
		Player player = World.current.getPlayers().get(0);
		
		switch(type){
		case GoldCoin:
			player.coins += actionFactor;
			player.inventory.inventory.remove(this);
			break;
		case HealthUp:
			player.addHealth(actionFactor);
			player.inventory.inventory.remove(this);
			break;
		case MeleeWeapon:
			player.currentWeapon = this;
			player.inventory.inventory.remove(this);
			
			break;
		}
	}
}
