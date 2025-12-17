package Utils.FileUtils;

import java.io.File;
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
		if(attributes == null) {
			try {
				attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return attributes;
	}
	public void setFile(File file) throws IOException {
		this.file = file;
		this.attributes = null;
		this.typeName = null;
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
    				case CREATION_TIME -> getAttributes() != null ? getAttributes().creationTime() : null;
    				case LAST_ACCESS_TIME -> getAttributes() != null ? getAttributes().lastAccessTime() : null;
    				case LAST_MODIFIED_TIME -> getAttributes() != null ? getAttributes().lastModifiedTime() : null;
    				case SIZE -> getAttributes() != null ? getAttributes().size() : null;
    				case TYPE -> getTypeName();
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
    
    public String getTypeName() {
    	if(typeName == null) {
			typeName = FileDetailsUtils.getExtensionName(file);
		}
		return typeName;
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