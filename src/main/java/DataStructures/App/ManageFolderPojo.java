package DataStructures.App;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import DataStructures.Json.DataInformation;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ManageFolderPojo {
	
	@JsonProperty(DataInformation.MANAGE_FOLDER_MAIN_PATH)
	private String urlParent;
	
	@JsonProperty("Movies")
	public Map<String, String> movieMap = new HashMap<>();
	
	@JsonProperty("TV")
	public Map<String, String> TVMap = new HashMap<>();
	
	@JsonProperty("Unkown_Media")
	public Map<String, String> unkownMediaMap = new HashMap<>();
	
	public ManageFolderPojo(@JsonProperty(DataInformation.MANAGE_FOLDER_MAIN_PATH) String urlParent) {
		this.urlParent = urlParent;
	}
	
	public String getUrlParent() {
		return urlParent;
	}
	
	public void setUrlParent(String urlParent) {
		this.urlParent = urlParent;
	}
}