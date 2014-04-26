package ca.mapboy.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Loader {
	public static Texture getTexture(URL url, int rotation){
		BufferedImage image;
		try {
			image = ImageIO.read(url);
			
			int width = image.getWidth();
			int height = image.getHeight();
			
			BufferedImage returnImage = new BufferedImage(width, height, image.getType());
			
			switch(rotation){
			case 0:
				returnImage = image;
				break;
			case 1:
				for( int x = 0; x < width; x++ ) {
					for( int y = 0; y < height; y++ ) {
						returnImage.setRGB(height - y - 1, x, image.getRGB( x, y  )  );
					}
				}
				break;
			case 2:
				for( int x = 0; x < width; x++ ) {
					for( int y = 0; y < height; y++ ) {
						returnImage.setRGB( width - x - 1, height - y - 1, image.getRGB( x, y  )  );
					}
				}
				break;
			case 3:
				for( int x = 0; x < width; x++ ) {
					for( int y = 0; y < height; y++ ) {
						returnImage.setRGB(y, width - x - 1, image.getRGB( x, y  )  );
					}
				}
			}
			
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(returnImage, "png", os);
			InputStream is = new ByteArrayInputStream(os.toByteArray());
			return TextureLoader.getTexture("PNG", is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
