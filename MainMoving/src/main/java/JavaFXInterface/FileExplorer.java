package JavaFXInterface;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;

import DataStructures.FileInfo;
import DataStructures.ManageFolder;
import DataStructures.NameInfo.NameInfoType;
import FileUtilities.FilesUtils;
import FileUtilities.MimeUtils;
import OtherUtilities.ImageUtils;
import SwingUtilities.DocumantFilterList;
import SwingUtilities.FocusContainer;
import SwingUtilities.FocusPaneView;
import SwingUtilities.SwingUtils;
import SwingUtilities.TraverseContainer;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class FileExplorer extends BorderPane {

	private RenameFilePanel infoPanel;
	private final BorderPane mainPanel;
	private final ManageFolder move;
	private FilePanel filePanel;
	private File folder;

	public FileExplorer(ManageFolder move) {
		this.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		this.setPrefSize((int) (width * 0.445), (int) (height * 0.445));
		this.toolMap = new HashMap<>();
		
		this.mainPanel = new BorderPane();
		
		this.move = move;
		this.infoPanel = new RenameFilePanel(this);
		
		
		ScrollPane scroll = new ScrollPane(mainPanel);
		scroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		this.setCenter(scroll);
		BorderPane.setAlignment(scroll, Pos.TOP_CENTER);
		
		//this.add(mainPanel, BorderLayout.CENTER);
		//JPanel toolPanel = createToolPanels();
		//this.add(createToolPanels(), BorderLayout.PAGE_START);
		
		this.setTop(getSearchPnl());
		
		
		JScrollPane sidePnl = new SideFilesList(this, new File(move.getMainFolderPath())) {
			
			@Override
			public Dimension getPreferredSize() {
				return SwingUtils.getHorizontalRatioSize(this, 0.3);
			}
		};
		this.setLeft(sidePnl);
		
		
		this.setVisible(true);
		setMainPanel(move.getMainFolderPath());
		this.mainPanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "Exist");
		this.mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "Exist");
		this.mainPanel.getActionMap().put("Exist", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				goToParentFile(folder);
				//setMainPanel(folder.getParent());
			}
	    });
	}
	
	private BorderPane getSearchPnl() {
		BorderPane pnl = new BorderPane();
		pnl.prefHeightProperty().bind(this.heightProperty().multiply(0.15));
		pnl.setCenter(createToolPanels());
		pnl.setRight(new SearchPanel(this));
		return pnl;
	}
	
	public ManageFolder getFolderManager() {
		return this.move;
	}
	
	private void addInfoPanel(ToolName tool) {
		this.getChildren().remove(infoPanel);
		Component component = null;
		switch (tool) {
		case ORGANIZE_FOLDER:
			move.moveFilesFromInput();
			break;
		case REFRESH_LOGO:
			break;
		case RENAME_FILE:
			infoPanel.setPanel(filePanel);
			if(infoPanel != null && !infoPanel.isDisplayable()) {
				this.setRight(infoPanel);
			}
			break;
		case SET_LOGO:
			break;
		case SET_SUBTITLES:
			break;
		default:
			break;
		}
	}

	private void setMainPanel(String path) {
		setMainPanel(new File(path));
	}

	public void setMainPanel(File folder) {
		setMainPanel(folder, null);
	}
	
	private void setMainPanel(File folder, File toFocus) {
		this.mainPanel.getChildren().removeAll();
		this.folder = folder;
		GridPane pnl = new GridPane();
		File[] files = folder.listFiles();
		for (File file : files) {
			if(!FilesUtils.isSystemFile(file)) {
				FilePanel filePnl = new FilePanel(file);
				pnl.getChildren().add(filePnl);
				if(toFocus != null && file.equals(toFocus)) {
					filePnl.requestFocus();
				}
			}
		}
		this.mainPanel.setCenter(pnl);
	}
	
	private void goToParentFile(File file) {
		setMainPanel(file.getParentFile(), file);
	}
	
	private final Map<ToolName, ToolPanel> toolMap;
	
	private HBox createToolPanels() {
		HBox pnl = new HBox();
		for(ToolName tool : ToolName.values())
			this.toolMap.put(tool, new ToolPanel(tool));
		pnl.getChildren().add(toolMap.get(ToolName.ORGANIZE_FOLDER));
		pnl.getChildren().add(toolMap.get(ToolName.RENAME_FILE));
		pnl.getChildren().add(toolMap.get(ToolName.REFRESH_LOGO));
		pnl.getChildren().add(toolMap.get(ToolName.SET_LOGO));
		pnl.getChildren().add(toolMap.get(ToolName.SET_SUBTITLES));
		return pnl;
	}
	
	private void updateToolPanels(FilePanel filePanel) {
		this.filePanel = filePanel;
		File file = filePanel.getFile();
		for(ToolPanel tool : this.toolMap.values() ) {
			tool.setUsage(file);
		}
	}
	
	
	private enum ToolName {
		ORGANIZE_FOLDER("Organize Folder", "Organize Folder"),
		RENAME_FILE("Rename file", "Rename file"),
		REFRESH_LOGO("Refresh File Logo", "Refresh File Logo"),
		SET_LOGO("Set File Logo", "Set File Logo"),
		SET_SUBTITLES("set main subtitles language", "set main subtitles language");
		
		
		private final String id;
		private final String name;
		
		private ToolName(String id, String name) {
			this.id = id;
			this.name = name;
		}
		
		public String getID() {
			return this.id;
		}
		
		public String getName() {
			return this.name;
		}	
	}
	
	private class ToolPanel extends BorderPane { 
		
		private final ToolName tool;
		private boolean canUse;
		
		public ToolPanel(ToolName tool) {
			this.tool = tool;
			this.canUse = false;
			final java.awt.Image img = ImageUtils.loadImage("Data\\Images\\" + tool.getID());
			final BufferedImage image = img != null ? ImageUtils.paintImageInColor(img, java.awt.Color.BLACK) : null;
			JPanel lblIcon = new JPanel() {
				
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					if(image != null) {
						Rectangle rect = SwingUtils.getComponentCenterRect(this, 0.6, 0.6);
					    g.drawImage(image, rect.x, rect.y, rect.width, rect.height, null);
					}
				}
				
			};
			lblIcon.setOpaque(false);
			this.setCenter(lblIcon);
			Label lblName = new Label(tool.getName());
			this.setTop(lblName);
			setOnMouseClicked(e -> {
				activate();
				if(canUse)
					this.requestFocus();
			});
			setOnMouseEntered(e -> {
				this.setBackground(new Background(new BackgroundFill(new Color(211, 211, 211, 60), CornerRadii.EMPTY, Insets.EMPTY)));
			});
			setOnMouseExited(e -> {
				this.setBackground(null);
			});
			this.focusedProperty().addListener((obs, oldValue, newValue) -> {
				if(newValue) {
					this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
							BorderWidths.DEFAULT)));
				}
				else {
					this.setBorder(null);
				}
			});
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if(!canUse) {
				Graphics2D g2d = (Graphics2D) g;
				float opacity = 0.1f;
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
			}
		}
		
		public void setUsage(File file) {
			//System.out.println("Useage:   ");
			if(file.isDirectory())
				this.canUse = true;
			else
				this.canUse = false;
		}
		
		public void activate() {
			if(canUse)
				addInfoPanel(tool);
		}
	}
	
	public class FilePanel extends BorderPane {
		
		private File file;
		private final FileName text;
		private ImageView imageView;
		private Icon icon;
		
		public FilePanel(File file) {
			this.file = file;
			prefWidthProperty().bind(mainPanel.widthProperty().multiply(0.5));
			prefHeightProperty().bind(mainPanel.heightProperty().multiply(0.5));
			imageView = new ImageView();
			imageView.setPreserveRatio(true);
			updateImage();
			this.text = new FileName(file);
			this.setCenter(imageView);
			this.setBottom(text);
			this.focusedProperty().addListener((obs, oldValue, newValue) -> {
				if(newValue) {
					updateToolPanels(this);
					this.setBorder(new Border(new BorderStroke(Color.PINK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
							BorderWidths.DEFAULT)));
				}
				else {
					this.setBorder(null);
				}
			});
			setOnMouseClicked(e -> {
				this.requestFocus();
				if(e.getClickCount() == 2 && e.isSecondaryButtonDown()) {
					setMainPanel(file);
				}
			});
		}
		
		public File getFile() {
			return this.file;
		}
		
		public void updateFile(File file) {
			this.file = file;
			text.updateText(file);
		}
		
		public void updateImage() {
			File imageFile = null;
			if(this.file.isDirectory())
				imageFile = FilesUtils.getFileLogo(file);
			java.awt.Image image;
			if(imageFile != null)
				image = ImageUtils.loadImage(imageFile.getPath());
			else {
				if(icon instanceof ImageIcon)
					image = ((ImageIcon) icon).getImage();
				else { 
					image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
					Graphics2D g = ((BufferedImage) image).createGraphics(); icon.paintIcon(null, g, 0, 0);
					g.dispose();
				}
			}
			if(image instanceof BufferedImage)
				imageView.setImage(SwingFXUtils.toFXImage((BufferedImage) image, null));
		}
	}

	class FileName extends TextField {

		private boolean hasFocus;
		private String text;
		
		public FileName(File file) {
			setAlignment(Pos.CENTER);
			updateText(file);
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
