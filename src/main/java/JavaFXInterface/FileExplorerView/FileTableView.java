package JavaFXInterface.FileExplorerView;

import java.io.File;
import java.util.Collection;

import javafx.collections.ObservableList;

public interface FileTableView<T> {
	
	public ObservableList<T> getItems();
	
	public ObservableList<T> getSelectedItems();
	
	public ObservableList<File> getSelectedFiles();
	
	public void setFiles(Collection<File> files);
	
	public void setFileToBeSelected(File file);
	
	public void closePanel();

}
