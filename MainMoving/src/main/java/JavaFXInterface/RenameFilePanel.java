package JavaFXInterface;

import java.io.File;
import java.io.IOException;

import DataStructures.NameInfo.NameInfoType;
import javafx.scene.control.Button;

public class RenameFilePanel extends FileInfoPanel {
	
	private FileExplorer explorer;
	
	public RenameFilePanel(FileExplorer explorer) {
		super();
		this.explorer = explorer;
		Button btn = new Button("Update");
		btn.setOnAction(e -> {
			try {
				updateChanges();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		setBottom(btn);
	}
	
	public void updateChanges() throws IOException {
		for (NameInfoType type : this.map.keySet()) {
			InfoField field = this.map.get(type);
			field.updateChanges();
		}
		File file = explorer.getFolderManager().renameFiles(info);
		/*
		if(this.pnl != null)
			this.pnl.updateFile(file);
		*/
	}
}