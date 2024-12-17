package FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ExpandedTitleContentHandler;
import org.xml.sax.SAXException;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

import FileUtilities.MimeUtils;

public class FileDetailsUtils {

	public static Metadata loadMetadata(File file) throws FileNotFoundException {
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
		} catch (Exception e) {
		}
		return null;
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
