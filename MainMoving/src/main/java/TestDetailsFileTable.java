import java.awt.FileDialog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileStoreAttributeView;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JFileChooser;

import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.parser.mp4.MP4Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ExpandedTitleContentHandler;
import org.controlsfx.control.CheckListView;
import org.controlsfx.control.cell.ColorGridCell;
import org.gagravarr.flac.FlacFile;
import org.gagravarr.tika.FlacParser;
import org.xml.sax.SAXException;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.file.FileSystemMetadataReader;
import com.drew.metadata.mp3.Mp3Descriptor;
import com.drew.metadata.mp4.Mp4Context;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

import DataStructures.ManageFolder;
import FileUtilities.MimeUtils;
import JavaFXInterface.AppUtils;
import JavaFXInterface.FileExplorer;
import JavaFXInterface.FilePanel;
import JavaFXInterface.SideFilesList;
import JavaFXInterface.controlsfx.GridCellSelected;
import impl.org.controlsfx.spreadsheet.TableViewSpanSelectionModel;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Callback;

public class TestDetailsFileTable extends Application {
	//--module-path "C:\JavaFX_22.02\lib" --add-modules javafx.controls,javafx.fxml

	public static void main(String[] args) {
		//String[] args2 = Arrays.asList(args, "--module-path \"C:\\JavaFX_22.02\\lib\" --add-modules javafx.controls,javafx.fxml").toArray(new String[0]);
		Application.launch(args);
	}

    @Override
    public void start(Stage stage) throws Exception {
		/*FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.showOpenDialog(stage);*/
    	
    	File file = new File("C:\\Users\\itay5\\OneDrive\\Pictures\\Main2024");
    	
    	file = new File("C:\\Users\\itay5\\OneDrive\\מסמכים\\New folder (2)");
    	
    	//SideFilesList list = new SideFilesList(file);
    	
    	//FileExplorer list = new FileExplorer(new ManageFolder(file.getAbsolutePath()));
    	
    	TableView<FileDetails> list = new FileTableView(file);
    	
    	//FileDialog
    	//list.setPrefWidth(400);
    	//list.setPrefHeight(500);
    	Scene scene = new Scene(list);
    	scene.focusOwnerProperty().addListener((obs) -> {
    		System.out.println(obs);
    	});
    	stage.setScene(scene);
    	
    	stage.setWidth(list.getWidth());
    	stage.setHeight(list.getHeight());
    	
    	//stage.setWidth(400);
    	//stage.setHeight(500);
    	System.out.println("hello");
    	stage.show();
    	
    	stage.setWidth(1200);
    	stage.setHeight(1000);
    	
    	list.setPrefWidth(stage.getWidth());
    	list.setPrefHeight(stage.getHeight());
    	
    	stage.setMinWidth(1200);
    	stage.setMinHeight(1000);
    	
        //stage.minWidthProperty().bind(list.widthProperty());
        //stage.minHeightProperty().bind(list.heightProperty());
    }
    
    private class FileTableView extends TableView<FileDetails> {
    	
    	private final ObservableList<String> fixedColumns;
    	
        public FileTableView() throws IOException {
        	this.fixedColumns = FXCollections.observableArrayList(
        			Arrays.asList(FileAttributesType.NAME, FileAttributesType.TYPE).stream().map((s) -> s.getName()).collect(Collectors.toList()));
        	this.fixedColumns.stream().forEach((s) -> addColumn(s));
        	//Arrays.asList(FileAttributesType.NAME, FileAttributesType.TYPE).stream().forEach((s) -> addColumn(s.getName()));
        	this.setEditable(false);
        }
        
        public FileTableView(File file) throws IOException {
        	this();
        	setFile(file);
        }
        
        public void setFile(File file) {
        	ObservableList<FileDetails> list = FXCollections.observableList(
        			Arrays.asList(file.listFiles()).stream()
        			.filter((f) -> !f.isHidden())
        			.map(f -> {
    					try {
    						return new FileDetails(f);
    					} catch (IOException | SAXException | TikaException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    						return null;
    					}
    				}).filter(p -> p != null).collect(Collectors.toList()));
        	this.setItems(list);
        }
        
