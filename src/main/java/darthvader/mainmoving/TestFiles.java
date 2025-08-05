package darthvader.mainmoving;
import static com.sun.nio.file.ExtendedWatchEventModifier.FILE_TREE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.sound.sampled.AudioInputStream;

import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.Property;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.metadata.XMPDM;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;
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
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

import Utils.DirectoryWatcher.DirectoryWatcher;

public class TestFiles {

    public static void main2(String[] args) throws Exception
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
		
		//TikaCoreProperties
		
		//Mp3Parser
		
		//XMPDM
		
		//inputstream = new BufferedInputStream(inputstream);
		
		
		
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
		
		TikaConfig tika = new TikaConfig();
		   Metadata md = new Metadata();
		   //TikaInputStream sets the TikaCoreProperties.RESOURCE_NAME_KEY
		   //when initialized with a file or path
		   MediaType mimetype = tika.getDetector().detect(
		      TikaInputStream.get(file.toPath(), md), md);
		   System.out.println("File " + file + " is " + mimetype);
		   
		   System.out.println(mimetype);
    	
		   //Desktop.getDesktop().open(file);  
		   
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
    
    public static void main(String[] args) throws Exception
    {
    	File file = new File("C:\\Users\\itay5\\OneDrive\\מסמכים\\New folder (2)\\06  John Williams - The Hologram , Binary Sunset (Medley).flac");
    	
		TikaConfig tika = new TikaConfig();
		   Metadata md = new Metadata();
		   //TikaInputStream sets the TikaCoreProperties.RESOURCE_NAME_KEY
		   //when initialized with a file or path
		   MediaType mediaType = tika.getDetector().detect(
		      TikaInputStream.get(file.toPath(), md), md);
		   //System.out.println("File " + file + " is " + mimetype);
		   
		   //MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
		   MimeTypes allTypes = tika.getMimeRepository();
		   MimeType mimeType = allTypes.forName(mediaType.toString());
		   
		   //allTypes.detect(null, md)
		   
		   //new Tika().detect
		   
		   System.out.println(mimeType.getExtension());
    }
    
    public static String getFileExtension(File file) throws TikaException, IOException {
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
    
    public static String getExtensionName(String extension) {
    	if(!MimeType.isValid(extension))
    		throw new RuntimeException("Not leggal");
    	String extensionSoftware = getDefaultAppToActivateExtension(extension);
    	if(extensionSoftware == null)
    		extensionSoftware = extension;
    	if(Advapi32Util.registryKeyExists(WinReg.HKEY_CLASSES_ROOT, extensionSoftware)) {
    		return Advapi32Util.registryGetStringValue(WinReg.HKEY_CLASSES_ROOT, extensionSoftware, "");
    	}
    	String format = extension.replace(".", "");
    	return format.toLowerCase() + " File";
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
