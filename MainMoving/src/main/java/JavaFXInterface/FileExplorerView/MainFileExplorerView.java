package JavaFXInterface.FileExplorerView;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import DirectoryWatcher.FileChange;
import DirectoryWatcher.FileRename;
import DirectoryWatcher.HandleFileChanges;
import DirectoryWatcher.WatchExample;
import DirectoryWatcher.FileChange.FileChaneType;
import FileUtils.FileDetails;
import JavaFXInterface.FileExplorer;
import JavaFXInterface.controlsfx.GridViewSelection;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import javafx.concurrent.Task;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

public class MainFileExplorerView extends BorderPane {
	
	public static enum FileExplorerView {
		DETAILS,
		ICONS
	}
	
	private Control fileView;
	private SimpleObjectProperty<File> folder;
	private WatchExample w;
	private ContextMenu switchMenu;
	private FileExplorer fileExplorer;

	public MainFileExplorerView(FileExplorer fileExplorer, FileExplorerView fileExplorerView) {
		this.fileExplorer = fileExplorer;
		this.fileTableDetailsView = (_) -> getDefualtFileDetailsView();
		this.fileTableIconView = (_) -> getDefualtFileIconView();
		w = new WatchExample();
	    w.setHandleFileChanges(new HandleFileChanges() {
			
			@Override
			public void handleFileChanges(List<FileChange> fileChanges) {
				for(FileChange fileChange : fileChanges) {
					Platform.runLater(() -> {
						if(fileView instanceof FileTableHandler) {
							((FileTableHandler) fileView).handleFileChange(fileChange);
						}
					});
				}
			}
		});
	    this.folder = new SimpleObjectProperty<>();
		this.folder.addListener((obs, oldV, newV) -> {
			System.out.println("rreee");
			w.shutdown();
			System.out.println("relax");
			//t.s
		});
		this.switchMenu = getSwitchViewMenu();
		setFileExplorerView(fileExplorerView);
	}
	
	public void setFileExplorerView(FileExplorerView view) {
		switch(view) {
		case DETAILS:
			fileView = fileTableDetailsView.call(null);
			break;
		case ICONS:
			fileView = fileTableIconView.call(null);
			break;
		default:
			break;
		}
		fileView.setOnMouseClicked(e -> {
			System.out.println(e);
			if (e.getButton() == MouseButton.SECONDARY) {
				switchMenu.show(this, e.getScreenX(), e.getScreenY());
			}
		});
		setMainPanel(folder.get());
		this.setCenter(fileView);
	}
	
	private ContextMenu getSwitchViewMenu() {
		ContextMenu menu = new ContextMenu();
		MenuItem imageView = new MenuItem("Icons View");
		imageView.setOnAction(a -> {
			setFileExplorerView(FileExplorerView.ICONS);
		});
		MenuItem detailsView = new MenuItem("Details View");
		detailsView.setOnAction(a -> {
			setFileExplorerView(FileExplorerView.DETAILS);
		});
		menu.getItems().addAll(imageView, detailsView);
		menu.setAutoHide(true);
		return menu;
	}
	
	public Control getFileView() {
		return this.fileView;
	}
	
	public FileExplorer getFileExplorer() {
		return this.fileExplorer;
	}
	
	public void setMainPanel(String path) {
		setMainPanel(new File(path));
	}

	public void setMainPanel(File folder) {
		setMainPanel(folder, null);
	}
	
	private void setMainPanel(File folder, File toFocus) {
		closePanel();
		if(folder == null  || !folder.isDirectory())
			return;
		this.folder.set(folder);
		
		List<File> files = Arrays.asList(folder.listFiles()).stream()
		.filter((f) -> !f.isHidden()).collect(Collectors.toList());
		
		if(fileView instanceof FileTableDetailsView) {
			((FileTableDetailsView) fileView).setFile(files);
		}
		else if(fileView instanceof FileTableIconView) {
			((FileTableIconView) fileView).getItems().setAll(files);
		}
		fileView.requestFocus();
		Task<Void> task = new Task<Void>() {
			
			
			@Override
			protected Void call() throws Exception {
			    // Should launch WatchExample PER Filesystem:
				System.out.println("Let's Start");
				w.setToRun();
				System.out.println("Okkkkk");
				w.register(folder.toPath());
			    // For 2 or more WatchExample use: new Thread(w[n]::run).start();
			    System.out.println("Partyyyy");
				w.run();
			    System.out.println("\nthtats it\n");
				return null;
			}
		};
		t = new Thread(task);
		t.start();
	}
	
	private Thread t;
	
	public void closePanel() {
		w.shutdown();
	}
	
	public void goToParentFile(File file) {
		setMainPanel(file.getParentFile(), file);
	}
	
	private Callback<Void, FileTableDetailsView> fileTableDetailsView;
	
	public void setFileDetailsView(Callback<Void, FileTableDetailsView> fileTableDetailsView) {
		this.fileTableDetailsView = fileTableDetailsView;
	}
	
	public FileTableDetailsView getDefualtFileDetailsView() {
		return new FileTableDetailsView(this);
	}
	
	private Callback<Void, FileTableIconView> fileTableIconView;
	
	public void setFileIconView(Callback<Void, FileTableIconView> fileTableIconView) {
		this.fileTableIconView = fileTableIconView;
	}
	
	public FileTableIconView getDefualtFileIconView() {
		return new FileTableIconView(fileExplorer);
	}

}

interface FileTableHandler {
	public void handleFileChange(FileChange fileChange);
}