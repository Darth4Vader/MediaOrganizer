package JavaFXInterface.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;

import OtherUtilities.ImageUtils;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class JavaFXImageUtils {
	
	public static Image getImageResource(Class<?> currentClass, String imagePath) {
		try {
			return SwingFXUtils.toFXImage((BufferedImage) ImageUtils.getImageResource(currentClass, imagePath), null);
		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
