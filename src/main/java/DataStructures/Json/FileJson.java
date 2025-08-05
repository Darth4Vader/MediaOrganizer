package DataStructures.Json;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import DataStructures.DataUtils;

public class FileJson {

	public static class FileFromRelativePathDeserializer extends JsonDeserializer<File> {

		@Override
		public File deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
			File mainFolder = getRelativePathMainFolder(ctxt);
			String key = p.getValueAsString();
			return DataUtils.getFileFromRelativePath(mainFolder, key);
		}
	}
	
	public static class FileToRelativePathSerializer extends JsonSerializer<File> {
		
		@Override
		public void serialize(File value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
			File mainFolder = getRelativePathMainFolder(gen);
			Path writeVal = DataUtils.getRelativePathFromOfFile(mainFolder, value);
			gen.writeString(writeVal != null ? writeVal.toString() : null);
		}
	}
	
	public static void setRelativePathMainFolder(ObjectMapper mapper, File folder) {
		InjectableValues.Std inj = JacksonUtils.createInjectableValues(mapper);
		if(folder != null) {
			inj.addValue(DataInformation.CURRENT_FOLDER, folder);
		}
	}
	
	public static File getRelativePathMainFolder(Object object) throws JsonMappingException {
		return JacksonUtils.getInjectValue(object, DataInformation.CURRENT_FOLDER, File.class);
	}
}