        private boolean doesColumnExists(String name) {
        	return this.getColumns().stream().anyMatch((c) -> c.getUserData().equals(name));
        }
        
        private boolean canRemoveColumn(TableColumn<FileDetails, ?> column, Collection<String> nameList) {
        	Object name = column.getUserData();
        	return !fixedColumns.contains(name) && !nameList.contains(name);
        }
        
        public void addColumn(String name) {
        	if(doesColumnExists(name) || name == null)
        		return;
        	TableColumn<FileDetails, ?> column;
        	if(name.equals(FileAttributesType.NAME.getName())) {
        		TableColumn<FileDetails, FileDetails> nameCol = new TableColumn<>();
        		nameCol.setCellValueFactory((file) ->  new SimpleObjectProperty<FileDetails>(file.getValue()));
        		nameCol.setCellFactory((f) -> new TableCell<>() {
        			
        	    	private final ImageView imageView = new ImageView();
        	    	
        		    @Override
        		    public void updateItem(FileDetails item, boolean empty) {
        		        super.updateItem(item, empty);
        	            if (item == null || empty) {
        		            setText(null);
        		            setGraphic(null);
        		            imageView.setImage(null);
        		        } else {
        		        	setText(item.getValue(name));
        		            imageView.setImage(AppUtils.getImageOfFile(item.getFile()));
        		            setGraphic(imageView);
        		        }
        		    }
        		});
        		column = nameCol;
        	}
        	else {
        		TableColumn<FileDetails, String> otherCols = new TableColumn<>();
        		otherCols = new TableColumn<FileDetails, String>();
        		otherCols.setCellValueFactory((file) -> new SimpleStringProperty(file.getValue().getValue(name)));
        		column = otherCols;
        	}
            VBox colName = new VBox(new Label(name));
            colName.setAlignment(Pos.CENTER);
        	column.setGraphic(colName);
        	column.setUserData(name);
        	
        	colName.setOnMouseClicked((e) -> {
        	//nameCol.getGraphic().addEventFilter(MouseEvent.MOUSE_CLICKED, (e) -> {
        		if(e.getButton() == MouseButton.SECONDARY) {
        			Set<String> keys = column.getTableView().getItems().stream().map(f -> f.getAllKeys()).flatMap(Set::stream).collect(Collectors.toSet());
        			CheckListView<String> keysView = new CheckListView<>();
        			keysView.setItems(FXCollections.observableArrayList(keys));
        			
        			//There is a bug when setting check to items that their indices are not sorted upward.
        			keysView.getCheckModel().checkIndices(this.getColumns().stream().map((col) -> {
        				String nam = col.getUserData().toString();
        				return keysView.getCheckModel().getItemIndex(nam);
        			})
        			.mapToInt(i -> i)
        			.sorted()
        			.toArray());
        			
        			Popup pop = new Popup();
        			Button btn = new Button();
        			btn.setOnMouseClicked((evt) -> {
        				ObservableList<String> checkedList = keysView.getCheckModel().getCheckedItems();
        				System.out.println(checkedList);
        				this.getColumns().removeIf((c) -> canRemoveColumn(c, checkedList));
        				for(String checked : checkedList) {
        					System.out.println("Checked: " + checked);
        					addColumn(checked);
        				}
        				pop.hide();
        			});
        			btn.setMaxWidth(Double.MAX_VALUE);
        			VBox view = new VBox();
        			view.getChildren().add(keysView);
        			view.getChildren().add(btn);
        			pop.getContent().add(view);
        			pop.show(this.getScene().getWindow());
        			pop.setAutoHide(true);
        			
        			//PopupBuilder.create().content(keysView).width(50).height(100).autoFix(true).build();
        			//pop.show(stage);
        			
        			//Alert a;
        			//dialogPane.
        		}
        	});
        	this.getColumns().add(column);
        }
    }
    
    class FileDetails {
    	private File file;
    	private BasicFileAttributes attributes;
    	private Metadata metadata;
    	private String typeName;
    	
