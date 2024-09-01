package JavaFXInterface;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import FileUtilities.FilesUtils;
import FileUtilities.MimeUtils;
import OtherUtilities.ImageUtils;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class FileTableCellEditor extends ListCell<FileRow> {
	
	private Region sizePane;
	
	private final GridPane box;
	
	private final List<FilePanel> cells;
	
	public FileTableCellEditor(Region sizePane, int number) {
		this.sizePane = sizePane;
		this.box = new GridPane();
		box.setAlignment(Pos.CENTER);
		this.cells = new ArrayList<>();
		for(int i = 0; i < number; i++) {
			ColumnConstraints cols = new ColumnConstraints();
			cols.setPercentWidth(100d / number);
			cols.setHalignment(HPos.CENTER);
			FilePanel cell = new FilePanel(sizePane);
			box.getColumnConstraints().add(cols);
			this.cells.add(cell);
			this.box.add(cell.b, i, 0);
		}
    	box.prefWidthProperty().bind(sizePane.widthProperty().subtract(40));
		setStyle("-fx-padding: 0px;");
	}
	
    @Override
    public void updateItem(FileRow item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
            for(FilePanel cell : cells)
            	cell.clean();
        }
        else {
        	List<File> files = item.getFiles();
        	for(int i = 0; i < cells.size(); i++) {
        		FilePanel cell = cells.get(i);
        		if(i < files.size()) {
        			cell.set(files.get(i));
        		}
        		else {
        			cell.clean();
        		}
        	}
            setGraphic(box);
        }
        setAlignment(Pos.CENTER_LEFT);
    }
}