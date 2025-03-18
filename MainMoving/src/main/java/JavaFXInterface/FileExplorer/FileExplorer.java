package JavaFXInterface.FileExplorer;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;

import org.controlsfx.control.GridView;

import JavaFXInterface.FileExplorerSearch.FileSearchView;
import JavaFXInterface.FileExplorerView.MainFileExplorerView;
import JavaFXInterface.FileExplorerView.MainFileExplorerView.FileExplorerView;
import JavaFXInterface.controlsfx.DragResizePane;
import JavaFXUtilities.CanvasPane;
import OtherUtilities.ImageUtils;
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
	
	private HistoryList<HistoryView> history;
	
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
		
		
		FileSearchView searchView = new FileSearchView(this, FileExplorerView.DETAILS);
		TextField searchField = new TextField();
		searchField.setPromptText("Search");
		searchField.textProperty().addListener((obs, oldVal, newVal) -> {
			if(!newVal.equals(oldVal)) {
				if(newVal.isBlank()) {
					this.setCenter(this.mainFileExplorerView);
				}
				else {
					System.out.println("New Value: " + newVal);
					this.setCenter(searchView);
					searchView.search(this.mainFileExplorerView.getFolder(), newVal);
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
			loadHistoryView(history.getPreviousValue());
		});
		forwardHistory.setImage(SwingFXUtils.toFXImage((BufferedImage) ImageUtils.getImageResource(ExpandPanel.class, "images/history_arrow.png"), null));
		forwardHistory.setPrefWidth(50);
		forwardHistory.visibleActiveProperty().bind(history.hasNext());
		forwardHistory.setOnMouseClicked(e -> {
			loadHistoryView(history.getNextValue());
        });
		functionBox.getChildren().addAll(backwardHistory, forwardHistory);
		
		
		functionBox.getChildren().add(searchField);
		
		this.setTop(functionBox);
		
		this.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		this.setVisible(true);
		
		this.mainFileExplorerView.setMainPanel(file);
		
	}
	
	public MainFileExplorerView getMainFileExplorerView() {
		return this.mainFileExplorerView;
	}
	
	public void closePanel() {
		this.mainFileExplorerView.closePanel();
	}
	
	public void setCurrentFileFocused(File file) {
		
	}
	
	public void resetCurrentFileFocused() {
        
    }
	
	private void loadHistoryView(HistoryView historyView) {
		if (historyView == null)
			return;
        this.mainFileExplorerView.setMainPanel(historyView.getFolder());
    }
	
	public void nextFileHistory(File file) {
		if (file != null && !Objects.equals(file, this.history.getCurrentValue().getFolder()))
			this.history.add(new HistoryView(file));
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
