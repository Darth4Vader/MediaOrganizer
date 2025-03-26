package JavaFXInterface;

import java.io.File;
import java.io.IOException;
import java.util.List;

import DataStructures.ManageFolder;
import DataStructures.App.MainAppData.ManageFolderHistory;
import DataStructures.App.ManageFolderPojo;
import DataStructures.Json.MainAppDataJson;
import DataStructures.Json.ManageFolderJson;
import JavaFXInterface.FileExplorer.FileExplorer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ActivateFileExplorer extends Application {
	
	
	/*
	 * --module-path "C:\JavaFX_22.02\lib" --add-modules javafx.controls,javafx.fxml
--add-opens=javafx.controls/javafx.scene.control.skin=ALL-UNNAMED
--add-opens=javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED
	 */
	
	public static final File APP_DATA_FILE = new File("Data\\appData.json");

	public static void main(String[] args) {
		Application.launch(args);
	}
	
	private FileExplorer explorer;
	private BorderPane mainPanel;
	
	private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
    	this.mainPanel = new BorderPane();
    	
    	Pane mainPanel = getMainPanel();
    	this.mainPanel.setCenter(mainPanel);
    	Scene scene = new Scene(this.mainPanel);
    	
    	this.stage = stage;
    	
    	//Scene scene = new Scene(loadFXML(MainFileSelectorController.PATH));
    	stage.setScene(scene);
    	stage.setWidth(800);
    	stage.setHeight(500);
    	stage.show();
    	
    	/*this.explorer = new FileInfoExplorer(new ManageFolder(file.getAbsolutePath()));
    	Scene scene = new Scene(explorer);
    	stage.setScene(scene);
    	stage.setWidth(800);
    	stage.setHeight(500);
    	stage.show();*/
    }
    
    private ManageFolderHistory managePojo;
    
	public Pane getMainPanel() {
		VBox mainPane = new VBox();
		
		Button selectBtn = new Button("Select Folder");
		selectBtn.setOnAction(e -> {
			DirectoryChooser chooser = new DirectoryChooser();
			chooser.setTitle("Choose Folder");
			File file = chooser.showDialog(stage);
			if(file == null) return;
	    	ManageFolder manage = new ManageFolder(file.getAbsolutePath());
	    	ManageFolderSelectorPanel selectorExplorer = new ManageFolderSelectorPanel(manage);
	    	selectorExplorer.finishSelectionProperty().addListener((obs, oldVal, newVal) -> {
	    		System.out.println("Done");
	    		if(newVal) {
	    			managePojo = MainAppDataJson.addManageFolderPojo(APP_DATA_FILE, manage);
	    			setManageFolderExplorer(manage);
	    		}
	    	});
	    	this.explorer = selectorExplorer;
	    	this.mainPanel.setCenter(this.explorer);
		});
		
		Button existingBtn = new Button("Open Existing Folder");
		existingBtn.setOnAction(e -> {
			List<ManageFolderHistory> list = MainAppDataJson.loadManageFolderHistoryPojo(APP_DATA_FILE);
			
			ListView<String> folderList = new ListView<>();
			
			
			ListView<ManageFolderHistory> listView = new ListView<>();
			listView.setCellFactory(view -> new ListCell<>() {
				@Override
                protected void updateItem(ManageFolderHistory item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        //setText(null);
                    	setGraphic(null);
                    } else {
                    	Label time = new Label("Last Access: " + item.getLastAccess());
                    	Label path = new Label(item.getManage().getUrlParent());
                    	VBox vbox = new VBox();
                    	vbox.getChildren().addAll(time, path);
                    	setGraphic(vbox);
                    	vbox.setOnMouseClicked(e -> {
                    		ManageFolderHistory selected = listView.getSelectionModel().getSelectedItem();
                    		
                    		VBox vbox2 = new VBox();
                    		Button changedButton = new Button("Change Main Path");
							changedButton.setOnAction(e2 -> {
								DirectoryChooser chooser = new DirectoryChooser();
								chooser.setTitle("Choose Folder");
								File file = chooser.showDialog(stage);
								if (file == null) return;
								selected.getManage().setUrlParent(file.getAbsolutePath());
								managePojo = MainAppDataJson.updateManageFolderHistory(APP_DATA_FILE, selected);
								setItem(selected);
							});
                    		
                    		Button enterButton = new Button("Enter");
							enterButton.setOnAction(e2 -> {
								poupStage.close();
								ManageFolder manage = ManageFolderJson.convertPojoToManageFolder(selected.getManage());
								managePojo = selected;
								setManageFolderExplorer(manage);
							});
							
							vbox2.getChildren().addAll(changedButton, enterButton);
							
							if (poupStage != null)
								poupStage.close();
							poupStage = new Stage();
							poupStage.setScene(new Scene(vbox2));
							poupStage.initOwner(stage);
							poupStage.initModality(Modality.APPLICATION_MODAL);
							poupStage.setAlwaysOnTop(true);
							poupStage.show();
                    	});
                    }
                }
			});
			listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
				if (newVal != null) {
					ManageFolderPojo manage = newVal.getManage();
					folderList.getItems().clear();
					if (manage.movieMap != null)
						folderList.getItems().addAll(manage.movieMap.values());
					
					if (manage.TVMap != null)
						folderList.getItems().addAll(manage.TVMap.values());
					
					if (manage.unkownMediaMap != null)
						folderList.getItems().addAll(manage.unkownMediaMap.values());
				}
			});
			listView.getItems().addAll(list);
			
			HBox hbox = new HBox();
			hbox.getChildren().addAll(listView, folderList);
			
			this.mainPanel.setCenter(hbox);
		});
		
		mainPane.getChildren().addAll(selectBtn, existingBtn);
		
		
		return mainPane;
	}
	
	public void setManageFolderExplorer(ManageFolder manage) {
		BorderPane mainPane = new BorderPane();
		this.explorer = new FileInfoExplorer(manage);
		
		mainPane.setCenter(this.explorer);
		MenuBar menuBar = new MenuBar();
		Menu fileExplorer = new Menu();
		Label fileExplorerLabel = new Label("File Explorer");
		fileExplorerLabel.setOnMouseClicked(e -> {
			mainPane.setCenter(this.explorer);
		});
		fileExplorer.setGraphic(fileExplorerLabel);
		Menu settings = new Menu();
		Label settingsLabel = new Label("Settings");
		settingsLabel.setOnMouseClicked(e -> {
			VBox settingsPane = new VBox();
			HBox customNamePane = new HBox();
			Label customNameLabel = new Label("Set Custom Name: ");
			TextField customNameField = new TextField(managePojo.getCustomName());
			customNamePane.getChildren().addAll(customNameLabel, customNameField);
			
			Button updateButton = new Button("Update Settings");
			
			updateButton.setOnAction(e2 -> {
				
				managePojo.setCustomName(customNameField.getText());
				managePojo = MainAppDataJson.updateManageFolderHistory(APP_DATA_FILE, managePojo);
			});
			settingsPane.getChildren().addAll(customNamePane, updateButton);
			
			mainPane.setCenter(settingsPane);
		});
		settings.setGraphic(settingsLabel);
		menuBar.getMenus().addAll(fileExplorer, settings);
		mainPane.setTop(menuBar);
		//mainPane.getChildren().addAll(menuBar, this.explorer);
		this.mainPanel.setCenter(mainPane);
	}
	
	private Stage poupStage = null;
    
    public FXMLLoader getFXMLLoader(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        return loader;
    }

    public Parent loadFXML(String fxmlPath) throws IOException {
        FXMLLoader loader = getFXMLLoader(fxmlPath);
        return loader.load();
    }
    
    @Override
    public void stop() {
    	if(this.explorer != null)
    		this.explorer.closePanel();
    	Platform.exit();
    }
}