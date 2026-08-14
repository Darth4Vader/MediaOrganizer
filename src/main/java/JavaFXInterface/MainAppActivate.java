package JavaFXInterface;

import javafx.application.Application;

public class MainAppActivate {

	/*
	--module-path=target/MediaOrganizer/libs/javafx-base-27-ea+26-win.jar;target/MediaOrganizer/libs/javafx-controls-27-ea+26-win.jar;target/MediaOrganizer/libs/javafx-fxml-27-ea+26-win.jar;target/MediaOrganizer/libs/javafx-graphics-27-ea+26-win.jar;target/MediaOrganizer/libs/javafx-base-27-ea+26-linux.jar;target/MediaOrganizer/libs/javafx-controls-27-ea+26-linux.jar;target/MediaOrganizer/libs/javafx-fxml-27-ea+26-linux.jar;target/MediaOrganizer/libs/javafx-graphics-27-ea+26-linux.jar;target/MediaOrganizer/libs/javafx-base-27-ea+26-mac.jar;target/MediaOrganizer/libs/javafx-controls-27-ea+26-mac.jar;target/MediaOrganizer/libs/javafx-fxml-27-ea+26-mac.jar;target/MediaOrganizer/libs/javafx-graphics-27-ea+26-mac.jar
	--add-modules javafx.controls,javafx.fxml
	--add-opens=javafx.controls/javafx.scene.control.skin=ALL-UNNAMED
	--add-opens=javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED
	--enable-native-access=javafx.graphics
	*/
	
	public static void main(String[] args) {
		Application.launch(ActivateFileExplorer.class, args);
	}

}
