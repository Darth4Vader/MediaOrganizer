package DataStructures.Json;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import DataStructures.FolderInfo;
import DataStructures.FolderInformation;

public class FolderInformationJson {
	
	public static void saveFolderInformation(File saveToFile, FolderInformation folderInformation) {
		try {
			ObjectMapper mapper = JacksonUtils.getObjectMapperSerialization();
			FileJson.setRelativePathMainFolder(mapper, folderInformation.getFolderInfo().getFile());
			mapper.writeValue(saveToFile, folderInformation);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static FolderInformation loadFolderInformation(File readFromFile, FolderInfo mainFolder) {
		try {
			ObjectMapper mapper = JacksonUtils.getObjectMapperSerialization();
			FileJson.setRelativePathMainFolder(mapper, mainFolder.getFile());
			FolderInformation folderInformation = mapper.readValue(readFromFile, FolderInformation.class);
			folderInformation.setFolderInfo(mainFolder);
			return folderInformation;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
