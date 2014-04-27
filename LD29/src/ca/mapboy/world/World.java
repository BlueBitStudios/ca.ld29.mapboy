package ca.mapboy.world;

import static org.lwjgl.opengl.GL11.GL_ALWAYS;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_EQUAL;
import static org.lwjgl.opengl.GL11.GL_KEEP;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_REPLACE;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColorMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glStencilFunc;
import static org.lwjgl.opengl.GL11.glStencilOp;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import org.newdawn.slick.opengl.Texture;

import ca.mapboy.Main;
import ca.mapboy.entity.Entity;
import ca.mapboy.entity.Mob;
import ca.mapboy.entity.Player;
import ca.mapboy.item.Item;
import ca.mapboy.item.WorldItem;
import ca.mapboy.tile.Tile;
import ca.mapboy.util.LightSource;
import ca.mapboy.util.Loader;
import ca.mapboy.util.Vector2;

public class World {
	public static World current;
	
	public int tileSize;
	
	private Comparator<Node> nodeSorter = new Comparator<Node>() {
		public int compare(Node n0, Node n1) {
			if(n1.fCost < n0.fCost) return +1;
			if(n1.fCost > n0.fCost) return -1;
			return 0;
		}
	};
	
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
	
	private ArrayList<WorldItem> items = new ArrayList<WorldItem>();
	
	public ArrayList<Mob> mobs = new ArrayList<Mob>();
	private ArrayList<Player> players = new ArrayList<Player>();
	
	public World(int tileSize, int worldWidth, int worldHeight){
		this.tileSize = tileSize;
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
		
		current = this;
	}
	
	public static boolean inInventory = false;
	
	public void update(){
		inInventory = players.get(0).inventory.isOpen;
		
		if(!inInventory){
			updateMobs();
		}
	}
	
	public void updateMobs(){
		for(int i = 0; i < items.size(); i++){
			items.get(i).update();
		}
		
		for(int i = 0; i < mobs.size(); i++){
			mobs.get(i).update();
		}
		
		for(Player e : players){
			e.update();
		}
	}
	
	public void renderLights(){
		for(LightSource source : lightSources){
			calculateShadows(source);
		}
		
		for(Player e : players){
			if(e.light != null) calculateShadows(e.light);
		}
		
		for(Mob e : mobs){
			if(e.light != null) calculateShadows(e.light);
		}
	}
	
	public void renderInventory(){
		if(inInventory){
			players.get(0).inventory.render();
		}else{
			players.get(0).inventory.renderButton();
		}
	}
	
	public void renderWorld(){
		
		
		for(int y = 0; y < worldHeight; y++){
			for(int x = 0; x < worldWidth; x++){
				Tile tile = Tile.getTileById(mapData.get((y * worldHeight) + x));
				
				tile.render(x * tileSize, y * tileSize);
			}
		}
		
		renderLights();
		renderSides();

		renderEntities();
		
	}
	
	Texture[] sideTextures = {
			Loader.getTexture(World.class.getResource("/edge.png"), 0),
			Loader.getTexture(World.class.getResource("/edge.png"), 1),
			Loader.getTexture(World.class.getResource("/edge.png"), 2),
			Loader.getTexture(World.class.getResource("/edge.png"), 3),
			Loader.getTexture(World.class.getResource("/corner.png"), 0),
			Loader.getTexture(World.class.getResource("/corner.png"), 1),
			Loader.getTexture(World.class.getResource("/corner.png"), 2),
			Loader.getTexture(World.class.getResource("/corner.png"), 3),
			
	};
	public void renderSides(){
		for(int i = 0; i < worldHeight - 2; i++){
			renderTileSize(sideTextures[0], 0, i + 1);
		}
		
		for(int i = 0; i < worldWidth - 2; i++){
			renderTileSize(sideTextures[1], i + 1, 0);
		}
		
		for(int i = 0; i < worldHeight - 2; i++){
			renderTileSize(sideTextures[2], worldWidth -1, i + 1);
		}
		
		for(int i = 0; i < worldWidth - 2; i++){
			renderTileSize(sideTextures[3], i + 1, worldHeight -1);
		}
		
		renderTileSize(sideTextures[4], 0, 0);
		renderTileSize(sideTextures[5], worldWidth -1, 0);
		renderTileSize(sideTextures[6], worldWidth -1, worldHeight -1);
		renderTileSize(sideTextures[7], 0, worldHeight -1);
	}
	
	public void renderTileSize(Texture texture, int x, int y){
		glEnable(GL_BLEND); glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_TEXTURE_2D);
		
		texture.bind();
		
		x *= tileSize;
		y *= tileSize;
		
