package DataStructures.Issues;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import DataStructures.ManageFolder;
import DataStructures.NameInfo;
import DataStructures.App.MainAppData;
import DataStructures.App.MainAppData.ManageFolderHistory;
import DataStructures.Json.MainAppDataJson;
import DataStructures.Json.ManageFolderJson;
import FileUtilities.MimeUtils;

class IssuesTest {
	
    @TempDir
    Path mainDir;
    
    private static final String APP_DATA_FILE = "Data/appData.json";
    
    private File createFile(String fileName) throws Exception {
		File file = mainDir.resolve(fileName).toFile();
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		file.createNewFile();
		return file;
    	
    }
    
    private File createFile(String dir, String fileName) throws Exception {
		File file = mainDir.resolve(dir).resolve(fileName).toFile();
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		file.createNewFile();
		return file;
    	
    }
    
    @Test
    void issue1() {
    	assertDoesNotThrow(() -> {
    		File mainFolder = mainDir.toFile();
    		String customName = "Testing";
    		// create a ManageFolder instance with the main folder path
    		ManageFolder manageFolder = new ManageFolder(mainFolder.getAbsolutePath());
    		// and add to it a Mini Series and move them
    		NameInfo ep1 = new NameInfo("Superman and Lois - S01E01 - Pilot");
    		createFile(ManageFolder.DEFAULT_INPUT, MimeUtils.createNameWithMime(ep1.getFullName(), "mkv"));
    		createFile(ManageFolder.DEFAULT_INPUT, MimeUtils.createNameWithMime(ep1.getFullName(), "srt"));
    		manageFolder.moveFilesFromInput();
    		
    		// read from jsom data file
    		File appDataFile = createFile(APP_DATA_FILE);
    		MainAppDataJson.saveMainAppData(appDataFile, new MainAppData());
    		// add a new ManageFolderHistory entry
    		ManageFolderHistory managePojo = MainAppDataJson.addManageFolderPojo(appDataFile, manageFolder);
    		managePojo.setCustomName(customName);
    		MainAppDataJson.updateManageFolderHistory(appDataFile, managePojo);
    		
    		// load the ManageFolderHistory entry
    		List<ManageFolderHistory> list = MainAppDataJson.loadManageFolderHistoryPojo(appDataFile);
    		ManageFolderHistory managePojoLoad = list.stream()
    				.filter(p -> p.getCustomName() != null && p.getCustomName().equals(customName))
    				.findFirst().orElse(null);
    		
    		// add new files to the Mini Series
    		ManageFolder manageFolderParse = ManageFolderJson.convertPojoToManageFolder(managePojoLoad.getManage());
    		NameInfo ep2 = new NameInfo("Superman and Lois - S01E02 - Superboy");
    		createFile(ManageFolder.DEFAULT_INPUT, MimeUtils.createNameWithMime(ep2.getFullName(), "mkv"));
    		createFile(ManageFolder.DEFAULT_INPUT, MimeUtils.createNameWithMime(ep2.getFullName(), "srt"));
    		manageFolderParse.moveFilesFromInput();
    	});
    }
}
