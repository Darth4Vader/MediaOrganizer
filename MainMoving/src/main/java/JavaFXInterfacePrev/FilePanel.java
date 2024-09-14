package JavaFXInterfacePrev;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

import FileUtilities.FilesUtils;
import FileUtilities.MimeUtils;
import OtherUtilities.ImageUtils;
import javafx.beans.value.ChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class FilePanel {

    public BorderPane b;
	private File file;
	private final FileName text;
	public ImageView imageView;
	private ChangeListener<? super Boolean> focusListener;
	
	public FilePanel(Region sizePane) {
		this.file = null;
		b = new BorderPane();
		
		vb = new VBox();
		
		b.prefHeightProperty().bind(vb.heightProperty().add(100));
		
		b.setCenter(vb);
		vb.prefWidthProperty().bind(b.prefWidthProperty().subtract(20));
		/*vb.maxWidthProperty().bind(vb.prefWidthProperty());*/
		
		//vb.prefWidthProperty().bind(b.prefWidthProperty());
		vb.maxWidthProperty().bind(vb.prefWidthProperty());
		
		//vb.prefHeightProperty().bind(vb.prefHeightProperty().subtract(10));
		vb.maxHeightProperty().bind(vb.prefHeightProperty().subtract(50));
		vb.setAlignment(Pos.TOP_CENTER);
		
		//b.prefWidthProperty().bind(sizePane.widthProperty().multiply(0.5));
		//b.prefHeightProperty().bind(sizePane.heightProperty().multiply(0.5));
		imageView = new ImageView();
		imageView.setPreserveRatio(true);
		imageView.fitHeightProperty().bind(sizePane.heightProperty().multiply(0.4));
		//imageView.fitWidthProperty().bind(b.widthProperty());
		imageView.fitWidthProperty().bind(vb.prefWidthProperty());
		this.text = new FileName();
		vb.getChildren().add(imageView);
		vb.getChildren().add(text);
		
		/*b.setBorder(new Border(new BorderStroke(Color.PINK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
				BorderWidths.DEFAULT, Insets.EMPTY)));*/
		
		b.setOnMouseEntered(e -> {
			if(!isSelected)
				b.setBackground(Background.fill(Color.rgb(185, 209, 234, 0.3)));
		});
		
		b.setOnMouseExited(e -> {
			if(!isSelected)
				b.setBackground(null);
		});
	}
	
	private VBox vb;
	
	private boolean isSelected;
	
	public void set(File file) {
		this.file = file;
		imageView.setImage(AppUtils.getImageOfFile(file));
		text.updateText(file);
		focusListener = (obs, oldValue, newValue) -> {
			isSelected = newValue;
			if(newValue) {
				FileExplorer.getFileExplorer().updateToolPanels(this);
				b.setBackground(Background.fill(Color.rgb(185, 209, 234)));
				b.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
						BorderWidths.DEFAULT, Insets.EMPTY)));
				/*vb.setBorder(new Border(new BorderStroke(Color.PINK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
						BorderWidths.DEFAULT, Insets.EMPTY)));*/
			}
			else {
				FileExplorer.getFileExplorer().restartToolPanels();
				b.setBackground(null);
				b.setBorder(null);
			}
		};
		b.focusWithinProperty().addListener(focusListener);
		b.setOnMouseClicked(e -> {
			System.out.println("Clicked "+ e.getClickCount());
			System.out.println(e.getButton());
			System.out.println(e.getClickCount() == 2);
			//System.out.println(e.butt());
			b.requestFocus();
			if(e.getClickCount() == 2 && e.getButton() == MouseButton.PRIMARY) {
				FileExplorer.getFileExplorer().setMainPanel(file);
			}
		});
	}
	
	public void clean() {
		this.file = null;
		imageView.setImage(null);
		text.updateText("");
		isSelected = false;
		if(focusListener != null)
			b.focusWithinProperty().removeListener(focusListener);
		focusListener = null;
		b.setOnMouseClicked(null);
		b.setBorder(null);
		b.setBackground(null);
	}
	
	public File getFile() {
		return this.file;
	}
	
	public void updateFile(File file) {
		this.file = file;
		text.updateText(file);
	}
	
	private class FileName extends TextField {

		private boolean hasFocus;
		private String text;
		
		public FileName() {
			setAlignment(Pos.CENTER);
			setEditable(false);
			setFocusTraversable(false);
			this.focusedProperty().addListener((obs, oldValue, newValue) -> {
				if(newValue)
					this.hasFocus = true;
				else if(oldValue) {
					this.hasFocus = false;
					setEditable(false);
					setText(text);
				}
			});
			setOnMouseClicked(e -> {
				if (hasFocus)
					setEditable(true);
			});
		}
		
		public void updateText(File file) {
			updateText(MimeUtils.getNameWithoutExtension(file));
		}
		
		public void updateText(String text) {
			this.text = text;
			setText(text);
		}
	}
}