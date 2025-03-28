package DataStructures.Json;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;

import DataStructures.DataUtils;
import DataStructures.FolderInfo;

public class FolderInfoJson {

	public static class FolderInfoFromRelativePathDeserializerKey extends KeyDeserializer {
		
		@Override
	    public FolderInfo deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			File mainFolder = FileJson.getRelativePathMainFolder(ctxt);
			File file = DataUtils.getFileFromRelativePath(mainFolder, key);
	    	return new FolderInfo(file);
	    }
	}
	
	public static class FolderInfoFromRelativePathDeserializer extends JsonDeserializer<FolderInfo> {

		@Override
		public FolderInfo deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
			File mainFolder = FileJson.getRelativePathMainFolder(ctxt);
			String key = p.getValueAsString();
			File file = DataUtils.getFileFromRelativePath(mainFolder, key);
	    	return new FolderInfo(file);
		}
	}
}
