package darthvader.mainmoving;
import java.io.File;
import java.util.Arrays;

import DataStructures.ManageFolder;
import JavaFXInterface.FileExplorer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestMovingJavaFXActivate extends Application {

	public static void main(String[] args) {
		//String[] args2 = Arrays.asList(args, "--module-path \"C:\\JavaFX_22.02\\lib\" --add-modules javafx.controls,javafx.fxml").toArray(new String[0]);
		Application.launch(args);
	}

    @Override
    public void start(Stage stage) throws Exception {
		/*FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.showOpenDialog(stage);*/
    	File mainFolder = new File("C:\\Users\\itay5\\OneDrive\\Pictures\\Main2024");
    	
    	File file = new File(mainFolder, "Star Wars");
    	
    	//SideFilesList list = new SideFilesList(file);
    	
    	
    	ManageFolder manage = new ManageFolder(mainFolder.getAbsolutePath(), Arrays.asList(mainFolder.listFiles()));
    	
    	//manage.setIconsToFolder();
    	
    	TestMovingJavaFX list = new TestMovingJavaFX(manage);
    	
    	
    	
    	//list.setPrefWidth(400);
    	//list.setPrefHeight(500);
    	Scene scene = new Scene(list);
    	scene.focusOwnerProperty().addListener((obs) -> {
    		System.out.println(obs);
    	});
    	stage.setScene(scene);
    	
    	stage.setWidth(400);
    	stage.setHeight(500);
    	System.out.println("hello");
    	stage.show();
    }

}
