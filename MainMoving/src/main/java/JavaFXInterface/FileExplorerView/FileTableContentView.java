package JavaFXInterface.FileExplorerView;

import java.io.File;
import java.util.Optional;

import Utils.FileUtils.FileDetails;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

public class FileTableContentView extends ListView<FileDetails> implements FileViewMode<FileDetails> {	
	private FileTableContentManger fileTableContentManger;

	public FileTableContentView() {
		this.fileTableContentManger = new FileTableContentManger(this);
		getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		this.setCellFactory(param -> new FileTableContentCell(param));
	}
	
	@Override
	public FileTableView<FileDetails> getFileTableView() {
		return fileTableContentManger;
	}

	@Override
	public FileTableHandler getFileTableHandler() {
		return fileTableContentManger;
	}

	@Override
	public Control getFileView() {
		return this;
	}
	
	private class FileTableContentCell extends ListCell<FileDetails> {
		
		private FileTableConentCellPanel cell;
		
		public FileTableContentCell(ListView<FileDetails> tableView) {
			this.cell = new FileTableConentCellPanel();
			//this.cell.bindWidth(tableView.widthProperty());
			this.cell.bindHeight(tableView.heightProperty());
		}
		
	    @Override
	    public void updateItem(FileDetails item, boolean empty) {
	        super.updateItem(item, empty);
	        if (item == null || empty) {
	            setGraphic(null);
	        	cell.reset();
	        }
	        else if(!cell.isSame(item)) {
	        	cell.set(item, this);
	        	setGraphic(cell.getView());
	        }
	        setAlignment(Pos.CENTER_LEFT);
	    }
	}
	
	private class FileTableContentManger extends FileDetailsTableManager {
		
		private FileTableContentView fileTableContentView;
		
		public FileTableContentManger(FileTableContentView fileTableContentView) {
			this.fileTableContentView = fileTableContentView;
		}
		
		@Override
		public void setItems(ObservableList<FileDetails> items) {
			fileTableContentView.setItems(items);
		}
		
		@Override
		public ObservableList<FileDetails> getItems() {
			return fileTableContentView.getItems();
		}

		@Override
		public ObservableList<FileDetails> getSelectedItems() {
			return fileTableContentView.getSelectionModel().getSelectedItems();
		}

		@Override
		public void setFileToBeSelected(File file) {
			// TODO Auto-generated method stub
			Optional<FileDetails> fileDetails = getItems().stream()
					.filter(f -> f.getFile().equals(file))
					.findFirst();
			if(fileDetails.isPresent()) {
				getSelectionModel().select(fileDetails.get());
				scrollTo(getSelectionModel().getSelectedIndex());
			}
		}
	}
}