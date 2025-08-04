package DataStructures.Json;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import DataStructures.ManageFolder;
import DataStructures.App.MainAppData;
import DataStructures.App.MainAppData.ManageFolderHistory;
import DataStructures.App.ManageFolderPojo;

public class MainAppDataJson {

	public static void saveMainAppData(File saveToFile, MainAppData mainAppData) throws IOException {
		ObjectMapper mapper = JacksonUtils.getObjectMapperSerialization();
		// Ensure file exists before writing
		if (!saveToFile.exists()) {
			saveToFile.getParentFile().mkdirs(); // Create parent directories if they don't exist
			saveToFile.createNewFile(); // Create the file
		}
		mapper.writeValue(saveToFile, mainAppData);
	}

	public static MainAppData loadMainAppData(File readFromFile) throws StreamReadException, DatabindException, IOException {
		ObjectMapper mapper = JacksonUtils.getObjectMapperDeserialization();
		return mapper.readValue(readFromFile, MainAppData.class);
	}
	
	public static List<ManageFolderHistory> loadManageFolderHistoryPojo(File readFromFile) throws StreamReadException, DatabindException, IOException {
		MainAppData appData = loadMainAppData(readFromFile);
		return appData.getManageFoldersHistory();
	}
	
	public static ManageFolderHistory addManageFolderPojo(File dataFile, ManageFolder manageFolder) throws IOException {
		MainAppData appData = loadMainAppData(dataFile, true);
		ManageFolderPojo pojo = ManageFolderJson.convertManageFolderToPojo(manageFolder);
		ManageFolderHistory history = appData.addManageFolderHistory(pojo);
		saveMainAppData(dataFile, appData);
		return history;
	}
	
	public static ManageFolderHistory updateManageFolderHistory(File dataFile, ManageFolderHistory manageFolderHistory) throws IOException {
		MainAppData appData = loadMainAppData(dataFile, true);
		List<ManageFolderHistory> manageFoldersHistory = appData.getManageFoldersHistory();
		for(ManageFolderHistory history : manageFoldersHistory) {
			if(history.equals(manageFolderHistory)) {
				manageFoldersHistory.remove(history);
				appData.addManageFolderHistory(manageFolderHistory);
				saveMainAppData(dataFile, appData);
				return manageFolderHistory;
			}
		}
		return null;
	}
	
	public static ManageFolderHistory updateManageFolderHistory(File dataFile, ManageFolderHistory manageFolderIndex, ManageFolder manageFolder) throws IOException {
		ManageFolderPojo pojo = ManageFolderJson.convertManageFolderToPojo(manageFolder);
		manageFolderIndex.setManage(pojo);
		return updateManageFolderHistory(dataFile, manageFolderIndex);
	}
	
	public static MainAppData createMainAppData(File saveToFile) throws IOException {
		MainAppData mainAppData;
		if (!saveToFile.exists()) {
			mainAppData = new MainAppData();
			saveMainAppData(saveToFile, mainAppData);
		}
		else {
			mainAppData = loadMainAppData(saveToFile);
		}
		return mainAppData;
	}
	
	public static MainAppData loadMainAppData(File readFromFile, boolean createIfNotExists) throws IOException {
		if(readFromFile.exists()) {
			return loadMainAppData(readFromFile);
		}
		if(createIfNotExists) {
			return createMainAppData(readFromFile);
		}
		return null;
	}
	
	public static List<ManageFolderHistory> loadManageFolderHistoryPojo(File readFromFile, boolean createIfNotExists) throws IOException {
		if(readFromFile.exists()) {
			return loadManageFolderHistoryPojo(readFromFile);
		}
		if(createIfNotExists) {
			MainAppData appData = createMainAppData(readFromFile);
			return appData.getManageFoldersHistory();
		}
		return null;
	}
}