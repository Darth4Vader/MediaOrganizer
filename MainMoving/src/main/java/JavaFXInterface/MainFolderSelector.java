package JavaFXInterface;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.CellRendererPane;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileView;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.poi.ss.formula.functions.T;

import com.healthmarketscience.jackcess.Table;

import DataStructures.NameInfo.NameInfoType;
import SwingUtilities.DocumantFilterList;
import SwingUtilities.SwingUtils;

public class MainFolderSelector extends JFileChooser {
	
	private List<String> filePaths = new ArrayList<>();
	
	public MainFolderSelector() {
		LookAndFeel prevFeel = UIManager.getLookAndFeel();
		Boolean old = UIManager.getBoolean("FileChooser.readOnly");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UIManager.put("FileChooser.readOnly", true);
			updateUI();
			UIManager.setLookAndFeel(prevFeel);
			UIManager.put("FileChooser.readOnly", old);  
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		Action details = getActionMap().get("viewTypeList");
		details.actionPerformed(null);
		viewTypeList();
		details = getActionMap().get("viewTypeDetails");
		details.actionPerformed(null);
		setCurrentDirectory(new File("C:\\Users\\Lenovo\\Desktop\\Main\\W-Output\\TV"));
		setFileView(new IconView());
		System.out.println(Arrays.asList(((Container) this.getComponents()[0]).getComponents()));
		System.out.println(Arrays.asList(((Container) this.getComponents()[2]).getComponents()));
		System.out.println(Arrays.asList(((Container) ((Container) this.getComponents()[2]).getComponents()[1]).getComponents()));
		System.out.println(Arrays.asList(((Container) ((Container) ((Container) this.getComponents()[2]).getComponents()[1]).getComponents()[0]).getComponents()));
		System.out.println(Arrays.asList(((Container) ((Container) ((Container) ((Container) this.getComponents()[2]).getComponents()[1]).getComponents()[0]).getComponents()[0]).getComponents()));
		System.out.println(Arrays.asList(((Container)((Container) ((Container) ((Container) ((Container) this.getComponents()[2]).getComponents()[1]).getComponents()[0]).getComponents()[0]).getComponents()[0]).getComponents()));
		
		//JTable table = SwingUtils.getComponents(JTable.class, this, true);
		
		/*System.out.println(table.getColumnModel().getColumn(0).getIdentifier());
		System.out.println(table.getColumn("Name"));
		System.out.println();*/
		
		
		viewTypeDetails();
		
		
		//viewTypeList();
		
		/*table.addPropertyChangeListener("viewType", new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				System.out.println(evt.getOldValue());
			}
		});
		*/
		
		/*
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				Component focus = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
				
				focus.setSize(focus.getHeight()*2, focus.getWidth());
				
				focus.setPreferredSize(focus.getSize());
			}
		});
		
		*/
	}
	
