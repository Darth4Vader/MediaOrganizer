package JavaFXInterface;

import java.io.File;
import java.nio.file.Path;

import DataStructures.ManageFolder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FileInfoMenuBarTest extends Application {
	
	
	/*
	 * --module-path "C:\JavaFX_22.02\lib" --add-modules javafx.controls,javafx.fxml
--add-opens=javafx.controls/javafx.scene.control.skin=ALL-UNNAMED
--add-opens=javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED
	 */

	public static void main(String[] args) {
		Application.launch(args);
	}

    @Override
    public void start(Stage stage) throws Exception {
    	BorderPane mainPane = new BorderPane();
    	Path path = Path.of(System.getProperty("user.home"), "OneDrive", "Pictures\\Main2024");
    	File file = path.toFile();
    	ManageFolder manage = new ManageFolder(file.getAbsolutePath());
    	mainPane.setCenter(new FileInfoExplorer(manage));
    	Scene scene = new Scene(mainPane);
    	stage.setScene(scene);
    	stage.setWidth(800);
    	stage.setHeight(500);
    	stage.show();
    }
}
