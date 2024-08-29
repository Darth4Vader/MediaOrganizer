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
import java.awt.Image;
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
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class FileExplorer extends BorderPane {

	private RenameFilePanel infoPanel;
	private final JPanel mainPanel;
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
		
		//this.mainPanel = new JPanel(new BorderLayout());
		this.mainPanel = new FocusPaneView();
		this.mainPanel.setLayout(new BorderLayout());
		
		mainPanel.setOpaque(false);
		this.move = move;
		this.infoPanel = new RenameFilePanel(this);
		
		
		JScrollPane scroll = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setAlignmentY(Component.TOP_ALIGNMENT);
		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);
		this.setCenter(scroll);
		scroll.setViewportView(mainPanel);
		scroll.addFocusListener(SwingUtils.ScrollPaneFocus);
		
		
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
	
	private JPanel getSearchPnl() {
		JPanel pnl = new TraverseContainer() {
			
			@Override
			public Dimension getPreferredSize() {
				return SwingUtils.getVerticalRatioSize(this, 0.15);
			}
			
		};
		pnl.setOpaque(false);
		pnl.setLayout(new BorderLayout());
		pnl.add(createToolPanels(), BorderLayout.CENTER);
		pnl.add(new SearchPanel(this), BorderLayout.LINE_END);
		return pnl;
	}
	
	public ManageFolder getFolderManager() {
		return this.move;
	}
	
	private void addInfoPanel(ToolName tool) {
		this.remove(infoPanel);
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
				this.add(infoPanel, BorderLayout.LINE_END);
			}
			break;
		case SET_LOGO:
			break;
		case SET_SUBTITLES:
			break;
		default:
			break;
		}
		refreshFrame();
	}

	private void setMainPanel(String path) {
		setMainPanel(new File(path));
	}

	public void setMainPanel(File folder) {
		setMainPanel(folder, null);
	}
	
	private void setMainPanel(File folder, File toFocus) {
		this.mainPanel.removeAll();
		this.folder = folder;
		JPanel pnl = new TraverseContainer(true, true);
		pnl.setLayout(new GridLayout(0, 5));
		pnl.setOpaque(false);
		File[] files = folder.listFiles();
		for (File file : files) {
			if(!FilesUtils.isSystemFile(file)) {
				FilePanel filePnl = new FilePanel(file);
				pnl.add(filePnl);
				if(toFocus != null && file.equals(toFocus)) {
					filePnl.requestFocusInWindow();
				}
			}
		}
		this.mainPanel.add(pnl, BorderLayout.CENTER);
		refreshFrame();
	}
	
	private void goToParentFile(File file) {
		setMainPanel(file.getParentFile(), file);
	}

	public void refreshFrame() {
		revalidate();
		repaint();
	}
	
	private final Map<ToolName, ToolPanel> toolMap;
	
	private JComponent createToolPanels() {
		JPanel pnl = new TraverseContainer();
		/*JToolBar pnl = new JToolBar() {
			
			@Override
			public Dimension getPreferredSize() {
				return SwingUtils.getRatioSize(this, SwingUtils.H_RATIO, 0.15);
			}
			
		};*/
		pnl.setLayout(new BoxLayout(pnl, BoxLayout.LINE_AXIS));
		pnl.setOpaque(false);
		//pnl.setBackground(Color.GRAY);
		for(ToolName tool : ToolName.values())
			this.toolMap.put(tool, new ToolPanel(tool));
		pnl.add(toolMap.get(ToolName.ORGANIZE_FOLDER));
		pnl.add(toolMap.get(ToolName.RENAME_FILE));
		pnl.add(toolMap.get(ToolName.REFRESH_LOGO));
		pnl.add(toolMap.get(ToolName.SET_LOGO));
		pnl.add(toolMap.get(ToolName.SET_SUBTITLES));
		
		
		
		/*pnl.add(new ToolPanel(ToolName.ORGANIZE_FOLDER));
		pnl.add(new ToolPanel(ToolName.RENAME_FILE));
		pnl.add(new ToolPanel(ToolName.REFRESH_LOGO));
		pnl.add(new ToolPanel(ToolName.SET_LOGO));
		pnl.add(new ToolPanel(ToolName.SET_SUBTITLES));*/
		
		
		/*pnl.add(new ToolPanel("Organize Folder", "Organize Folder"));
		pnl.add(new ToolPanel("Refresh File Logo", "Refresh File Logo"));
		pnl.add(new ToolPanel("Rename file", "Rename file"));
		pnl.add(new ToolPanel("Set File Logo", "Set File Logo"));
		pnl.add(new ToolPanel("set main subtitles language", "set main subtitles language"));*/
		/*pnl.add();
		pnl.add();*/
		return pnl;
	}
	
	private void updateToolPanels(FilePanel filePanel) {
		this.filePanel = filePanel;
		File file = filePanel.getFile();
		for(ToolPanel tool : this.toolMap.values() ) {
			tool.setUsage(file);
		}
		refreshFrame();
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
	
	private class ToolPanel extends FocusContainer /* JPanel */ implements MouseListener, FocusListener { 
		
		private final ToolName tool;
		private boolean canUse;
		
		public ToolPanel(ToolName tool) {
			this.tool = tool;
			this.canUse = false;
			this.setLayout(new BorderLayout());
			this.setOpaque(false);
			final Image img = ImageUtils.loadImage("Data\\Images\\" + tool.getID());
			final BufferedImage image = img != null ? ImageUtils.paintImageInColor(img, Color.BLACK) : null;
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
			add(lblIcon, BorderLayout.CENTER);
			JLabel lblName = new JLabel(tool.getName());
			add(lblName, BorderLayout.PAGE_END);
			addFocusListener(this);
			addMouseListener(this);
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

		@Override
		public void focusGained(FocusEvent e) {
			this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			refreshFrame();
		}

		@Override
		public void focusLost(FocusEvent e) {
			this.setBorder(null);
			refreshFrame();
		}


		@Override
		public void mouseClicked(MouseEvent e) {
			activate();
			if(canUse)
				this.requestFocusInWindow();
		}


		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void mouseEntered(MouseEvent e) {
			this.setOpaque(true);
			this.setBackground(new Color(211, 211, 211, 60));
			refreshFrame();
		}


		@Override
		public void mouseExited(MouseEvent e) {
			this.setOpaque(false);
			this.setBackground(null);
			refreshFrame();
		}
	}
	
	public class FilePanel extends BorderPane implements FocusListener {
		
		private File file;
		private final FileName text;
		private Image image;
		private Icon icon;
		
		public FilePanel(File file) {
			this.file = file;
			this.icon = FileSystemView.getFileSystemView().getSystemIcon(file);
			setLayout(new BorderLayout());
			setOpaque(false);
			updateImage();
			JPanel img = new JPanel() {

				@Override
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					if(image != null)
						g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
				}
			};
			img.setOpaque(false);
			this.text = new FileName(file);
			setCenter(img);
			setBottom(text);
			addFocusListener(this);
			setOnMouseClicked(e -> {
				this.requestFocus();
				if(e.getClickCount() == 2 && e.isSecondaryButtonDown()) {
					setMainPanel(file);
				}
			});
		}
		
		@Override
		public Dimension getPreferredSize() {
			Dimension size = FocusPaneView.getRatioDimension(this, 0.5, 0.5);
			if(size != null) 
				return size;
			return super.getPreferredSize();
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
		}

		@Override
		public void focusGained(FocusEvent e) {
			//System.out.println("Remrember  ");
			updateToolPanels(this);
			this.setBorder(BorderFactory.createLineBorder(Color.PINK));
		}

		@Override
		public void focusLost(FocusEvent e) {
			// TODO Auto-generated method stub
			this.setBorder(null);
			refreshFrame();
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
