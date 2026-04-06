package darthvader.mainmoving;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import DataStructures.ManageFolder;

public class TestManagerLogger {

	public static void main(String[] args) {
    	File file = new File("C:\\Users\\itay5\\OneDrive\\Pictures\\MainLogger");
    	//File file = new File("C:\\Users\\itay5\\OneDrive\\מסמכים\\output\\replicate");
		
    	/*ManageFolder manage = new ManageFolder(file.getAbsolutePath());
		manage.refreshIconsOfFolder();*/
    	
    	
    	
    	File inputFolder = new File(file, "input");
		
		// create a custom movie name using now()
		String customMovieName = "Movie_" + System.currentTimeMillis() + "";
		createDummyInputFile(inputFolder, customMovieName + ".mkv");
		
		ManageFolder manage = new ManageFolder(file.getAbsolutePath());
		// simulate moving the file to the input folder, and then start the logger
		manage.moveFilesFromInput();
		
		// create Poster dummy file
		createPoster(inputFolder, customMovieName);
		
		ManageFolder manage2 = new ManageFolder(file.getAbsolutePath());
		System.out.println("Maps before moving files from input: " + manage2.movieMap + " " + manage2.TVMap + " " + manage2.unkownMediaMap);
		// simulate moving the file to the input folder, and then start the logger
		manage2.moveFilesFromInput();
	}
	
	public static void createPoster(File inputFolder, String customMovieName) {
		// create Poster dummy file
		File posterFile = createDummyInputFile(inputFolder, customMovieName + " - Poster" + ".jpg");
		// draw a stick figure on the poster file
		try {
			BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = image.createGraphics();
			g2d.setColor(Color.WHITE);
			g2d.fillRect(0, 0, 200, 200);
			g2d.setColor(Color.BLACK);
			g2d.drawOval(90, 50, 20, 20); // head
			g2d.drawLine(100, 70, 100, 120); // body
			g2d.drawLine(100, 80, 80, 100); // left arm
			g2d.drawLine(100, 80, 120, 100); // right arm
			g2d.drawLine(100, 120, 80, 150); // left leg
			g2d.drawLine(100, 120, 120, 150); // right leg
			g2d.dispose();
			ImageIO.write(image, "jpg", posterFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static File createDummyInputFile(File inputFolder, String customMovieName) {
		File dummyInputFile = new File(inputFolder, customMovieName);
		// mkdir the dummy input file
		dummyInputFile.getParentFile().mkdirs();
		try {
			dummyInputFile.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dummyInputFile;
	}
}