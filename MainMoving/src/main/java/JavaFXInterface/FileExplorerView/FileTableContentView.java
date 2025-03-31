package JavaFXInterface.FileExplorerView;

import java.io.File;
import java.util.Optional;

import Utils.FileUtils.FileDetails;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

public class FileTableContentView extends ListView<FileDetails> implements FileViewMode<FileDetails> {	
	private FileTableContentManger fileTableContentManger;

	public FileTableContentView() {
		this.fileTableContentManger = new FileTableContentManger(this);
		getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
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