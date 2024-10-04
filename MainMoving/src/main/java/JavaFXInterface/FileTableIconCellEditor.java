package JavaFXInterface;

import java.io.File;

import JavaFXInterface.controlsfx.GridCellSelected;
import javafx.geometry.Pos;

public class FileTableIconCellEditor extends GridCellSelected<File> {
	
	private final FilePanel cell;
	
	public FileTableIconCellEditor() {
		this.cell = new FilePanel();
		this.cell.bindWidth(widthProperty());
		this.cell.bindHeight(heightProperty());
	}
	
    @Override
    public void updateItem(File item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
        	cell.reset();
        }
        else {
        	cell.set(item, this);
			System.out.println("Render " + item);
            setGraphic(cell.getView());
        }
        setAlignment(Pos.CENTER_LEFT);
    }
}