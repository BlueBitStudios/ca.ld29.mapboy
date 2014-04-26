package ca.mapboy.gui;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;

import ca.mapboy.Main;
import ca.mapboy.util.Colour;
import ca.mapboy.util.Loader;

public class Inventory {
	public boolean inventoryOpened = false;
	
	public Texture invButton = Loader.getTexture(Inventory.class.getResource("/inventory.png"), 0);
	
	public void render(){
		if(inventoryOpened){
			renderInventory();
		}else{
			int x = Main.WIDTH - 256;
			int y = Main.HEIGHT - 32;
			
			if(Mouse.getX() > Main.WIDTH - 250 && (Main.HEIGHT - Mouse.getY()) > Main.HEIGHT - 75){
				new Colour(1, 1, 1, 1).bind();
			}else{
				new Colour(0.6, 0.6, 0.6, 0.6).bind();
			}
			
			invButton.bind();
			glBegin(GL_TRIANGLES); {
				glTexCoord2f(0, 0);
				glVertex2f(x, y);
				glTexCoord2f(1, 0);
				glVertex2f(x + 256, y);
				glTexCoord2f(0, 1);
				glVertex2f(x, y + 32);

				glTexCoord2f(1, 0);
				glVertex2f(x + 256, y);
				glTexCoord2f(1, 1);
				glVertex2f(x + 256, y + 32);
				glTexCoord2f(0, 1);
				glVertex2f(x, y + 32);
			} glEnd();
		}
	}

	private void renderInventory() {
		
	}
}
