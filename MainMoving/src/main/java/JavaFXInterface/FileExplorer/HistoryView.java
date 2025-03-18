package JavaFXInterface.FileExplorer;

import java.io.File;

import JavaFXInterface.FileExplorerView.MainFileExplorerView.FileExplorerView;

public class HistoryView {
	
	private File folder;
	private File focusedFile;
	private FileExplorerView fileExplorerView;
	private boolean isSearch;
	
	public HistoryView(File folder) {
		this.folder = folder;
		this.focusedFile = null;
		this.fileExplorerView = null;
		this.isSearch = false;
	}
	
	public File getFolder() {
		return folder;
	}
	
	public File getFocusedFile() {
        return focusedFile;
    }
	
	public FileExplorerView getFileExplorerView() {
		return fileExplorerView;
	}
	
	public boolean isSearch() {
		return isSearch;
	}
	
	public void setFocusedFile(File focusedFile) {
		this.focusedFile = focusedFile;
	}
	
	public void setFileExplorerView(FileExplorerView fileExplorerView) {
		this.fileExplorerView = fileExplorerView;
	}
	
	public void setIsSearch(boolean isSearch) {
		this.isSearch = isSearch;
	}
}