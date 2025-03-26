package DataStructures.Json;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import DataStructures.ManageFolder;
import DataStructures.App.MainAppData;
import DataStructures.App.MainAppData.ManageFolderHistory;
import DataStructures.App.ManageFolderPojo;

public class MainAppDataJson {

	public static void saveMainAppData(File saveToFile, MainAppData mainAppData) {
		try {
			ObjectMapper mapper = JacksonUtils.getObjectMapperSerialization();
			mapper.writeValue(saveToFile, mainAppData);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static MainAppData loadMainAppData(File readFromFile) {
		try {
			ObjectMapper mapper = JacksonUtils.getObjectMapperDeserialization();
			return mapper.readValue(readFromFile, MainAppData.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<ManageFolderHistory> loadManageFolderHistoryPojo(File readFromFile) {
		try {
			MainAppData appData = loadMainAppData(readFromFile);
			return appData.getManageFoldersHistory();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ManageFolderHistory addManageFolderPojo(File dataFile, ManageFolder manageFolder) {
		MainAppData appData = loadMainAppData(dataFile);
		if(appData == null) {
			appData = new MainAppData();
		}
		ManageFolderPojo pojo = ManageFolderJson.convertManageFolderToPojo(manageFolder);
		ManageFolderHistory history = appData.addManageFolderHistory(pojo);
		saveMainAppData(dataFile, appData);
		return history;
	}
	
	public static ManageFolderHistory updateManageFolderHistory(File dataFile, ManageFolderHistory manageFolderHistory) {
		MainAppData appData = loadMainAppData(dataFile);
		if(appData == null) {
			appData = new MainAppData();
		}
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
}