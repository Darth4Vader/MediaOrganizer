package JavaFXInterface.FileExplorerSearch;

import java.io.File;
import java.io.IOException;
import java.util.List;

import JavaFXInterface.FileExplorer.FileExplorer;
import JavaFXInterface.FileExplorerView.MainFileExplorerView;
import JavaFXInterface.FileExplorerView.MainFileExplorerView.FileExplorerView;

public class FileSearchView extends MainFileExplorerView{
	
	private File searchFolder;
	
	public FileSearchView(FileExplorer fileExplorer, FileExplorerView fileExplorerView) {
		super(fileExplorer, fileExplorerView);
	}
	
	public void search(File searchFolder, String search) {
		try {
			List<File> files = FileSearch.searchFiles(searchFolder, search);
			this.searchFolder = searchFolder;
			this.setMainPanel(searchFolder, files);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
