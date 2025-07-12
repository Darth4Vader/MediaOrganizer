package JavaFXInterface.ManageFileExplorer;

import java.io.File;

import DataStructures.FileInfo;
import DataStructures.FolderInfo;
import DataStructures.ManageFolder;
import DataStructures.ManageFolder.FlagSearchMainFolderExeception;
import JavaFXInterface.FileExplorer.FileExplorer;
import Utils.DirectoryWatcher.FileChange;

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
	
	@Override
	public void handleFileChange(FileChange fileChange) {
		switch(fileChange.getFileChaneType()) {
			case CREATED -> {
				File file = fileChange.getPath().toFile();
				if(!file.isHidden()) {
					if(file.isDirectory()) {
						FileInfo fileInfo = new FileInfo(file);
						try {
							FolderInfo folderInfo = this.move.getMainFolder(fileInfo);
							if(folderInfo != null) {
								File removedFolder = folderInfo.getFile();
								if(removedFolder.exists() != true) {
									if(file.getName().equals(removedFolder.getName())) {
										this.move.removeFromMap(folderInfo);
										this.move.toAddInsideMap(file);
									}
								}
							}
						} catch (FlagSearchMainFolderExeception e) {
						}
					}
				}
				break;
			}
			case DELETED -> {
				break;
			}
			case RENAMED -> {
				break;
			}
			case UPDATED -> {
				break;
			}
			default -> {}
		}
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