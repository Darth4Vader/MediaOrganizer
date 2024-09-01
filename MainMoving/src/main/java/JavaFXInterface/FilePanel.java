package JavaFXInterface;

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
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
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
	private ImageView imageView;
	private ChangeListener<? super Boolean> focusListener;
	
	public FilePanel(Region sizePane) {
		this.file = null;
		b = new BorderPane();
		b.prefWidthProperty().bind(sizePane.widthProperty().multiply(0.5));
		b.prefHeightProperty().bind(sizePane.heightProperty().multiply(0.5));
		imageView = new ImageView();
		imageView.setPreserveRatio(true);
		this.text = new FileName();
		b.setCenter(imageView);
		b.setBottom(text);
	}
	
	public void set(File file) {
		this.file = file;
		imageView.setImage(AppUtils.getImageOfFile(file));
		text.updateText(file);
		focusListener = (obs, oldValue, newValue) -> {
			if(newValue) {
				FileExplorer.getFileExplorer().updateToolPanels(this);
				b.setBorder(new Border(new BorderStroke(Color.PINK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
						BorderWidths.DEFAULT)));
			}
			else {
				b.setBorder(null);
			}
		};
		b.focusedProperty().addListener(focusListener);
		b.setOnMouseClicked(e -> {
			b.requestFocus();
			if(e.getClickCount() == 2 && e.isSecondaryButtonDown()) {
				FileExplorer.getFileExplorer().setMainPanel(file);
			}
		});
	}
	
	public void clean() {
		this.file = null;
		imageView.setImage(null);
		text.updateText("");
		if(focusListener != null)
			b.focusedProperty().removeListener(focusListener);
		focusListener = null;
		b.setOnMouseClicked(null);
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
			this.focusedProperty().addListener((obs, oldValue, newValue) -> {
				if(newValue)
					this.hasFocus = true;
				else if(oldValue) {
					setText(text);
					this.hasFocus = false;
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