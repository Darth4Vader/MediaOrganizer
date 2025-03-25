package DataStructures.Json;

public class DataInformation {
	
	public static final String CURRENT_FOLDER = "currentFolder";
	public static final String MANAGE_FOLDER_MAIN_PATH = "path";
	
	/*
	NAME("name"),
	TYPE("type"),
	PATH("path"),
	PARENT_PATH("parentPath"),
	PARENT_NAME("parentName"),
	ORIGINAL_NAME("originalName"),
	NAME_WITHOUT_TYPE("nameWithoutType");
	*/
	
	/*
	CURRENT_FOLDER("currentFolder");
	//MANAGE_FOLDER_MAIN_PATH("path");

	private String value;

	private DataInformation(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public static DataInformation getEnum(String value) {
		for (DataInformation v : values())
			if (v.getValue().equalsIgnoreCase(value))
				return v;
		throw new IllegalArgumentException();
	}
	*/

}
