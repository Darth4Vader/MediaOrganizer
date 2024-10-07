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
import JavaFXInterface.controlsfx.GridViewSelection;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import javafx.concurrent.Task;
import javafx.scene.control.Control;
import javafx.scene.layout.BorderPane;

public class MainFileExplorerView extends BorderPane {
	
	public static enum FileExplorerView {
		DETAILS,
		ICONS
	}
	
	private Control fileView;
	private SimpleObjectProperty<File> folder;
	private WatchExample w;

	public MainFileExplorerView(FileExplorerView fileExplorerView) {
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
		setFileExplorerView(fileExplorerView);
	}
	
	public void setFileExplorerView(FileExplorerView view) {
		switch(view) {
		case DETAILS:
			fileView = new FileTableDetailsView();
			break;
		case ICONS:
			fileView = new FileTableIconView();
			break;
		default:
			break;
		}
		this.setCenter(fileView);
	}
	
	public Control getFileView() {
		return this.fileView;
	}
	
	public void setMainPanel(String path) {
		setMainPanel(new File(path));
	}

	public void setMainPanel(File folder) {
		setMainPanel(folder, null);
	}
	
	private void setMainPanel(File folder, File toFocus) {
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

}

interface FileTableHandler {
	public void handleFileChange(FileChange fileChange);
}