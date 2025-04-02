package JavaFXInterface.FileExplorerView;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import JavaFXInterface.FileExplorer.FileExplorer;
import JavaFXInterface.FileExplorer.HistoryView;
import Utils.DirectoryWatcher.DirectoryWatcher;
import Utils.DirectoryWatcher.FileChange;
import Utils.DirectoryWatcher.HandleFileChanges;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

public class MainFileExplorerView extends BorderPane {
	
	public static enum FileExplorerView {
		DETAILS,
		ICONS,
		CONTENT
	}
	
	private FileViewMode<?> fileView;
	private SimpleObjectProperty<File> folder;
	private DirectoryWatcher w;
	private ContextMenu switchMenu;
	private FileExplorer fileExplorer;
	private FileExplorerView fileExplorerViewType;

	public MainFileExplorerView(FileExplorer fileExplorer, FileExplorerView fileExplorerView) {
		this.fileExplorer = fileExplorer;
		this.fileTableDetailsView = (detailsView) -> detailsView;
		this.fileTableIconView = (iconsView) -> iconsView;
		this.fileTableContentView = (contentView) -> contentView;
		w = new DirectoryWatcher();
	    w.setHandleFileChanges(new HandleFileChanges() {
			
			@Override
			public void handleFileChanges(List<FileChange> fileChanges) {
				for(FileChange fileChange : fileChanges) {
					Platform.runLater(() -> {
						if(getFileTableHandler() != null) {
							getFileTableHandler().handleFileChange(fileChange);
						}
					});
				}
			}
		});
	    this.folder = new SimpleObjectProperty<>();
		this.folder.addListener((_, _, _) -> {
			System.out.println("rreee");
			w.shutdown();
			System.out.println("relax");
			//t.s
		});
		this.parentProperty().addListener((_, _, _) -> {
			closePanel();
		});
		this.switchMenu = getSwitchViewMenu();
		setFileExplorerView(fileExplorerView);
	}
	
	public void setFileExplorerView(FileExplorerView view) {
		if (this.fileExplorerViewType == view)
			return;
		this.fileExplorerViewType = view;
		if (getFileTableView() != null) {
			getFileTableView().closePanel();
		}
		fileView = switch(view) {
			case DETAILS -> fileTableDetailsView.call(getDefualtFileDetailsView());
			case ICONS -> fileTableIconView.call(getDefualtFileIconView());
			case CONTENT -> fileTableContentView.call(getDefualtFileContentView());
			default -> fileView;
		};
		Control fileViewPane = getFileView();
		fileViewPane.setOnMouseClicked(e -> {
			System.out.println(e);
			if (e.getButton() == MouseButton.SECONDARY) {
				switchMenu.show(this, e.getScreenX(), e.getScreenY());
			}
		});
		enterFolder(getFolder());
		this.setCenter(fileViewPane);
	}
	
	private void refreshFileExplorerView() {
		this.fileView = switch(this.fileExplorerViewType) {
			case DETAILS -> fileTableDetailsView.call(getDefualtFileDetailsView());
			case ICONS -> fileTableIconView.call(getDefualtFileIconView());
			case CONTENT -> fileTableContentView.call(getDefualtFileContentView());
			default -> fileView;
		};
		Control fileViewPane = getFileView();
		enterFolder(getFolder());
		fileViewPane.setOnMouseClicked(e -> {
			System.out.println(e);
			if (e.getButton() == MouseButton.SECONDARY) {
				switchMenu.show(this, e.getScreenX(), e.getScreenY());
			}
		});
		this.setCenter(fileViewPane);
	}
	
	private ContextMenu getSwitchViewMenu() {
		ContextMenu menu = new ContextMenu();
		MenuItem imageView = new MenuItem("Icons View");
		imageView.setOnAction(_ -> {
			setFileExplorerView(FileExplorerView.ICONS);
		});
		MenuItem detailsView = new MenuItem("Details View");
		detailsView.setOnAction(_ -> {
			setFileExplorerView(FileExplorerView.DETAILS);
		});
		MenuItem contentView = new MenuItem("Content View");
		contentView.setOnAction(_ -> {
			setFileExplorerView(FileExplorerView.CONTENT);
		});
		menu.getItems().addAll(imageView, detailsView, contentView);
		menu.setAutoHide(true);
		return menu;
	}
	
	public Control getFileView() {
		return this.fileView != null ? this.fileView.getFileView() : null;
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
		if (getFileTableView() != null) {
			File focuedFile = historyView.getFocusedFile();
			if (focuedFile != null) {
				getFileTableView().setFileToBeSelected(focuedFile);
			}
		}
	}
	
	protected HistoryView createHistoryView(File folder) {
		return new HistoryView(folder);
	}
	
	protected final void loadHistoryView(HistoryView historyView) {
		if(getFileTableView() != null) {
			ObservableList<File> selectedFiles = getFileTableView().getSelectedFiles();
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
		
		if(getFileTableView() != null) {
			getFileTableView().setFiles(files);
		}
		getFileView().requestFocus();
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
		if (getFileTableView() != null) {
			getFileTableView().closePanel();
		}
	}
	
	public File getFolder() {
		return this.folder.get();
	}
	
	private Callback<FileTableDetailsView, FileTableDetailsView> fileTableDetailsView;
	
	public final void setFileDetailsView(Callback<FileTableDetailsView, FileTableDetailsView> fileTableDetailsView) {
		this.fileTableDetailsView = fileTableDetailsView;
		if(this.fileView instanceof FileTableDetailsView)
            refreshFileExplorerView();
	}
	
	public FileTableDetailsView getDefualtFileDetailsView() {
		return new FileTableDetailsView(this);
	}
	
	private Callback<FileTableIconView, FileTableIconView> fileTableIconView;
	
	public final void setFileIconView(Callback<FileTableIconView, FileTableIconView> fileTableIconView) {
		this.fileTableIconView = fileTableIconView;
		if(this.fileView instanceof FileTableIconView)
			refreshFileExplorerView();
	}
	
	public FileTableIconView getDefualtFileIconView() {
		return new FileTableIconView(fileExplorer);
	}
	
	private Callback<FileTableContentView, FileTableContentView> fileTableContentView;
	
	public final void setFileContentView(Callback<FileTableContentView, FileTableContentView> fileTableContentView) {
		this.fileTableContentView = fileTableContentView;
		if(this.fileView instanceof FileTableContentView)
			refreshFileExplorerView();
	}
	
	public FileTableContentView getDefualtFileContentView() {
		return new FileTableContentView();
	}
	
	private FileTableView<?> getFileTableView() {
		return this.fileView != null ? this.fileView.getFileTableView() : null;
	}
	
	private FileTableHandler getFileTableHandler() {
		return this.fileView != null ? this.fileView.getFileTableHandler() : null;
	}

}

interface FileTableHandler {
	public void handleFileChange(FileChange fileChange);
}