		public FileDetails(File file) throws IOException, SAXException, TikaException {
			super();
			setFile(file);
			//this.file = file;
			//this.attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
			
			
	        /*Tika tika = new Tika();
	        Metadata metadata = new Metadata();
	        tika.parse(file, metadata);
	        System.out.println("creation date from metadata "+metadata.get("dcterms:created"));  //created date time
	        System.out.println("modified date from metadata "+metadata.get("dcterms:modified")); //last modified date time
	        
	        for(String key : metadata.names())
	            System.out.println(key+" = "+metadata.get(key));
			
	        System.out.println();
	        
	        */
			
			
			//this.metadata = loadMetadata(file);
			
	        //UserDefinedFileAttributeView fileAttributeView = Files.getFileAttributeView(file.toPath(), UserDefinedFileAttributeView.class);
	        /*List<String> allAttrs = attributes.
	        System.out.println(allAttrs);
	        for (String att : allAttrs) {
	            System.out.println("att = " + att);
	        }*/
		}
		
		private Metadata loadMetadata(File file) throws FileNotFoundException {
			if(file == null || file.isDirectory() || !file.canRead())
				return null;
			BodyContentHandler handler = new BodyContentHandler();
			Metadata metadata = new Metadata();
			FileInputStream inputstream = new FileInputStream(file);
			ParseContext context = new ParseContext();
			Parser parser = new AutoDetectParser();
			try {
				parser.parse(inputstream, new ExpandedTitleContentHandler(handler), metadata, context);
				return metadata;
			} catch (IOException | SAXException | TikaException e) {
			}
			return null;
		}
		
		public File getFile() {
			return file;
		}
		public BasicFileAttributes getAttributes() {
			return attributes;
		}
		public void setFile(File file) throws IOException {
			this.file = file;
			this.attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
			this.metadata = loadMetadata(file);
			this.typeName = getExtensionName(file);
		}
		
	    public String getValue(String name) {
	    	Object value = null;
	    	if(this.metadata != null) {
	    		//String[] names = this.metadata.names();
	    		value = this.metadata.get(name);
	    	}
	    	//System.out.println(name+" " + value);
	    	if(value == null) {
	    		FileAttributesType type = FileAttributesType.getTypeByName(name);
	    		value = type != null ?
	    	    		switch(type) {
	    				case CREATION_TIME -> attributes.creationTime();
	    				case LAST_ACCESS_TIME -> attributes.lastAccessTime();
	    				case LAST_MODIFIED_TIME -> attributes.lastModifiedTime();
	    				case SIZE -> attributes.size();
	    				case TYPE -> typeName;
	    				case NAME -> file.getName();
	    				default -> null;
	    	    		} : null;
	    	    /*
	    		switch(type) {
				case CREATION_TIME:
					value = attributes.creationTime();
					break;
				case LAST_ACCESS_TIME:
					value = attributes.lastAccessTime();
					break;
				case LAST_MODIFIED_TIME:
					value = attributes.lastModifiedTime();
					break;
				case SIZE:
					value = attributes.size();
					break;
				case TYPE:
					System.out.println("Bonny");
					value = typeName;
					break;
				case NAME:
					System.out.println("Clyde");
					value = file.getName();
					break;
				case null:
					break;
				default:
					break;
	    		}
	    		*/
	    	}
	    	//System.out.println(name+" " + value + " " + typeName);
	    	return value != null ? value.toString() : null;
	    }
	    
	    public Set<String> getAllKeys() {
	    	Set<String> set = new HashSet<>();
	    	set.addAll(Arrays.asList(FileAttributesType.values()).stream().map(type -> type.getName()).collect(Collectors.toList()));
	    	if(metadata != null) {
	    		List<String> keys = Arrays.asList(metadata.names());
	    		final String XMPDM = "xmpDM:";
	    		keys = keys.stream().filter(key -> key.startsWith(XMPDM)).map(key -> key.substring(key.lastIndexOf(':')+1)).collect(Collectors.toList());
	    		set.addAll(keys);
	    	}
	    	return set;
	    }
    }
    
