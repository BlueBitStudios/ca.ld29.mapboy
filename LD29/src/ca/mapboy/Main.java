package ca.mapboy;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import ca.mapboy.tile.BaseCollidableTile;
import ca.mapboy.tile.BaseVoidTile;
import ca.mapboy.tile.Tile;
import ca.mapboy.util.Colour;
import ca.mapboy.util.LightSource;
import ca.mapboy.util.Vector2;
import ca.mapboy.world.World;

public class Main {
	public static final int WIDTH = 640;
	public static final int HEIGHT = 360;
	public static final String TITLE = "LD29";
	
	public static void main(String args[]) {
		new Main();
	}
	
	/* ------------ */
	
	public static int shaderProgram;
	private int fragmentShader;
	
	public Main(){
		try{
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setTitle(TITLE);
			Display.create();
		}catch(LWJGLException e){
			e.printStackTrace();
		}
		
		init();
		
		while(!Display.isCloseRequested()){
			update();
			render();
			
			Display.update();
			Display.sync(60);
		}
	}
	
	World map;
	
	public void init(){
		Tile.tileIds.add(new BaseVoidTile(0, null, null));
		Tile.tileIds.add(new BaseCollidableTile(1, new Colour(0.8, 0.8, 0.8, 1), null));
		
		map = new World(16, 10, 10);
		map.loadMapFromFile("/map.txt");
		
		map.addLight(new LightSource(new Vector2(0, 0), 4, 8, 16));
		
		shaderProgram = glCreateProgram();
		fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		StringBuilder fragmentShaderSource = new StringBuilder();

		try {
			String line;
			BufferedReader reader = new BufferedReader(new FileReader("res/lightshader.fs"));
			while ((line = reader.readLine()) != null) {
				fragmentShaderSource.append(line).append("\n");
			}
			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		glShaderSource(fragmentShader, fragmentShaderSource);
		glCompileShader(fragmentShader);
		if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Fragment shader not compiled!");
		}

		glAttachShader(shaderProgram, fragmentShader);
		glLinkProgram(shaderProgram);
		glValidateProgram(shaderProgram);

		
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glOrtho(0, WIDTH / 3, HEIGHT / 3, 0, -1, 1);
		glMatrixMode(GL_PROJECTION);
	}
	
	public void update(){
		
	}
	
	public void render(){
		glClear(GL_COLOR_BUFFER_BIT);
		glClearColor(0, 0, 0, 1);
		
		map.renderWorld();
	}
}

