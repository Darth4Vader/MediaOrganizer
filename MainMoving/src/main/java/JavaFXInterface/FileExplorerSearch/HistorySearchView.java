package JavaFXInterface.FileExplorerSearch;

import java.io.File;
import java.io.IOException;
import java.util.List;

import JavaFXInterface.FileExplorer.HistoryView;

public class HistorySearchView extends HistoryView {
	
	private String search;
	
	public HistorySearchView(File folder) {
		super(folder);
		this.search = "";
	}
	
	public String getSearch() {
		return search;
	}
	
	public void setSearch(String search) {
        this.search = search;
	}
	
	@Override
	public List<File> getFiles() {
		if (search == null)
			return null;
		try {
			List<File> files = FileSearch.searchFiles(getFolder(), search);
			return files;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
