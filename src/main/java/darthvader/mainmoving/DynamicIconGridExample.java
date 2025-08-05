package darthvader.mainmoving;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DynamicIconGridExample  extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Sample data for the ListView
        ObservableList<String> items = FXCollections.observableArrayList();
        for (int i = 1; i <= 100; i++) {
            items.add("file" + i + ".png");
        }

        // Create a ListView and set a custom cell factory
        ListView<String> listView = new ListView<>(items);
        listView.setCellFactory(lv -> new GridCell());

        // Create the scene and add it to the stage
        Scene scene = new Scene(listView, 800, 600);
        primaryStage.setTitle("JavaFX Virtualized Grid ListView Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Custom ListCell with a TilePane for grid layout
    private static class GridCell extends ListCell<String> {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setGraphic(null);
                return;
            }

            // Create a TilePane to display the icons
            TilePane tilePane = new TilePane();

            tilePane.setHgap(10);
            tilePane.setVgap(10);
            tilePane.setPadding(new Insets(10));
            tilePane.setPrefColumns(3); // Adjust based on preference
            tilePane.setPrefTileWidth(100);
            tilePane.setPrefTileHeight(100);

            // Load the image only when the cell is about to be displayed
            ImageView imageView = new ImageView(new Image("file:" + item));
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.setPreserveRatio(true);

            // Add image and label to a VBox
            VBox vbox = new VBox();
            vbox.setAlignment(Pos.CENTER);
            vbox.getChildren().addAll(imageView, new Label(item));

            // Add VBox to TilePane
            tilePane.getChildren().add(vbox);

            // Set the TilePane as the graphic for the cell
            setGraphic(tilePane);
        }
    }
}