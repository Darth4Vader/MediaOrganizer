package Utils.FileUtils;

public enum FileAttributesType {
	LAST_MODIFIED_TIME("Date last saved"),
	LAST_ACCESS_TIME("Date accessed"),
	CREATION_TIME("Date created"),
	TYPE("Type"),
	SIZE("Size"),
	NAME("Name");
	//ICON("Icon");
	
	public static FileAttributesType getTypeByName(String name) {
		FileAttributesType[] arr = FileAttributesType.values();
		for(FileAttributesType type : arr) {
			if(type.getName().equals(name))
				return type;
		}
		return null;
	}
	
	private String name;

	private FileAttributesType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}