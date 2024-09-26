import java.awt.FileDialog;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileStoreAttributeView;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.swing.JFileChooser;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.parser.mp4.MP4Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.controlsfx.control.cell.ColorGridCell;
import org.gagravarr.flac.FlacFile;
import org.gagravarr.tika.FlacParser;
import org.xml.sax.SAXException;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.file.FileSystemMetadataReader;
import com.drew.metadata.mp3.Mp3Descriptor;
import com.drew.metadata.mp4.Mp4Context;

import DataStructures.ManageFolder;
import JavaFXInterface.FileExplorer;
import JavaFXInterface.SideFilesList;
import impl.org.controlsfx.spreadsheet.TableViewSpanSelectionModel;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.Media;
import javafx.stage.FileChooser;
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
    	
    	TableView<FileDetails> list = createTable(file);
    	
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
    
    private TableView<FileDetails> createTable(File file) throws IOException {
    	ObservableList<FileDetails> list = FXCollections.observableList(
    			Arrays.asList(file.listFiles()).stream().map(f -> {
					try {
						return new FileDetails(f);
					} catch (IOException | SAXException | TikaException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return null;
					}
				}).filter(p -> p != null).collect(Collectors.toList()));
    	TableView<FileDetails> table = new TableView<>();
    	table.setItems(list);
    	TableColumn<FileDetails, String> nameCol = new TableColumn<>();
    	TableColumn<FileDetails, String> dateCol = new TableColumn<>();
    	File file2 = null;//new File();
    	//BasicFileAttributes attr = Files.readAttributes(file2.toPath(), BasicFileAttributes.class);
    	//nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    	//nameCol.setCellValueFactory((file) -> file.getValue().getFile().getName());
    	//table.fact
    	table.getColumns().add(nameCol);
    	/*nameCol.setCellValueFactory(new Callback<CellDataFeatures<File, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<File, String> p) {
		         return null;//p.getValue().getPhoneProperty();
		     }
		});*/
    	//TableColumn<File, Contact> removeCol;
    	table.setEditable(false);
    	return table;
    }
    
    class FileDetails {
    	private File file;
    	private BasicFileAttributes attributes;
		public FileDetails(File file) throws IOException, SAXException, TikaException {
			super();
			this.file = file;
			this.attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
			//file.last
			//Metadata metadata = FileSystemMetadataReader.readMetadata(file);
			
			BodyContentHandler handler = new BodyContentHandler();
			Metadata metadata = new Metadata();
			FileInputStream inputstream = new FileInputStream(file);
			ParseContext pcontext = new ParseContext();

			// Specific parser
			OOXMLParser msOfficeParser = new OOXMLParser();
			ParseContext context = new ParseContext();
			Parser parser = new AutoDetectParser();
			parser.parse(inputstream, handler, metadata, context);

			System.out.println("Contents of the document:" + handler.toString());
			System.out.println("Metadata of the document:");

			/*Properties p = new Properties();
			p.load(new FileInputStream(file));
			System.out.println(p.toString());*/
			
			
			//MP4Parser
			
			
			//metadata.len
			//FlacFile flac = FlacFile.open(input);
			
			
			
			
			//Media
			
			String[] metadataNames = metadata.names();

			for (String name : metadataNames) {
			  System.out.println(name + "-: " + metadata.get(name));
			}
			
			
	        UserDefinedFileAttributeView fileAttributeView = Files.getFileAttributeView(file.toPath(), UserDefinedFileAttributeView.class);
	        List<String> allAttrs = fileAttributeView.list();
	        System.out.println(allAttrs);
	        for (String att : allAttrs) {
	            System.out.println("att = " + att);
	        }
		}
		public File getFile() {
			return file;
		}
		public BasicFileAttributes getAttributes() {
			return attributes;
		}
		public void setFile(File file) {
			this.file = file;
		}
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