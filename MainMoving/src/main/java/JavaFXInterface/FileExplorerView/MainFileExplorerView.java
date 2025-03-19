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
import JavaFXInterface.FileExplorer.HistoryView;
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
		this.parentProperty().addListener((obs, oldV, newV) -> {
			closePanel();
		});
		this.switchMenu = getSwitchViewMenu();
		setFileExplorerView(fileExplorerView);
	}
	
	public void setFileExplorerView(FileExplorerView view) {
		if (this.fileExplorerViewType == view)
			return;
		this.fileExplorerViewType = view;
		if (fileView instanceof FileTableView) {
			((FileTableView<?>) fileView).closePanel();
		}
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
		enterFolder(getFolder());
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
		enterFolder(getFolder());
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
	
	public HistoryView enterFolder(File folder) {
		if(folder == null  || !folder.isDirectory())
			return null;
		HistoryView historyView = createHistoryView(folder);
		loadHistoryView(historyView);
		setMainPanel(historyView);
		return historyView;
	}
	
	public void enterHistoryView(HistoryView historyView) {
		if (historyView == null)
			return;
		setMainPanel(historyView);
		if (fileView instanceof FileTableView) {
			File focuedFile = historyView.getFocusedFile();
			if (focuedFile != null) {
				((FileTableView<?>) fileView).setFileToBeSelected(focuedFile);
			}
		}
	}
	
	protected HistoryView createHistoryView(File folder) {
		return new HistoryView(folder);
	}
	
	protected final void loadHistoryView(HistoryView historyView) {
		if(fileView instanceof FileTableView) {
			ObservableList<File> selectedFiles = ((FileTableView<?>) fileView).getSelectedFiles();
			if(selectedFiles.size() == 1) {
				historyView.setFocusedFile(selectedFiles.get(0));
			}
		}
	}
	
	private void setMainPanel(HistoryView historyView) {
		if (historyView == null)
			return;
		File folder = historyView.getFolder();
		closePanel();
		if(folder == null  || !folder.isDirectory())
			return;
		this.folder.set(folder);
		
		List<File> subFiles = historyView.getFiles();
		if(subFiles == null)
			return;
		
		List<File> files = subFiles.stream()
		.filter((f) -> !f.isHidden()).collect(Collectors.toList());
		
		if(fileView instanceof FileTableView) {
			((FileTableView<?>) fileView).setFiles(files);
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
		if (fileView instanceof FileTableView) {
			((FileTableView<?>) fileView).closePanel();
		}
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