package ca.mapboy.world;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.util.ArrayList;
import java.util.Scanner;

import ca.mapboy.Main;
import ca.mapboy.entity.Entity;
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
	
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private ArrayList<Player> players = new ArrayList<Player>();
	
	public World(int tileSize, int worldWidth, int worldHeight){
		this.tileSize = tileSize;
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
		
		current = this;
	}
	
	public void renderWorld(){
		for(LightSource source : lightSources){
			calculateShadows(source);
		}
		
		for(int y = 0; y < worldHeight; y++){
			for(int x = 0; x < worldWidth; x++){
				Tile tile = Tile.getTileById(mapData.get((y * worldHeight) + x));
				
				tile.render(x * tileSize, y * tileSize);
			}
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

		for (Vector2 block : new Vector2[] { new Vector2(32, 32) }) {
			Vector2[] vertices = {
					new Vector2(block.x, block.y),
					new Vector2(block.x + tileSize, block.y),
					new Vector2(block.x + tileSize, block.y + tileSize),
					new Vector2(block.x, block.y + tileSize),
			};
			
			for (int i = 0; i < vertices.length; i++) {
				Vector2 currentVertex = vertices[i];
				Vector2 nextVertex = vertices[(i + 1) % vertices.length];
				Vector2 edge = Vector2.sub(nextVertex, currentVertex);
				Vector2 normal = new Vector2(edge.y, -edge.x);
				Vector2 lightToCurrent = Vector2.sub(currentVertex, light.location);
				if (Vector2.dot(normal, lightToCurrent) > 0) {
					Vector2 point1 = Vector2.add(currentVertex, (Vector2) Vector2.sub(currentVertex, light.location).scale(800));
					Vector2 point2 = Vector2.add(nextVertex, (Vector2) Vector2.sub(nextVertex, light.location).scale(800));
					glBegin(GL_QUADS); {
						glVertex2f(currentVertex.x, currentVertex.y);
						glVertex2f(point1.x, point1.y);
						glVertex2f(point2.x, point2.y);
						glVertex2f(nextVertex.x, nextVertex.y);
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
	
	public void addEntity(Entity e){
		entities.add(e);
	}
	
	public void addPlayer(Player e){
		players.add(e);
	}
	
	public void addLight(LightSource e){
		lightSources.add(e);
	}
	
}

