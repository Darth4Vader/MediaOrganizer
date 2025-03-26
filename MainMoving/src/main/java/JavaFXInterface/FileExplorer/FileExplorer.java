package JavaFXInterface.FileExplorer;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;

import org.controlsfx.control.GridView;

import JavaFXInterface.FileExplorerSearch.FileSearchView;
import JavaFXInterface.FileExplorerSearch.HistorySearchView;
import JavaFXInterface.FileExplorerView.MainFileExplorerView;
import JavaFXInterface.FileExplorerView.MainFileExplorerView.FileExplorerView;
import JavaFXInterface.controlsfx.DragResizePane;
import JavaFXUtilities.CanvasPane;
import OtherUtilities.ImageUtils;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Control;
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
import javafx.scene.transform.Affine;

public class FileExplorer extends BorderPane {

	private MainFileExplorerView mainFileExplorerView;
	
	private FileSearchView searchView;
	
	private HistoryList<HistoryView> history;
	
	private TextField searchField;
	
	private boolean canRegisterHistory;
	
	public FileExplorer(String filePath) {
		this(new File(filePath));
	}

	public FileExplorer(File file) {
		this.mainFileExplorerView = new MainFileExplorerView(this, FileExplorerView.DETAILS);
		
		Control fileView = this.mainFileExplorerView.getFileView();
		if(fileView instanceof GridView) {
			GridView<?> listView = (GridView<?>) fileView;
			listView.setCellWidth(this.getPrefWidth()*0.4);
			listView.setCellHeight(this.getPrefHeight()*0.4);
		}
		
		this.setCenter(mainFileExplorerView);
		BorderPane.setAlignment(mainFileExplorerView, Pos.TOP_CENTER);
		
		
		SideFilesList sidePnl = new SideFilesList(this, file);
		
		DragResizePane.makeResizable(sidePnl);
		
		sidePnl.setPrefWidth(150);
		
		this.setLeft(sidePnl);
		
		
		searchView = new FileSearchView(this, FileExplorerView.DETAILS);
		searchField = new TextField();
		searchField.setPromptText("Search");
		this.canRegisterHistory = true;
		searchField.textProperty().addListener((obs, oldVal, newVal) -> {
			if(!this.canRegisterHistory)
				return;
			if(!newVal.equals(oldVal)) {
				if(newVal.isBlank()) {
					this.setCenter(this.mainFileExplorerView);
				}
				else {
					System.out.println("New Value: " + newVal);
					this.setCenter(searchView);
					HistoryView historyView = searchView.search(this.mainFileExplorerView.getFolder(), newVal);
					if(historyView != null)
			            this.history.add(historyView);
				}
			}
		});
		
		this.history = new HistoryList<HistoryView>();
		
	
		HBox functionBox = new HBox();
		functionBox.setSpacing(10);
		ExpandPanel backwardHistory = new ExpandPanel();
		ExpandPanel forwardHistory = new ExpandPanel();
		backwardHistory.setImage(SwingFXUtils.toFXImage((BufferedImage) ImageUtils.getImageResource(ExpandPanel.class, "images/history_arrow.png"), null));
		backwardHistory.setPrefWidth(50);
		backwardHistory.visibleActiveProperty().bind(history.hasPrevious());
		backwardHistory.setOnMouseClicked(e -> {
			loadPreviousHistoryView();
		});
		forwardHistory.setImage(SwingFXUtils.toFXImage((BufferedImage) ImageUtils.getImageResource(ExpandPanel.class, "images/history_arrow.png"), null));
		forwardHistory.setPrefWidth(50);
		forwardHistory.visibleActiveProperty().bind(history.hasNext());
		forwardHistory.setOnMouseClicked(e -> {
			loadNextHistoryView();
        });
		functionBox.getChildren().addAll(backwardHistory, forwardHistory);
		
		
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
		
		this.enterFolder(file);
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
		if(historyView instanceof HistorySearchView) {
			this.searchField.setText(((HistorySearchView) historyView).getSearch());
			this.setCenter(searchView);
			this.searchView.enterHistoryView(historyView);
		}
		else {
			this.searchField.clear();
			this.setCenter(mainFileExplorerView);
			this.mainFileExplorerView.enterHistoryView(historyView);
		}
		this.canRegisterHistory = true;
    }
	
	public void enterFolder(File folder) {
		Platform.runLater(() -> {
			if(this.getChildren().contains(this.mainFileExplorerView)) {
				this.setCenter(mainFileExplorerView);
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
		
		public ExpandPanel() {
            color = invisibleColor;
            this.parentProperty().addListener((obs, oldVal, newVal) -> {
            	if(newVal != null)
            		paintComponent();
            });
            visibleActiveProperty().addListener((obs, oldVal, newVal) -> {
            	if(newVal)
            		color = visibleColor;
            	else
            		color = invisibleColor;
            	paintComponent();
            });
            /*
			this.setOnMouseExited(e -> {
				color = getExpended() ? Color.BLACK : Color.GRAY;
				paintComponent();
			});
			this.setOnMouseEntered(e -> {
				color = getExpended() ? Color.GRAY : Color.BLACK;
				paintComponent();
			});
			*/
			paintComponent();
		}
		
		@Override
		protected void paintComponent() {
			if(image == null)
				return;
            int x = (int) (getWidth() / 2);
            int y = (int) (getHeight() / 2);
			Point center = new Point(x, y);
	        GraphicsContext gc = this.getCanvas().getGraphicsContext2D();
	        gc.clearRect(0, 0, getWidth(), getHeight());
	        gc.save();
			Affine saveAT = gc.getTransform();
			
			/*if(visibleActive.get()) {
		        Affine at = new Affine(saveAT);
		        at.appendRotation(90, center.x, center.y);
		        gc.setTransform(at);
			}*/
			
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
		
		private final BooleanProperty visibleActive = new SimpleBooleanProperty();
		
		public BooleanProperty visibleActiveProperty() {
			return this.visibleActive;
		}
		
		public boolean getVisibleActive() {
			return visibleActive.get();
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
