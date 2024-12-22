package JavaFXInterface.FileExplorer;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import org.controlsfx.control.GridView;

import JavaFXInterface.FileExplorerView.MainFileExplorerView;
import JavaFXInterface.FileExplorerView.MainFileExplorerView.FileExplorerView;
import JavaFXInterface.controlsfx.DragResizePane;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class FileExplorer extends BorderPane {

	private MainFileExplorerView mainFileExplorerView;
	
	public FileExplorer(String filePath) {
		this(new File(filePath));
	}

	public FileExplorer(File file) {
		this.mainFileExplorerView = new MainFileExplorerView(this, FileExplorerView.DETAILS);
		
		Control fileView = this.mainFileExplorerView.getFileView();
		if(fileView instanceof GridView) {
			GridView<?> listView = (GridView<?>) fileView;
			listView.setCellWidth(this.getPrefWidth()*0.4);
			listView.setCellHeight(this.getPrefHeight()*0.4);
		}
		
		this.setCenter(mainFileExplorerView);
		BorderPane.setAlignment(mainFileExplorerView, Pos.TOP_CENTER);
		
		
		SideFilesList sidePnl = new SideFilesList(this, file);
		
		DragResizePane.makeResizable(sidePnl);
		
		sidePnl.setPrefWidth(150);
		
		this.setLeft(sidePnl);
		
		this.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		this.setVisible(true);
		
		this.mainFileExplorerView.setMainPanel(file);
		
	}
	
	public MainFileExplorerView getMainFileExplorerView() {
		return this.mainFileExplorerView;
	}
	
	public void closePanel() {
		this.mainFileExplorerView.closePanel();
	}
	
	public void setCurrentFileFocused(File file) {
		
	}
	
	public void resetCurrentFileFocused() {
        
    }
}
