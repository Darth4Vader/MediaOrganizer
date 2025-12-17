package JavaFXInterface.ManageFileExplorer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import FileUtilities.FilesUtils;
import JavaFXInterface.Logger.CreateMovieLoggerControl;
import OtherUtilities.ImageUtils;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class FileInfoMenuBar extends MenuBar {
	
	private final Map<ToolName, ToolPanel> toolMap;
	private FileInfoExplorer explorer;
	private File currentFileToInfo;
	private CreateMovieLoggerControl createMovieLoggerControl;

	public FileInfoMenuBar(FileInfoExplorer explorer) {
		this.explorer = explorer;
		this.toolMap = new HashMap<>();
		for(ToolName tool : ToolName.values())
			this.toolMap.put(tool, new ToolPanel(tool));
		getMenus().add(toolMap.get(ToolName.ORGANIZE_FOLDER));
		getMenus().add(toolMap.get(ToolName.RENAME_FILE));
		getMenus().add(toolMap.get(ToolName.REFRESH_LOGO));
		getMenus().add(toolMap.get(ToolName.SET_LOGO));
		getMenus().add(toolMap.get(ToolName.SET_SUBTITLES));
		this.createMovieLoggerControl = new CreateMovieLoggerControl(explorer.getFolderManager());
		getStylesheets().add(FileInfoMenuBar.class.getResource("FileInfoToolsPanel.css").toExternalForm());
	}
	
	
	private void addInfoPanel(ToolName tool) {
		FileInfoPanel infoPanel = explorer.getFileInfoPanel();
		boolean exists = explorer.getChildren().remove(infoPanel);
		switch (tool) {
		case ORGANIZE_FOLDER:
			startMoving();
			break;
		case REFRESH_LOGO:
			break;
		case RENAME_FILE:
			if(!exists) {
				infoPanel.setPanel(currentFileToInfo);
				if(infoPanel != null && explorer.getRight() == null) {
					explorer.setRight(infoPanel);
				}
			}
			break;
		case SET_LOGO:
			explorer.getFolderManager().createIconToFolder(currentFileToInfo);
			break;
		case SET_SUBTITLES:
			break;
		default:
			break;
		}
	}
	
	private void startMoving() {
		// first we will create a task in order to push the custom logger message to ListView by updating JavaFX
		Task<Void> task = new Task<Void>() {
			
			@Override
			protected Void call() throws Exception {
				explorer.getFolderManager().moveFilesFromInput();
				return null;
			}
		};
		task.setOnFailed(v -> {
			// we will handle every possible exception of the method that we call
			createMovieLoggerControl.close();
			Throwable exception = task.getException();
			UncaughtExceptionHandler exceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
			if(exceptionHandler != null)
				exceptionHandler.uncaughtException(Thread.currentThread(), exception);
			else {
				System.out.println("NOOOOOOOOOOOOOOOOOO");
				System.err.println(exception.getMessage());
				exception.printStackTrace();
			}
			/*if (exception instanceof NumberFormatException) {
				AdminPagesUtils.parseNumberException(movie.getMediaID());
			} else if (exception instanceof CreateMovieException) {
				// when the movie creation fails, we will alert the user of the reasons
				AdminPagesUtils.createMovieExceptionAlert((CreateMovieException) exception);
			} else if (exception instanceof CanUpdateException) {
				// if the movie already exists, then we will alert the user that he can update the movie if he wants
				showAlertThatMovieCanUpdate((CanUpdateException) exception);
			} else { // and if the exception is not specified in the method, like RuntimeException, then we will let the default handler to handle it
				Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), exception);
			}*/
		});
		// we will add a successful notify id the method finish
		task.setOnSucceeded(v -> {
			createMovieLoggerControl.finishedTask();
		});
		// if the task is cancelled, before the method finish, then we notify the user of the consequences
		task.setOnCancelled(v -> {
			//createMovieLoggerControl.close();
		});
		// we will open the custom view with the task
		createMovieLoggerControl.start(task);
		// and then start the task as a new thread, in order to synchronize with JavaFX
		new Thread(task).start();
	}
	
	public void updateToolPanels(File currentFileToInfo) {
		this.currentFileToInfo = currentFileToInfo;
		for(ToolPanel tool : this.toolMap.values() ) {
			tool.setUsage(currentFileToInfo);
		}
	}
	
	public void restartToolPanels() {
		this.currentFileToInfo = null;
		for(ToolPanel tool : this.toolMap.values() ) {
			tool.setUsage(null);
		}
	}
	
	
	private enum ToolName {
		ORGANIZE_FOLDER("Organize_Folder", "Organize Folder"),
		RENAME_FILE("Rename_file", "Rename file"),
		REFRESH_LOGO("Refresh_File_Logo", "Refresh File Logo"),
		SET_LOGO("Set_File_Logo", "Set File Logo"),
		SET_SUBTITLES("set_main_subtitles_language", "set main subtitles language");
		
		
		private final String id;
		private final String name;
		
		private ToolName(String id, String name) {
			this.id = id;
			this.name = name;
		}
		
		public String getID() {
			return this.id;
		}
		
		public String getName() {
			return this.name;
		}	
	}
	
	private class ToolPanel extends Menu { 
		
		public ToolPanel(ToolName tool) {
			BorderPane pane = new BorderPane();
			String fileName = tool.getID() + ".png";
			String fileRealtivePath = "images" + FilesUtils.addPackageName(ToolPanel.class, fileName);
			java.awt.Image img;
			try {
				img = ImageUtils.getImageResource(ToolPanel.class, fileRealtivePath);
			} catch (URISyntaxException | IOException e) {
				e.printStackTrace();
				img = null;
			}
			final BufferedImage image = img != null ? ImageUtils.paintImageInColor(img, java.awt.Color.BLACK) : null;
			ImageView iconImg = new ImageView();
			iconImg.fitWidthProperty().bind(pane.widthProperty().multiply(0.6));
			iconImg.fitHeightProperty().bind(pane.prefHeightProperty().multiply(0.6));
			if(image != null) {
				iconImg.setImage(SwingFXUtils.toFXImage(image, null));
			}
			pane.setCenter(iconImg);
			Label lblName = new Label(tool.getName());
			pane.setBottom(lblName);
			
			pane.prefHeightProperty().bind(FileInfoMenuBar.this.heightProperty());
			this.setGraphic(pane);
			
			pane.setOnMouseClicked(_ -> {
				if(!this.isDisable())
                    addInfoPanel(tool);
			});
		}
		
		public void setUsage(File file) {
			setDisable(!(file != null && file.isDirectory()));
		}
	}
}