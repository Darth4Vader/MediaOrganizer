package darthvader.mainmoving;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class ChevronExample extends Application {
    @Override
    public void start(Stage primaryStage) {
        StackPane chevron = new StackPane();
        chevron.getStyleClass().add("chevron");

        Pane topPart = new Pane();
        topPart.getStyleClass().add("chevron-part");
        topPart.getStyleClass().add("top");

        Pane bottomPart = new Pane();
        bottomPart.getStyleClass().add("chevron-part");
        //bottomPart.getStyleClass().add("bottom");

        chevron.getChildren().addAll(topPart, bottomPart);

        Scene scene = new Scene(chevron, 100, 100);
        scene.getStylesheets().add("chevron.css");

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
