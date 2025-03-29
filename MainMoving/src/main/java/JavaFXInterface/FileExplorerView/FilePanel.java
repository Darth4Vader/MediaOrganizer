package JavaFXInterface.FileExplorerView;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

import org.controlsfx.control.GridCell;
import org.controlsfx.control.cell.ColorGridCell;
import org.controlsfx.tools.Borders;
import org.controlsfx.tools.Borders.LineBorders;

import FileUtilities.FilesUtils;
import FileUtilities.MimeUtils;
import JavaFXInterface.AppUtils;
import JavaFXInterface.utils.controlsfx.NodeCellSetter;
import OtherUtilities.ImageUtils;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Cell;
import javafx.scene.control.Control;
import javafx.scene.control.IndexedCell;
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class FilePanel extends BorderPane /*VBox*/ implements NodeCellSetter<File> {
	
	private File file;
	private Cell<File> currentCell;
	private final FileName text;
	public ImageView imageView;
	private BooleanProperty isSelected;
	private BorderPane mainPane;
	
	public FilePanel() {
		this.file = null;
		this.text = new FileName();
		imageView = new ImageView();
		imageView.setPreserveRatio(true);
		imageView.fitHeightProperty().bind(heightProperty().subtract(text.heightProperty()));
		imageView.fitWidthProperty().bind(prefWidthProperty());
		setCenter(imageView);
		setBottom(text);
		mainPane = new BorderPane(this);
		mainPane.setBackground(Background.fill(Color.WHITE));
		setBackgroundHover(mainPane, Color.rgb(185, 209, 234, 0.3));
		this.isSelected = new SimpleBooleanProperty();
		isSelected.addListener(c -> setSelectedState(isSelected.get()));
		prefHeightProperty().bind(mainPane.prefHeightProperty().subtract(20));
		//prefHeightProperty().bind(mainPane.prefHeightProperty());
		minHeightProperty().bind(prefHeightProperty());
		maxHeightProperty().bind(prefHeightProperty());
	}
	
	private void setBackgroundHover(Region node, Color color) {
		final SimpleObjectProperty<Background> defaultBackground = new SimpleObjectProperty<>(node.getBackground());
		node.setOnMouseEntered(e -> {
			defaultBackground.set(node.getBackground());
			if(!isSelected.get()) {
				node.setBackground(Background.fill(color));
			}
		});
		
		node.setOnMouseExited(e -> {
			if(!isSelected.get()) {
				node.setBackground(getBackground(false, color, defaultBackground));
			}
		});
	}
	
	private Background getBackground(boolean set, Color colorSet, ObjectProperty<Background> defaultBackground) {
		if(set)
			return Background.fill(colorSet);
		else {
			if(defaultBackground != null) {
				Background background = defaultBackground.get();
				if(background != null)
					return background;
			}
			return Background.fill(Color.WHITE);
		}
	}
	
	@Override
	public void set(File item) {
		set(item, null);
	}
	
	@Override
	public void set(File file, Cell<File> cell) {
		this.file = file;
		Platform.runLater(() -> {
			if(this.file != null)
				imageView.setImage(AppUtils.getImageOfFile(this.file));
		});
		text.updateText(file);
		final SimpleObjectProperty<Background> defaultBackground = new SimpleObjectProperty<>(this.getBackground());
		setOnMouseClicked(e -> {
			requestFocus();
			if(e.getClickCount() == 2 && e.getButton() == MouseButton.PRIMARY) {
				Control cellControlView = AppUtils.getCellOwner(currentCell);
				if(cellControlView != null) {
					Parent owner = cellControlView.getParent();
					if(owner instanceof MainFileExplorerView)
						((MainFileExplorerView) owner).getFileExplorer().enterFolder(file);
				}
			}
		});
		currentCell = cell;
		if(cell != null)
			this.isSelected.bind(cell.selectedProperty());
	}
	
	@Override
	public boolean isSame(File item) {
		try {
			return this.file != null && item != null && Files.isSameFile(this.file.toPath(), item.toPath());
		}
		catch(Exception e) {
			return false;
		}
	}
	
	private void setSelectedState(boolean selected) {
		if(selected) {
			//FileExplorer.getFileExplorer().updateToolPanels(this);
			setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
					BorderWidths.DEFAULT, Insets.EMPTY)));
		}
		else {
			//FileExplorer.getFileExplorer().restartToolPanels();
			setBorder(null);
		}
		mainPane.setBackground(getBackground(selected, Color.rgb(185, 209, 234), null));
	}
	
	public void reset() {
		this.file = null;
		imageView.setImage(null);
		text.updateText("");
		setOnMouseClicked(null);
		setBorder(null);
		setBackground(null);
		this.isSelected.unbind();
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

	@Override
	public Node getView() {
		return mainPane;
	}

	@Override
	public void bindWidth(ReadOnlyDoubleProperty property) {
		mainPane.prefWidthProperty().bind(property);
		prefWidthProperty().bind(mainPane.prefWidthProperty().subtract(50));
		maxWidthProperty().bind(prefWidthProperty());
	}
	
	@Override
	public void bindHeight(ReadOnlyDoubleProperty property) {
		mainPane.prefHeightProperty().bind(property);
	}
}