package JavaFXInterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import SwingUtilities.SwingUtils;

public class SearchPanel extends JPanel {

	private FileExplorer explorer;
	private boolean advencedSearchActive;
	
	public SearchPanel(FileExplorer explorer) {
		setLayout(new BorderLayout());
		this.explorer = explorer;
		JTextField searchText = new JTextField();
		//JPanel advencedSearch = new JPanel();
		//FileInfoPanel fileInfoPnl = new FileInfoPanel();
		JButton advencedSearch = new JButton("AdvencedSearch") {
			@Override
			public Dimension getPreferredSize() {
				return SwingUtils.getHorizontalRatioSize(this, 0.2);
			}
		};
		advencedSearch.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				activateAdvancedSearch();
			}
		});
		add(searchText, BorderLayout.CENTER);
		add(advencedSearch, BorderLayout.LINE_END);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return SwingUtils.getHorizontalRatioSize(this, 0.2);
	}
	
	private synchronized void activateAdvancedSearch() {
		if(advencedSearchActive) return;
		this.advencedSearchActive = true;
		FileInfoPanel filePnl = new FileInfoPanel();
		explorer.add(filePnl, BorderLayout.LINE_END);
		explorer.refreshFrame();
	}
	
}