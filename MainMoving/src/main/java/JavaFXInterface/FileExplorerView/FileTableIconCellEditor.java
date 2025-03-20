package JavaFXInterface.FileExplorerView;

import java.io.File;

import JavaFXInterface.controlsfx.GridCellSelected;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;

public class FileTableIconCellEditor extends GridCellSelected<File> {
	
	private final FilePanel cell;
	
	public FileTableIconCellEditor() {
		this.cell = new FilePanel();
		this.cell.bindWidth(widthProperty());
		this.cell.bindHeight(heightProperty());
		this.focusedProperty().addListener((obs, oldVal, newVal) -> {
			System.out.println("Focuss");
			if (newVal) {
				cell.setBackground(Background.fill(Color.RED));
			}
			else {
				cell.setBackground(Background.EMPTY);
			}
		});
	}
	
    @Override
    public void updateItem(File item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
        	cell.reset();
        }
        else if(!cell.isSame(item)) {
        	cell.set(item, this);
        	setGraphic(cell.getView());
        }
        setAlignment(Pos.CENTER_LEFT);
    }
}