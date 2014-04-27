package ca.mapboy.entity;

import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.opengl.Texture;

import ca.mapboy.util.Loader;
import ca.mapboy.world.World;

public class Objectives {
	public static Objectives objectiveHandler;
	
	private Texture[] messages = {
			Loader.getTexture(Objectives.class.getResource("/msg/msg1.png"), 0),
			Loader.getTexture(Objectives.class.getResource("/msg/msg2.png"), 0),
			Loader.getTexture(Objectives.class.getResource("/msg/msg3.png"), 0)
	};
	
	private boolean[] show = {
			false,
			false,
			false,
	};
	
	public Objectives(){
		objectiveHandler = this;
	}
	
	int counter;
	
	public void updateObjectivesAndMessages(){
		counter++;

		int tileSize = World.current.tileSize;
		Player player = World.current.getPlayers().get(0);
		
		if(counter > 60){
			show[0] = true;
		}
		
		if(player.getY() > 9 * tileSize) show[1] = true;
		if(player.getX() > 10 * tileSize) show[2] = true;
	}
	
	public void render(){
		int tileSize = World.current.tileSize;
		
		if(show[0]){
			renderSign(3 * tileSize, 2 * tileSize, messages[0]);
		}
		
		if(show[1]){
			renderSign(3 * tileSize, 7 * tileSize, messages[1]);
		}
		
		if(show[2]){
			renderSign(11 * tileSize, 7 * tileSize, messages[2]);
		}
	}
	
	private void renderSign(int x, int y, Texture texture){
		int width = 256;
		int height = 128;
		
		texture.bind();
		glBegin(GL_TRIANGLES); {
			glTexCoord2f(0, 0);
			glVertex2f(x, y);
			glTexCoord2f(1, 0);
			glVertex2f(x + width, y);
			glTexCoord2f(0, 1);
			glVertex2f(x, y + height);

			glTexCoord2f(1, 0);
			glVertex2f(x + width, y);
			glTexCoord2f(1, 1);
			glVertex2f(x + width, y + height);
			glTexCoord2f(0, 1);
			glVertex2f(x, y + height);
		} glEnd();
	}
}
