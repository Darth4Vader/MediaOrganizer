package DataStructures.Json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class JacksonUtils {

	public static ObjectMapper getObjectMapperSerialization() {
		ObjectMapper mapper = new ObjectMapper();
		mapper = JsonMapper.builder()
				.findAndAddModules()
				.build();
		
		
		
		mapper.setVisibility(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.ANY));
		//mapper.addMixIn(MediaSimple.class, MediaSimpleMixIn.class);
		//MapType type = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, MediaSimple.class);
		
		
		mapper.setSerializationInclusion(Include.NON_EMPTY);
		
		mapper.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION);
		return mapper;
	}
	
	public static ObjectMapper getObjectMapperDeserialization() {
		ObjectMapper mapper = new ObjectMapper();
		mapper = JsonMapper.builder()
				.findAndAddModules()
				.build();
		
		
		
		mapper.setVisibility(mapper.getDeserializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.ANY));
		//mapper.addMixIn(MediaSimple.class, MediaSimpleMixIn.class);
		//MapType type = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, MediaSimple.class);
		
		
		//mapper.setSerializationInclusion(Include.NON_EMPTY);
		
		//mapper.registerModule(new JavaT());
		
		mapper.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION);
		return mapper;
	}
	
	public static InjectableValues.Std createInjectableValues(ObjectMapper mapper) {
		InjectableValues injObj = mapper.getInjectableValues();
		InjectableValues.Std inj;
		if(injObj instanceof InjectableValues.Std) {
			inj = (InjectableValues.Std) injObj;
		}
		else {
			inj = new InjectableValues.Std();
			mapper.setInjectableValues(inj);
		}
		return inj;
	}
	
	public static <T> T getInjectValue(Object object, String name, Class<T> typeClass) throws JsonMappingException {
		Object val = null;
		if(object instanceof JsonGenerator) {
			JsonGenerator gen = (JsonGenerator) object;
			ObjectMapper mapper = ((ObjectMapper) gen.getCodec());
			InjectableValues inject = mapper.getInjectableValues();
			val = inject.findInjectableValue(name, null, null, null);
		}
		else if (object instanceof DeserializationContext) {
			DeserializationContext ctxt = (DeserializationContext) object;
			val = ctxt.findInjectableValue(name, null, null);
		}
		return typeClass.cast(val);
	}

}
