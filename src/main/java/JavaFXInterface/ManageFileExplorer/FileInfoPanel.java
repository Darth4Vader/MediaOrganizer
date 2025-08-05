package JavaFXInterface.ManageFileExplorer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import DataStructures.FileInfo;
import DataStructures.NameInfo.NameInfoType;
import JavaFXInterface.utils.AlphaNumericTextFormatter;
import JavaFXInterface.utils.MaxLengthTextFormatter;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FileInfoPanel extends BorderPane {
	
	protected FileInfo info;
	protected File pnl;
	private final VBox infoFields;
	protected final Map<NameInfoType, InfoField> map;
	
	public FileInfoPanel() {
		this.pnl = null;
		this.map = new HashMap<>();
		createMap();
		this.infoFields = createInfoFields();
		setCenter(infoFields);
	}

	public void createMap() {
		for (NameInfoType type : NameInfoType.values()) {
			final InfoField field = new InfoField(type);
			map.put(type, field);
			TextFormatter<String> formatter;
			int size = field.getNameInfoType().getInfoLength();
			if (type == NameInfoType.YEAR || type == NameInfoType.SEASON || type == NameInfoType.EPISODE)
				formatter = new AlphaNumericTextFormatter(size);
			else
				formatter = new MaxLengthTextFormatter(size);
			field.field.setTextFormatter(formatter);
		}
	}
	
	public void setPanel(File pnl) {
		this.pnl = pnl;
		File file = pnl;
		this.info = new FileInfo(file);
		setPanel();
	}

	private void setPanel() {
		setPanel(info.createInfoMap());
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

	private VBox createInfoFields() {
		VBox pnl = new VBox();
		pnl.getChildren().add(map.get(NameInfoType.NAME));
		pnl.getChildren().add(map.get(NameInfoType.YEAR));
		
		HBox series = new HBox();
		series.getChildren().add(map.get(NameInfoType.SEASON));
		InfoField episode = map.get(NameInfoType.EPISODE);
		
		series.getChildren().add(map.get(NameInfoType.EPISODE));
		pnl.getChildren().add(series);
		pnl.getChildren().add(map.get(NameInfoType.DESCRIPTION));
		return pnl;

		/*
		 * JLabel lblName = new JLabel(name); pnl.add(lblName, BorderLayout.LINE_START);
		 * JTextField text = new JTextField(); pnl.add(text, BorderLayout.CENTER);
		 * return pnl;
		 */
	}
	
	private void updateInfoFields() {
		this.infoFields.getChildren().clear();
		System.out.println("File " + pnl);
		this.infoFields.getChildren().add(map.get(NameInfoType.NAME));
		this.infoFields.getChildren().add(map.get(NameInfoType.YEAR));
		if(info.hasEpisode()) {
			HBox series = new HBox();
			series.getChildren().add(map.get(NameInfoType.SEASON));
			series.getChildren().add(map.get(NameInfoType.EPISODE));
			this.infoFields.getChildren().add(series);
			this.infoFields.getChildren().add(map.get(NameInfoType.DESCRIPTION));
		}
		else if(info.hasSeason()) {
			this.infoFields.getChildren().add(map.get(NameInfoType.SEASON));
		}
	}

	class InfoField extends BorderPane {

		private final NameInfoType type;
		private final Label lbl;
		public final TextField field;

		public InfoField(NameInfoType type) {
			this.type = type;
			this.lbl = new Label(type.getInfoName());
			this.field = new TextField();
			if(type == NameInfoType.EPISODE || type == NameInfoType.SEASON) {
				this.field.setEditable(false);
			}
			setLeft(lbl);
			setCenter(field);
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