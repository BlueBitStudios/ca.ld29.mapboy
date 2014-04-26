package ca.mapboy;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_STENCIL_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
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
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import ca.mapboy.entity.Player;
import ca.mapboy.tile.BaseCollidableTile;
import ca.mapboy.tile.BaseTile;
import ca.mapboy.tile.BaseVoidTile;
import ca.mapboy.tile.Tile;
import ca.mapboy.util.Loader;
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
			Display.create(new PixelFormat(0, 16, 1));
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
		
		cleanup();
	}
	
	public void cleanup(){
		Display.destroy();
		System.exit(0);
	}
	
	World map;
	
	public void init(){
		try {
			Tile.tileIds.add(new BaseVoidTile(0, null, null));
			Tile.tileIds.add(new BaseCollidableTile(1, null, Loader.getTexture(Main.class.getResource("/block.png"), 0), true));
			Tile.tileIds.add(new BaseCollidableTile(2, null, TextureLoader.getTexture("PNG", Main.class.getResourceAsStream("/glass.png")), false));
			Tile.tileIds.add(new BaseTile(3, null, TextureLoader.getTexture("PNG", Main.class.getResourceAsStream("/grass.png")), false));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		map = new World(64, 10, 10);
		map.loadMapFromFile("/map.txt");
		
		try {
			Texture[] playerTextures = {
				TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/char/char4.png")),
				TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/char/char3.png")),
				TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/char/char2.png")),
				TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/char/char1.png")),
			};
			
			map.addPlayer(new Player(new Vector2(0, 0), null, playerTextures));
		} catch (IOException e1){
			e1.printStackTrace();
		}
		
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
		glOrtho(0, WIDTH, HEIGHT, 0, -1, 1);
		glMatrixMode(GL_PROJECTION);
		
		glEnable(GL_STENCIL_TEST);
		glClearColor(0, 0, 0, 0);
	}
	
	public void update(){
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			cleanup();
		}
		
		map.update();
	}
	
	public void render(){
		glClear(GL_COLOR_BUFFER_BIT);
		glClearColor(0, 0, 0, 1);
		
		map.renderWorld();
	}
}

