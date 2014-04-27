package ca.mapboy.entity;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import ca.mapboy.AudioHandler;
import ca.mapboy.tile.Tile;
import ca.mapboy.util.Colour;
import ca.mapboy.util.LightSource;
import ca.mapboy.util.Vector2;
import ca.mapboy.world.World;

public class Mob extends Entity {
	protected Texture[] textures;
	protected int textureIndex;
	public LightSource light;
	public int health, maxHealth;
	protected Colour baseColor;
	
	public Mob(Vector2 position, Colour color, Texture[] textures, int size, int maxHealth) {
		super(position, color, size);
		this.textures = textures;
		this.textureIndex = 1;
		this.maxHealth = maxHealth;
		this.health = maxHealth;
		this.baseColor = color;
	}
	
	public void update(){
		if(countUp){
			counter++;
		}
		
		if(counter > 5){
			color = baseColor;
			countUp = false;
			counter = 0;
		}
	}
	
	private int counter;
	private boolean countUp = false;
	public boolean move(int xa, int ya){
		
		
		if(ya < 0){
			textureIndex = 0;
		}
		
		if(ya > 0){
			textureIndex = 1;
		}
		
		if(xa > 0){
			textureIndex = 2;
		}
		
		if(xa < 0){
			textureIndex = 3;
		}
		
		if(position.x + xa < 0 || position.y + ya < 0 || position.x + xa > ((World.current.getWorldWidth() - 1) * World.current.tileSize) || position.y + ya > ((World.current.getWorldHeight() - 1) * World.current.tileSize)){
			return false;
		}
		
		for(Vector2 tilePos : World.current.getSolidTiles()){
			if(Tile.isColliding(new Vector2(position.x + xa + size/2, position.y + ya + size/2), tilePos.x, tilePos.y, size/2)){
				return false;
			}
		}
		
		position.x += xa;
		position.y += ya;
		
		return true;
	}
	
	public void render(){
		glEnable(GL_BLEND); glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_TEXTURE_2D);
		
		if(color != null) color.bind();
		if(textures != null) textures[textureIndex].bind();
		
		int x = position.x;
		int y = position.y;
		
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
	
	public void hurt(int damage, int xa, int ya){
		health -= damage;
		
		AudioHandler.playSound(AudioHandler.hurt);
		
		color = new Colour(1, 0, 0, 1);
		countUp = true;
		move(xa, ya);
	}
	
	public void addHealth(int health) {
		if(health + this.health > this.maxHealth) this.health = this.maxHealth;
		else this.health += health;
	}

}
