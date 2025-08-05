package JavaFXInterface.FileExplorerView;

import java.io.File;
import java.util.Collection;

import JavaFXInterface.FileExplorer.FileExplorer;
import JavaFXInterface.utils.controlsfx.BetterGridView;
import Utils.DirectoryWatcher.FileChange;
import Utils.DirectoryWatcher.FileRename;
import Utils.DirectoryWatcher.FileChange.FileChaneType;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.ScrollEvent;

public class FileTableIconView extends BetterGridView<File> implements FileViewMode<File> {
	
	private static final double CELL_MIN_WIDTH = 100, CELL_MIN_HEIGHT = 100;
	
	private FileTableIconManager fileTableIconManager;
	
	public FileTableIconView(FileExplorer explorer) {
		super(FXCollections.observableArrayList());
		this.fileTableIconManager = new FileTableIconManager(this);
		setCellFactory(_ -> new FileTableIconCellEditor());
		setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
		setStyle("-fx-focus-color: -fx-control-inner-background ; -fx-faint-focus-color: -fx-control-inner-background ;");
		setFocusTraversable(false);
		
		getSelectionModel().getSelectedItems().addListener(new ListChangeListener<File>() {

			@Override
			public void onChanged(Change<? extends File> c) {
				if(c.next()) {
					ObservableList<? extends File> list = c.getList();
					if(c.wasRemoved()) {
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
		getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}
	
	@Override
	public FileTableView<File> getFileTableView() {
		return fileTableIconManager;
	}

	@Override
	public FileTableHandler getFileTableHandler() {
		return fileTableIconManager;
	}

	@Override
	public Control getFileView() {
		return this;
	}
	
	public class FileTableIconManager extends FileTableManager {
		
		private FileTableIconView fileTableView;
		
		public FileTableIconManager(FileTableIconView fileTableView) {
			this.fileTableView = fileTableView;
		}
		
		@Override
		public ObservableList<File> getItems() {
			return fileTableView.getItems();
		}
		
		@Override
		public ObservableList<File> getSelectedItems() {
			return fileTableView.getSelectionModel().getSelectedItems();
		}

		@Override
		public ObservableList<File> getSelectedFiles() {
			return fileTableView.getSelectionModel().getSelectedItems();
		}
		
		@Override
		public void setFileToBeSelected(File file) {
			GridViewMultipleSelectionModel<File> selectionModel = fileTableView.getSelectionModel();
			if(selectionModel != null) {
				selectionModel.select(file);
			}
		}
	}
}