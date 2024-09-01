package JavaFXInterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import SwingUtilities.SwingUtils;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class SearchPanel extends BorderPane {

	private FileExplorer explorer;
	private boolean advencedSearchActive;
	
	public SearchPanel(FileExplorer explorer) {
		this.explorer = explorer;
		TextField searchText = new TextField();
		//JPanel advencedSearch = new JPanel();
		//FileInfoPanel fileInfoPnl = new FileInfoPanel();
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