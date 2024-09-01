package JavaFXInterface;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

import FileUtilities.FilesUtils;
import OtherUtilities.ImageUtils;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

public class AppUtils {

	public static WritableImage getImageOfFile(File file) {
		File imageFile = null;
		if(file.isDirectory())
			imageFile = FilesUtils.getFileLogo(file);
		java.awt.Image image = null;
		if(imageFile != null)
			image = ImageUtils.loadImage(imageFile.getPath());
		else {
			Icon icon = FileSystemView.getFileSystemView().getSystemIcon(file);
			if(icon instanceof ImageIcon) {
				image = ImageUtils.toBufferedImage(((ImageIcon) icon).getImage());
			}
			else if(icon != null) { 
				image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = ((BufferedImage) image).createGraphics(); icon.paintIcon(null, g, 0, 0);
				g.dispose();
			}
		}
		System.out.println(image.getClass());
		if(image instanceof BufferedImage)
			return SwingFXUtils.toFXImage((BufferedImage) image, null);
		return null;
	}

}
