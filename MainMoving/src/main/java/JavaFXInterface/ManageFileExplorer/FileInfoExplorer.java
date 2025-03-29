package JavaFXInterface.ManageFileExplorer;

import java.io.File;

import DataStructures.ManageFolder;
import JavaFXInterface.FileExplorer.FileExplorer;

public class FileInfoExplorer extends FileExplorer {
	
	private final ManageFolder move;
	private FileInfoMenuBar infoBar;
	private RenameFilePanel infoPanel;

	public FileInfoExplorer(ManageFolder move) {
		super(move.getMainFolderPath());
		this.move = move;
		this.infoBar = new FileInfoMenuBar(this);
		this.infoBar.prefHeightProperty().bind(this.heightProperty().multiply(0.15));
		setMenuBar(infoBar);
		
		this.infoPanel = new RenameFilePanel(this);
		infoPanel.prefWidthProperty().bind(this.widthProperty().multiply(0.3));
		
		
		
		//this.setTop(getSearchPnl());
		
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
	
	@Override
	public void setCurrentFileFocused(File file) {
		if(infoBar != null) {
			this.infoBar.updateToolPanels(file);
		}
	}
	
	@Override
	public void resetCurrentFileFocused() {
		if(infoBar != null) {
			this.infoBar.restartToolPanels();
		}
    }

}