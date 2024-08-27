package Interface;

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
import SwingUtilities.DocumantFilterList;
import SwingUtilities.SwingUtils;

public class FileInfoPanel extends JPanel {
	
	protected FileInfo info;
	protected FilePanel pnl;
	private final JPanel infoFields;
	protected final Map<NameInfoType, InfoField> map;
	
	public FileInfoPanel() {
		setLayout(new BorderLayout());
		this.pnl = null;
		this.map = new HashMap<>();
		createMap();
		this.infoFields = createInfoFields();
		this.infoFields.setLayout(new BoxLayout(this.infoFields, BoxLayout.PAGE_AXIS));
		add(infoFields, BorderLayout.CENTER);
	}

	@Override
	public Dimension getPreferredSize() {
		return SwingUtils.getRatioSize(this, 0.3, SwingUtils.V_RATIO);
	}

	public void createMap() {
		for (NameInfoType type : NameInfoType.values()) {
			final InfoField field = new InfoField(type);
			map.put(type, field);
			Document document = field.field.getDocument();
			if (document instanceof AbstractDocument) {
				AbstractDocument doc = (AbstractDocument) document;
				DocumantFilterList filter = new DocumantFilterList();
				if (type == NameInfoType.YEAR || type == NameInfoType.SEASON || type == NameInfoType.EPISODE)
					filter.addFilter(DocumantFilterList.documentDigitsFilter());
				filter.addFilter(DocumantFilterList.documentSizeFilter(field.getNameInfoType().getInfoLength()));
				doc.setDocumentFilter(filter);
			}
		}
	}
	
	public void setPanel(FilePanel pnl) {
		this.pnl = pnl;
		File file = pnl.getFile();
		this.info = new FileInfo(file);
		setPanel();
	}

	private void setPanel() {
		setPanel(info.createInfoMap());
	}

	private void setPanel(Map<NameInfoType, String> map, Component container) {
		if (container instanceof Container) {
			Component[] arr = ((Container) container).getComponents();
			if (arr != null)
				for (Component component : arr) {
					if (component instanceof InfoField) {
						InfoField field = (InfoField) component;
						String text = map.get(field.getNameInfoType());
						if (text != null) {
							field.setInfoText(text);
						}
					} else
						setPanel(map, component);
				}
		}
		revalidate();
		repaint();
	}

	private void setPanel(Map<NameInfoType, String> map) {
		for (NameInfoType type : this.map.keySet()) {
			InfoField field = this.map.get(type);
			if (map.containsKey(type)) {
				String text = map.get(type);
				field.setInfoText(text);

			} else
				field.setInfoText("");
		}
		updateInfoFields();
	}

	private JPanel createInfoFields() {
		JPanel pnl = new JPanel();
		pnl.setLayout(new BoxLayout(pnl, BoxLayout.PAGE_AXIS));
		pnl.add(map.get(NameInfoType.NAME));
		pnl.add(map.get(NameInfoType.YEAR));
		
		JPanel series = new JPanel();
		series.setLayout(new BoxLayout(series, BoxLayout.LINE_AXIS));
		series.add(map.get(NameInfoType.SEASON));
		InfoField episode = map.get(NameInfoType.EPISODE);

		episode.field.getDocument().addDocumentListener(new DocumentListener() {

			private boolean show = false;

			@Override
			public void removeUpdate(DocumentEvent e) {
				addEpisodeName(e);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				addEpisodeName(e);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				addEpisodeName(e);
			}

			public void addEpisodeName(DocumentEvent e) {
				String text = episode.field.getText();
				if (text.isEmpty()) {
					show = false;
				} else if (show != true) {
					show = true;
				}
			}
		});

		series.add(map.get(NameInfoType.EPISODE));
		pnl.add(series);
		pnl.add(map.get(NameInfoType.DESCRIPTION));
		return pnl;

		/*
		 * JLabel lblName = new JLabel(name); pnl.add(lblName, BorderLayout.LINE_START);
		 * JTextField text = new JTextField(); pnl.add(text, BorderLayout.CENTER);
		 * return pnl;
		 */
	}
	
	private void updateInfoFields() {
		this.infoFields.removeAll();
		this.infoFields.add(map.get(NameInfoType.NAME));
		this.infoFields.add(map.get(NameInfoType.YEAR));
		if(info.hasEpisode()) {
			JPanel series = new JPanel();
			series.setLayout(new BoxLayout(series, BoxLayout.LINE_AXIS));
			series.add(map.get(NameInfoType.SEASON));
			series.add(map.get(NameInfoType.EPISODE));
			this.infoFields.add(series);
			this.infoFields.add(map.get(NameInfoType.DESCRIPTION));
		}
		else if(info.hasSeason()) {
			this.infoFields.add(map.get(NameInfoType.SEASON));
		}
	}

	class InfoField extends JPanel {

		private final NameInfoType type;
		private final JLabel lbl;
		public final JTextField field;

		public InfoField(NameInfoType type) {
			setLayout(new BorderLayout());
			this.type = type;
			this.lbl = new JLabel(type.getInfoName());
			this.field = new JTextField();
			if(type == NameInfoType.EPISODE || type == NameInfoType.SEASON) {
				this.field.setEditable(false);
			}
			add(lbl, BorderLayout.LINE_START);
			add(field, BorderLayout.CENTER);
		}
		
		public void setEditable(boolean edit) {
			this.field.setEditable(edit);
		}

		public void setInfoText(String text) {
			field.setText(text);
		}

		public String getInfoText() {
			return field.getText();
		}

		public void updateChanges() {
			info.setContent(type, getInfoText());
		}

		public NameInfoType getNameInfoType() {
			return type;
		}
	}
}