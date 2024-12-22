package JavaFXInterface;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import JavaFXInterface.Logger.CreateMovieLoggerControl;
import OtherUtilities.ImageUtils;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class FileInfoToolsPanel extends HBox {
	
	private final Map<ToolName, ToolPanel> toolMap;
	private FileInfoExplorer explorer;
	private File currentFileToInfo;
	private CreateMovieLoggerControl createMovieLoggerControl;

	public FileInfoToolsPanel(FileInfoExplorer explorer) {
		this.explorer = explorer;
		this.toolMap = new HashMap<>();
		for(ToolName tool : ToolName.values())
			this.toolMap.put(tool, new ToolPanel(tool));
		getChildren().add(toolMap.get(ToolName.ORGANIZE_FOLDER));
		getChildren().add(toolMap.get(ToolName.RENAME_FILE));
		getChildren().add(toolMap.get(ToolName.REFRESH_LOGO));
		getChildren().add(toolMap.get(ToolName.SET_LOGO));
		getChildren().add(toolMap.get(ToolName.SET_SUBTITLES));
		this.createMovieLoggerControl = new CreateMovieLoggerControl(explorer.getFolderManager());
	}
	
	
	private void addInfoPanel(ToolName tool) {
		FileInfoPanel infoPanel = explorer.getFileInfoPanel();
		explorer.getChildren().remove(infoPanel);
		switch (tool) {
		case ORGANIZE_FOLDER:
			startMoving();
			break;
		case REFRESH_LOGO:
			break;
		case RENAME_FILE:
			infoPanel.setPanel(currentFileToInfo);
			if(infoPanel != null && explorer.getRight() == null) {
				explorer.setRight(infoPanel);
			}
			break;
		case SET_LOGO:
			explorer.getFolderManager().createIconToFolder(currentFileToInfo);
			break;
		case SET_SUBTITLES:
			break;
		default:
			break;
		}
	}
	
	private void startMoving() {
		// first we will create a task in order to push the custom logger message to ListView by updating JavaFX
		Task<Void> task = new Task<Void>() {
			
			@Override
			protected Void call() throws Exception {
				explorer.getFolderManager().moveFilesFromInput();
				return null;
			}
		};
		task.setOnFailed(v -> {
			// we will handle every possible exception of the method that we call
			createMovieLoggerControl.close();
			Throwable exception = task.getException();
			Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), exception);
			/*if (exception instanceof NumberFormatException) {
				AdminPagesUtils.parseNumberException(movie.getMediaID());
			} else if (exception instanceof CreateMovieException) {
				// when the movie creation fails, we will alert the user of the reasons
				AdminPagesUtils.createMovieExceptionAlert((CreateMovieException) exception);
			} else if (exception instanceof CanUpdateException) {
				// if the movie already exists, then we will alert the user that he can update the movie if he wants
				showAlertThatMovieCanUpdate((CanUpdateException) exception);
			} else { // and if the exception is not specified in the method, like RuntimeException, then we will let the default handler to handle it
				Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), exception);
			}*/
		});
		// we will add a successful notify id the method finish
		task.setOnSucceeded(v -> {
			createMovieLoggerControl.finishedTask();
		});
		// if the task is cancelled, before the method finish, then we notify the user of the consequences
		task.setOnCancelled(v -> {
			//createMovieLoggerControl.close();
		});
		// we will open the custom view with the task
		createMovieLoggerControl.start(task);
		// and then start the task as a new thread, in order to synchronize with JavaFX
		new Thread(task).start();
	}
	
	public void updateToolPanels(File currentFileToInfo) {
		this.currentFileToInfo = currentFileToInfo;
		for(ToolPanel tool : this.toolMap.values() ) {
			tool.setUsage(currentFileToInfo);
		}
	}
	
	public void restartToolPanels() {
		this.currentFileToInfo = null;
		for(ToolPanel tool : this.toolMap.values() ) {
			tool.setUsage(null);
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
			this.setOpacity(0.1);
			
			
			
			
			this.tool = tool;
			this.canUse = false;
			final java.awt.Image img = ImageUtils.loadImage("Data\\Images\\" + tool.getID());
			final BufferedImage image = img != null ? ImageUtils.paintImageInColor(img, java.awt.Color.BLACK) : null;
			//Canvas lblIcon = new C
			BorderPane lblIcon = new BorderPane();
			ImageView iconImg = new ImageView();
			iconImg.fitWidthProperty().bind(lblIcon.widthProperty().multiply(0.6));
			iconImg.fitHeightProperty().bind(lblIcon.heightProperty().multiply(0.6));
			if(image != null) {
				iconImg.setImage(SwingFXUtils.toFXImage(image, null));
			}
			lblIcon.setCenter(iconImg);
			
			this.setCenter(lblIcon);
			Label lblName = new Label(tool.getName());
			this.setTop(lblName);
			setOnMouseClicked(e -> {
				activate();
				if(canUse)
					this.requestFocus();
			});
			setOnMouseEntered(e -> {
				this.setBackground(new Background(new BackgroundFill(Color.rgb(211, 211, 211, 0.6), CornerRadii.EMPTY, Insets.EMPTY)));
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
		
		/*@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if(!canUse) {
				Graphics2D g2d = (Graphics2D) g;
				float opacity = 0.1f;
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
			}
		}*/
		
		public void setUsage(File file) {
			if(file != null && file.isDirectory())
				this.canUse = true;
			else
				this.canUse = false;
			if(!canUse) {
				this.setOpacity(0.1);
			}
			else
				this.setOpacity(1);
		}
		
		public void activate() {
			if(canUse)
				addInfoPanel(tool);
		}
	}
	
	/*
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
			java.awt.Image image = null;
			if(imageFile != null)
				image = ImageUtils.loadImage(imageFile.getPath());
			else {
				if(icon instanceof ImageIcon)
					image = ((ImageIcon) icon).getImage();
				else if(icon != null) { 
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
	
	*/

}
