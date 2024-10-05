package JavaFXInterface;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
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
import OtherUtilities.ImageUtils;
import SwingUtilities.FocusContainer;
import SwingUtilities.FocusPaneView;
import SwingUtilities.JLabelTextFill;
import SwingUtilities.SwingUtils;
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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.ImageView;
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

public class SideFilesList2 extends ScrollPane {

	private FileExplorer explorer;
	private BorderPane view;
	
	public SideFilesList2(File root) {
		this(null, root);
	}
	
	public SideFilesList2(FileExplorer explorer, File root) {
		this.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		//this.setHbarPolicy(ScrollBarPolicy.NEVER);
		this.explorer = explorer;
		this.view = new BorderPane();
		view.setCenter(new SideFilePanel(root));
		this.setContent(view);
	}
	
	public static final javafx.scene.image.Image SIDE_ARROW = SwingFXUtils.toFXImage((BufferedImage) ImageUtils.getImageResource(ExpandPanel.class, "images/side_arrow.png"), null);
	
	public class ExpandPanel extends Canvas {
		
		private boolean expanded;
		
		private Color color;
		
		//private final Image SIDE_ARROW = ImageUtils.getImageResource(ExpandPanel.class, "images/side_arrow.png");
		
		public ExpandPanel() {
            widthProperty().addListener(evt -> draw());
            heightProperty().addListener(evt -> draw());
			
            
            this.setOnMouseClicked(e -> {
				System.out.println("llll");
				expanded = !expanded;
				draw();
			});
			
			
            
            /*
            this.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
				System.out.println("llll");
				expanded = !expanded;
				draw();
			});
            */
            
			this.setOnMouseExited(e -> {
				color = Color.GRAY;
				draw();
			});
			this.setOnMouseEntered(e -> {
				System.out.println("fffff");
				color = Color.BLACK;
				draw();
			});
			color = Color.GRAY;
			draw();
			/*this.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
					BorderWidths.DEFAULT)));*/
		}
		
		private void draw() {
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
			
			if(expanded) {
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
			gc.drawImage(SIDE_ARROW, 0, 0, getWidth(), getHeight());
			gc.restore(); // back to original state (before rotation)
			//gc.setTransform(saveAT);
		}
		
		public boolean activated() {
			return expanded;
		}
	}
	
	public class SideFilePanel extends VBox {
		
		private SideFilePanel(File root) {
			File[] list = root.listFiles();
			for(File file : list)
				if(file.isDirectory()) {
					/*JPanel pnl = new JPanel(new BorderLayout());
					pnl.add(new SideFilePanel(file, false));
					pnl.setAlignmentY(TOP_ALIGNMENT);
					pnl.setAlignmentX(LEFT_ALIGNMENT);
					add(pnl);*/
					_SideFilePanel sidePnl = new _SideFilePanel(file);
					getChildren().add(sidePnl);
					
				}
			r = true;
			//setBorder(BorderFactory.createLineBorder(Color.GREEN));
		}
		private class _SideFilePanel extends HBox {
			
			private _SideFilePanel(File file) {
				this.setAlignment(Pos.TOP_LEFT);
				final Image image = getImage(file);
				ImageView img = new ImageView();
				img.setPreserveRatio(true);
				img.fitWidthProperty().bind(SideFilesList2.this.widthProperty().multiply(0.15));
				img.fitHeightProperty().bind(SideFilesList2.this.heightProperty().multiply(V_RATIO));
				
				/*if(image instanceof BufferedImage)
					img.setImage(SwingFXUtils.toFXImage((BufferedImage) image, null));*/
				
				
				Label text = new Label(file.getName());
				ContextMenu jp = new ContextMenu();
				//jp.prefHeightProperty().bind(text.prefHeightProperty());
				Label pop = new Label(text.getText());
				pop.setOnMouseClicked(e -> {
					System.out.println(file);
					explorer.getMainFileExplorerView().setMainPanel(file);
				});
				pop.setCursor(Cursor.HAND);
				MenuItem cut = new MenuItem();
				cut.setGraphic(pop);
				jp.getItems().addAll(cut);
				pop.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
		            @Override
		            public void handle(MouseEvent event) {
		            	
		            	Bounds boundsInScene = pop.localToScreen(pop.getBoundsInLocal());
						System.out.println("Bye " + boundsInScene);
						System.out.println(event.getScreenX() + " , " + event.getScreenY());
		            	if(!boundsInScene.contains(event.getScreenX(), event.getScreenY()))
							jp.hide();
		            }
		        });
				BorderPane fileWithName = new BorderPane();
				fileWithName.prefHeightProperty().bind(SideFilesList2.this.heightProperty().multiply(V_RATIO));
				

