package JavaFXInterface;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;

import DataStructures.ManageFolder;
import DataStructures.App.MainAppData;
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
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ActivateFileExplorer extends Application {
	
	
	/*
	 * --module-path "C:\JavaFX_22.02\lib" --add-modules javafx.controls,javafx.fxml
--add-opens=javafx.controls/javafx.scene.control.skin=ALL-UNNAMED
--add-opens=javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED
	 */
	
	public static final File APP_DATA_FILE = Path.of(System.getenv("APPDATA"), "MediaOrganizerApp", "Data", "appData.json").toFile();
	
	private FileExplorer explorer;
	private BorderPane mainPanel;
	
	private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
    	this.stage = stage;
    	this.mainPanel = new BorderPane();
    	
    	setSetupPane();
    	
    	/*File file = new File("C:\\Users\\itay5\\OneDrive\\Pictures\\Main");
		ManageFolder manage = new ManageFolder(file.getAbsolutePath());
		setManageFolderExplorer(manage);*/
    	
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
	    			try {
						managePojo = MainAppDataJson.addManageFolderPojo(APP_DATA_FILE, manage);
		    			setManageFolderExplorer(manage);
					} catch (IOException e) {
						e.printStackTrace();
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle("Error");
						alert.setHeaderText("Failed to save manage folder");
						alert.setContentText("An error occurred while saving the manage folder to the app data file.");
						alert.showAndWait();
					}
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
		
		Button loadAppData = new Button("Import App Data");
		loadAppData.setOnAction(_ -> {
			FileChooser chooser = new FileChooser();
			chooser.getExtensionFilters().add(new ExtensionFilter("JSON Files", "*.json"));
			chooser.setTitle("Choose App Data File");
			File file = chooser.showOpenDialog(stage);
			if (file == null) return;
			MainAppData mainAppData;
			try {
				// load the app data from the selected file
				mainAppData = MainAppDataJson.loadMainAppData(file);
				
				// save the app data to the default location
				try {
					MainAppDataJson.saveMainAppData(APP_DATA_FILE, mainAppData);
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Success");
					alert.setHeaderText("App data loaded successfully");
					alert.setContentText("The app data has been successfully loaded and saved to the default location.");
					alert.showAndWait();
				}
				catch (IOException e) {
					e.printStackTrace();
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Failed to save app data");
					alert.setContentText("An error occurred while saving the app data to the default location.");
					alert.showAndWait();
				}
			} catch (IOException e) {
				e.printStackTrace();
				// problems reading the file
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Failed to load app data");
				alert.setContentText("The selected file could not be read or is not a valid app data file.");
				alert.showAndWait();
			}
		});
		
		Button exportAppData = new Button("Export App Data");
		exportAppData.setOnAction(_ -> {
			SaveFileAction action = file -> {
				try {
					MainAppData mainAppData = MainAppDataJson.loadMainAppData(APP_DATA_FILE);
					try {
						MainAppDataJson.saveMainAppData(file, mainAppData);
						// close the popup stage
						popupStage.close();
						// show success alert
						Alert alert = new Alert(Alert.AlertType.INFORMATION);
						alert.setTitle("Success");
						alert.setHeaderText("App data exported successfully");
						alert.setContentText("The app data has been successfully exported to the selected file: " + file.getAbsolutePath());
						alert.showAndWait();
					} catch (IOException e) {
						e.printStackTrace();
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle("Error");
						alert.setHeaderText("Failed to save app data");
						alert.setContentText("An error occurred while saving the app data to the selected file: " + file.getAbsolutePath());
						alert.showAndWait();
					}
				} catch (IOException e) {
					e.printStackTrace();
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Failed to load app data");
					alert.setContentText("An error occurred while loading the app data from the default location.");
					alert.showAndWait();
				}
			};
			GridPane saveFileChooser = createSaveFileChooser(action, ".json");
			if (popupStage != null)
				popupStage.close();
			popupStage = new Stage();
			popupStage.setScene(new Scene(saveFileChooser));
			popupStage.initOwner(stage);
			popupStage.initModality(Modality.APPLICATION_MODAL);
			popupStage.setAlwaysOnTop(true);
			popupStage.show();
		});
		
		mainPane.getChildren().addAll(loadAppData, exportAppData);
		
		this.mainPanel.setCenter(mainPane);
	}
	
	private interface SaveFileAction {
		void save(File file);
	}
	
	private GridPane createSaveFileChooser(SaveFileAction action, String... fileTypes) {
		GridPane saveFileChooser = new GridPane(3, 3);
		
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setHgrow(javafx.scene.layout.Priority.ALWAYS);
		saveFileChooser.getColumnConstraints().add(col1);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setHgrow(javafx.scene.layout.Priority.NEVER);
		saveFileChooser.getColumnConstraints().add(col2);
		
		saveFileChooser.add(new Label("File Name:"), 0, 0);
		TextField fileNameField = new TextField();
		saveFileChooser.add(fileNameField, 0, 1);
		saveFileChooser.add(new Label("File Type:"), 1, 0);
		ComboBox<String> fileTypeCombo = new ComboBox<>();
		saveFileChooser.add(fileTypeCombo, 1, 1);
		saveFileChooser.add(new Label("Directory:"), 0, 2);
		TextField directoryField = new TextField();
		directoryField.setEditable(false);
		saveFileChooser.add(directoryField, 0, 3);
		Button directoryButton = new Button("Change");
		saveFileChooser.add(directoryButton, 1, 3);
		HBox saveButtonPane = new HBox();
		saveButtonPane.setAlignment(Pos.CENTER);
		Button saveButton = new Button("Save");
		saveButtonPane.getChildren().add(saveButton);
		saveFileChooser.add(saveButtonPane, 0, 4, 2, 1);
		
		fileTypeCombo.getItems().addAll(fileTypes);
		if(fileTypeCombo.getItems().size() == 1) {
			fileTypeCombo.getSelectionModel().selectFirst();
		}
		fileTypeCombo.setMaxWidth(Double.MAX_VALUE);
		directoryButton.setMaxWidth(Double.MAX_VALUE);
		
		saveButton.disableProperty().bind(fileNameField.textProperty().isEmpty()
                .or(fileTypeCombo.getSelectionModel().selectedItemProperty().isNull()
                        .or(directoryField.textProperty().isEmpty())));
		directoryButton.setOnAction(_ -> {
	        DirectoryChooser directoryChooser = new DirectoryChooser();
	        File selectedDirectory = directoryChooser.showDialog(popupStage);
	        if (selectedDirectory != null) directoryField.setText(selectedDirectory.getAbsolutePath());
		});
		saveButton.setOnAction(_ -> {
	        Path file = Path.of(directoryField.getText(), fileNameField.getText().trim() + fileTypeCombo.getSelectionModel().getSelectedItem());
        	action.save(file.toFile());
		});
		
		return saveFileChooser;
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
			try {
				List<ManageFolderHistory> list = MainAppDataJson.loadManageFolderHistoryPojo(APP_DATA_FILE, true);
				table.getItems().addAll(list);
			} catch (IOException e) {
				e.printStackTrace();
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Failed to load manage folder history");
				alert.setContentText("An error occurred while loading the manage folder history from the app data file.");
				alert.showAndWait();
			}
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
			try {
				managePojo = MainAppDataJson.updateManageFolderHistory(APP_DATA_FILE, selected);
			} catch (IOException e) {
				e.printStackTrace();
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Failed to update manage folder");
				alert.setContentText("An error occurred while updating the manage folder path in the app data file.");
				alert.showAndWait();
			}
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
		
		Menu setupPage = new Menu();
		Label setupPageLabel = new Label("Back to Setup Page");
		setupPageLabel.setOnMouseClicked(_ -> {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Confirmation");
			alert.setHeaderText("Are you sure you want to go back to the setup page?");
			alert.setContentText("This will close the current manage folder and return to the setup page.");
			alert.showAndWait().ifPresent(response -> {
				if (response == javafx.scene.control.ButtonType.OK) {
					// User confirmed, go back to setup page
					setSetupPane();
				}
			});
		});
		setupPage.setGraphic(setupPageLabel);
		
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
				try {
					managePojo = MainAppDataJson.updateManageFolderHistory(APP_DATA_FILE, managePojo);
				} catch (IOException e) {
					e.printStackTrace();
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Failed to update manage folder settings");
					alert.setContentText("An error occurred while updating the manage folder settings (custom name) in the app data file.");
					alert.showAndWait();
				}
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
					try {
						managePojo = MainAppDataJson.updateManageFolderHistory(APP_DATA_FILE, managePojo, manage);
					} catch (IOException e) {
						e.printStackTrace();
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle("Error");
						alert.setHeaderText("Failed to update manage folder");
						alert.setContentText("An error occurred while updating the manage folder path in the app data file.");
						alert.showAndWait();
					}
				}
			});
			setMainFileExplorer(manageSelector);
			mainPane.setCenter(this.explorer);
		});
		
		settings.getItems().addAll(settingsAttributes, settingsMainFiles);
		menuBar.getMenus().addAll(fileExplorer, settings, setupPage);
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