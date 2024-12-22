package JavaFXInterface;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import JavaFXUtilities.CanvasPane;
import OtherUtilities.ImageUtils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class SideFilesList extends TreeView<File> {
	
	public static final javafx.scene.image.Image SIDE_ARROW = SwingFXUtils.toFXImage((BufferedImage) ImageUtils.getImageResource(ExpandPanel.class, "images/side_arrow.png"), null);
	
	public SideFilesList(FileExplorer explorer, File root) {
	    TreeItem<File> dummyRoot = new TreeItem<>();
	    dummyRoot.getChildren().addAll(getTreeFileItems(root));
	    this.setRoot(dummyRoot);
		this.setShowRoot(false);
		this.setCellFactory(treeView -> new TreeCell<>() {
			
			private ExpandPanel p = new ExpandPanel();
			
			private final ImageView imageView = new ImageView();
			
			{
		        setAlignment(Pos.CENTER_LEFT);
		        setBorder(Border.stroke(Color.AQUA));
		        p.setPrefWidth(10);
		        p.setPrefHeight(10);
			}
			
		    @Override
		    public void updateItem(File item, boolean empty) {
		        super.updateItem(item, empty);
		        if (item == null || empty) {
		            setGraphic(null);
		            p.setImage(null);
		            p.reset();
		            setText(null);
		            setOnMouseClicked(null);
		            imageView.setImage(null);
		        }
		        else {
					setText(item.getName());
					p.setImage(SIDE_ARROW);
					p.set(this);
		            imageView.setImage(AppUtils.getImageOfFile(item));
		            imageView.setFitWidth(10);
		            imageView.setFitHeight(10);
		            setGraphicTextGap(5);
		            setGraphic(imageView);
		            setOnMouseClicked(e -> {
						requestFocus();
						if(e.getClickCount() == 1 && e.getButton() == MouseButton.PRIMARY) {
							if(explorer !=  null)
								explorer.getMainFileExplorerView().setMainPanel(item);
						}
					});
		        }
		        if (getTreeItem() != null)
		        	setDisclosureNode(p);
		        else
		        	setDisclosureNode(null);
		    }
		});
	}
	
	private List<TreeItem<File>> getTreeFileItems(File root) {
		List<TreeItem<File>> treeItems = new ArrayList<>();
		File[] list = root.listFiles();
		for(File file : list)
			if(file.isDirectory()) {
				TreeItem<File> treeItem = new TreeItem<File>(file);
				treeItem.getChildren().add(new TreeItem<File>(file));
				treeItems.add(treeItem);
				treeItem.expandedProperty().addListener(new ChangeListener<Boolean>() {

					private boolean isFirstTime;
					
					@Override
					public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
							Boolean newValue) {
						if(newValue) {
							if(!isFirstTime) {
								treeItem.getChildren().clear();
								treeItem.getChildren().addAll(getTreeFileItems(file));
								isFirstTime = true;
							}
						}
					}
				});
			}
		return treeItems;
	}
	
	public class ExpandPanel extends CanvasPane {
		
		private Image image;
		private Color color;
		
		public ExpandPanel() {
            color = Color.GRAY;
            this.parentProperty().addListener((obs, oldVal, newVal) -> {
            	if(newVal != null)
            		paintComponent();
            });
            expendedProperty().addListener((obs, oldVal, newVal) -> {
            	if(newVal)
            		color = Color.BLACK;
            	else
            		color = Color.GRAY;
            	paintComponent();
            });
			this.setOnMouseExited(e -> {
				color = getExpended() ? Color.BLACK : Color.GRAY;
				paintComponent();
			});
			this.setOnMouseEntered(e -> {
				color = getExpended() ? Color.GRAY : Color.BLACK;
				paintComponent();
			});
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
			
			if(expanded.get()) {
		        Affine at = new Affine(saveAT);
		        at.appendRotation(90, center.x, center.y);
		        gc.setTransform(at);
			}
			
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
		
		private final BooleanProperty expanded = new SimpleBooleanProperty();
		
		public BooleanProperty expendedProperty() {
			return this.expanded;
		}
		
		public boolean getExpended() {
			return expanded.get();
		}
		
		public void setImage(Image image) {
			this.image = image;
		}

		public void set(TreeCell<File> cell) {
	        if (cell.getTreeItem() != null) {
	        	TreeItem<File> treeItem = cell.getTreeItem();
	        	expendedProperty().bind(treeItem.expandedProperty());
	        }
		}
		
		public void reset() {
			expendedProperty().unbind();
			expanded.set(false);
		}
	}
}