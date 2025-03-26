package DataStructures.Json;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import DataStructures.ManageFolder;
import DataStructures.App.ManageFolderPojo;

public class ManageFolderJson {
	
	public static void saveManageFolder(ManageFolder manageFolder, File saveToFile) {
		try {
			ObjectMapper mapper = JacksonUtils.getObjectMapperSerialization();
			String mainPath = manageFolder.getMainFolderPath();
			File mainFolder = new File(mainPath);
			FileJson.setRelativePathMainFolder(mapper, mainFolder);
			mapper.writeValue(saveToFile, manageFolder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ManageFolder loadManageFolder(File readFromFile) {
		try {
			ObjectMapper mapper = JacksonUtils.getObjectMapperDeserialization();
			JsonNode root = mapper.readTree(readFromFile);
			String mainPath = root.get(DataInformation.MANAGE_FOLDER_MAIN_PATH).asText();
			File mainFolder = new File(mainPath);
			FileJson.setRelativePathMainFolder(mapper, mainFolder);
			return mapper.readValue(readFromFile, ManageFolder.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static ManageFolderPojo convertManageFolderToPojo(ManageFolder manageFolder) {
		ObjectMapper mapper = manageFolderObjectMapperDeserialization(manageFolder.getMainFolderPath());
		return mapper.convertValue(manageFolder, ManageFolderPojo.class);
	}
	
	public static ManageFolder convertPojoToManageFolder(ManageFolderPojo manageFolder) {
		ObjectMapper mapper = manageFolderObjectMapperDeserialization(manageFolder.getUrlParent());
		return mapper.convertValue(manageFolder, ManageFolder.class);
	}
	
	private static ObjectMapper manageFolderObjectMapperDeserialization(String mainPath) {
		ObjectMapper mapper = JacksonUtils.getObjectMapperDeserialization();
		File mainFolder = new File(mainPath);
		FileJson.setRelativePathMainFolder(mapper, mainFolder);
		return mapper;
	}
}
