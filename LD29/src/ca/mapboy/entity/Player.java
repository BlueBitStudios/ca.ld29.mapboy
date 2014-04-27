package ca.mapboy.entity;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;

import ca.mapboy.AudioHandler;
import ca.mapboy.Main;
import ca.mapboy.item.Inventory;
import ca.mapboy.item.Item;
import ca.mapboy.util.Colour;
import ca.mapboy.util.Input;
import ca.mapboy.util.LightSource;
import ca.mapboy.util.Loader;
import ca.mapboy.util.Vector2;
import ca.mapboy.world.World;

public class Player extends Mob {
	public Inventory inventory;
	public Item currentWeapon = null;
	public int coins = 0;
	
	public Player(Vector2 position, Colour color, Texture[] textures) {
		super(position, color, textures, 48, 10);
		
		heart = Loader.getTexture(Player.class.getResource("/heart.png"), 0);
		inventory = new Inventory(9, 4, Loader.getTexture(Player.class.getResource("/inventoryslot.png"), 0), Loader.getTexture(Player.class.getResource("/inventory.png"), 0));
		light = new LightSource(new Vector2(8, 8), 10, 10, 10);
	}
	
	public void attack(){
		ArrayList<Mob> nearMobs = World.current.getMobsInRadius(this, 1 * World.current.tileSize);
		
		switch(textureIndex){
		case 0:
			for(Mob mob : nearMobs){
				if(mob.getY() < position.y){
					mob.hurt(1, 0, -30);
				}
			}
			break;
		case 1:
			for(Mob mob : nearMobs){
				if(mob.getY() > position.y){
					mob.hurt(1, 0, 30);
				}
			}
			break;
		case 2:
			for(Mob mob : nearMobs){
				if(mob.getX() > position.x){
					mob.hurt(1, 30, 0);
				}
			}
			break;
		case 3:
			for(Mob mob : nearMobs){
				if(mob.getX() < position.x){
					mob.hurt(1, -30, 0);
				}
			}
			break;
		}
	}
	
	public void update(){
		super.update();
		
		if(Input.checkForPress(Keyboard.KEY_SPACE)){
			attack();
		}
		
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

	public void renderHealth(){
		float px = 0 - Main.px;
		float py = 0 + Main.py;

		if(health < 3){
			
			if(!AudioHandler.heartBeat.isPlaying()){
				AudioHandler.playSound(AudioHandler.heartBeat);
			}
			
			glColor4d(1, 0.2, 0.2, 0.5);
			glBegin(GL_QUADS); {
				glVertex2f(px, py);
				glVertex2f(px + Main.WIDTH, py);
				glVertex2f(px + Main.WIDTH, py + Main.HEIGHT);
				glVertex2f(px, py + Main.HEIGHT);
				
			} glEnd();
		}
		
		
		for(int i = 0; i < maxHealth; i++){
			if(health <= i){
				glColor4d(0.6, 0.6, 0.6, 0.6);
			}else{
				glColor4d(1, 1, 1, 1);
			}
			
			renderHeart(Main.WIDTH - 48 + px, i * 48 + 16 + py);
		}
		
		
		
	}
	
	Texture heart;
	
	private void renderHeart(float x, float y){
		heart.bind();
		
		glBegin(GL_QUADS); {
			glTexCoord2f(0, 0);
			glVertex2f(x, y);
			glTexCoord2f(1, 0);
			glVertex2f(x + 32, y);
			glTexCoord2f(1, 1);
			glVertex2f(x + 32, y + 32);
			glTexCoord2f(0, 1);
			glVertex2f(x, y + 32);
			
		} glEnd();
	}
	
}
