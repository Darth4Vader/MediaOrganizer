package JavaFXInterface;

import java.io.File;

import DataStructures.ManageFolder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class ActivateFileExplorer extends Application {
	
	
	/*
	 * --module-path "C:\JavaFX_22.02\lib" --add-modules javafx.controls,javafx.fxml
--add-opens=javafx.controls/javafx.scene.control.skin=ALL-UNNAMED
--add-opens=javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED
	 */

	public static void main(String[] args) {
		Application.launch(args);
	}
	
	private FileInfoExplorer explorer;

    @Override
    public void start(Stage stage) throws Exception {
    	DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose Folder");
        File file = chooser.showDialog(null);
        if(file == null) {
        	stop();
        	return;
        }
    	//file = new File("C:\\Users\\itay5\\OneDrive\\מסמכים\\New folder (2)");
    	
    	this.explorer = new FileInfoExplorer(new ManageFolder(file.getAbsolutePath()));
    	
    	Scene scene = new Scene(explorer);
    	stage.setScene(scene);
    	stage.setWidth(800);
    	stage.setHeight(500);
    	stage.show();
    }
    
    @Override
    public void stop() {
    	if(this.explorer != null)
    		this.explorer.closePanel();
    	Platform.exit();
    }
}