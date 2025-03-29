package JavaFXInterface;

import java.io.File;
import java.util.ArrayList;

import DataStructures.ManageFolder;
import JavaFXInterface.FileExplorer.FileExplorer;
import JavaFXInterface.FileExplorerView.MainFileExplorerView.FileExplorerView;
import Utils.FileUtils.FileDetails;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.CheckBoxTableCell;

public class ManageFolderSelectorPanel extends FileExplorer {
	
	private ObservableList<File> selectedFiles = FXCollections.observableArrayList();

	public ManageFolderSelectorPanel(ManageFolder manage) {
		super(manage.getMainFolderPath());
		this.selectedFiles.addAll(manage.getManageFolderFiles());
		this.getMainFileExplorerView().setFileDetailsView(detailsView -> {
			TableColumn<FileDetails, Boolean> checkColumn = new TableColumn<>();
			checkColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkColumn));
			checkColumn.setCellValueFactory(cellData -> {
				BooleanProperty selected = new SimpleBooleanProperty(selectedFiles.contains(cellData.getValue().getFile()));
				selected.addListener((_, _, newVal) -> {
					if (newVal) {
						selectedFiles.add(cellData.getValue().getFile());
					} else {
						selectedFiles.remove(cellData.getValue().getFile());
					}
				});
				return selected;
			});
			detailsView.getColumns().addFirst(checkColumn);
			detailsView.setEditable(true);
			return detailsView;
		});
		
		Button finish = new Button("Finish");
		finish.setOnAction(_ -> {
			//create new list in order to avoid the function removing/adding to the list
			manage.setManageFolderFiles(new ArrayList<>(selectedFiles));
			closePanel();
			setFinishSelection(true);
		});
		setBottom(finish);
		
		this.getMainFileExplorerView().setFileExplorerView(FileExplorerView.DETAILS);
	}
	
	private BooleanProperty finishSelection = new SimpleBooleanProperty(false);
	
	public BooleanProperty finishSelectionProperty() {
		return finishSelection;
	}
	
	private void setFinishSelection(boolean value) {
		finishSelection.set(value);
	}
}
