import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import DataStructures.FolderInfo;
import DataStructures.ManageFolder;
import JavaFXInterface.FilePanel;
import JavaFXInterface.FileRow;
import JavaFXInterface.FileTableCellEditor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

public class TestMovingJavaFX extends BorderPane {
	
	private final BorderPane mainPanel;
	private final ManageFolder move;
	private FilePanel filePanel;
	private File folder;
	
	private ListView<FileRow> fileListView;
	private ObservableList<FileRow> fileList;

	public TestMovingJavaFX(ManageFolder manageFolder) {
		this.move = manageFolder;
		fileList = FXCollections.observableArrayList();
		
		final int MAX = 5;
		fileListView = new ListView<FileRow>(fileList);
		fileListView.setCellFactory(x -> new FileTableCellEditor(fileListView, MAX));
		fileListView.setSelectionModel(null);
		fileListView.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
		
		fileListView.setStyle("-fx-focus-color: -fx-control-inner-background ; -fx-faint-focus-color: -fx-control-inner-background ;");
		fileListView.setFocusTraversable(false);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		this.setPrefSize((int) (width * 0.445), (int) (height * 0.445));
		
		this.mainPanel = new BorderPane();
		
		this.setCenter(mainPanel);
		BorderPane.setAlignment(mainPanel, Pos.TOP_CENTER);
		
		this.mainPanel.setCenter(fileListView);
		
		List<File> files =  new ArrayList<>();
		
		for(FolderInfo folderInfo : manageFolder.movieMap.values()) {
			files.add(folderInfo.getFile());
		}
		
		for(FolderInfo folderInfo : manageFolder.TVMap.values()) {
			files.add(folderInfo.getFile());
		}
		
		for(FolderInfo folderInfo : manageFolder.unkownMediaMap.values()) {
			files.add(folderInfo.getFile());
		}
		
		System.out.println(files);
		
		UpdatelistViewAsGridPage(files);
		
		this.setVisible(true);
	}
	
	public void UpdatelistViewAsGridPage(List<File> list) {
		fileList.clear();
		int i = 0;
		//The maximum number of items in one row.
		final int MAX = 5;
		FileRow fileRow = new FileRow();
		for(File file : list) {
			if(file != null) {
				fileRow.add(file);
				i++;
				if(i == MAX) {
					fileList.add(fileRow);
					fileRow = new FileRow();
					i = 0;
				}
			}
		}
		if(!fileRow.getFiles().isEmpty()) {
			fileList.add(fileRow);
		}
	}
}