				fileWithName.setLeft(img);
				fileWithName.setCenter(text);
				fileWithName.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
						BorderWidths.DEFAULT)));
				fileWithName.setCursor(Cursor.HAND);
				BorderPane fileWithNamePnl = new BorderPane();
				fileWithNamePnl.setOnMouseClicked(e -> {
					System.out.println(file);
					explorer.getMainFileExplorerView().setMainPanel(file);
				});
				fileWithNamePnl.setLeft(fileWithName);
				
				VBox filesPanel = new VBox();
				
				text.setOnMouseEntered(e -> {
					Bounds scrollBound = getVisibleBounds(SideFilesList2.this.getContent());
					Bounds insideScrollBounds = getBoundsInAncestor(fileWithNamePnl, this);
					if(insideScrollBounds.getMaxX() > scrollBound.getMaxX()) {
						Bounds inScreen = fileWithNamePnl.localToScreen(fileWithNamePnl.getBoundsInLocal());
						jp.show(text, inScreen.getMaxX(), inScreen.getMinY());
					}
				});
				
				text.setOnMouseExited(event -> {
	            	Bounds boundsInScene = text.localToScreen(text.getBoundsInLocal());
					System.out.println("Bye " + boundsInScene);
					System.out.println(event.getScreenX() + " , " + event.getScreenY());
	            	if(!boundsInScene.contains(event.getScreenX(), event.getScreenY())) {
	            		Bounds popBounds = pop.localToScreen(pop.getBoundsInLocal());
        				if(popBounds == null || !popBounds.contains(event.getScreenX(), event.getScreenY()))
        					jp.hide();
	            	}
				});
				
				filesPanel.getChildren().add(fileWithNamePnl);
				ExpandPanel expand = new ExpandPanel();
				
				expand.widthProperty().bind(SideFilesList2.this.widthProperty().multiply(0.5/*0.1*/));
				expand.heightProperty().bind(SideFilesList2.this.heightProperty().multiply(V_RATIO));
				
				EventHandler<? super MouseEvent> prevEvent = expand.getOnMouseClicked();
				
				
				
				/*
				expand.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
					//prevEvent.handle(e);
					System.out.println("kkkk");
					if(expand.activated()) {
						if(subFolderPanel == null)
							subFolderPanel = new SideFilePanel(file);
						else {
							filesPanel.getChildren().remove(subFolderPanel);
							subFolderPanel.getChildren().clear();
						}
						if(!subFolderPanel.getChildren().isEmpty())
							filesPanel.getChildren().add(subFolderPanel);
					}
				});
				
				*/
				
				
				expand.setOnMouseClicked(e -> {
					prevEvent.handle(e);
					System.out.println("kkkk");
					if(expand.activated()) {
						if(subFolderPanel == null)
							subFolderPanel = new SideFilePanel(file);
						else {
							System.out.println("Remove");
							filesPanel.getChildren().remove(subFolderPanel);
							//System.out.println(filesPanel.getChildren().remove(subFolderPanel));
							System.out.println(filesPanel.getChildren());
							//filesPanel.getChildren().clear();
							//subFolderPanel.getChildren().clear();
						}
						System.out.println("hiii");
						if(!subFolderPanel.getChildren().isEmpty())
							filesPanel.getChildren().add(subFolderPanel);
					}
					else {
						if(subFolderPanel != null)
							filesPanel.getChildren().remove(subFolderPanel);
					}
				});
				
				
				
				getChildren().add(expand);
				getChildren().add(filesPanel);
			}
		}
		
		private static final double V_RATIO = 0.15; //0.15; 
		
		private SideFilePanel subFolderPanel;
		
		private boolean r;
		
		public Image getImage(File file) {
			File imageFile = null;
			if(file.isDirectory())
				imageFile = FilesUtils.getFileLogo(file);
			Image image;
			if(imageFile != null)
				image = ImageUtils.loadImage(imageFile.getPath());
			else {
				Icon icon = FileSystemView.getFileSystemView().getSystemIcon(file);
				if(icon instanceof ImageIcon)
					image = ((ImageIcon) icon).getImage();
				else { 
					image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
					Graphics2D g = ((BufferedImage) image).createGraphics(); icon.paintIcon(null, g, 0, 0);
					g.dispose();
				}
			}
			return image;
		}
	}
	
	static Point getTextCenter(JLabel lbl) {
        Rectangle viewR = new Rectangle();
        viewR.width = lbl.getSize().width;
        viewR.height = lbl.getSize().height;
        Rectangle iconR = new Rectangle();
        Rectangle textR = new Rectangle();
        String clippedText = SwingUtilities.layoutCompoundLabel
        (
            lbl,
            lbl.getFontMetrics(lbl.getFont()),
            lbl.getText(),
            lbl.getIcon(),
            lbl.getVerticalAlignment(),
            lbl.getHorizontalAlignment(),
            lbl.getVerticalTextPosition(),
            lbl.getHorizontalTextPosition(),
            viewR,
            iconR,
            textR,
            lbl.getIconTextGap()
        );
        return ImageUtils.getRectCenter(textR.getBounds());
	}
	
    public static Bounds getVisibleBounds(Node aNode)
    {
        // If node not visible, return empty bounds
        if(!aNode.isVisible()) return new BoundingBox(0,0,-1,-1);
        //System.out.println(aNode);
        // If node has clip, return clip bounds in node coords
        if(aNode.getClip()!=null) return aNode.getClip().getBoundsInParent();

        // If node has parent, get parent visible bounds in node coords
        Bounds bounds = aNode.getParent()!=null? getVisibleBounds(aNode.getParent()) : null;
        if(bounds!=null && !bounds.isEmpty()) bounds = aNode.parentToLocal(bounds);
        return bounds;
    }
    
    public static Bounds getBoundsInAncestor(Node node, Node ancestor)
    {
    	Bounds bounds = node.getBoundsInParent();
    	node = node.getParent();
    	while(node != null) {
    		bounds = node.localToParent(bounds);
    		if(ancestor.equals(node))
    			break;
    		else
    			node = node.getParent();
    		//System.out.println(aNode.getClip()!=null);
    	}
    	return bounds;
    }
}