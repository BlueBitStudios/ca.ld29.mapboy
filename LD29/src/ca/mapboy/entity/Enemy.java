package ca.mapboy.entity;

import java.util.ArrayList;

import org.newdawn.slick.opengl.Texture;

import ca.mapboy.item.Item;
import ca.mapboy.item.WorldItem;
import ca.mapboy.util.Colour;
import ca.mapboy.util.Vector2;
import ca.mapboy.world.Node;
import ca.mapboy.world.World;

public class Enemy extends Mob {
	private ArrayList<Node> path = null;
	private Mob target;
	
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
	
	int attackTimer;
	
	public void attemptAttack(){
		if(target != null){
			System.out.println(Vector2.distance(position, target.position));
			if(Vector2.distance(position, target.position) <= 64){
				switch(textureIndex){
				case 0:
					target.hurt(1, 0, -30);
					break;
				case 1:
					target.hurt(1, 0, 30);
					break;
				case 2:
					target.hurt(1, 30, 0);
					break;
				case 3:
					target.hurt(1, -30, 0);
					break;
				}
			}
		}
	}
	
	public void die(){
		World.current.removeMob(this);
		
		World.current.addItem(new WorldItem(position.x, position.y, Item.items.get(0)));
	}
	
	public void update(){
		super.update();
		
		if(health <= 0){
			die();
		}
		
		attackTimer++;
		
		if(attackTimer % 150 == 0){
			attemptAttack();
		}
		ArrayList<Player> nearMobs = World.current.getPlayersInRadius(this, World.current.tileSize * 5);
		
		if(nearMobs.size() > 0){
			if(target == null) target = nearMobs.get(0);
		}
		
		if(target != null) moveToTarget();
	}
}
