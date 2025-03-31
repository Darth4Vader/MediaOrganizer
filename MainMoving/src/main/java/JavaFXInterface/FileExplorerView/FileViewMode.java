package JavaFXInterface.FileExplorerView;

import javafx.scene.control.Control;

public interface FileViewMode<T> {
	
	public FileTableView<T> getFileTableView();
	
	public FileTableHandler getFileTableHandler();
	
	public Control getFileView();
}