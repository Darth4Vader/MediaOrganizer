package JavaFXInterface.FileExplorerView;

import java.io.File;
import java.util.Collection;

import Utils.DirectoryWatcher.FileChange;
import Utils.DirectoryWatcher.FileRename;
import Utils.DirectoryWatcher.FileChange.FileChaneType;
import javafx.collections.ObservableList;

public abstract class FileTableManager implements FileTableHandler, FileTableView<File> {

	@Override
	public void handleFileChange(FileChange fileChange) {
		System.out.println(fileChange.getFileChaneType() + " " + fileChange.getPath());
		FileChaneType type = fileChange.getFileChaneType();
		File file = fileChange.getPath().toFile();
		ObservableList<File> fileList = this.getItems();
		switch(type) {
		case CREATED:
			if(!file.isHidden())
				fileList.add(file);
			break;
		case DELETED:
			fileList.remove(0);
			break;
		case RENAMED:
			if(fileChange instanceof FileRename fileRename) {
			    int itemIndex = fileList.indexOf(file);
			    if (itemIndex != -1) {
			    	File newFile = fileRename.getNewPath().toFile();
			        fileList.set(itemIndex, newFile);
			    }
			}
			break;
		case UPDATED:
			break;
		default:
			break;
		}
	}

	@Override
	public void setFiles(Collection<File> files) {
		getItems().setAll(files);
	}

	@Override
	public void closePanel() {
		
	}
}