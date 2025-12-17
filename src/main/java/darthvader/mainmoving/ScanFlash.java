package darthvader.mainmoving;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ScanFlash {
	
	public static void main(String[] args) {
		File outJson = new File("C:\\Users\\itay5\\OneDrive\\מסמכים\\output\\out.json");
		ObjectMapper mapper = new ObjectMapper();
		/*File dir = new File("F:\\");
		List<FileJson> files = new ArrayList<>();
		
		scanFolder(dir, files);
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new File("C:\\Users\\itay5\\OneDrive\\מסמכים\\output\\out.json"), files);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		File createReplicateDir = new File("C:\\Users\\itay5\\OneDrive\\מסמכים\\output\\replicate");
		createReplicateDir.mkdirs();
		try {
			List<FileJson> files = mapper.readValue(outJson, 
					mapper.getTypeFactory().constructCollectionType(List.class, FileJson.class));
			replicateFolderStructure(files, createReplicateDir);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void scanFolder(File folder, List<FileJson> files) {
		File[] folderFiles = folder.listFiles();
		if(folderFiles != null) for (File file : folderFiles) {
			System.out.println(file);
			FileJson fileJson = new FileJson();
			fileJson.name = file.getName();
			if(file.isDirectory()) {
				fileJson.type = "folder";
				fileJson.children = new ArrayList<>();
				scanFolder(file, fileJson.children);
			} else {
				fileJson.type = "file";
			}
			files.add(fileJson);
		}
	}
	
	public static void replicateFolderStructure(List<FileJson> files, File parentFolder) {
		for (FileJson fileJson : files) {
			File newFile = new File(parentFolder, fileJson.name);
			if(fileJson.type.equals("folder")) {
				newFile.mkdirs();
				replicateFolderStructure(fileJson.children, newFile);
			} else {
				try {
					newFile.createNewFile();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private static class FileJson {
		public String name;
		public String type;
		public List<FileJson> children;
	} 

}
