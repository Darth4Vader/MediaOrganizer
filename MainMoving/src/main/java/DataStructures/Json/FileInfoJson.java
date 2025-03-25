package DataStructures.Json;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import DataStructures.DataUtils;
import DataStructures.FileInfo;

public class FileInfoJson {
	
	public static class FileInfoToRelativePathSerializer extends JsonSerializer<FileInfo> {
		
		@Override
		public void serialize(FileInfo value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
			File mainFolder = FileJson.getRelativePathMainFolder(gen);
			Path writeVal = DataUtils.getRelativePathFromOfFile(mainFolder, value.getFile());
			gen.writeString(writeVal != null ? writeVal.toString() : null);
		}
	}

	public static class FileInfoFromRelativePathDeserializerKey extends KeyDeserializer {
		
		@Override
	    public FileInfo deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			File mainFolder = FileJson.getRelativePathMainFolder(ctxt);
			File file = DataUtils.getFileFromRelativePath(mainFolder, key);
	    	return new FileInfo(file);
	    }
	}
	
	public static class FileInfoFromRelativePathDeserializer extends JsonDeserializer<FileInfo> {

		@Override
		public FileInfo deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
			File mainFolder = FileJson.getRelativePathMainFolder(ctxt);
			String key = p.getValueAsString();
			File file = DataUtils.getFileFromRelativePath(mainFolder, key);
	    	return new FileInfo(file);
		}
	}
}