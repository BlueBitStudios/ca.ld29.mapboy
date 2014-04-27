package ca.mapboy.entity;

import java.util.ArrayList;

import org.newdawn.slick.opengl.Texture;

import ca.mapboy.util.Colour;
import ca.mapboy.util.Vector2;
import ca.mapboy.world.Node;
import ca.mapboy.world.World;

public class Enemy extends Mob {
	private ArrayList<Node> path = null;
	private Entity target;
	
	public Enemy(Vector2 position, Colour color, Texture[] textures) {
		super(position, color, textures, 48, 10);
	}
	
	public void moveToTarget(){
		int px = target.getX();
		int py = target.getY();
		
		int tileSize = World.current.tileSize;
		
		Vector2 start = new Vector2(getX() / tileSize, getY() / tileSize);
		Vector2 goal = new Vector2(px / tileSize, py / tileSize);
		
		path = World.current.findPath(start, goal);
		
		
		if(path != null){
			if(path.size() > 0){
				
				Vector2 vec = path.get(path.size() - 1).tile;
				if(position.x < (vec.x * tileSize)){
					move(1, 0);
				}
				
				if(position.x > (vec.x * tileSize)){
					move(-1, 0);
				}
				
				if(position.y < (vec.y * tileSize)){
					move(0, 1);
				}
				
				if(position.y > (vec.y * tileSize)){
					move(0, -1);
				}
				
			}
		}
	}
	
	public void update(){
		ArrayList<Player> nearMobs = World.current.getPlayersInRadius(this, World.current.tileSize * 4);
		
		if(nearMobs.size() > 0){
			if(target == null) target = nearMobs.get(0);
			moveToTarget();
		}else{
			target = null;
		}
	}
}
