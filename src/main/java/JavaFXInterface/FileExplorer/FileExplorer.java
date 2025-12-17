package JavaFXInterface.FileExplorer;

import java.io.File;

import org.controlsfx.control.GridView;

import JavaFXInterface.FileExplorerSearch.FileSearchView;
import JavaFXInterface.FileExplorerSearch.HistorySearchView;
import JavaFXInterface.FileExplorerView.MainFileExplorerView;
import JavaFXInterface.FileExplorerView.MainFileExplorerView.FileExplorerView;
import JavaFXInterface.utils.JavaFXImageUtils;
import JavaFXInterface.utils.controlsfx.DragResizePane;
import JavaFXUtilities.CanvasPane;
import Utils.DirectoryWatcher.FileChange;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Control;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class FileExplorer extends BorderPane {

	private MainFileExplorerView mainFileExplorerView;
	
	private FileSearchView searchView;
	
	private HistoryList<HistoryView> history;
	
	private TextField searchField;
	
	private boolean canRegisterHistory;
	
	private BorderPane viewPane;
	
	public FileExplorer(String filePath) {
		this(new File(filePath));
	}

	public FileExplorer(File file) {
		this.mainFileExplorerView = new MainFileExplorerView(this, FileExplorerView.DETAILS);
		
		Control fileView = this.mainFileExplorerView.getFileView();
		if(fileView instanceof GridView gridView) {
			gridView.setCellWidth(this.getPrefWidth()*0.4);
			gridView.setCellHeight(this.getPrefHeight()*0.4);
		}
		
		this.viewPane = new BorderPane();
		viewPane.setCenter(mainFileExplorerView);
		SideFilesList sidePnl = new SideFilesList(this, file);
		DragResizePane.makeResizable(sidePnl);
		sidePnl.setPrefWidth(150);
		viewPane.setLeft(sidePnl);
		
		this.setCenter(viewPane);
		
		
		searchView = new FileSearchView(this, FileExplorerView.DETAILS);
		this.canRegisterHistory = true;
		this.history = new HistoryList<HistoryView>();
		searchField = new TextField();
		searchField.setPromptText("Search");
		searchField.textProperty().addListener((_, oldVal, newVal) -> {
			if(!this.canRegisterHistory)
				return;
			if(!newVal.equals(oldVal)) {
				if(newVal.isBlank()) {
					viewPane.setCenter(this.mainFileExplorerView);
				}
				else {
					viewPane.setCenter(searchView);
					HistoryView historyView = searchView.search(this.mainFileExplorerView.getFolder(), newVal);
					if(historyView != null)
			            this.history.add(historyView);
				}
			}
		});
		
	
		HBox functionBox = new HBox();
		functionBox.setSpacing(10);
		ExpandPanel backwardHistory = new ExpandPanel();
		ExpandPanel forwardHistory = new ExpandPanel();
		ExpandPanel moveToParent = new ExpandPanel();
		backwardHistory.setImage(JavaFXImageUtils.getImageResource(ExpandPanel.class, "images/history_arrow.png"));
		backwardHistory.setPrefWidth(50);
		backwardHistory.visibleActiveProperty().bind(history.hasPrevious());
		backwardHistory.setOnMouseClicked(_ -> {
			loadPreviousHistoryView();
		});
		forwardHistory.setImage(JavaFXImageUtils.getImageResource(ExpandPanel.class, "images/history_arrow.png"));
		forwardHistory.setPrefWidth(50);
		forwardHistory.setRotate(180);
		forwardHistory.visibleActiveProperty().bind(history.hasNext());
		forwardHistory.setOnMouseClicked(_ -> {
			loadNextHistoryView();
        });
		
		moveToParent.setImage(JavaFXImageUtils.getImageResource(ExpandPanel.class, "images/history_arrow.png"));
		moveToParent.setPrefWidth(50);
		moveToParent.setImageRotate(90);
		moveToParent.visibleActiveProperty().bind(mainFileExplorerView.folderProperty().isNotEqualTo(file)
				.and(mainFileExplorerView.folderProperty().isNotNull())
				.and(Bindings.createBooleanBinding(() -> {
					File currentFolder = mainFileExplorerView.getFolder();
					if(currentFolder == null)
						return false;
					return currentFolder.toPath().startsWith(file.toPath());
				}, mainFileExplorerView.folderProperty())));
		moveToParent.setOnMouseClicked(_ -> {
			File currentFile = mainFileExplorerView.getFolder();
			if(currentFile == null)
				return;

			File parentFolder = currentFile.getParentFile();
			if(parentFolder == null)
				return;
			
			// parent must be root or inside root
			if(parentFolder.equals(file) || parentFolder.toPath().startsWith(file.toPath())) {
				enterFolder(parentFolder);
			}
		});
		
		functionBox.getChildren().addAll(backwardHistory, forwardHistory, moveToParent);
		functionBox.getChildren().add(searchField);
		this.setTop(functionBox);
		
		this.setOnKeyPressed(e -> {
			switch (e.getCode()) {
			case BACK_SPACE:
				loadPreviousHistoryView();
				break;
			default:
				break;
			}
		});
		
		this.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		this.setVisible(true);
		this.parentProperty().addListener((_, _, newVal) -> {
			if (newVal == null) {
				closePanel();
			}
		});
		this.enterFolder(file);
	}
	
	public void handleFileChange(FileChange fileChange) {
		
	}
	
	public void setMenuBar(MenuBar menuBar) {
		this.viewPane.setTop(menuBar);
	}
	
	public MainFileExplorerView getMainFileExplorerView() {
		return this.mainFileExplorerView;
	}
	
	public void closePanel() {
		this.mainFileExplorerView.closePanel();
		this.searchView.closePanel();
	}
	
	public void setCurrentFileFocused(File file) {
		
	}
	
	public void resetCurrentFileFocused() {
        
    }
	
	private void loadPreviousHistoryView() {
		loadHistoryView(this.history.getPreviousValue());
	}
	
	private void loadNextHistoryView() {
		loadHistoryView(this.history.getNextValue());
	}
	
	private void loadHistoryView(HistoryView historyView) {
		if (historyView == null)
			return;
		this.canRegisterHistory = false;
		if(historyView instanceof HistorySearchView historySearchView) {
			this.searchField.setText(historySearchView.getSearch());
			viewPane.setCenter(searchView);
			this.searchView.enterHistoryView(historyView);
		}
		else {
			this.searchField.clear();
			viewPane.setCenter(mainFileExplorerView);
			this.mainFileExplorerView.enterHistoryView(historyView);
		}
		this.canRegisterHistory = true;
    }
	
	public void enterFolder(File folder) {
		Platform.runLater(() -> {
			if(this.getChildren().contains(this.mainFileExplorerView)) {
				viewPane.setCenter(mainFileExplorerView);
			}
			this.searchField.clear();
			HistoryView historyView = this.mainFileExplorerView.enterFolder(folder);
			if(historyView != null)
	            this.history.add(historyView);
		});
	}
	
	public class ExpandPanel extends CanvasPane {
		
		private Image image;
		private Color color;
		private Color visibleColor = Color.BLACK;
		private Color invisibleColor = Color.GRAY;
		private double rotationAngle = 0;
		
		public ExpandPanel() {
            color = invisibleColor;
            this.parentProperty().addListener((_, _, newVal) -> {
            	if(newVal != null)
            		paintComponent();
            });
            visibleActiveProperty().addListener((_, _, newVal) -> {
            	if(newVal)
            		color = visibleColor;
            	else
            		color = invisibleColor;
            	paintComponent();
            });
			paintComponent();
		}
		
		@Override
		protected void paintComponent() {
			if(image == null)
				return;
	        GraphicsContext gc = this.getCanvas().getGraphicsContext2D();
	        gc.clearRect(0, 0, getWidth(), getHeight());
	        gc.save();
	        
	        // rotate around center
			gc.translate(getWidth() / 2, getHeight() / 2);
			gc.rotate(rotationAngle);
			gc.translate(-getWidth() / 2, -getHeight() / 2);
			
			Lighting lighting = new Lighting(new Light.Distant(45, 90, color));
			ColorAdjust bright = new ColorAdjust(0, 1, 1, 1);
			lighting.setContentInput(bright);
			lighting.setSurfaceScale(0.0);
			gc.setEffect(lighting);
			
			gc.setFill(color);
			gc.setStroke(color);
			gc.drawImage(image, 0, 0, getWidth(), getHeight());
			gc.restore(); // back to original state (before rotation)
		}
		
		public void setImageRotate(double angle) {
			this.rotationAngle = angle;
			paintComponent();
		}
		
		private final BooleanProperty visibleActive = new SimpleBooleanProperty();
		
		public BooleanProperty visibleActiveProperty() {
			return this.visibleActive;
		}
		
		public boolean getVisibleActive() {
			return visibleActive.get();
		}
		
		public void setVisibleActive(boolean value) {
			visibleActive.set(value);
		}
		
		public void setImage(Image image) {
			this.image = image;
		}
		
		public void reset() {
			visibleActiveProperty().unbind();
			visibleActive.set(false);
		}
	}
}