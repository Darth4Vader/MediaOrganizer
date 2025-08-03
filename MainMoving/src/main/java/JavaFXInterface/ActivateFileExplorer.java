package JavaFXInterface;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import DataStructures.ManageFolder;
import DataStructures.App.MainAppData.ManageFolderHistory;
import DataStructures.App.ManageFolderPojo;
import DataStructures.Json.MainAppDataJson;
import DataStructures.Json.ManageFolderJson;
import JavaFXInterface.FileExplorer.FileExplorer;
import JavaFXInterface.ManageFileExplorer.FileInfoExplorer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
	
	private FileExplorer explorer;
	private BorderPane mainPanel;
	
	private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
    	this.stage = stage;
    	this.mainPanel = new BorderPane();
    	setSetupPane();
    	Scene scene = new Scene(this.mainPanel);
    	stage.setScene(scene);
    	stage.setWidth(800);
    	stage.setHeight(500);
    	stage.show();
    }
    
    private void setCenter(Node control) {
    	this.mainPanel.setCenter(control);
    	stage.sizeToScene();
    	stage.setWidth(800);
    	stage.setHeight(500);
	}
    
    private ManageFolderHistory managePojo;
    
	public void setSetupPane() {
		VBox mainPane = new VBox();
		
		Button selectBtn = new Button("Select Folder");
		selectBtn.setOnAction(_ -> {
			DirectoryChooser chooser = new DirectoryChooser();
			chooser.setTitle("Choose Folder");
			File file = chooser.showDialog(stage);
			if(file == null) return;
	    	ManageFolder manage = new ManageFolder(file.getAbsolutePath());
	    	ManageFolderSelectorPanel selectorExplorer = new ManageFolderSelectorPanel(manage);
	    	selectorExplorer.finishSelectionProperty().addListener((_, _, newVal) -> {
	    		if(newVal) {
	    			managePojo = MainAppDataJson.addManageFolderPojo(APP_DATA_FILE, manage);
	    			setManageFolderExplorer(manage);
	    		}
	    	});
	    	setMainFileExplorer(selectorExplorer);
	    	setCenter(this.explorer);
		});
		
		Button existingBtn = new Button("Open Existing Folder");
		existingBtn.setOnAction(_ -> {
			setMainPanelHistorySelector();
		});
		
		mainPane.getChildren().addAll(selectBtn, existingBtn);
		
		this.mainPanel.setCenter(mainPane);
	}
	
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm");
	
	private void setMainPanelHistorySelector() {
		TableView<ManageFolderHistory> table = new TableView<>();
		TableColumn<ManageFolderHistory, String> customNameColumn = new TableColumn<>("Custom Name");
		customNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomName()));
		TableColumn<ManageFolderHistory, String> folderPathColumn = new TableColumn<>("Folder Path");
		folderPathColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getManage().getUrlParent()));
		TableColumn<ManageFolderHistory, String> lastAccessColumn = new TableColumn<>("Last Access");
		lastAccessColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastAccess().format(formatter)));
		
		table.getColumns().add(customNameColumn);
		table.getColumns().add(folderPathColumn);
		table.getColumns().add(lastAccessColumn);
		
		table.setRowFactory(_ -> {
		    TableRow<ManageFolderHistory> row = new TableRow<>();
		    row.setOnMouseClicked(event -> {
		        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
		        	event.consume();
		        	mangeFolderHistoryPopup(row.getItem());
		        }
		    });
		    return row;
		});
		table.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == KeyCode.ENTER) {
				event.consume();
				ManageFolderHistory selected = table.getSelectionModel().getSelectedItem();
				if(selected == null) return;
				mangeFolderHistoryPopup(selected);
			}
		});
		ListView<String> folderList = new ListView<>();
		table.getSelectionModel().selectedItemProperty().addListener((_, _, newVal) -> {
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
		
		Platform.runLater(() -> {
			List<ManageFolderHistory> list = MainAppDataJson.loadManageFolderHistoryPojo(APP_DATA_FILE);
			table.getItems().addAll(list);
		});
		
		HBox hbox = new HBox();
		table.prefWidthProperty().bind(hbox.widthProperty().multiply(0.7));
		HBox.setHgrow(folderList, javafx.scene.layout.Priority.ALWAYS);
		hbox.getChildren().addAll(table, folderList);
		
		BorderPane mainPane = new BorderPane();
		
		mainPane.setCenter(hbox);
		
		Button backButton = new Button("Back");
		mainPane.setTop(new BorderPane(backButton));
		mainPane.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
			System.out.println("Key Typed: " + event.getCode());
            if (event.getCode() == KeyCode.BACK_SPACE) {
                event.consume();
                setSetupPane();
            }
        });
		backButton.setOnAction(_ -> {
			setSetupPane();
		});
		
		this.mainPanel.setCenter(mainPane);
	}
	
	private void mangeFolderHistoryPopup(ManageFolderHistory selected) {
		VBox popupPnae = new VBox();
		Button changedButton = new Button("Change Main Path");
		changedButton.setOnAction(_ -> {
			DirectoryChooser chooser = new DirectoryChooser();
			chooser.setTitle("Choose Folder");
			File file = chooser.showDialog(stage);
			if (file == null) return;
			selected.getManage().setUrlParent(file.getAbsolutePath());
			managePojo = MainAppDataJson.updateManageFolderHistory(APP_DATA_FILE, selected);
		});
		
		Button enterButton = new Button("Enter");
		enterButton.setOnAction(_ -> {
			popupStage.close();
			ManageFolder manage = ManageFolderJson.convertPojoToManageFolder(selected.getManage());
			managePojo = selected;
			setManageFolderExplorer(manage);
		});
		
		popupPnae.getChildren().addAll(changedButton, enterButton);
		
		if (popupStage != null)
			popupStage.close();
		popupStage = new Stage();
		popupStage.setScene(new Scene(popupPnae));
		popupStage.initOwner(stage);
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.setAlwaysOnTop(true);
		popupStage.show();
	}
	
	public void setManageFolderExplorer(ManageFolder manage) {
		BorderPane mainPane = new BorderPane();
		setMainFileExplorer(new FileInfoExplorer(manage));
		mainPane.setCenter(this.explorer);
		
		MenuBar menuBar = new MenuBar();
		Menu fileExplorer = new Menu();
		Label fileExplorerLabel = new Label("File Explorer");
		fileExplorerLabel.setOnMouseClicked(_ -> {
			if (!(this.explorer instanceof FileInfoExplorer))
				setMainFileExplorer(new FileInfoExplorer(manage));
			mainPane.setCenter(this.explorer);
		});
		fileExplorer.setGraphic(fileExplorerLabel);
		
		Menu settings = new Menu("Settings");
		MenuItem settingsAttributes = new MenuItem("Attributes");
		settingsAttributes.setOnAction(_ -> {
			VBox settingsPane = new VBox();
			HBox customNamePane = new HBox();
			Label customNameLabel = new Label("Set Custom Name: ");
			TextField customNameField = new TextField(managePojo.getCustomName());
			customNamePane.getChildren().addAll(customNameLabel, customNameField);
			
			Button updateButton = new Button("Update Settings");
			
			updateButton.setOnAction(_ -> {
				managePojo.setCustomName(customNameField.getText());
				managePojo = MainAppDataJson.updateManageFolderHistory(APP_DATA_FILE, managePojo);
			});
			settingsPane.getChildren().addAll(customNamePane, updateButton);
			
			mainPane.setCenter(settingsPane);
		});
		MenuItem settingsMainFiles = new MenuItem("Change Main Files");
		settingsMainFiles.setOnAction(_ -> {
			ManageFolderSelectorPanel manageSelector = new ManageFolderSelectorPanel(manage);
			manageSelector.finishSelectionProperty().addListener((_, _, newVal) -> {
				if (newVal) {
					System.out.println("Update meeeeeeee");
					managePojo = MainAppDataJson.updateManageFolderHistory(APP_DATA_FILE, managePojo, manage);
				}
			});
			setMainFileExplorer(manageSelector);
			mainPane.setCenter(this.explorer);
		});
		
		settings.getItems().addAll(settingsAttributes, settingsMainFiles);
		menuBar.getMenus().addAll(fileExplorer, settings);
		mainPane.setTop(menuBar);
		setCenter(mainPane);
	}
	
	private void setMainFileExplorer(FileExplorer explorer) {
		if (explorer == this.explorer) return;
		if (this.explorer != null)
			this.explorer.closePanel();
		this.explorer = explorer;
	}
	
	private void switchMainPane(Pane pane) {
		if(pane instanceof FileExplorer)
			setMainFileExplorer((FileExplorer) pane);
		else
			setMainFileExplorer(null);
		this.mainPanel.setCenter(pane);
	}
	
	private Stage popupStage = null;
    
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