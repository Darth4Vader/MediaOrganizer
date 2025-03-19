package JavaFXInterface.FileExplorer;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import JavaFXInterface.FileExplorerView.MainFileExplorerView.FileExplorerView;

public class HistoryView {
	
	private File folder;
	private File focusedFile;
	private FileExplorerView fileExplorerView;
	
	public HistoryView(File folder) {
		this.folder = folder;
		this.focusedFile = null;
		this.fileExplorerView = null;
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
	
	public void setFocusedFile(File focusedFile) {
		this.focusedFile = focusedFile;
	}
	
	public void setFileExplorerView(FileExplorerView fileExplorerView) {
		this.fileExplorerView = fileExplorerView;
	}
	
	public List<File> getFiles() {
		return Arrays.asList(folder.listFiles());
	}
}