	public void viewTypeDetails() {
		JTable table = SwingUtils.getComponents(JTable.class, this, true);
		JScrollPane pane = SwingUtils.getAncestor(JScrollPane.class, table);
		table.addContainerListener(JFileChooserFilesListContainerListener());
		TableCellRenderer cell = (TableCellRenderer) table.getDefaultRenderer(Object.class);
		TableCellRenderer nameRenderer = new TableCellRenderer() {
			
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int col) {
				Component component = cell.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
				JPanel pnl = new JPanel(new BorderLayout());
				pnl.add(component, BorderLayout.CENTER);
				JCheckBox check = new JCheckBox();
				String path = value.toString();
				if(filePaths.contains(path))
					check.setSelected(true);
				else
					check.setSelected(false);
				pnl.add(check, BorderLayout.LINE_START);
				pnl.setBorder(BorderFactory.createLineBorder(Color.PINK));
				pnl.addMouseListener(new MouseAdapter() {
					
					@Override
					public void mousePressed(MouseEvent e) {
						System.out.println("sksjsjsjs");
						System.out.println(check.getBounds());
						System.out.println(check.getPreferredSize());
						System.out.println(e.getPoint());
						if(SwingUtilities.isLeftMouseButton(e))
							if(check.contains(e.getPoint())) {
								if(filePaths.contains(path)) {
									filePaths.remove(path);
									//check.setSelected(false);
								}
								else {
									filePaths.add(path);
									//check.setSelected(true);
								}
								table.updateUI();
							}
					}
				});
				pnl.setOpaque(false);
				return pnl;
				
				
			}
		};
		TableColumn column = table.getColumn("Name");
		column.setCellRenderer(nameRenderer);
		TableColumn columnSize = table.getColumn("Size");
		if(columnSize != null) table.removeColumn(columnSize);
		System.out.println("gg  "  + column.getPreferredWidth());
		for(TableColumn colum : IteratorUtils.toList(table.getColumnModel().getColumns().asIterator()))
			System.out.println(colum.getIdentifier() + "  " + column.getPreferredWidth());
		table.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String eventName = evt.getPropertyName();
				if(eventName.equals("columnModel")) {
					TableColumn nameCol = table.getColumn("Name");
			        int tableWidth = table.getPreferredSize().width;
			        int width = pane.getViewport().getWidth();
			        if (tableWidth < width) {
			            nameCol.setPreferredWidth(nameCol.getPreferredWidth() + width - tableWidth);
			        }
			        nameCol.setCellRenderer(nameRenderer);	
					System.out.println("gg " + column.getPreferredWidth());
					for(TableColumn colum : IteratorUtils.toList(table.getColumnModel().getColumns().asIterator()))
						System.out.println(colum.getIdentifier() + "  " + column.getPreferredWidth());
					TableColumn columnSize = table.getColumn("Size");
					if(columnSize != null) table.removeColumn(columnSize);
				}
			}
		});
		table.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				activateMousePressed(getJTableValueAtPoint(table, e));
			}
		});
		/*table.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				
				if(evt.getPropertyName().equals("visibleRowCount")) {
					
					System.out.println(table.getComponents().length);
					
					for(Component comp : table.getComponents()) {
						System.out.println(comp);
						if(comp instanceof JTextField) {
							comp.setSize(comp.getWidth(), comp.getHeight()*2);
							comp.setPreferredSize(comp.getSize());
						}
					}
				}
			}
		});*/
		/*GridLayout layout = new GridLayout();
		table.setLayout(layout);*/		
	}
	
	
	public ContainerListener JFileChooserFilesListContainerListener() {
		ContainerListener containerListener = new ContainerListener() {
			
			@Override
			public void componentRemoved(ContainerEvent e) {
				MainFolderSelector.this.refreshFrame();
			}
			
			@Override
			public void componentAdded(ContainerEvent e) {
				Component component = e.getComponent();
				for(Component comp : ((Container) component).getComponents()) {	
						if(comp instanceof JTextField) {
							e.getContainer().remove(comp);
						}
						else if(comp instanceof JComponent) {
							((JComponent) comp).setBorder(BorderFactory.createLineBorder(Color.BLUE, 4));
						}
				}
			}
		};
		return new ContainerAdapter() {
			
			@Override
			public void componentRemoved(ContainerEvent e) {
				Component comp = e.getComponent();
				if(comp.getClass().getName().contains("FilePane")) {
					Container container = ((Container) comp);
					container.removeContainerListener(containerListener);
				}
			}
			
			@Override
			public void componentAdded(ContainerEvent e) {
				Component comp = e.getComponent();
				if(comp.getClass().getName().contains("FilePane")) {
					Container container = ((Container) comp);
					container.addContainerListener(containerListener);
				}
			}
		};
	}
	
	public void activateMousePressed(MouseEvent e) {
		if(e != null) {
			Component component = (Component) e.getSource();
			if(component != null) {
				MouseListener[] listeners = component.getMouseListeners();
				if(listeners != null) for(MouseListener listener : listeners) {
					listener.mousePressed(e);
				}
			}
		}
	}
	
	public <E extends Object> MouseEvent getJListValueAtPoint(JList<E> list, MouseEvent e) {
		Point point = e.getPoint();
		int index = list.locationToIndex(point);
		Rectangle rect = list.getCellBounds(index, index);
		if (index >= 0 && rect.contains(point)){
			E objectIndex = list.getModel().getElementAt(index);
			ListCellRenderer<? super E> listCell = list.getCellRenderer();
			Component component = listCell.getListCellRendererComponent(list, objectIndex, index, true, true);
			component.setBounds(rect);
			refreshCell(component);
			return getMouseEventOfChild(e, component);
		}
		return null;
	}
	
	public MouseEvent getJTableValueAtPoint(JTable table, MouseEvent e) {
		Point point = e.getPoint();
		int col = table.columnAtPoint(point), row = table.rowAtPoint(point);
		Rectangle rect = table.getCellRect(row, col, false);
		System.out.println(rect + "  " + point);
		System.out.println(col + "  " + row);
		System.out.println(rect.contains(point));
		System.out.println(e.getComponent());
		if (col >= 0 && row >= 0 && rect.contains(point)) {
			Object objectIndex = table.getValueAt(row, col);
			TableCellRenderer tableCell = table.getCellRenderer(row, col);
			Component component = tableCell.getTableCellRendererComponent(table, objectIndex, true, true, row, col);
			component.setBounds(rect);
			refreshCell(component);
			return getMouseEventOfChild(e, component);
		}
		return null;
	}
	
	private void refreshCell(Component component) {
		component.revalidate();
		component.repaint();
		if(component instanceof Container) {
			Container container = (Container) component;
			LayoutManager layout = container.getLayout();
			if(layout != null) layout.layoutContainer(container);
		}
	}
	
	public MouseEvent getMouseEventOfChild(MouseEvent containerMouseEvent, Component component) {
		MouseEvent componentMouseEvent = null;
		Point point = containerMouseEvent.getPoint();
		Rectangle rect = component.getBounds();
		if(rect.contains(point)) {
			Point newPoint = new Point(point.x - rect.x, point.y - rect.y);
			componentMouseEvent = new MouseEvent(component, containerMouseEvent.getID(), containerMouseEvent.getWhen(),
					containerMouseEvent.getModifiersEx(), newPoint.x, newPoint.y, containerMouseEvent.getXOnScreen(), containerMouseEvent.getYOnScreen(), 
					containerMouseEvent.getClickCount(), containerMouseEvent.isPopupTrigger(), containerMouseEvent.getButton());
		}
		return componentMouseEvent;
	}
	
    private static void fixRowCountForVisibleColumns(JList list) {
        int nCols = computeVisibleColumnCount(list);
        int nItems = list.getModel().getSize();

        // Compute the number of rows that will result in the desired number of
        // columns
        int nRows = nItems / nCols;
        if (nItems % nCols > 0) nRows++;
        list.setVisibleRowCount(nRows);
    }

    private static int computeVisibleColumnCount(JList list) {
        // It's assumed here that all cells have the same width. This method
        // could be modified if this assumption is false. If there was cell
        // padding, it would have to be accounted for here as well.
        int cellWidth = list.getCellBounds(0, 0).width;
        int width = list.getVisibleRect().width;
        return width == 0 ? 1 : width / cellWidth;
    }
	
	
	public void viewTypeList() {
		JList<?> list = SwingUtils.getComponents(JList.class, this, true);
		System.out.println(list.getParent().getParent().getParent().getParent().getParent());
		System.out.println(list);
		list.removeListSelectionListener(list.getListSelectionListeners()[0]);
		list.addContainerListener(JFileChooserFilesListContainerListener());
		ListCellRenderer<Object> cell = (ListCellRenderer<Object>) list.getCellRenderer();
		list.setLayoutOrientation(JList.VERTICAL_WRAP);
		JScrollPane pane = SwingUtils.getAncestor(JScrollPane.class, list);
		
		
		//pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		fixRowCountForVisibleColumns(list);
		
		//System.out.println(list.getParent());
		//System.out.println(pane.getVerticalScrollBarPolicy());
		//System.out.println(pane.getHorizontalScrollBarPolicy());
		list.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				activateMousePressed(getJListValueAtPoint(list, e));
			}
		});
		list.setCellRenderer(new DefaultListCellRenderer() {

			@Override
			public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				//System.out.println("Sup");
				Component component = cell.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				JLabel lbl = (JLabel) component;
				Image image;
				Icon icon = lbl.getIcon();
				if(icon instanceof ImageIcon) {
					image = ((ImageIcon) icon).getImage();
				}
				else {
					image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
					Graphics2D g = ((BufferedImage) image).createGraphics();
					icon.paintIcon(null, g, 0, 0);					
					g.dispose();
				}
				lbl.setIcon(null);
				JPanel pnl = new JPanel(new BorderLayout());
				pnl.add(lbl, BorderLayout.PAGE_END);
				JPanel img = new JPanel() {
					
					@Override
					public Dimension getPreferredSize() {
						return new Dimension(100, 50);
					}
					
					@Override
					public void paintComponent(Graphics g) {
						super.paintComponent(g);
						g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
					}
				};
				pnl.add(img, BorderLayout.CENTER); 
				JCheckBox check = new JCheckBox();
				String path = value.toString();
				if(filePaths.contains(path))
					check.setSelected(true);
				else
					check.setSelected(false);
				pnl.add(check, BorderLayout.LINE_START);
				pnl.addMouseListener(new MouseAdapter() {
					
					@Override
					public void mousePressed(MouseEvent e) {
						if(SwingUtilities.isLeftMouseButton(e))
							if(check.contains(e.getPoint())) {
								if(filePaths.contains(path))
									filePaths.remove(path);
								else
									filePaths.add(path);
								list.updateUI();
							}
					}
				});
				//System.out.println(component);
				return pnl;
			}
			
		});
		list.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if(evt.getPropertyName().equals("visibleRowCount")) {
					fixRowCountForVisibleColumns(list);
					System.out.println(list.getComponents().length);
					
					for(Component comp : list.getComponents()) {
						System.out.println(comp);
						if(comp instanceof JTextField) {
							comp.setSize(comp.getWidth(), comp.getHeight()*2);
							comp.setPreferredSize(comp.getSize());
						}
					}
				}
			}
		});
		GridLayout layout = new GridLayout();
		list.setLayout(layout);		
	}
	
	public List<String> getFilesList() {
		return this.filePaths;
	}
	
	public void removeAllMouseListeners(Component component) {
		if(component instanceof Container) {
			Container container = (Container) component;
			MouseListener[] arr = container.getMouseListeners();
			if(arr != null) 
				for(MouseListener listener : arr)
					container.removeMouseListener(listener);
		}
	}
	
	
	public void removeAllFocusListeners(Component component) {
		if(component instanceof Container) {
			Container container = (Container) component;
			FocusListener[] arr = container.getFocusListeners();
			if(arr != null) 
				for(FocusListener listener : arr)
					container.removeFocusListener(listener);
		}
	}
	
	public void refreshFrame() {
		revalidate();
		repaint();
	}
	
	private class IconView extends FileView {
		
		@Override
		public Icon getIcon(File f) {
			Icon ico = FileSystemView.getFileSystemView().getSystemIcon(f);
			return ico;
		}	
	}
	
}
