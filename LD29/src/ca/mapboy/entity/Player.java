package ca.mapboy.entity;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;

import ca.mapboy.AudioHandler;
import ca.mapboy.item.Inventory;
import ca.mapboy.item.Item;
import ca.mapboy.util.Colour;
import ca.mapboy.util.LightSource;
import ca.mapboy.util.Loader;
import ca.mapboy.util.Vector2;

public class Player extends Mob {
	public Inventory inventory;
	public Item currentWeapon = null;
	public int coins = 0;
	
	public Player(Vector2 position, Colour color, Texture[] textures) {
		super(position, color, textures, 48, 10);
		
		inventory = new Inventory(9, 4, Loader.getTexture(Player.class.getResource("/inventoryslot.png"), 0), Loader.getTexture(Player.class.getResource("/inventory.png"), 0));
		light = new LightSource(new Vector2(8, 8), 10, 10, 10);
	}
	
	
	public void update(){
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			move(0, -2);
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			move(0, 2);
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			move(-2, 0);
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			move(2, 0);
		}
		
		light.location = this.position.plus(size/2);
	}

	

}
