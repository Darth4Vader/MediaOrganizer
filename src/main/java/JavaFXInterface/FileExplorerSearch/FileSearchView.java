package JavaFXInterface.FileExplorerSearch;

import java.io.File;
import java.io.IOException;
import java.util.List;

import JavaFXInterface.FileExplorer.FileExplorer;
import JavaFXInterface.FileExplorer.HistoryView;
import JavaFXInterface.FileExplorerView.MainFileExplorerView;
import JavaFXInterface.FileExplorerView.MainFileExplorerView.FileExplorerView;

public class FileSearchView extends MainFileExplorerView {
	
	private File searchFolder;
	private String search;
	
	public FileSearchView(FileExplorer fileExplorer, FileExplorerView fileExplorerView) {
		super(fileExplorer, fileExplorerView);
	}
	
	public HistoryView search(File searchFolder, String search) {
		this.search = search;
		return this.enterFolder(searchFolder);
	}

	@Override
	protected HistoryView createHistoryView(File folder) {
		HistorySearchView historySearchView = new HistorySearchView(folder);
		historySearchView.setSearch(search);
		return historySearchView;
	}
}
