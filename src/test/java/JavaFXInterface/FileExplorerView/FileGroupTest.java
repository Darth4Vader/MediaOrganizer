package JavaFXInterface.FileExplorerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import JavaFXInterface.FileExplorer.FileExplorer;
import JavaFXInterface.FileExplorerView.MainFileExplorerView.FileExplorerView;
import Utils.FileUtils.FileDetails;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FileGroupTest extends Application {
	
	
	/*
	 * --module-path "C:\JavaFX_22.02\lib" --add-modules javafx.controls,javafx.fxml
--add-opens=javafx.controls/javafx.scene.control.skin=ALL-UNNAMED
--add-opens=javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED
	 */
	
	//private FileExplorer fileExplorer;
	
	private ExecutorService loadFileDetailsService;

	public static void main(String[] args) {
		Application.launch(args);
	}

    @Override
    public void start(Stage stage) throws Exception {
    	BorderPane mainPane = new BorderPane();
    	Path path = Path.of(System.getProperty("user.home"), "Music\\My Music\\Soundtracks\\Gladiator\\2000 Gladiator Recording Sessions Edition BOOTLEG");
    	File file = path.toFile();
    	/*fileExplorer = new FileExplorer(file);
    	fileExplorer.getMainFileExplorerView().setFileExplorerView(FileExplorerView.CONTENT);*/
		List<File> files = Arrays.asList(file.listFiles()).stream()
						.filter((f) -> !f.isHidden())
						.collect(Collectors.toList());
    	ObservableList<FileDetails> fileDetails = setFiles(files);
    	TreeTableView<FileDetails> fileExplorer = new TreeTableView<FileDetails>();
    	
    	fileExplorer.setRoot(new TreeItem(fileDetails));
    	
    	fileExplorer.setShowRoot(false);
    	
    	final String attribute = "type";
    	
		Map<String, List<FileDetails>> grouped = fileDetails.stream()
				.collect(Collectors.groupingBy(fd -> fd.getValue(attribute)));
		for (Map.Entry<String, List<FileDetails>> entry : grouped.entrySet()) {
			ListView w;
		}
    	
    	mainPane.setCenter(fileExplorer);
    	Scene scene = new Scene(mainPane);
    	stage.setScene(scene);
    	stage.setWidth(800);
    	stage.setHeight(500);
    	stage.show();
    }
    
	public ObservableList<FileDetails> setFiles(Collection<File> files) {
		
    	ObservableList<FileDetails> fileList = FXCollections.observableArrayList();
    	files.stream()
		.map(f -> {
			try {
				return new FileDetails(f);
			} catch (IOException | SAXException | TikaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}).filter(p -> p != null).forEach(fileList::add);
    	
    	loadFileDetailsService = Executors.newFixedThreadPool(10);
		for (FileDetails file : fileList) {
			/*CompletableFuture.supplyAsync(() -> {
				try {
					file.loadMetadata();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			});*/
			Task<Void> task = new Task<Void>() {
				
				
				@Override
				protected Void call() throws Exception {
					System.out.println("Loading metadata");
					try {
						file.loadMetadata();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				}
			};
			loadFileDetailsService.execute(task);
			
			/*executorService.execute(() -> {
				try {
					file.loadMetadata();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});*/
		}
		loadFileDetailsService.shutdown();
		return fileList;
	}
    
    /*
    @Override
    public void stop() {
    	if(this.fileExplorer != null)
    		this.fileExplorer.closePanel();
    	Platform.exit();
    }
    */
}