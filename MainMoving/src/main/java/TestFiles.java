import static com.sun.nio.file.ExtendedWatchEventModifier.FILE_TREE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioInputStream;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.Property;
import org.apache.tika.metadata.XMPDM;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.audio.AudioParser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.parser.mp4.MP4Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.TaggedContentHandler;
import org.gagravarr.flac.FlacTags;
import org.gagravarr.tika.FlacParser;
import org.gagravarr.tika.TestFlacParser;

import com.drew.metadata.wav.WavDescriptor;

import DirectoryWatcher.DirectoryWatcher;

public class TestFiles {

    public static void main(String[] args) throws Exception
    {
    	
    	File file = new File("C:\\Users\\itay5\\OneDrive\\מסמכים\\New folder (2)\\06  John Williams - The Hologram , Binary Sunset (Medley).flac");
    	//System.out.println(Files.readAttributes(file.toPath(), "user:tags"));
    	
    	
    	//file = new File("C:\\Users\\itay5\\OneDrive\\מסמכים\\New folder (2)\\06 visions at dagobah.mp3");
    	
    	//FileInputStream in = new FileInputStream(file.getAbsolutePath()+":\u2663SummaryInformation\"");
    	
    	//System.out.println(in.readAllBytes());
    	
    	
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		InputStream inputstream = new FileInputStream(file);
		ParseContext pcontext = new ParseContext();

		// Specific parser
		//OOXMLParser msOfficeParser = new OOXMLParser();
		ParseContext context = new ParseContext();
		Parser parser = new AutoDetectParser();
		//Parser parser = new FlacParser();
		
		//Mp3Parser
		
		inputstream = new BufferedInputStream(inputstream);
		
		
		
		//inputstream = new AudioInputStream(inputStream);
		
		parser.parse(inputstream, handler, metadata, context);

		
		
		System.out.println("Contents of the document:" + handler.toString());
		System.out.println("Metadata of the document:");
    	
		String[] metadataNames = metadata.names();
		
		//XMPDM
		//MP4Parser
		//FlacParser

		for (String name : metadataNames) {
		  System.out.println(name + "-: " + metadata.get(name));
		}
    	
        /*FileSystem fs = FileSystems.getDefault();
        WatchService ws = fs.newWatchService();
        Path pTemp = Paths.get("C:\\Users\\itay5\\OneDrive\\מסמכים\\New folder (2)");
        pTemp.register(ws, new WatchEvent.Kind[] {ENTRY_MODIFY, ENTRY_CREATE, ENTRY_DELETE}, FILE_TREE);
        while(true)
        {
            WatchKey k = ws.take();
            for (WatchEvent<?> e : k.pollEvents())
            {
                Object c = e.context();
                System.out.printf("%s %d %s\n", e.kind(), e.count(), c);
                System.out.println(k.watchable());
                //System.out.println(e.);
            }
            k.reset();
        }*/
    	
    	
    	
    	/*
    	DirectoryWatcher watcher = new DirectoryWatcher.Builder()
    	        .addDirectories("C:\\Users\\itay5\\OneDrive\\מסמכים\\New folder (2)")
    	        .setPreExistingAsCreated(true)
    	        .build(new DirectoryWatcher.Listener() {
    	            public void onEvent(DirectoryWatcher.Event event, Path path) {
    	                switch (event) {
    	                    case ENTRY_CREATE:
    	                        System.out.println(path + " created.");
    	                        break;

    	                    case ENTRY_MODIFY:
     	                        System.out.println(path + " modified.");
    	                        break;

    	                    case ENTRY_DELETE:
     	                        System.out.println(path + " deleted.");
    	                        break;
    	                }
    	            }
    	        });

    	try {
    	    watcher.start(); // Actual watching starts here
    	    TimeUnit.SECONDS.sleep(30);
    	    watcher.stop(); // Stop watching
    	} catch (Exception e) {
    	    // Do something
    	}
    	*/
    }

}
