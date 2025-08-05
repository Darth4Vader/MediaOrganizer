package JavaFXInterface.FileExplorerView;

import java.io.File;
import java.nio.file.Path;

import JavaFXInterface.FileExplorer.FileExplorer;
import JavaFXInterface.FileExplorerView.MainFileExplorerView.FileExplorerView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FileTableContentViewTest extends Application {
	
	
	/*
	 * --module-path "C:\JavaFX_22.02\lib" --add-modules javafx.controls,javafx.fxml
--add-opens=javafx.controls/javafx.scene.control.skin=ALL-UNNAMED
--add-opens=javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED
	 */
	
	private FileExplorer fileExplorer;

	public static void main(String[] args) {
		Application.launch(args);
	}

    @Override
    public void start(Stage stage) throws Exception {
    	BorderPane mainPane = new BorderPane();
    	Path path = Path.of(System.getProperty("user.home"), "Music\\My Music\\Soundtracks\\Gladiator\\2000 Gladiator Recording Sessions Edition BOOTLEG");
    	File file = path.toFile();
    	fileExplorer = new FileExplorer(file);
    	fileExplorer.getMainFileExplorerView().setFileExplorerView(FileExplorerView.CONTENT);
    	mainPane.setCenter(fileExplorer);
    	Scene scene = new Scene(mainPane);
    	stage.setScene(scene);
    	stage.setWidth(800);
    	stage.setHeight(500);
    	stage.show();
    }
    
    @Override
    public void stop() {
    	if(this.fileExplorer != null)
    		this.fileExplorer.closePanel();
    	Platform.exit();
    }
}