		glBegin(GL_TRIANGLES); {
			glTexCoord2f(0, 0);
			glVertex2f(x, y);
			glTexCoord2f(1, 0);
			glVertex2f(x + tileSize, y);
			glTexCoord2f(0, 1);
			glVertex2f(x, y + tileSize);

			glTexCoord2f(1, 0);
			glVertex2f(x + tileSize, y);
			glTexCoord2f(1, 1);
			glVertex2f(x + tileSize, y + tileSize);
			glTexCoord2f(0, 1);
			glVertex2f(x, y + tileSize);
		} glEnd();
	}
	
	public void renderEntities(){
		for(int i = 0; i < items.size(); i++){
			items.get(i).render();
		}
		
		for(Mob e : mobs){
			e.render();	
		}
		
		for(Player e : players){
			e.render();
			renderInventory();
			e.renderHealth();
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
	
	public ArrayList<Vector2> getOpaqueTiles(){
		ArrayList<Vector2> result = new ArrayList<Vector2>();
		
		for(int y = 0; y < worldHeight; y++){
			for(int x = 0; x < worldWidth; x++){
				Tile tile = Tile.getTileById(mapData.get((y * worldHeight) + x));
				
				if(tile.isOpaque){
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

		for (Vector2 block : getOpaqueTiles()) {
			Vector2[] vertices = {
					new Vector2(block.x, block.y),
					new Vector2(block.x, block.y + tileSize),
					new Vector2(block.x + tileSize, block.y + tileSize),
					new Vector2(block.x + tileSize, block.y)
			};
			
			for (int i = 0; i < vertices.length; i++) {
				Vector2 currentVertex = vertices[i];
				Vector2 nextVertex = vertices[(i + 1) % vertices.length];
				Vector2 edge = Vector2.sub(nextVertex, currentVertex);
				Vector2 normal = new Vector2(edge.y, -edge.x);
				Vector2 lightToCurrent = Vector2.sub(currentVertex, new Vector2(light.location.x, light.location.y));
				if (Vector2.dot(normal, lightToCurrent) > 0) {
					Vector2 point1 = Vector2.add(currentVertex, Vector2.sub(currentVertex, new Vector2(light.location.x, light.location.y)).scale(800));
					Vector2 point2 = Vector2.add(nextVertex, Vector2.sub(nextVertex, new Vector2(light.location.x, light.location.y)).scale(800));
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
		glUniform2f(glGetUniformLocation(Main.shaderProgram, "lightLocation"), light.location.x + Main.px, Main.HEIGHT - light.location.y + Main.py);
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
	
	public void removeMob(Mob e){
		mobs.remove  (e);
	}
	
	public void removePlayer(Player e){
		players.remove(e);
	}
	
	public void addItem(WorldItem e){
		items.add(e);
	}
	
	public void removeItem(WorldItem e){
		items.remove(e);
	}
	
	public void addPlayer(Player e){
		players.add(e);
	}
	
	public void addLight(LightSource e){
		lightSources.add(e);
	}
	
	public ArrayList<Player> getPlayers(){
		return players;
	}
	
	public ArrayList<Mob> getMobsInRadius(Entity e, int radius){
		ArrayList<Mob> result = new ArrayList<Mob>();
		float ex = e.getX();
		float ey = e.getY();
		for(int i = 0; i < mobs.size(); i++){
			Mob entity = mobs.get(i);
			float x = entity.getX();
			float y = entity.getY();
			float dx = Math.abs(x - ex);
			float dy = Math.abs(y - ey);
			double distance = Math.sqrt((dx * dx) + (dy * dy));
			if(distance <= radius) result.add(entity);
		}
		
		return result;
	}
	
	public ArrayList<Player> getPlayersInRadius(Entity e, int radius){
		ArrayList<Player> result = new ArrayList<Player>();
		float ex = e.getX();
		float ey = e.getY();
		for(int i = 0; i < players.size(); i++){
			Player entity = players.get(i);
			float x = entity.getX();
			float y = entity.getY();
			float dx = Math.abs(x - ex);
			float dy = Math.abs(y - ey);
			double distance = Math.sqrt((dx * dx) + (dy * dy));
			if(distance <= radius) result.add(entity);
		}
		
		return result;
	}
	
	public ArrayList<Node> findPath(Vector2 start, Vector2 goal){
		ArrayList<Node> openList = new ArrayList<Node>();
		ArrayList<Node> closedList = new ArrayList<Node>();
		Node currentNode = new Node(start, null, 0, Vector2.distance(start, goal));
		openList.add(currentNode);
		
		while(openList.size() > 0) {
			Collections.sort(openList, nodeSorter);
			currentNode = openList.get(0);
			
			if(currentNode.tile.x == goal.x && currentNode.tile.y == goal.y){
				ArrayList<Node> path = new ArrayList<Node>();
				while(currentNode.parent != null){
					path.add(currentNode);
					currentNode = currentNode.parent;
				}
				
				openList.clear();
				closedList.clear();
				return path;
			}
			
			openList.remove(currentNode);
			closedList.add(currentNode);
			
			for(int i = 0; i < 4; i++){
				if(i == 4) continue;
				
				int x = (int) currentNode.tile.x;
				int y = (int) currentNode.tile.y;
				int xi = 0, yi = 0;
				
				switch(i){
				case 0:
					yi = +1;
					xi = 0;
					break;
				case 1:
					yi = 0;
					xi = -1;
					break;
				case 2:
					yi = -1;
					xi = 0;
					break;
				case 3:
					yi = 0;
					xi = 1;
					break;
				}
				
				Tile at = getTile((x + xi), (y + yi));
				if(at == null) continue;
				if(at.isSolid) continue;
				
				Vector2 a = new Vector2(x + xi, y + yi);
				double gCost = currentNode.gCost + Vector2.distance(currentNode.tile, a);
				double hCost = Vector2.distance(a, goal);
				
				Node node = new Node(a, currentNode, gCost, hCost);
				
				if(vecInList(closedList, a) && gCost >= node.gCost) continue;
				if(!vecInList(openList, a) || gCost < currentNode.gCost) openList.add(node);
			}
		}
		
		closedList.clear();
		return null;
	}
	
	public Tile getTile(int x, int y){
		if(x < 0 || y < 0 || x >= worldWidth || y >= worldHeight) return null;
		
		return Tile.getTileById(mapData.get((y * worldWidth) + x));
	}
	
	public int getTileID(int x, int y){
		if(x < 0 || y < 0 || x >= worldWidth || y >= worldHeight) return -1;
		
		return mapData.get((y * worldWidth) + x);
	}
	
	private boolean vecInList(ArrayList<Node> list, Vector2 vector){
		for(Node n : list) {
			if(n.tile.x == vector.x && n.tile.y == vector.y) return true;
		}
		
		return false;
	}
	
}

