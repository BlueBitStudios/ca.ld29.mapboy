package ca.mapboy.util;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Input {
	public static boolean checkForClick(int button){
		while(Mouse.next()){
			if(Mouse.getEventButtonState()){
				if(Mouse.getEventButton() == button) return true;
			}
		}
		
		return false;
	}
	
	public static boolean checkForPress(int button){
		while(Keyboard.next()){
			if(Keyboard.getEventKeyState()){
				if(Keyboard.getEventKey() == button) return true;
			}
		}
		
		return false;
	}
}
