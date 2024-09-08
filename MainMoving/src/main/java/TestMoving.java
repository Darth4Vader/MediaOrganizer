import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import DataStructures.FileInfoType;
import DataStructures.FileInfoType.FolderType;
import DataStructures.ManageFolder;
import FileUtilities.FileFormats;
import FileUtilities.FileFormats.FileFormat;

public class TestMoving {
	
	public static void main(String[] args) throws IOException {
		/*File folder = new File("F:\\Star Wars");
		File destFolder = new File("C:\\Users\\itay5\\OneDrive\\Pictures\\New folder (2)\\Main2024");
		copyOnlyFolders(folder, destFolder);*/
		
    	File mainFolder = new File("C:\\Users\\itay5\\OneDrive\\Pictures\\New folder (2)\\Main2024");
    	
    	File file = new File(mainFolder, "Star Wars");
		
		//ManageFolder manage = new ManageFolder(mainFolder.getAbsolutePath(), Arrays.asList(mainFolder.listFiles()));
		
		//manage.toAddInsideMap(file);
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