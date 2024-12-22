package JavaFXInterface.FileExplorerView;

import java.io.File;

import DirectoryWatcher.FileChange;
import DirectoryWatcher.FileChange.FileChaneType;
import DirectoryWatcher.FileRename;
import JavaFXInterface.FileExplorer;
import JavaFXInterface.controlsfx.GridViewSelection;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.input.ScrollEvent;

public class FileTableIconView extends GridViewSelection<File> implements FileTableHandler {
	
	private static final double CELL_MIN_WIDTH = 100, CELL_MIN_HEIGHT = 100;

	public FileTableIconView(FileExplorer explorer) {
		super(FXCollections.observableArrayList());
		setCellFactory(x -> new FileTableIconCellEditor());
		setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
		setStyle("-fx-focus-color: -fx-control-inner-background ; -fx-faint-focus-color: -fx-control-inner-background ;");
		setFocusTraversable(false);
		addSelectionListener(new ListChangeListener<File>() {

			@Override
			public void onChanged(Change<? extends File> c) {
				if(c.next()) {
					ObservableList<? extends File> list = c.getList();
					if(c.wasRemoved()) {
						System.out.println("alone " + list);
						if(list.isEmpty())
							explorer.resetCurrentFileFocused();
					}
					if(c.wasAdded()) {
						if(list.size() == 1)
							explorer.setCurrentFileFocused(list.getFirst());
						else if(list.size() > 1) {
							
						}
					}
				}
			}
			
		});
		setCellWidth(CELL_MIN_WIDTH);
		setCellHeight(CELL_MIN_HEIGHT);
		
		//add zoom for the cells size
		this.addEventFilter(ScrollEvent.ANY, e -> {
            //if resizing, then don't allow to scroll the list
			if(e.isControlDown()) {
				double zoomFactor = 1.05;
	            double deltaY = e.getDeltaY();
	
	            if (deltaY < 0){
	                zoomFactor = 0.95;
	            }
	            double cellWidth = getCellWidth() * zoomFactor;
	            double cellHeight = getCellHeight() * zoomFactor;
	            System.out.println(cellWidth + " " + cellHeight);
	            setCellWidth(Math.max(CELL_MIN_WIDTH, cellWidth));
	            setCellHeight(Math.max(CELL_MIN_HEIGHT, cellHeight));
	            e.consume();
            }
		});
	}

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
			if(fileChange instanceof FileRename) {
			    int itemIndex = fileList.indexOf(file);
			    if (itemIndex != -1) {
			    	File newFile = ((FileRename) fileChange).getNewPath().toFile();
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

}
