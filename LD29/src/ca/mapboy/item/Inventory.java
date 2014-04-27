package ca.mapboy.item;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4d;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;

import ca.mapboy.Main;
import ca.mapboy.util.Input;

public class Inventory {
	public ArrayList<Item> inventory = new ArrayList<Item>();
	private int width, height;
	private Texture inventorySlot;
	private Texture buttonTexture;
	
	public Inventory(int width, int height, Texture inventorySlot, Texture buttonTexture){
		this.width = width;
		this.height = height;
		this.inventorySlot = inventorySlot;
		this.buttonTexture = buttonTexture;
		
		for(int i = 0; i < 20; i++) inventory.add(Item.items.get(0));
	}

	float xOff;
	float yOff;
	
	public void render(){
		glEnable(GL_BLEND); glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			isOpen = false;
		}
		
		xOff = 0 - Main.px;
		yOff = 0 + Main.py;
		
		glBindTexture(GL_TEXTURE_2D, 0);
		glColor4d(0, 0, 0, 0.8);
		glBegin(GL_QUADS); {
			glVertex2f(xOff, yOff);
			glVertex2f(xOff + Main.WIDTH, yOff);
			glVertex2f(xOff + Main.WIDTH, yOff + Main.HEIGHT);
			glVertex2f(xOff, yOff + Main.HEIGHT);
		} glEnd();
		glColor4d(1, 1, 1, 1);
		
		for(int xx = 0; xx < width; xx++){
			for(int yy = 0; yy < height; yy++){
				boolean selected = false;
				int mx = Mouse.getX();
				int my = Main.HEIGHT - Mouse.getY();
				
				if(mx > xx * 48 + 16 && my > yy * 48 + 16 && mx < xx * 48 + 48 && my < yy * 48 + 48){
					if(Input.checkForClick(0)){
						if(inventory.size() > 0){
							Item item = inventory.get((yy * width) + xx);
							if(item != null){
								item.action();
							}
						}
						
					}
					selected = true;
				}else{
					selected = false;
				}
				
				renderSlot(xx * 48 + 16, yy * 48 + 16, selected);
				renderItem(xx, yy);
			}
		}
	}
	
	public boolean addItem(Item item){
		if(inventory.size() > width * height) return false;
		
		inventory.add(item);
		return true;
	}
	
	public boolean isOpen;
	
	public void renderItem(int x, int y){
		if(inventory.size() > (y * width) + x){
		Item item = inventory.get((y * width) + x);
			if(item != null) {
				item.render(x * 48 + 16 + xOff + 4, y * 48 + 16 + yOff + 4, 24);
			}
		}
	}
	
	public void renderButton(){
		glEnable(GL_BLEND); glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		int mx = Mouse.getX();
		int my = Main.HEIGHT - Mouse.getY();
		
		if(mx < 128 && my < 32){
			if(Input.checkForClick(0)){
				isOpen = true;
			}
			
			glColor4d(1, 1, 1, 1);
		}else{
			glColor4d(0.6, 0.6, 0.6, 0.6);
		}
		buttonTexture.bind();
		
		float ax = 0 - Main.px;
		float ay = 0 + Main.py;
		
		glBegin(GL_TRIANGLES); {
			glTexCoord2f(0, 0);
			glVertex2f(ax, ay);
			glTexCoord2f(1, 0);
			glVertex2f(ax + 128, ay);
			glTexCoord2f(0, 1);
			glVertex2f(ax, ay + 32);
			
			glTexCoord2f(1, 0);
			glVertex2f(ax + 128, ay);
			glTexCoord2f(1, 1);
			glVertex2f(ax + 128, ay + 32);
			glTexCoord2f(0, 1);
			glVertex2f(ax + 0, ay + 32);
		} glEnd();
	}
	
	public void renderSlot(int x, int y, boolean highlighted){
		int ax = x;
		int ay = y;
		
		ax -= Main.px;
		ay += Main.py;
		
		if(highlighted){
			glColor4d(1, 1, 1, 1);
		}else{
			glColor4d(0.6, 0.6, 0.6, 1);
		}
		inventorySlot.bind();
		glBegin(GL_TRIANGLES); {
			glTexCoord2f(0, 0);
			glVertex2f(ax, ay);
			glTexCoord2f(1, 0);
			glVertex2f(ax + 32, ay);
			glTexCoord2f(0, 1);
			glVertex2f(ax, ay + 32);
			
			glTexCoord2f(1, 0);
			glVertex2f(ax + 32, ay);
			glTexCoord2f(1, 1);
			glVertex2f(ax + 32, ay + 32);
			glTexCoord2f(0, 1);
			glVertex2f(ax, ay + 32);
		} glEnd();
	}
}
