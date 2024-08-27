package WTrash;


import java.io.File;

import DataStructures.FileInfo;
import DataStructures.ManageFolder;
import FileUtilities.MimeUtils;

public class main2 {

	public static void main(String[] args) {
		/*File folder = new File("C:\\Users\\itay5\\OneDrive\\Desktop\\Miiii");
		for(File file : folder.listFiles()) {
			File newFile = new File(file.getParent() + "\\House (2004) - " + file.getName());
			file.renameTo(newFile);
		}*/
		
		/*String text = ".eng";
		for(File file : folder.listFiles()) {
			String name = file.getName();
			int idx = name.lastIndexOf(text);
			if(idx != -1) {
				String newName = name.substring(0, idx) + name.substring(idx + text.length());
				File newFile = new File(file.getParent() + "\\" + newName);
				file.renameTo(newFile);
			}
		}*/
		
		ManageFolder move = new ManageFolder("E:\\Main");
		//move.addToMap(new File("E:\\House (2004)"));
		move.moveFilesFromInput();
		
		/*ManageFolder move = new ManageFolder("E:\\Main");
		move.moveFilesFromInput();*/
		
		FileInfo info;//= new FileInfo("House (2004) - S02E19 - House vs. God - featurettes", false);
		
		String str = "House (2004) - S02E20 - Euphoria (1)";
		
		//str = "House (2004) - S02E19 - House vs. God";
		
		//str = "Life (Sort of) (2023) bluray";
		
		str = "House (2004) - S03E08 - Whac-A-Mole Featurettes";
		
		//info = new FileInfo(new File("C:\\Users\\itay5\\OneDrive\\Pictures\\Main\\Input\\Loki (2021) - S02E01"));
		
		//FileInfo info = new FolderTypeInfo(str, false);
		
		//House (2004) - S02E20 - Euphoria (1)
		//House (2004) - S02E21 - Euphoria (2)
		
		
		//System.out.println(info.getFullName());
		
		//System.out.println(info.getFolderType());
		
		//System.out.println(MimeUtils.getMimeContentType(".mkv"));
		
		/*ManageFolder move = new ManageFolder("C:\\Users\\itay5\\OneDrive\\Pictures\\Main");
		move.moveFilesFromInput();*/
		
		/*System.out.println(info.isEpisode("", ""));
		System.out.println(info.isEpisode("01", ""));
		System.out.println(info.isEpisode("01", "02"));
		System.out.println(info.isEpisode("01", "01"));
		System.out.println(info.isEpisode("02", "01"));
		System.out.println(info.isEpisode("02", ""));
		System.out.println(info.isEpisode("02", "02"));
		System.out.println(info.isEpisode("", "02"));
		System.out.println(info.isEpisode("", "01"));*/
		
		
		/*for(Exception e : move.setIconsToFolder())
			e.printStackTrace();*/
		
		//new moveFilesNewUI("C:\\Users\\itay5\\OneDrive\\Pictures\\Main");
	}

}
