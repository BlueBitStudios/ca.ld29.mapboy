package ca.mapboy;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;

import ca.mapboy.util.Input;
import ca.mapboy.util.Loader;

public class MainMenu {
	public static boolean open = true;
	
	public int page = 0;
	
	Texture[] textures = {
			Loader.getTexture(MainMenu.class.getResource("/title.png"), 0),
			Loader.getTexture(MainMenu.class.getResource("/play.png"), 0),
	};
	
	public MainMenu(){
	}
	
	public void update(){
		
	}
	
	public void render(){
		glEnable(GL_BLEND); glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_TEXTURE_2D);
		switch(page){
		case 0:
			renderMainPage();
			break;
		case 1:
			renderHowToPage();
			break;
		}
	}
	
	public void renderMainPage(){
		int x = 40;
		int y = Main.HEIGHT / 2 - 128;
		glColor4d(1, 1, 1, 1);
		textures[0].bind();
		glBegin(GL_TRIANGLES); {
			glTexCoord2f(0, 0);
			glVertex2i(x, y);
			glTexCoord2f(1, 0);
			glVertex2i(x + 256, y);
			glTexCoord2f(0, 1);
			glVertex2i(x, y + 256);

			glTexCoord2f(1, 0);
			glVertex2i(x + 256, y);
			glTexCoord2f(1, 1);
			glVertex2i(x + 256, y + 256);
			glTexCoord2f(0, 1);
			glVertex2i(x, y + 256);
		} glEnd();
		
		x = Main.WIDTH - 296;
		y = Main.HEIGHT / 2 - 32;
		
		int mx = Mouse.getX();
		int my = Main.HEIGHT - Mouse.getY();
		
		if(mx > x && my > y && mx < x + 256 && my < y + 64){
			if(Input.checkForClick(0)){
				open = false;
			}
			
			glColor4d(1, 1, 1, 1);
		}else{
			glColor4d(0.6, 0.6, 0.6, 1);
		}
		textures[1].bind();
		glBegin(GL_TRIANGLES); {
			glTexCoord2f(0, 0);
			glVertex2i(x, y);
			glTexCoord2f(1, 0);
			glVertex2i(x + 256, y);
			glTexCoord2f(0, 1);
			glVertex2i(x, y + 64);

			glTexCoord2f(1, 0);
			glVertex2i(x + 256, y);
			glTexCoord2f(1, 1);
			glVertex2i(x + 256, y + 64);
			glTexCoord2f(0, 1);
			glVertex2i(x, y + 64);
		} glEnd();
	}
	
	public void renderHowToPage(){
		
	}
}
