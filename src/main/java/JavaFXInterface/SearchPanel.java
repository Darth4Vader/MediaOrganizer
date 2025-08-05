package JavaFXInterface;

import JavaFXInterface.FileExplorer.FileExplorer;
import JavaFXInterface.ManageFileExplorer.FileInfoPanel;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class SearchPanel extends BorderPane {

	private FileExplorer explorer;
	private boolean advencedSearchActive;
	
	public SearchPanel(FileExplorer explorer) {
		this.explorer = explorer;
		TextField searchText = new TextField();
		Button advencedSearch = new Button("AdvencedSearch");
		advencedSearch.prefWidthProperty().bind(this.widthProperty().multiply(0.2));
		advencedSearch.setOnAction(e -> {
			activateAdvancedSearch();
		});
		setCenter(searchText);
		setRight(advencedSearch);
	}
	
	private synchronized void activateAdvancedSearch() {
		if(advencedSearchActive) return;
		this.advencedSearchActive = true;
		FileInfoPanel filePnl = new FileInfoPanel();
		filePnl.prefWidthProperty().bind(explorer.widthProperty().multiply(0.3));
		filePnl.prefHeightProperty().bind(explorer.heightProperty());
		explorer.setRight(filePnl);
	}
	
}