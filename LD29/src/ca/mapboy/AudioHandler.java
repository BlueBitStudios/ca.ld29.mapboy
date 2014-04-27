package ca.mapboy;

import java.io.IOException;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;

public class AudioHandler {
	public static Audio mainMenuMusic;
	public static Audio battleMusic;
	public static Audio finalMusic;
	public static Audio stepSound;
	
	public void init(){
		try {
			mainMenuMusic = AudioLoader.getAudio("WAV", Audio.class.getResourceAsStream("/audio/mainmenu.wav"));
			battleMusic = AudioLoader.getAudio("WAV", Audio.class.getResourceAsStream("/audio/battle.wav"));
			finalMusic = AudioLoader.getAudio("WAV", Audio.class.getResourceAsStream("/audio/final.wav"));
			stepSound = AudioLoader.getAudio("WAV", Audio.class.getResourceAsStream("/audio/step.wav"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void playSound(Audio audio){
		audio.playAsSoundEffect(1, 1, false);
	}
	
	public static void playLoopingMusic(Audio audio){
		audio.playAsMusic(1, 1, true);
	}
	
	
	public void update(){
		SoundStore.get().poll(0);
	}
}
