package JavaFXInterface;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.ToolTipManager;
import javax.swing.filechooser.FileSystemView;

import org.apache.poi.ddf.EscherColorRef.SysIndexProcedure;

import FileUtilities.FilesUtils;
import JavaFXInterface.controlsfx.NodeCellSetter;
import OtherUtilities.ImageUtils;
import SwingUtilities.FocusContainer;
import SwingUtilities.FocusPaneView;
import SwingUtilities.JLabelTextFill;
import SwingUtilities.SwingUtils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Cell;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.skin.TreeCellSkin;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

public class SideFilesList extends TreeView<File> {

	private FileExplorer explorer;
	
	/*public SideFilesList(File root) {
		this(null, root);
	}*/
	
	public SideFilesList(FileExplorer explorer, File root) {
		this.explorer = explorer;
	    TreeItem<File> dummyRoot = new TreeItem<>();
	    dummyRoot.getChildren().addAll(getTreeFileItems(root));
	    this.setRoot(dummyRoot);
		this.setShowRoot(false);
		this.setCellFactory((c) -> new TreeCell<>() {
			
			private ExpandPanel p = new ExpandPanel();
			
			private final ImageView imageView = new ImageView();
			
		    @Override
		    public void updateItem(File item, boolean empty) {
		        super.updateItem(item, empty);
		        if (item == null || empty) {
		            setGraphic(null);
		            p.setImage(null);
		            p.reset();
		            setText(null);
		            setOnMouseClicked(null);
		            System.out.println("innn " + getTreeItem());
		        	//cell.reset();
		            imageView.setImage(null);
		        }
		        else {
		        	//cell.set(item, this);
					System.out.println("Render " + item);
					System.out.println("Escobar " + getTreeItem());
		            //setGraphic(cell.getView());
					setText(item.getName());
					//p.setImage(SwingFXUtils.toFXImage((BufferedImage) ImageUtils.getImageResource(ExpandPanel.class, "images/side_arrow.png"), null));
					p.setImage(SIDE_ARROW);
					p.set(this);
		            imageView.setImage(AppUtils.getImageOfFile(item));
		            setGraphic(imageView);
					setOnMouseClicked(e -> {
						requestFocus();
						if(e.getClickCount() == 1 && e.getButton() == MouseButton.PRIMARY) {
							if(explorer !=  null)
								explorer.getMainFileExplorerView().setMainPanel(item);
							/*Control cellControlView = AppUtils.getCellOwner(this);
							if(cellControlView != null) {
								Parent owner = cellControlView.getParent();
								System.out.println("My life " + owner);
								if(owner instanceof FileExplorer) {
									((FileExplorer) owner).getMainFileExplorerView().setMainPanel(item);
								}
							}*/
						}
					});
					/*setGraphic(p);*/
		        }
		        if (getTreeItem() != null)
		        	setDisclosureNode(p);
		        else
		        	setDisclosureNode(null);
		        setAlignment(Pos.CENTER_LEFT);
		        
		        setBorder(Border.stroke(Color.AQUA));
		        
		        //p.
		        
	            /*if (getTreeItem() != null) {
	                // update disclosureNode depending on expanded state
	                setDisclosureNode(getTreeItem().isExpanded() ? new Label("-") : new Label("+"));
	            }*/
		        
		        //this.selecte
		        
		        p.setWidth(10);
		        p.setHeight(10);
		        
		        
		        /*imageView.setFitWidth(10);
		        imageView.setFitHeight(10);*/
		        
		        /*
		        if (getTreeItem() != null) {
		        	TreeItem<File> treeItem = getTreeItem();
		        	System.out.println("Bind: " + treeItem);
		        	p.getExpended().bind(treeItem.expandedProperty());
		        	//System.out.println(p.getExpended());
		        	setDisclosureNode(p);
		        }
		        else {
		        	p.getExpended().unbind();
		        	setDisclosureNode(null);
		        }
		        */
		        
		        
		        //TreeCellSkin<T>
		    }
		    
		});
		
		
		//private class TreeFilePanel extends 
	}
	
