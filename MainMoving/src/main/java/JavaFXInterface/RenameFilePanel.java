package JavaFXInterface;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;

import DataStructures.FileInfo;
import DataStructures.NameInfo.NameInfoType;
import Interface.FileExplorer.FilePanel;
import Interface.FileInfoPanel.InfoField;
import SwingUtilities.DocumantFilterList;
import SwingUtilities.SwingUtils;

public class RenameFilePanel extends FileInfoPanel {
	
	private FileExplorer explorer;
	
	public RenameFilePanel(FileExplorer explorer) {
		super();
		this.explorer = explorer;
		JButton btn = new JButton("Update");
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					updateChanges();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		add(btn, BorderLayout.PAGE_END);
	}

	@Override
	public Dimension getPreferredSize() {
		return SwingUtils.getRatioSize(this, 0.3, SwingUtils.V_RATIO);
	}
	
	public void updateChanges() throws IOException {
		for (NameInfoType type : this.map.keySet()) {
			InfoField field = this.map.get(type);
			field.updateChanges();
		}
		File file = explorer.getFolderManager().renameFiles(info);
		if(this.pnl != null)
			this.pnl.updateFile(file);
	}
}