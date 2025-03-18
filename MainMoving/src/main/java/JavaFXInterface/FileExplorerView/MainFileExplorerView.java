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
import JavaFXInterface.FileExplorer.FileExplorer;
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
	private FileExplorerView fileExplorerViewType;

	public MainFileExplorerView(FileExplorer fileExplorer, FileExplorerView fileExplorerView) {
		this.fileExplorer = fileExplorer;
		this.fileTableDetailsView = (detailsView) -> detailsView;
		this.fileTableIconView = (iconsView) -> iconsView;
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
		this.folder.addListener((obs, oldV, newV) -> {
			fileExplorer.nextFileHistory(newV);
		});
		this.switchMenu = getSwitchViewMenu();
		setFileExplorerView(fileExplorerView);
	}
	
	public void setFileExplorerView(FileExplorerView view) {
		this.fileExplorerViewType = view;
		switch(view) {
		case DETAILS:
			fileView = fileTableDetailsView.call(getDefualtFileDetailsView());
			break;
		case ICONS:
			fileView = fileTableIconView.call(getDefualtFileIconView());
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
	
	private void refreshFileExplorerView() {
		switch(this.fileExplorerViewType) {
		case DETAILS:
			fileView = fileTableDetailsView.call(getDefualtFileDetailsView());
			break;
		case ICONS:
			fileView = fileTableIconView.call(getDefualtFileIconView());
			break;
		default:
			break;
		}
		setMainPanel(folder.get());
		fileView.setOnMouseClicked(e -> {
			System.out.println(e);
			if (e.getButton() == MouseButton.SECONDARY) {
				switchMenu.show(this, e.getScreenX(), e.getScreenY());
			}
		});
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
		setMainPanelFocus(folder, null);
	}
	
	public void setMainPanel(File folder, List<File> subFiles) {
		closePanel();
		if(folder == null  || !folder.isDirectory())
			return;
		this.folder.set(folder);
		
		List<File> files = subFiles.stream()
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
	
	private void setMainPanelFocus(File folder, File toFocus) {
		if(folder == null  || !folder.isDirectory())
			return;
		setMainPanel(folder, Arrays.asList(folder.listFiles()));
	}
	
	private Thread t;
	
	public void closePanel() {
		w.shutdown();
	}
	
	public void goToParentFile(File file) {
		setMainPanelFocus(file.getParentFile(), file);
	}
	
	public File getFolder() {
		return this.folder.get();
	}
	
	private Callback<FileTableDetailsView, FileTableDetailsView> fileTableDetailsView;
	
	public void setFileDetailsView(Callback<FileTableDetailsView, FileTableDetailsView> fileTableDetailsView) {
		this.fileTableDetailsView = fileTableDetailsView;
		if(this.fileView instanceof FileTableDetailsView)
            refreshFileExplorerView();
	}
	
	public FileTableDetailsView getDefualtFileDetailsView() {
		return new FileTableDetailsView(this);
	}
	
	private Callback<FileTableIconView, FileTableIconView> fileTableIconView;
	
	public void setFileIconView(Callback<FileTableIconView, FileTableIconView> fileTableIconView) {
		this.fileTableIconView = fileTableIconView;
		if(this.fileView instanceof FileTableIconView)
			refreshFileExplorerView();
	}
	
	public FileTableIconView getDefualtFileIconView() {
		return new FileTableIconView(fileExplorer);
	}

}

interface FileTableHandler {
	public void handleFileChange(FileChange fileChange);
}