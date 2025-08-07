package JavaFXInterface;

import javafx.application.Application;

public class MainAppActivate {

	/*
	--module-path=target/MediaOrganizer/libs/javafx-base-24.0.2-win.jar;target/MediaOrganizer/libs/javafx-controls-24.0.2-win.jar;target/MediaOrganizer/libs/javafx-fxml-24.0.2-win.jar;target/MediaOrganizer/libs/javafx-graphics-24.0.2-win.jar;target/MediaOrganizer/libs/javafx-base-24.0.2-linux.jar;target/MediaOrganizer/libs/javafx-controls-24.0.2-linux.jar;target/MediaOrganizer/libs/javafx-fxml-24.0.2-linux.jar;target/MediaOrganizer/libs/javafx-graphics-24.0.2-linux.jar;target/MediaOrganizer/libs/javafx-base-24.0.2-mac.jar;target/MediaOrganizer/libs/javafx-controls-24.0.2-mac.jar;target/MediaOrganizer/libs/javafx-fxml-24.0.2-mac.jar;target/MediaOrganizer/libs/javafx-graphics-24.0.2-mac.jar
	--add-modules javafx.controls,javafx.fxml
	--add-opens=javafx.controls/javafx.scene.control.skin=ALL-UNNAMED
	--add-opens=javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED
	*/
	
	public static void main(String[] args) {
		Application.launch(ActivateFileExplorer.class, args);
	}

}
