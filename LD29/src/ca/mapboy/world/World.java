package ca.mapboy.world;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.util.ArrayList;
import java.util.Scanner;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import ca.mapboy.Main;
import ca.mapboy.entity.Entity;
import ca.mapboy.entity.Mob;
import ca.mapboy.entity.Player;
import ca.mapboy.tile.Tile;
import ca.mapboy.util.LightSource;
import ca.mapboy.util.Vector2;

public class World {
	public static World current;
	
	public int tileSize;
	
	private int worldWidth;
	public int getWorldWidth() {
		return worldWidth;
	}

	public void setWorldWidth(int worldWidth) {
		this.worldWidth = worldWidth;
	}

	public int getWorldHeight() {
		return worldHeight;
	}

	public void setWorldHeight(int worldHeight) {
		this.worldHeight = worldHeight;
	}

	private int worldHeight;
	
	private ArrayList<Integer> mapData = new ArrayList<Integer>();
	
	private ArrayList<LightSource> lightSources = new ArrayList<LightSource>();
	
	private ArrayList<Mob> mobs = new ArrayList<Mob>();
	private ArrayList<Player> players = new ArrayList<Player>();
	
	public World(int tileSize, int worldWidth, int worldHeight){
		this.tileSize = tileSize;
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
		
		current = this;
	}
	
	public void update(){
		for(Mob e : mobs){
			e.update();
		}
		
		for(Player e : players){
			e.update();
		}
	}
	
	public void renderWorld(){
		for(LightSource source : lightSources){
			calculateShadows(source);
		}
		
		for(Player e : players){
			if(e.light != null) calculateShadows(e.light);
		}
		
		for(Mob e : mobs){
			if(e.light != null) calculateShadows(e.light);
		}
		
		for(int y = 0; y < worldHeight; y++){
			for(int x = 0; x < worldWidth; x++){
				Tile tile = Tile.getTileById(mapData.get((y * worldHeight) + x));
				
				tile.render(x * tileSize, y * tileSize);
			}
		}
		
		renderEntities();
	}
	
	public void renderEntities(){
		for(Mob e : mobs){
			e.render();	
		}
		
		for(Player e : players){
			e.render();
		}
	}
	
	public ArrayList<Vector2> getSolidTiles(){
		ArrayList<Vector2> result = new ArrayList<Vector2>();
		
		for(int y = 0; y < worldHeight; y++){
			for(int x = 0; x < worldWidth; x++){
				Tile tile = Tile.getTileById(mapData.get((y * worldHeight) + x));
				
				if(tile.isSolid){
					result.add(new Vector2(x * tileSize, y * tileSize));
				}
			}
		}
		
		return result;
	}
	
	public void calculateShadows(LightSource light){
		
		glColorMask(false, false, false, false);
		glStencilFunc(GL_ALWAYS, 1, 1);
		glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);

		for (Vector2 block : getSolidTiles()) {
			Vector2f[] vertices = {
					new Vector2f(block.x, block.y),
					new Vector2f(block.x, block.y + tileSize),
					new Vector2f(block.x + tileSize, block.y + tileSize),
					new Vector2f(block.x + tileSize, block.y)
			};
			
			for (int i = 0; i < vertices.length; i++) {
				Vector2f currentVertex = vertices[i];
				Vector2f nextVertex = vertices[(i + 1) % vertices.length];
				Vector2f edge = Vector2f.sub(nextVertex, currentVertex, null);
				Vector2f normal = new Vector2f(edge.getY(), -edge.getX());
				Vector2f lightToCurrent = Vector2f.sub(currentVertex, new Vector2f(light.location.x, light.location.y), null);
				if (Vector2f.dot(normal, lightToCurrent) > 0) {
					Vector2f point1 = Vector2f.add(currentVertex, (Vector2f) Vector2f.sub(currentVertex, new Vector2f(light.location.x, light.location.y), null).scale(800), null);
					Vector2f point2 = Vector2f.add(nextVertex, (Vector2f) Vector2f.sub(nextVertex, new Vector2f(light.location.x, light.location.y), null).scale(800), null);
					glBegin(GL_QUADS); {
						glVertex2f(currentVertex.getX(), currentVertex.getY());
						glVertex2f(point1.getX(), point1.getY());
						glVertex2f(point2.getX(), point2.getY());
						glVertex2f(nextVertex.getX(), nextVertex.getY());
					} glEnd();
				}
			}
		}

		glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
		glStencilFunc(GL_EQUAL, 0, 1);
		glColorMask(true, true, true, true);

		glUseProgram(Main.shaderProgram);
		glUniform2f(glGetUniformLocation(Main.shaderProgram, "lightLocation"), light.location.x, Main.HEIGHT - light.location.y);
		glUniform3f(glGetUniformLocation(Main.shaderProgram, "lightColor"), light.red, light.green, light.blue);
		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE);

		glBegin(GL_QUADS); {
			glVertex2f(0, 0);
			glVertex2f(0, worldHeight * tileSize);
			glVertex2f(worldWidth * tileSize, worldHeight * tileSize);
			glVertex2f(worldWidth * tileSize, 0);
		} glEnd();

		glDisable(GL_BLEND);
		glUseProgram(0);
		
		glClear(GL_STENCIL_BUFFER_BIT);
	}
	
	public void loadMapFromFile(String filePath){
		Scanner scanner = new Scanner(World.class.getResourceAsStream(filePath));
		mapData.clear();
		
		while(scanner.hasNextInt()){
			mapData.add(scanner.nextInt());
		}
		
		scanner.close();
	}
	
	public void addMob(Mob e){
		mobs.add(e);
	}
	
	public void addPlayer(Player e){
		players.add(e);
	}
	
	public void addLight(LightSource e){
		lightSources.add(e);
	}
	
}

