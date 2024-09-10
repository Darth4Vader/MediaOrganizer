package DataStructures;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.StringKeySerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class FolderInformation {
	
	@JsonProperty("posters")
	@JsonDeserialize(keyUsing = FileInfoDeserializer.class, contentUsing = FileDeserializer.class)
	@JsonSerialize(keyUsing = FileInfoSerializer.class, contentUsing = FileSerializer.class)
	private Map<FileInfo, File> postersOfFolders;
	
	@JsonIgnore
	private FolderInfo folderInfo;
	
	public Map<FileInfo, File> getPostersOfFolders() {
		return postersOfFolders;
	}

	public void setPostersOfFolders(Map<FileInfo, File> postersOfFolders) {
		this.postersOfFolders = postersOfFolders;
	}
	
	/*private static class FileInfoDeserializer extends KeyDeserializer {
	    
		public FileInfoDeserializer() {
			super();
		}
		
		@Override
	    public File deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
	        //Use the string key here to return a real map key object
	        System.out.println("Desssss " + key);
	        
	        Object folderInfoObj = ctxt.findInjectableValue(FolderInfo.class.getName(), null, null);
			if(folderInfoObj instanceof FolderInfo) {
				FolderInfo folderInfo = (FolderInfo) folderInfoObj;
				System.out.println("Time: " + key);
			}
	        
	        
	    	return null;
	    }
	}*/
	
	private static class FileDeserializer extends JsonDeserializer<File> {

		@Override
		public File deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
	        JsonToken jsonToken = p.getCurrentToken();
	        if (jsonToken == JsonToken.VALUE_STRING) {
	            String filePath = p.getValueAsString();
		        Object folderInfoObj = ctxt.findInjectableValue(FolderInfo.class.getName(), null, null);
				if(folderInfoObj instanceof FolderInfo) {
					FolderInfo folderInfo = (FolderInfo) folderInfoObj;
					File file = getFileFromFolder(folderInfo, filePath);
					return file;
				}
	        }
	    	return null;
		}
	}
	
	private static class FileInfoDeserializer extends KeyDeserializer {
	    
		public FileInfoDeserializer() {
			super();
		}
		
		@Override
	    public FileInfo deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
	        //Use the string key here to return a real map key object
	        System.out.println("Desssss " + key);
	        
	        Object folderInfoObj = ctxt.findInjectableValue(FolderInfo.class.getName(), null, null);
			if(folderInfoObj instanceof FolderInfo) {
				FolderInfo folderInfo = (FolderInfo) folderInfoObj;
				File file = getFileFromFolder(folderInfo, key);
				System.out.println("Time: " + key);
				return new FileInfo(file);
			}
	        
	        
	    	return null;
	    }
	}
	
	private static class FileSerializer extends JsonSerializer<File> {
		
		@Override
		public void serialize(File value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
			System.out.println("Geron: ");
			
			FolderInfo folderInfo = getFolderInfo(gen);
			String writeVal = null;
			System.out.println("FolderInfo " + folderInfo);
			if(folderInfo != null) {
				writeVal = getPathFromFolder(folderInfo.getFile(), value);
				System.out.println("Own: " + writeVal + " " + value);
			}
			gen.writeString(writeVal);
		}
	}
	
	private static FolderInfo getFolderInfo(JsonGenerator gen) throws JsonMappingException {
		ObjectMapper mapper = ((ObjectMapper) gen.getCodec());
		InjectableValues inject = mapper.getInjectableValues();
		
		Object folderInfoObj = inject.findInjectableValue(FolderInfo.class.getName(), null, null, null);
		if(folderInfoObj instanceof FolderInfo) {
			FolderInfo folderInfo = (FolderInfo) folderInfoObj;
			return folderInfo;
		}
		return null;
	}
	
	private static class FileInfoSerializer extends JsonSerializer<FileInfo> {
		
		@Override
		public void serialize(FileInfo value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
			System.out.println("Geron: ");
			
			FolderInfo folderInfo = getFolderInfo(gen);
			String writeVal = null;
			if(folderInfo != null) {
				writeVal = getPathFromFolder(folderInfo.getFile(), value.getFile());
				System.out.println("Own: " + folderInfo);
			}
			
			gen.writeFieldName(writeVal);
			//gen.writeString(writeVal);
		}
	}
	
	private static String getPathFromFolder(File folder, File file) {
		Path filePath = file.toPath();
		Path folderPath = folder.toPath();
		if(filePath.startsWith(folderPath)) {
			System.out.println("Hello " + folderPath + " " + filePath);
			return filePath.subpath(folderPath.getParent().getNameCount(), filePath.getNameCount()).toString();
		}
		return null;
	}
	
	private static File getFileFromFolder(FolderInfo folderInfo, String path) {
		return new File(folderInfo.getFile().getParent(), path);
	}

	public void setFolderInfo(FolderInfo folderInfo) {
		this.folderInfo = folderInfo;
	}
}