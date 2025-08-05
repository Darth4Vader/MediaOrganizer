package JavaFXInterface;

import javafx.application.Application;

public class MainAppActivate {

	/*
	--module-path "target/MediaOrganizer/libs" --add-modules javafx.controls,javafx.fxml
	--add-opens=javafx.controls/javafx.scene.control.skin=ALL-UNNAMED
	--add-opens=javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED
	*/
	
	public static void main(String[] args) {
		Application.launch(ActivateFileExplorer.class, args);
	}

}
