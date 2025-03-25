package DataStructures;

import java.io.File;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import DataStructures.Json.FileInfoJson.FileInfoFromRelativePathDeserializerKey;
import DataStructures.Json.FileInfoJson.FileInfoToRelativePathSerializer;
import DataStructures.Json.FileJson.FileFromRelativePathDeserializer;
import DataStructures.Json.FileJson.FileToRelativePathSerializer;

public class FolderInformation {
	
	@JsonProperty("posters")
	@JsonDeserialize(keyUsing = FileInfoFromRelativePathDeserializerKey.class, contentUsing = FileFromRelativePathDeserializer.class)
	@JsonSerialize(keyUsing = FileInfoToRelativePathSerializer.class, contentUsing = FileToRelativePathSerializer.class)
	private Map<FileInfo, File> postersOfFolders;
	
	@JsonIgnore
	private FolderInfo folderInfo;
	
	public Map<FileInfo, File> getPostersOfFolders() {
		return postersOfFolders;
	}

	public void setPostersOfFolders(Map<FileInfo, File> postersOfFolders) {
		this.postersOfFolders = postersOfFolders;
	}

	public void setFolderInfo(FolderInfo folderInfo) {
		this.folderInfo = folderInfo;
	}
	
	public FolderInfo getFolderInfo() {
		return folderInfo;
	}
}