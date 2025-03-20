package JavaFXInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultSingleSelectionModel;

import DataStructures.ManageFolder;
import FileUtils.FileDetails;
import JavaFXInterface.FileExplorer.FileExplorer;
import JavaFXInterface.FileExplorerView.MainFileExplorerView;
import JavaFXInterface.FileExplorerView.MainFileExplorerView.FileExplorerView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.CheckBoxTableCell;

public class ManageFolderSelectorPanel extends FileExplorer {
	
	private List<File> selectedFiles = new ArrayList<>();

	public ManageFolderSelectorPanel(ManageFolder manage) {
		super(manage.getMainFolderPath());
		this.getMainFileExplorerView().setFileDetailsView(detailsView -> {
			TableColumn<FileDetails, Boolean> checkColumn = new TableColumn<>();
			checkColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkColumn));
			checkColumn.setCellValueFactory(cellData -> {
				BooleanProperty selected = new SimpleBooleanProperty(selectedFiles.contains(cellData.getValue().getFile()));
				selected.addListener((obs, oldVal, newVal) -> {
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
		finish.setOnAction(e -> {
			manage.setManageFolderFiles(selectedFiles);
			closePanel();
			finishSelectionProperty().set(true);
		});
		setBottom(finish);
		
		this.getMainFileExplorerView().setFileExplorerView(FileExplorerView.ICONS);
	}
	
	private BooleanProperty finishSelection = new SimpleBooleanProperty(false);
	
	public BooleanProperty finishSelectionProperty() {
		return finishSelection;
	}
}