	private List<TreeItem<File>> getTreeFileItems(File root) {
		List<TreeItem<File>> treeItems = new ArrayList<>();
		File[] list = root.listFiles();
		for(File file : list)
			if(file.isDirectory()) {
				TreeItem<File> treeItem = new TreeItem<File>(file);
				treeItem.getChildren().add(new TreeItem<File>(file));
				treeItems.add(treeItem);;
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
	
	public static final javafx.scene.image.Image SIDE_ARROW = SwingFXUtils.toFXImage((BufferedImage) ImageUtils.getImageResource(ExpandPanel.class, "images/side_arrow.png"), null);
	
	public class ExpandPanel extends Canvas {
		
		//private boolean expanded;
		
		private BooleanProperty expanded;
		
		private Color color;
		
		//private final Image SIDE_ARROW = ImageUtils.getImageResource(ExpandPanel.class, "images/side_arrow.png");
		
		private Image image;
		
		public ExpandPanel() {
            widthProperty().addListener(evt -> draw());
            heightProperty().addListener(evt -> draw());
			
            expanded = new SimpleBooleanProperty();
            
            /*this.setOnMouseClicked(e -> {
				System.out.println("llll");
				expanded = !expanded;
				draw();
			});*/
			
			
            this.parentProperty().addListener((obs, oldVal, newVal) -> {
            	if(newVal != null)
            		draw();
            });
            
            /*
            this.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
				System.out.println("llll");
				expanded = !expanded;
				draw();
			});
            */
            
            expanded.addListener((obs, oldVal, newVal) -> {
            	if(newVal)
            		color = Color.BLACK;
            	else
            		color = Color.GRAY;
            	draw();
            });
            
			this.setOnMouseExited(e -> {
				System.out.println(e);
				
				color = expanded.get() ? Color.BLACK : Color.GRAY;
				//color = Color.GRAY;
				//color = getOppositeColor(color, Color.GRAY, Color.BLACK);
				draw();
			});
			this.setOnMouseEntered(e -> {
				System.out.println("fffff");
				//color = Color.BLACK;
				//color = getOppositeColor(color, Color.GRAY, Color.BLACK);
				color = expanded.get() ? Color.GRAY : Color.BLACK ;
				draw();
			});
			color = Color.GRAY;
			draw();
			/*this.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
					BorderWidths.DEFAULT)));*/
		}
		
		private Color getOppositeColor(Color color, Color color1, Color color2) {
			return color == color1 ? color2 : color1;
		}
		
		
		/*public void setExpand(boolean isExpanded) {
			this.expanded = isExpanded;
		}*/
		
		public BooleanProperty getExpended() {
			return this.expanded;
		}
		
		public void setImage(Image image) {
			this.image = image;
		}
		
		private void draw() {
			if(image == null)
				return;
            int x = (int) (getWidth() / 2);
            int y = (int) (getHeight() / 2);
			Point center = new Point(x, y);
					//getTextCenter(this);
			//System.out.println("Heeerrr");
	        GraphicsContext gc = getGraphicsContext2D();
	        gc.clearRect(0, 0, getWidth(), getHeight());
	        gc.save();
	        //g2d.drawOval(center.x, center.y, 50, 50);
			Affine saveAT = gc.getTransform();
			
			if(expanded.get()) {
		        Affine at = new Affine(saveAT);
		        Rotate rotate = new Rotate(Math.toRadians(90), center.x, center.y);
		        //at.appendRotation(90);
		        at.appendRotation(90, center.x, center.y);
		        //gc.rotate(90);
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
			//gc.setTransform(saveAT);
		}
		
		public boolean activated() {
			return expanded.get();
		}

		public void set(TreeCell<File> cell) {
	        if (cell.getTreeItem() != null) {
	        	TreeItem<File> treeItem = cell.getTreeItem();
	        	System.out.println("Bind: " + treeItem);
	        	getExpended().bind(treeItem.expandedProperty());
	        	//System.out.println(p.getExpended());
	        	//setDisclosureNode(p);
	        }
		}
		
		public void reset() {
			getExpended().unbind();
			expanded.set(false);
		}
	}
}