    enum FileAttributesType {
    	LAST_MODIFIED_TIME("Date last saved"),
    	LAST_ACCESS_TIME("Date accessed"),
    	CREATION_TIME("Date created"),
    	TYPE("Type"),
    	SIZE("Size"),
    	NAME("Name");
    	//ICON("Icon");
    	
    	public static FileAttributesType getTypeByName(String name) {
    		FileAttributesType[] arr = FileAttributesType.values();
    		for(FileAttributesType type : arr) {
    			if(type.getName().equals(name))
    				return type;
    		}
    		return null;
    	}
    	
    	private String name;

		private FileAttributesType(String name) {
			this.name = name;
		}
    	
		public String getName() {
			return name;
		}
    }
    
    public static String getFileExtension(File file) {
    	try {
	    	TikaConfig tika = new TikaConfig();
	    	Metadata md = new Metadata();
	    	//TikaInputStream sets the TikaCoreProperties.RESOURCE_NAME_KEY
	    	//when initialized with a file or path
	    	MediaType mediaType = tika.getDetector().detect(
	    			TikaInputStream.get(file.toPath(), md), md);
	    	//MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
	    	MimeTypes allTypes = tika.getMimeRepository();
	    	MimeType mimeType = allTypes.forName(mediaType.toString());
	    	return mimeType.getExtension();
    	}
    	catch (Exception e) {
			// TODO: handle exception
		}
    	return null;
    }
    
    public static String getExtensionName(File file) {
    	if(file.isHidden()) return null;
    	if(file.isDirectory()) {
    		return "File folder";
    	}
    	/* getFileExtension(file) */
    	return getExtensionName(MimeUtils.getMimeTypeAsExtension(file));
    }
    
    public static String getExtensionName(String extension) {
    	if(!MimeUtils.hasMimeType(extension))
    		throw new RuntimeException("Not leggal");
    	String extensionSoftware = getDefaultAppToActivateExtension(extension);
    	if(extensionSoftware == null)
    		extensionSoftware = extension;
    	System.out.println(extensionSoftware);
    	if(Advapi32Util.registryValueExists(WinReg.HKEY_CLASSES_ROOT, extensionSoftware, "")) {
    		return Advapi32Util.registryGetStringValue(WinReg.HKEY_CLASSES_ROOT, extensionSoftware, "");
    	}
    	String format = extension.replace(".", "");
    	return format.toUpperCase() + " File";
    	/*Preferences p = Preferences.userRoot();
    	System.out.println(p);
    	String userPreference = String.format("HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\FileExts\\%s",extension);
    	if(p.nodeExists(userPreference)) {
    		p = p.node(userPreference);
    		System.out.println("shook");
    		System.out.println(p);
    	}*/
    }
    
