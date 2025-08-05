package debug;

import java.io.File;
import java.io.IOException;
import java.util.List;

import DataStructures.ManageFolder;
import DataStructures.App.MainAppData.ManageFolderHistory;
import DataStructures.Json.MainAppDataJson;
import DataStructures.Json.ManageFolderJson;

public class TestBugs {
	
	public static final File APP_DATA_FILE = new File("Data\\appData.json");

	public TestBugs() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		File mainFolder = new File("C:\\Users\\itay5\\OneDrive\\Pictures\\Main20252");
		String customName = "Testing";
		
		/*
		ManageFolder manageFolder = new ManageFolder(mainFolder.getAbsolutePath());
		ManageFolderHistory managePojo = MainAppDataJson.addManageFolderPojo(APP_DATA_FILE, manageFolder);
		managePojo.setCustomName(customName);
		managePojo = MainAppDataJson.updateManageFolderHistory(APP_DATA_FILE, managePojo);
		*/
		
		List<ManageFolderHistory> list;
		try {
			list = MainAppDataJson.loadManageFolderHistoryPojo(APP_DATA_FILE);
			ManageFolderHistory managePojo = list.stream()
					.filter(p -> p.getCustomName() != null && p.getCustomName().equals(customName))
					.findFirst().orElse(null);
			if(managePojo != null) {
				System.out.println(managePojo.getManage().movieMap);
			}
			ManageFolder manageFolder = ManageFolderJson.convertPojoToManageFolder(managePojo.getManage());
			manageFolder.moveFilesFromInput();
			System.out.println(manageFolder.unkownMediaMap);
			System.out.println(manageFolder.TVMap);
			
			//managePojo = MainAppDataJson.updateManageFolderHistory(APP_DATA_FILE, managePojo, manageFolder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
