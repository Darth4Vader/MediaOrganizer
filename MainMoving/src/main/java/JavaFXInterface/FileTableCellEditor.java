package JavaFXInterface;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.graphbuilder.struc.LinkedList.Node;

import FileUtilities.FilesUtils;
import FileUtilities.MimeUtils;
import OtherUtilities.ImageUtils;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollBar;
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class FileTableCellEditor extends ListCell<FileRow> {
	
	private Region sizePane;
	
	private final GridPane box;
	
	private final List<FilePanel> cells;
	
	public FileTableCellEditor(Region sizePane, int number) {
		//getStylesheets().add("noFocus.css");
		//setStyle("-fx-focus-color: -fx-control-inner-background ; -fx-faint-focus-color: -fx-control-inner-background ;");
		//setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
		this.sizePane = sizePane;
		this.box = new GridPane();
		box.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
		//box.setHgap(10);
		box.setAlignment(Pos.CENTER);
		this.cells = new ArrayList<>();
		for(int i = 0; i < number; i++) {
			ColumnConstraints cols = new ColumnConstraints();
			cols.setPercentWidth(100d / number);
			//cols.setMaxWidth(GridPane.USE_COMPUTED_SIZE);
			cols.setHalignment(HPos.CENTER);
			/*cols.setFillWidth(true);
			cols.setHgrow(Priority.ALWAYS);*/
			FilePanel cell = new FilePanel(sizePane);
			box.getColumnConstraints().add(cols);
			
			//cell.b.prefWidthProperty().bind(box.prefWidthProperty().subtract(box.getHgap()*number).divide(number));
			cell.b.prefWidthProperty().bind(box.prefWidthProperty().subtract(box.getHgap()*number).divide(number));
			
			
			this.cells.add(cell);
			this.box.add(cell.b, i, 0);
		}
    	box.prefWidthProperty().bind(widthProperty());
    	
    	/*
    	box.prefWidthProperty().addListener((obs, old, newV) -> {
    		//System.out.println(old + " " + newV);
    		System.out.println("Start " + obs);
    		//System.out.println("What: " + box.widthProperty().doubleValue());
    		for(FilePanel node : cells) {
    			System.out.println(node.b.prefWidthProperty().doubleValue() + "-> ");
    			System.out.println(node.b.widthProperty().doubleValue() + " " + node.imageView.fitWidthProperty().doubleValue());
    		}
    	});
    	*/
    	
    	/*
    	box.heightProperty().addListener((obs, old, newV) -> {
    		//System.out.println(old + " " + newV);
    		System.out.println("Start " + obs.getValue() + " -> " + box.getPrefHeight());
    		//System.out.println("What: " + box.widthProperty().doubleValue());
    		for(FilePanel node : cells) {
    			//System.out.println(node.b.prefHeightProperty().doubleValue() + "-> ");
    			System.out.println(node.b.prefHeightProperty().doubleValue() + " " + node.imageView.fitHeightProperty().doubleValue());
    		}
    	});
    	
    	*/
    	
		//box.prefWidthProperty().bind(sizePane.widthProperty().subtract(40));
    	//box.prefWidthProperty().bind(sizePane.widthProperty().subtract(40));
		//box.prefWidthProperty().bind(sizePane.widthProperty().subtract(40));
		
    	
    	setStyle("-fx-padding: 0px;");
		
		//prefWidthProperty().bind(sizePane.widthProperty());
		/*setPrefWidth(0);
        prefWidthProperty().bind(sizePane.widthProperty().subtract(20));
        setMaxWidth(Control.USE_PREF_SIZE);*/
	}
	
    @Override
    public void updateItem(FileRow item, boolean empty) {
        super.updateItem(item, empty);
        ScrollBar scrollBar = (ScrollBar) sizePane.queryAccessibleAttribute(AccessibleAttribute.VERTICAL_SCROLLBAR);
        ReadOnlyDoubleProperty width = sizePane.widthProperty();
        if(scrollBar != null) {
        	prefWidthProperty().bind(width.subtract(scrollBar.widthProperty()));
        }
        else
        	prefWidthProperty().bind(width);
        
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