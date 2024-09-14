package JavaFXInterfacePrev;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import DataStructures.ManageFolder;

public class FIleExplorerSetup extends JFrame implements WindowFocusListener {
	
	private FileExplorer mainPanel;
	
	public FIleExplorerSetup() {
		setLayout(new BorderLayout());
		this.setBackground(Color.WHITE);
		this.getContentPane().setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 400);
		setVisible(true);
		this.addWindowFocusListener(this);
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	windowGainedFocus(null);
		    }
		});
		MainFolderSelector chooser = new MainFolderSelector();
        chooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JInternalFrame parent = (JInternalFrame) SwingUtilities.getAncestorOfClass(JInternalFrame.class, chooser);
            	//System.out.println(e.getActionCommand());
                if (JFileChooser.CANCEL_SELECTION.equals(e.getActionCommand())) {
                	//dispatchEvent(new WindowEvent(moveFileFrameUI.this, WindowEvent.WINDOW_CLOSING));
                	//System.out.println("Error: Not Selected");
                } else if (JFileChooser.APPROVE_SELECTION.equals(e.getActionCommand())) {
                	File selected = chooser.getSelectedFile();
                	List<String> list = chooser.getFilesList();
                	List<File> files = new ArrayList<>();
                	for(String path : list)
                		files.add(new File(path));
                	ManageFolder move = new ManageFolder(selected.getAbsolutePath(), files);
                	mainPanel = new FileExplorer(move);
                	add(mainPanel, BorderLayout.CENTER);
                	refreshFrame();
                }
                //parent.dispose();
            }
        });
        chooser.showOpenDialog(this);
        //JOptionPane.showInternalOptionDialog(dp, chooser, "Choose", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[0], null);
	}

	@Override
	public void windowGainedFocus(WindowEvent e) {
		setBackground(new Color(249,239,248));
		if(mainPanel != null)
			mainPanel.requestFocusInWindow();
		refreshFrame();
	}

	@Override
	public void windowLostFocus(WindowEvent e) {
		setBackground(new Color(237, 237, 237));
		refreshFrame();
	}
	
	public void refreshFrame() {
		revalidate();
		repaint();
	}
}
