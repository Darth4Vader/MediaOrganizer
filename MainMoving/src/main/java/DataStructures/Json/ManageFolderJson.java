package DataStructures.Json;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import DataStructures.ManageFolder;

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
}
