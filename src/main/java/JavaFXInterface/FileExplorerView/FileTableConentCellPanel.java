package JavaFXInterface.FileExplorerView;

import java.io.File;
import java.nio.file.Files;

import FileUtilities.MimeUtils;
import JavaFXInterface.AppUtils;
import JavaFXInterface.utils.controlsfx.NodeCellSetter;
import Utils.FileUtils.FileDetails;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Cell;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class FileTableConentCellPanel extends HBox /*VBox*/ implements NodeCellSetter<FileDetails> {
	
	private FileDetails file;
	private final FileName text;
	private Label mainInfoLabel;
	public ImageView imageView;
	private BooleanProperty isSelected;
	
	public FileTableConentCellPanel() {
		this.file = null;
		this.text = new FileName();
		imageView = new ImageView();
		imageView.setPreserveRatio(true);
		imageView.fitHeightProperty().bind(heightProperty());
		imageView.fitWidthProperty().bind(prefWidthProperty().multiply(0.1));
		
		BorderPane namePane = new BorderPane();
		namePane.setTop(text);
		this.mainInfoLabel = new Label();
		namePane.setBottom(mainInfoLabel);
		
		
		this.getChildren().addAll(imageView, namePane);
		
		setBackgroundHover(this, Color.rgb(185, 209, 234, 0.3));
		this.isSelected = new SimpleBooleanProperty();
		isSelected.addListener(_ -> setSelectedState(isSelected.get()));
		
		minHeightProperty().bind(prefHeightProperty());
		maxHeightProperty().bind(prefHeightProperty());
		
		setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
				BorderWidths.DEFAULT, Insets.EMPTY)));
	}
	
	private void setBackgroundHover(Region node, Color color) {
		final SimpleObjectProperty<Background> defaultBackground = new SimpleObjectProperty<>(node.getBackground());
		node.setOnMouseEntered(_ -> {
			defaultBackground.set(node.getBackground());
			if(!isSelected.get()) {
				node.setBackground(Background.fill(color));
			}
		});
		
		node.setOnMouseExited(_ -> {
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
	public void set(FileDetails item) {
		set(item, null);
	}
	
	@Override
	public void set(FileDetails file, Cell<FileDetails> cell) {
		updateFile(file);
		Platform.runLater(() -> {
			if(this.file != null)
				imageView.setImage(AppUtils.getImageOfFile(this.file.getFile()));
		});
		final SimpleObjectProperty<Background> defaultBackground = new SimpleObjectProperty<>(this.getBackground());
		setOnMouseClicked(e -> {
			requestFocus();
			if(e.getClickCount() == 2 && e.getButton() == MouseButton.PRIMARY) {
				Control cellControlView = AppUtils.getCellOwner(cell);
				if(cellControlView != null) {
					Parent owner = cellControlView.getParent();
					if(owner instanceof MainFileExplorerView mainFileExplorerView)
						mainFileExplorerView.getFileExplorer().enterFolder(file.getFile());
				}
			}
		});
		if(cell != null)
			this.isSelected.bind(cell.selectedProperty());
	}
	
	@Override
	public boolean isSame(FileDetails item) {
		try {
			return this.file != null && item != null && Files.isSameFile(this.file.getFile().toPath(), item.getFile().toPath());
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
		//mainPane.setBackground(getBackground(selected, Color.rgb(185, 209, 234), null));
	}
	
	public void reset() {
		this.file = null;
		imageView.setImage(null);
		text.updateText("");
		mainInfoLabel.setText("");
		setOnMouseClicked(null);
		setBorder(null);
		setBackground(null);
		this.isSelected.unbind();
	}
	
	public FileDetails getFile() {
		return this.file;
	}
	
	public void updateFile(FileDetails file) {
		this.file = file;
		text.updateText(file.getFile());
		mainInfoLabel.setText(file.getTypeName());
	}
	
	private class FileName extends TextField {

		private boolean hasFocus;
		private String text;
		
		public FileName() {
			setAlignment(Pos.CENTER);
			setEditable(false);
			setFocusTraversable(false);
			this.focusedProperty().addListener((_, oldValue, newValue) -> {
				if(newValue)
					this.hasFocus = true;
				else if(oldValue) {
					this.hasFocus = false;
					setEditable(false);
					setText(text);
				}
			});
			setOnMouseClicked(_ -> {
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
		return this;
	}

	@Override
	public void bindWidth(ReadOnlyDoubleProperty property) {
		prefWidthProperty().bind(property.subtract(20));
		//maxWidthProperty().bind(prefWidthProperty());
	}
	
	@Override
	public void bindHeight(ReadOnlyDoubleProperty property) {
		prefHeightProperty().bind(property.multiply(0.2));
	}
}