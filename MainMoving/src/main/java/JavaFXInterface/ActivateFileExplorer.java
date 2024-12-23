package JavaFXInterface;

import java.io.File;
import java.io.IOException;

import DataStructures.ManageFolder;
import JavaFXInterface.FileExplorer.FileExplorer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
	
	private FileExplorer explorer;

    @Override
    public void start(Stage stage) throws Exception {
    	/*DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose Folder");
        File file = chooser.showDialog(null);
        if(file == null) {
        	stop();
        	return;
        }*/
    	
    	
    	
    	File file = new File("C:\\Users\\itay5\\OneDrive\\Pictures\\Main2024");
    	
    	this.explorer = new ManageFolderSelectorPanel(new ManageFolder(file.getAbsolutePath()));
    	//Scene scene = new Scene(explorer);
    	
    	Scene scene = new Scene(loadFXML(MainFileSelectorController.PATH));
    	stage.setScene(scene);
    	stage.setWidth(800);
    	stage.setHeight(500);
    	stage.show();
    	
    	/*this.explorer = new FileInfoExplorer(new ManageFolder(file.getAbsolutePath()));
    	Scene scene = new Scene(explorer);
    	stage.setScene(scene);
    	stage.setWidth(800);
    	stage.setHeight(500);
    	stage.show();*/
    }
    
    public FXMLLoader getFXMLLoader(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        return loader;
    }

    public Parent loadFXML(String fxmlPath) throws IOException {
        FXMLLoader loader = getFXMLLoader(fxmlPath);
        return loader.load();
    }
    
    @Override
    public void stop() {
    	if(this.explorer != null)
    		this.explorer.closePanel();
    	Platform.exit();
    }
}