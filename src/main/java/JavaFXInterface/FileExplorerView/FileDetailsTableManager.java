package JavaFXInterface.FileExplorerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.apache.tika.exception.TikaException;
import org.controlsfx.control.tableview2.FilteredTableView;
import org.xml.sax.SAXException;

import Utils.DirectoryWatcher.FileChange;
import Utils.DirectoryWatcher.FileChange.FileChaneType;
import Utils.FileUtils.FileDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public abstract class FileDetailsTableManager implements FileTableHandler, FileTableView<FileDetails> {
	
	public abstract void setItems(ObservableList<FileDetails> items);
	
	private ExecutorService loadFileDetailsService;
	
	@Override
	public ObservableList<File> getSelectedFiles() {
		return this.getSelectedItems().stream()
				.map((f) -> f.getFile())
				.collect(Collectors.toCollection(FXCollections::observableArrayList));
	}

	@Override
	public void setFiles(Collection<File> files) {
		closePanel();
		
    	ObservableList<FileDetails> fileList = getItems();
    	if(fileList == null) {
    		fileList = FXCollections.observableArrayList();
    		setItems(fileList);
    	}
    	fileList.clear();
    	files.stream()
		.map(f -> {
			try {
				return new FileDetails(f);
			} catch (IOException | SAXException | TikaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}).filter(p -> p != null).forEach(fileList::add);
    	
    	loadFileDetailsService = Executors.newFixedThreadPool(10);
		for (FileDetails file : fileList) {
			/*CompletableFuture.supplyAsync(() -> {
				try {
					file.loadMetadata();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			});*/
			Task<Void> task = new Task<Void>() {
				
				
				@Override
				protected Void call() throws Exception {
					try {
						file.loadMetadata();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				}
			};
			loadFileDetailsService.execute(task);
			
			/*executorService.execute(() -> {
				try {
					file.loadMetadata();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});*/
		}
		loadFileDetailsService.shutdown();
	}
	
	@Override
	public void handleFileChange(FileChange fileChange) {
		System.out.println(fileChange.getFileChaneType() + " " + fileChange.getPath());
		FileChaneType type = fileChange.getFileChaneType();
		File file = fileChange.getPath().toFile();
		ObservableList<FileDetails> fileList = getItems();
		switch(type) {
		case CREATED:
			if(!file.isHidden())
				try {
					fileList.add(new FileDetails(file));
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			break;
		case DELETED:
			//fileList.remove(0);
			break;
		case RENAMED:
			/*if(fileChange instanceof FileRename) {
			    int itemIndex = fileList.indexOf(file);
			    if (itemIndex != -1) {
			    	File newFile = ((FileRename) fileChange).getNewPath().toFile();
			        fileList.set(itemIndex, newFile);
			    }
			}*/
			break;
		case UPDATED:
			break;
		default:
			break;
		}
	}

	@Override
	public void closePanel() {
		if(loadFileDetailsService != null) loadFileDetailsService.shutdownNow();
	}
}