package Utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ExpandedTitleContentHandler;
import org.xml.sax.SAXException;

public class FileDetails {
	
	private File file;
	private BasicFileAttributes attributes;
	private Metadata metadata;
	private String typeName;
	
	public FileDetails(File file) throws IOException, SAXException, TikaException {
		setFile(file);
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
		this.typeName = FileDetailsUtils.getExtensionName(file);
	}
	
	public void loadMetadata() throws FileNotFoundException {
		this.metadata = FileDetailsUtils.loadMetadata(file);
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
    
	@Override
	public int hashCode() {
		return Objects.hash(file);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileDetails other = (FileDetails) obj;
		return Objects.equals(file, other.file);
	}
}