    public static String getDefaultAppToActivateExtension(String extension) {
    	String fileExtPath = String.format("Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\FileExts\\%s",extension);
    	if(Advapi32Util.registryKeyExists(WinReg.HKEY_CURRENT_USER, fileExtPath)) {
    		String userChoice = Paths.get(fileExtPath, "UserChoice").toString();
    		if(Advapi32Util.registryValueExists(WinReg.HKEY_CURRENT_USER, userChoice, "ProgId")) {
    			return Advapi32Util.registryGetStringValue(WinReg.HKEY_CURRENT_USER, userChoice, "ProgId");
    		}
    	}
    	return null;
    }
}
/*
public class PhoneBookController {
	
	
	@FXML private TableView<Contact> table;
	@FXML private TableColumn<Contact, String> phoneCol;
	@FXML private TableColumn<Contact, String> nameCol;
	@FXML private TableColumn<Contact, Contact> removeCol;
	@FXML private TextField searchField;
	@FXML private TextField phoneLabel;
	@FXML private TextField nameLabel;
	
	private static final String DATA_FILE = "book_data.txt";
	private ObservableList<Contact> list;
	
	public void initilize() throws ClassNotFoundException, IOException {
		list = loadData();
		table.setEditable(true);
		nameCol.setCellValueFactory(new Callback<CellDataFeatures<Contact, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<Contact, String> p) {
		         return p.getValue().getNameProperty();
		     }
		});
		nameCol.setCellFactory(new Callback<TableColumn<Contact, String>, TableCell<Contact, String>>() {
		    @Override
		    public TableCell<Contact, String> call(final TableColumn<Contact, String> column) {
		    	TableCell<Contact, String> tc = new TextFieldTableCell<Contact, String>(new DefaultStringConverter()) {
		            @Override
					public void startEdit() {
		                super.startEdit();
		                Node node = getGraphic();
		                if(node instanceof TextField) 
		                	((TextField) node).setAlignment(Pos.CENTER);
		            }
			    };
		    	tc.setAlignment(Pos.CENTER);
		        return tc;
      		}
		});
		phoneCol.setCellValueFactory(new Callback<CellDataFeatures<Contact, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<Contact, String> p) {
		         return p.getValue().getPhoneProperty();
		     }
		});
		phoneCol.setCellFactory(new Callback<TableColumn<Contact, String>, TableCell<Contact, String>>() {
		    @Override
		    public TableCell<Contact, String> call(final TableColumn<Contact, String> column) {
		    	return new CustomCellEdit<>();
		    }
		});
		phoneLabel.textProperty().addListener(CustomCellEdit.textPropertyChangeListener(phoneLabel, 9));
		removeCol.setCellFactory(new Callback<TableColumn<Contact, Contact>, TableCell<Contact, Contact>>() {
            @Override
            public TableCell<Contact, Contact> call(final TableColumn<Contact, Contact> param) {
                return new TableCell<Contact, Contact>() {
                    final Button btn = new Button("Delete");
                    {
                        btn.setOnAction(event -> {
                            list.remove(getIndex());
                        });
                    }

                    @Override
                    public void updateItem(Contact item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        }
                        else {
                            setGraphic(btn);
                            setText(null);
                        }
                        setAlignment(Pos.CENTER);
                    }
                };
            }
        });
        nameCol.prefWidthProperty().bind(table.widthProperty().divide(4)); // w * 1/4
        phoneCol.prefWidthProperty().bind(table.widthProperty().divide(2)); // w * 1/2
        removeCol.prefWidthProperty().bind(table.widthProperty().divide(4));
		FilteredList<Contact> filteredData = new FilteredList<>(list);
		searchField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	            filteredData.setPredicate(new Predicate<Contact>() {

					@Override
					public boolean test(Contact t) {
		                // If filter text is empty, display all persons.
						if (newValue == null || newValue.isEmpty()) {
		                    return true;
		                }
		                String lowerCaseFilter = newValue.toLowerCase();
		                StringProperty property = t.getNameProperty(); 
		                if(property.get().toLowerCase().contains(lowerCaseFilter)) {
		                    return true;
		                }
		                return false;
					}
	            });
			}
		});
        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<Contact> sortedData = new SortedList<>(filteredData);
        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        // 5. Add sorted (and filtered) data to the table.
        table.setItems(sortedData);
    }
	
	private void checkIfExists() throws IOException {
		File file = new File(DATA_FILE);
		if(!file.exists()) {
			this.list = FXCollections.observableArrayList(new ArrayList<Contact>());
			saveData();
		}
	}
	
	@SuppressWarnings("unchecked")
	private ObservableList<Contact> loadData() throws IOException, ClassNotFoundException {
		checkIfExists();
		FileInputStream fin = new FileInputStream(DATA_FILE);
		ObjectInputStream ois = new ObjectInputStream(fin);
		List<Contact> list = (List<Contact>) ois.readObject();
		ois.close();
 	   	return FXCollections.observableArrayList(list);
    }
    
    public void saveData() throws IOException {
    	FileOutputStream fos = new FileOutputStream(DATA_FILE);
 	    ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(new ArrayList<>(list));
        oos.close();
    }
    
	@FXML
	private void addToPhoneBook(ActionEvent e) {
		e.consume();
		Object object = e.getSource();
		if(object instanceof Button)
			if(!nameLabel.getText().isEmpty() && !phoneLabel.getText().isEmpty())  {
				Contact phone = new Contact(nameLabel.getText(), phoneLabel.getText());
				nameLabel.setText("");
				phoneLabel.setText("");
				list.add(phone);			
			}
	}
}
*/