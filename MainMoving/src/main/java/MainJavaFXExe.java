import java.io.File;
import java.util.Arrays;
import java.util.Collections;

import org.controlsfx.control.cell.ColorGridCell;

import DataStructures.ManageFolder;
import JavaFXInterface.FileExplorer;
import JavaFXInterface.SideFilesList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainJavaFXExe extends Application {
	//--module-path "C:\JavaFX_22.02\lib" --add-modules javafx.controls,javafx.fxml

	public static void main(String[] args) {
		//String[] args2 = Arrays.asList(args, "--module-path \"C:\\JavaFX_22.02\\lib\" --add-modules javafx.controls,javafx.fxml").toArray(new String[0]);
		Application.launch(args);
	}

    @Override
    public void start(Stage stage) throws Exception {
		/*FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.showOpenDialog(stage);*/
    	
    	File file = new File("C:\\Users\\itay5\\OneDrive\\Pictures\\Main2024");
    	
    	//SideFilesList list = new SideFilesList(file);
    	
    	FileExplorer list = new FileExplorer(new ManageFolder(file.getAbsolutePath()));
    	
    	
    	//list.setPrefWidth(400);
    	//list.setPrefHeight(500);
    	Scene scene = new Scene(list);
    	scene.focusOwnerProperty().addListener((obs) -> {
    		System.out.println(obs);
    	});
    	stage.setScene(scene);
    	
    	stage.setWidth(list.getWidth());
    	stage.setHeight(list.getHeight());
    	
    	//stage.setWidth(400);
    	//stage.setHeight(500);
    	System.out.println("hello");
    	stage.show();
    	
    	stage.setWidth(1200);
    	stage.setHeight(1000);
    	
    	list.setPrefWidth(stage.getWidth());
    	list.setPrefHeight(stage.getHeight());
    	
    	stage.setMinWidth(1200);
    	stage.setMinHeight(1000);
    	
        //stage.minWidthProperty().bind(list.widthProperty());
        //stage.minHeightProperty().bind(list.heightProperty());
    }
}
