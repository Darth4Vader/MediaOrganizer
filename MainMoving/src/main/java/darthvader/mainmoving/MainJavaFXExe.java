package darthvader.mainmoving;
import java.awt.FileDialog;
import java.io.File;
import java.nio.file.WatchService;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.JFileChooser;

import org.controlsfx.control.cell.ColorGridCell;

import DataStructures.ManageFolder;
import JavaFXInterface.FileExplorer;
import JavaFXInterface.SideFilesList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainJavaFXExe extends Application {
	
	
	/*
	 * --module-path "C:\JavaFX_22.02\lib" --add-modules javafx.controls,javafx.fxml
--add-opens=javafx.controls/javafx.scene.control.skin=ALL-UNNAMED
--add-opens=javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED
	 */
	
	//--module-path "C:\JavaFX_22.02\lib" --add-modules javafx.controls,javafx.fxml

	public static void main(String[] args) {
		//String[] args2 = Arrays.asList(args, "--module-path \"C:\\JavaFX_22.02\\lib\" --add-modules javafx.controls,javafx.fxml").toArray(new String[0]);
		Application.launch(args);
	}
	
	private FileExplorer list;

    @Override
    public void start(Stage stage) throws Exception {
		/*FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.showOpenDialog(stage);*/
    	
    	File file = new File("C:\\Users\\itay5\\OneDrive\\Pictures\\Main2024");
    	
    	//file = new File("C:\\Users\\itay5\\OneDrive\\מסמכים\\New folder (2)");
    	
    	//SideFilesList list = new SideFilesList(file);
    	
    	/*FileExplorer*/ list = new FileExplorer(new ManageFolder(file.getAbsolutePath()));
    	
    	//FileDialog
    	//list.setPrefWidth(400);
    	//list.setPrefHeight(500);
    	Scene scene = new Scene(list);
    	scene.focusOwnerProperty().addListener((obs) -> {
    		System.out.println(obs);
    	});
    	stage.setScene(scene);
    	
    	/*stage.setWidth(list.getWidth());
    	stage.setHeight(list.getHeight());*/
    	
    	stage.setWidth(800);
    	stage.setHeight(500);
    	System.out.println("hello");
    	stage.show();
    	
    	/*stage.setWidth(1200);
    	stage.setHeight(1000);
    	
    	list.setPrefWidth(stage.getWidth());
    	list.setPrefHeight(stage.getHeight());
    	
    	stage.setMinWidth(1200);
    	stage.setMinHeight(1000);*/
    	
        //stage.minWidthProperty().bind(list.widthProperty());
        //stage.minHeightProperty().bind(list.heightProperty());
    	
    }
    
    @Override
    public void stop() {
        /*// When stopping the market app, close the rest application and the JavaFX application
        if (appContext != null) {
            appContext.close();
        }
        Platform.exit();*/
    	this.list.closePanel();
    	Platform.exit();
    }
}
