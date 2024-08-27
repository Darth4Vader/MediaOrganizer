package Interface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
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
import java.awt.event.MouseEvent;
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
import Interface.FileExplorer.FileName;
import OtherUtilities.ImageUtils;
import SwingUtilities.FocusContainer;
import SwingUtilities.FocusPaneView;
import SwingUtilities.JLabelTextFill;
import SwingUtilities.SwingUtils;

public class SideFilesList extends JScrollPane {

	private FileExplorer explorer;
	private FocusPaneView view;
	
	public SideFilesList(File root) {
		this(null, root);
	}
	
	public SideFilesList(FileExplorer explorer, File root) {
		super(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.explorer = explorer;
		this.view = new FocusPaneView(this);
		this.view.setOpaque(false);
		view.setLayout(new BorderLayout());
		view.add(new SideFilePanel(root), BorderLayout.CENTER);
		setViewportView(view);
		setOpaque(false);
		getViewport().setOpaque(false);
	}
	
	public class ExpandPanel2 extends JLabelTextFill {
		
		private boolean expanded;
		
		public ExpandPanel2(String text) {
			super(text);
			this.setHorizontalTextPosition(CENTER);
			this.setVerticalTextPosition(CENTER);
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					expanded = !expanded;
					revalidate();
					repaint();
				}
				
				@Override
			    public void mouseExited(MouseEvent e) {
					setForeground(Color.GRAY);
				}

			    @Override
			    public void mouseEntered(MouseEvent e) {
			    	setForeground(Color.BLACK);
			    }
			});
			setForeground(Color.GRAY);
		}
		
		
		@Override
		protected void paintComponent(Graphics g) {
	        Point center = getTextCenter(this);
	        Graphics2D g2d = (Graphics2D) g;
	        //g2d.drawOval(center.x, center.y, 50, 50);
			AffineTransform saveAT = g2d.getTransform();
			if(expanded) {
		        AffineTransform at = new AffineTransform();
		        at.concatenate(saveAT);
		        at.rotate(Math.toRadians(90), center.x, center.y);
		        g2d.setTransform(at);
			}
			super.paintComponent(g2d);
			g2d.setTransform(saveAT);
			g2d.dispose();
		}
		
		public boolean activated() {
			return expanded;
		}
	}
	
	public static final Image SIDE_ARROW = ImageUtils.getImageResource(ExpandPanel.class, "images/side_arrow.png");
	
	public class ExpandPanel extends JPanel {
		
		private boolean expanded;
		
		//private final Image SIDE_ARROW = ImageUtils.getImageResource(ExpandPanel.class, "images/side_arrow.png");
		
		public ExpandPanel() {
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					expanded = !expanded;
					revalidate();
					repaint();
				}
				
				@Override
			    public void mouseExited(MouseEvent e) {
					setForeground(Color.GRAY);
				}

			    @Override
			    public void mouseEntered(MouseEvent e) {
			    	setForeground(Color.BLACK);
			    }
			});
			setForeground(Color.GRAY);
			setBorder(BorderFactory.createLineBorder(Color.RED));
		}
		
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
            int x = getWidth() / 2;
            int y = getHeight() / 2;
			Point center = new Point(x, y);
					//getTextCenter(this);
	        Graphics2D g2d = (Graphics2D) g;
	        //g2d.drawOval(center.x, center.y, 50, 50);
			AffineTransform saveAT = g2d.getTransform();
			if(expanded) {
		        AffineTransform at = new AffineTransform();
		        at.concatenate(saveAT);
		        at.rotate(Math.toRadians(90), center.x, center.y);
		        g2d.setTransform(at);
			}
			g2d.drawImage(SIDE_ARROW, 0, 0, getWidth(), getHeight(), this);
			g2d.setTransform(saveAT);
			g2d.dispose();
		}
		
		public boolean activated() {
			return expanded;
		}
	}
	
	public class SideFilePanel extends JPanel {
		
		private static final double V_RATIO = 0.15; //0.15; 
		
		private JPanel subFolderPanel;
		
		private boolean r;
		
		public SideFilePanel(File root) {
			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			setOpaque(false);
			File[] list = root.listFiles();
			for(File file : list)
				if(file.isDirectory()) {
					/*JPanel pnl = new JPanel(new BorderLayout());
					pnl.add(new SideFilePanel(file, false));
					pnl.setAlignmentY(TOP_ALIGNMENT);
					pnl.setAlignmentX(LEFT_ALIGNMENT);
					add(pnl);*/
					
					add(new SideFilePanel(file, false));
				}
			r = true;
			//setBorder(BorderFactory.createLineBorder(Color.GREEN));
		}
		
		private SideFilePanel(File file, boolean sub) {
			setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
			setOpaque(false);
			setAlignmentX(LEFT_ALIGNMENT);
			setAlignmentY(TOP_ALIGNMENT);
			setOpaque(false);
			final Image image = getImage(file);
			JPanel img = new JPanel() {

				@Override
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					if(image != null)
						g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
				}
				
				@Override
				public Dimension getPreferredSize() {
					return FocusPaneView.getRatioDimensionWithoutLayout(this, 0.15, V_RATIO);
				}
			};
			img.setOpaque(false);
			JLabel text = new JLabel(file.getName());
			JPopupMenu jp = new JPopupMenu() {
				
				@Override
				public Dimension getPreferredSize() {
					Dimension size = super.getPreferredSize();
					Dimension textSize = text.getSize(); 
					return new Dimension(size.width, textSize.height);
				}
				
			};
			JLabel pop = new JLabel(text.getText());
			pop.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mouseClicked(MouseEvent e) {
					System.out.println(file);
					explorer.setMainPanel(file);
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					System.out.println("Bravo");
					/*System.out.println("inside2");
					System.out.println(fileWithName.getPreferredSize());
					System.out.println(fileWithName.getSize());*/
					//System.out.println(e);
				}
				
				/*@Override
				public void mouseExited(MouseEvent e) {
					System.out.println(e);
				}*/
			});
			pop.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			jp.setLayout(new BorderLayout());
			jp.add(pop);
			jp.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseExited(MouseEvent e) {
					if(!jp.getVisibleRect().contains(e.getPoint()))
						jp.setVisible(false);
				}
				
			});
			JPanel fileWithName = new JPanel() {
				@Override
				public Dimension getPreferredSize() {
					return FocusPaneView.getVerticalRatioDimension(this, V_RATIO, super.getPreferredSize());
				}
			};
			//fileWithName.setLayout(new BoxLayout(fileWithName, BoxLayout.LINE_AXIS));
			fileWithName.setLayout(new BorderLayout(0, 0));
			fileWithName.add(img, BorderLayout.LINE_START);
			fileWithName.add(text, BorderLayout.CENTER);
			
			/*img.setAlignmentY(TOP_ALIGNMENT);
			fileWithName.add(img);
			text.setAlignmentY(TOP_ALIGNMENT);
			fileWithName.add(text);*/
			
			fileWithName.setOpaque(false);
			fileWithName.setBorder(BorderFactory.createLineBorder(Color.RED));
			fileWithName.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			JPanel fileWithNamePnl = new JPanel(/*new BorderLayout()*/);
			fileWithNamePnl.setLayout(new BorderLayout());
			
			
			fileWithNamePnl.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mouseClicked(MouseEvent e) {
					System.out.println(file);
					explorer.setMainPanel(file);
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					System.out.println("Bravo");
					/*System.out.println("inside2");
					System.out.println(fileWithName.getPreferredSize());
					System.out.println(fileWithName.getSize());*/
					//System.out.println(e);
				}
				
				/*@Override
				public void mouseExited(MouseEvent e) {
					System.out.println(e);
				}*/
			});
			
			//fileWithNamePnl.setLayout(new BoxLayout(fileWithNamePnl, BoxLayout.LINE_AXIS));
			//fileWithNamePnl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			fileWithNamePnl.add(fileWithName, BorderLayout.LINE_START);
			//fileWithNamePnl.add(fileWithName)
			
			text.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					//System.out.println(text.getPreferredSize().width +  ">" +  text.getSize().width);
					System.out.println("Text");
					System.out.println(text.getPreferredSize());
					System.out.println(text.getSize());
					//if(text.getPreferredSize().width > text.getSize().width)
					if(fileWithName.getSize().width > fileWithNamePnl.getVisibleRect().width)
						jp.show(text, 0, 0);
				}
			});
			
			//text.setToolTipText(text.getText());
			
			fileWithNamePnl.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					System.out.println("Border " + fileWithNamePnl.getVisibleRect().getSize());
					System.out.println(fileWithNamePnl.getPreferredSize());
					System.out.println(fileWithNamePnl.getSize());
					System.out.println("inside: "  + fileWithName.getVisibleRect().getSize());
					System.out.println(fileWithName.getPreferredSize());
					System.out.println(fileWithName.getSize());
				}
			});
			
			fileWithNamePnl.setAlignmentX(LEFT_ALIGNMENT);
			fileWithNamePnl.setAlignmentY(TOP_ALIGNMENT);
			fileWithNamePnl.setOpaque(false);
			
			
			/*fileWithNamePnl.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					System.out.println("Border");
					System.out.println(fileWithNamePnl.getPreferredSize());
					System.out.println(fileWithNamePnl.getSize());
				}
			});*/
			
			
			JPanel filesPanel = new JPanel();
			filesPanel.setLayout(new BoxLayout(filesPanel, BoxLayout.PAGE_AXIS));
			filesPanel.add(fileWithNamePnl);
			ExpandPanel expand = new ExpandPanel() {
				@Override
				public Dimension getPreferredSize() {
					Dimension size = FocusPaneView.getRatioDimensionWithoutLayout(this, 0.1, V_RATIO);
					if(size != null) 
						return size;
					return super.getPreferredSize();
				}
				
				@Override
				public Dimension getMinimumSize() {
					return getPreferredSize();
				}
				
				@Override
				public Dimension getMaximumSize() {
					return getPreferredSize();
				}
			};
			expand.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					filesPanel.remove(subFolderPanel);
					subFolderPanel.removeAll();
					if(expand.activated()) {
						subFolderPanel = new SideFilePanel(file);
						subFolderPanel.setAlignmentX(LEFT_ALIGNMENT);
						subFolderPanel.setAlignmentY(TOP_ALIGNMENT);
						if(subFolderPanel.getComponentCount() != 0)
							filesPanel.add(subFolderPanel);
					}
					revalidate();
					repaint();
				}
			});
			
			expand.setBorder(BorderFactory.createLineBorder(Color.GREEN));
			expand.setAlignmentY(TOP_ALIGNMENT);
			filesPanel.setAlignmentY(TOP_ALIGNMENT);
			expand.setOpaque(false);
			add(expand);
			//add(Box.createHorizontalStrut(20));
			/*JPanel pnl = new JPanel(new BorderLayout());
			pnl.add(filesPanel);
			pnl.setAlignmentY(TOP_ALIGNMENT);
			add(pnl);*/
			add(filesPanel);
			filesPanel.setOpaque(false);
			subFolderPanel = new JPanel();
			subFolderPanel.setLayout(new BoxLayout(subFolderPanel, BoxLayout.PAGE_AXIS));
			subFolderPanel.setAlignmentX(LEFT_ALIGNMENT);
		}
		
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
}
