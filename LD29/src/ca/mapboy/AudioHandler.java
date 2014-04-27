package ca.mapboy;

import java.io.IOException;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;

public class AudioHandler {
	public Audio mainMenuMusic;
	public Audio battleMusic;
	public Audio finalMusic;
	
	public void init(){
		try {
			mainMenuMusic = AudioLoader.getAudio("WAV", Audio.class.getResourceAsStream("/audio/mainmenu.wav"));
			battleMusic = AudioLoader.getAudio("WAV", Audio.class.getResourceAsStream("/audio/battle.wav"));
			finalMusic = AudioLoader.getAudio("WAV", Audio.class.getResourceAsStream("/audio/final.wav"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void update(){
		SoundStore.get().poll(0);
	}
}
