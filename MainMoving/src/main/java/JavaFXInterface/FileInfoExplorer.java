package JavaFXInterface;

import java.io.File;

import DataStructures.ManageFolder;
import JavaFXInterface.FileExplorer.FileExplorer;
import javafx.scene.layout.BorderPane;

public class FileInfoExplorer extends FileExplorer {
	
	private RenameFilePanel infoPanel;
	private final ManageFolder move;

	public FileInfoExplorer(ManageFolder move) {
		super(move.getMainFolderPath());
		this.move = move;
		this.infoPanel = new RenameFilePanel(this);
		infoPanel.prefWidthProperty().bind(this.widthProperty().multiply(0.3));
		infoPanel.prefHeightProperty().bind(this.heightProperty());
		this.setTop(getSearchPnl());
		
		/*
		MainFileExplorerView fileView = getMainFileExplorerView();
		this.getMainFileExplorerView().setFileIconView((_) -> {
			FileTableIconView iconView = fileView.getDefualtFileIconView();
			iconView.addSelectionListener(new ListChangeListener<File>() {

				@Override
				public void onChanged(Change<? extends File> c) {
					if(c.next()) {
						ObservableList<? extends File> list = c.getList();
						if(c.wasRemoved()) {
							System.out.println("alone " + list);
							if(list.isEmpty())
								explorer.restartToolPanels();
						}
						if(c.wasAdded()) {
							if(list.size() == 1)
								explorer.updateToolPanels(list.getFirst());
							else if(list.size() > 1) {
								
							}
						}
					}
				}
				
			});
		});
		*/
	}
	
	public ManageFolder getFolderManager() {
		return this.move;
	}
	
	public FileInfoPanel getFileInfoPanel() {
		return this.infoPanel;
	}
	
	private FileInfoToolsPanel infoTools;
	
	private BorderPane getSearchPnl() {
		BorderPane pnl = new BorderPane();
		pnl.prefHeightProperty().bind(this.heightProperty().multiply(0.15));
		this.infoTools = new FileInfoToolsPanel(this);
		pnl.setCenter(this.infoTools);
		SearchPanel searchPnl = new SearchPanel(this);
		pnl.setRight(searchPnl);
		searchPnl.prefWidthProperty().bind(pnl.widthProperty().multiply(0.2));
		return pnl;
	}
	
	@Override
	public void setCurrentFileFocused(File file) {
		if(infoTools != null) {
			this.infoTools.updateToolPanels(file);
		}
	}
	
	@Override
	public void resetCurrentFileFocused() {
		if(infoTools != null) {
			this.infoTools.restartToolPanels();
		}
    }

}
