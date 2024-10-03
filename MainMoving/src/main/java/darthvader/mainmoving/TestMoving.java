package darthvader.mainmoving;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import DataStructures.FileInfoType;
import DataStructures.FolderInfo;
import DataStructures.FileInfoType.FolderType;
import DataStructures.ManageFolder;
import DataStructures.NameInfo;
import FileUtilities.FileFormats;
import FileUtilities.FileFormats.FileFormat;

public class TestMoving {
	
	public static void main(String[] args) throws IOException {
		/*File folder = new File("F:\\Star Wars");
		File destFolder = new File("C:\\Users\\itay5\\OneDrive\\Pictures\\New folder (2)\\Main2024");
		copyOnlyFolders(folder, destFolder);*/
		
    	File mainFolder = new File("C:\\Users\\itay5\\OneDrive\\Pictures\\Main2024");
    	
		
		
    	File file = new File(mainFolder, "Star Wars");
		
		ManageFolder manage = new ManageFolder(mainFolder.getAbsolutePath(), Arrays.asList(mainFolder.listFiles()));
		
		manage.toAddInsideMap(file);
		
		manage.createIconToFolder();
    	
    	
    	//System.out.println(new NameInfo("The Bad Batch (2021) Season 1 S01 (1080p DSNP WEB-DL x265 HEVC 10bit EAC3 5.1 YOGI)").getName());
    	
		
		/*
		File mainFolder = new File("C:\\Users\\itay5\\OneDrive\\Pictures\\Main");
    	ManageFolder manage = new ManageFolder(mainFolder.getAbsolutePath(), Arrays.asList(mainFolder.listFiles()));
    	
    	File poster = new File("C:\\Users\\itay5\\OneDrive\\Pictures\\Main\\W-Output\\TV\\Ahsoka (2024)\\Ahsoka (2024) -Extras\\Ahsoka (2024) - Posters\\Ahsoka (2024).jpg");
    	
    	File ahsokaFolder = new File("C:\\Users\\itay5\\OneDrive\\Pictures\\Main\\W-Output\\TV\\Ahsoka (2024)");
    	
    	//manage.setIconToFolder(ahsokaFolder, poster);
    	
    	File episode2 = new File("C:\\Users\\itay5\\OneDrive\\Pictures\\Main\\W-Output\\TV\\Ahsoka (2024)\\Ahsoka (2024) - E02");
    	
    	//manage.setIconToFolder(episode2, poster);
    	
    	manage.createIconToFolder();
    	
    	System.out.println(new File("../../../PAY").getCanonicalPath());
    	
    	System.out.println(new File("/Ahsoka (2024) -Extras/Ahsoka (2024) -Information/Ahsoka (2024) - Logo.ico").toPath().relativize(new File("/Ahsoka (2024)/Ahsoka (2024) -Extras/Ahsoka (2024) -Information/Ahsoka (2024) - Logo.ico").toPath()));
    	
    	System.out.println(new File("Ahsoka (2024)/Ahsoka (2024) -Extras/Ahsoka (2024) -Information/Ahsoka (2024) - Logo.ico").toPath().relativize(new File("Ahsoka (2024) -Extras/Ahsoka (2024) -Information/Ahsoka (2024) - Logo.ico").toPath()));
    	
    	System.out.println(
    			new File("Ahsoka (2024)/Ahsoka (2024) -Extras/Ahsoka (2024) -Information/Ahsoka (2024) - Logo.ico")
    			.toPath().relativize(new File("Ahsoka (2024)/Ahsoka (2024) - E02").toPath()));
    	
    	System.out.println(new File("Ahsoka (2024)/Ahsoka (2024) - E02").toPath()
    			.relativize(new File("Ahsoka (2024)/Ahsoka (2024) -Extras/Ahsoka (2024) -Information/Ahsoka (2024) - Logo.ico").toPath()));
    	
    	//System.out.println(getPathFromFolder(ahsokaFolder, poster));
    	 */
	}
	
	private static String getPathFromFolder(File folder, File file) {
		Path filePath = file.toPath();
		Path folderPath = folder.toPath();
		if(filePath.startsWith(folderPath)) {
			System.out.println("Hello");
			return filePath.subpath(folderPath.getParent().getNameCount(), filePath.getNameCount()).toString();
		}
		return null;
	}
	
	private static void copyOnlyFolders(File folder, File destFolder) throws IOException {
		if(folder == null)
			return;
		if(folder.isDirectory()) {
			File curDestFolder = new File(destFolder, folder.getName());
			if(!curDestFolder.exists())
				curDestFolder.mkdir();
			File[] files = folder.listFiles();
			if(files != null)
				for(File file : files) {
					copyOnlyFolders(file, curDestFolder);
				}
		}
		else {
			if(FileFormats.comapreFileFormats(folder, FileFormat.SUBTILTE)) {
				OrderEpisodesUtils.writeToFile(new File(destFolder, folder.getName()), "");
			}
			if(FileFormats.comapreFileFormats(folder, FileFormat.IMAGE)) {
				//System.out.println(folder);
				if(FileInfoType.getFolderType(folder.getParentFile()) == FolderType.POSTERS) {
					try {
						Files.copy(folder.toPath(), new File(destFolder, folder.getName()).toPath());
					}
					catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			}
		}
	}
}