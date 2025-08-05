package DataStructures.App;

import java.io.File;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import DataStructures.ManageFolder;
import DataStructures.Json.ManageFolderJson;

public class ManageFolderJsonTest {	
	
	@Test
	public void test() {
		File manageFolder = new File("C:\\Users\\itay5\\OneDrive\\Pictures\\Main2024");
		
		ManageFolder manage = new ManageFolder(manageFolder.getAbsolutePath());
		File outputFile = new File("C:\\Users\\itay5\\OneDrive\\Pictures\\Main2024\\manage.json");
		ManageFolderJson.saveManageFolder(manage, outputFile);
		
		ManageFolder manage2 = ManageFolderJson.loadManageFolder(outputFile);
		File outputFile2 = new File("C:\\Users\\itay5\\OneDrive\\Pictures\\Main2024\\manage2.json");
		ManageFolderJson.saveManageFolder(manage2, outputFile2);
		
		Assertions.assertThat(outputFile).hasSameContentAs(outputFile2);
	}

}
