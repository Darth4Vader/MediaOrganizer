package DataStructures;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import FileUtilities.MimeUtils;

public class FileInfoType {
	
	public static enum FolderType {
		MAIN_FOLDER,
		MOVIE("Movie"),
		TV_SERIES,
		MINI_SERIES,
		TV_SERIES_AND_MOVIE,
		TV_EPISODE,
		EXTRAS("-Extras"),
		POSTERS("Poster"),
		FEATURETTES("Featurette"),
		CHARACTER_POSTERS("Character Poster"),
		INFORMATION("-Information"),
		LOGO("Logo"),
		NONE;
		
		private final String name;
		
		FolderType() {
			this("");
		}
		
		FolderType(String name) {
			this.name = name;
		}
		
		public String getFolderName() {
			String name = this.name;
			if(!name.isEmpty()) {
				if(!name.startsWith("-"))
					name = "- " + name;
				if(!name.endsWith("s"))
					if(this != MOVIE & this != LOGO & this != INFORMATION)
						name += "s";
				name = " " + name;
			}
			return name;
		}
		
		public String[] getTypeList() {
			String[] arr = {};
			if(name.equals(""))
				return arr;
			List<String> list = new ArrayList<>();
			list.add(name);
			if(!name.startsWith("-"))
				list.add("Main " + name);
			if(!name.endsWith("s"))
				list.add(name+"s");
			if(name.startsWith("-"))
				list.add(name.substring(1));
			return list.toArray(new String[list.size()]);
		}
	}
	
	private FolderType type;
	private String folderTypeName;
	private String nameWithoutType;
	
	public FileInfoType(File file) {
		this(MimeUtils.getNameWithoutExtension(file));
	}
	
	public FileInfoType(String name) {
		this.type = FolderType.NONE;
		this.folderTypeName = "";
		this.nameWithoutType = name;
		name = name.toLowerCase();
		for(FolderType type : FolderType.values()) {
			for(String typeName : type.getTypeList()) {
				String compare = typeName.toLowerCase();
				if(!compare.isEmpty() && name.endsWith(compare)) {
					this.type = type;
					this.folderTypeName = typeName;
					this.nameWithoutType = this.nameWithoutType.substring(0, name.lastIndexOf(compare));
					break;
				}
			}
		}
		
	}
	
	public FileInfoType(FileInfoType fileInfoType) {
		this.type = fileInfoType.type;
		this.folderTypeName = fileInfoType.folderTypeName;
		this.nameWithoutType = fileInfoType.nameWithoutType;
	}
	
	public String getNameWithoutFolderType() {
		return this.nameWithoutType;
	}
	
	public String getFolderTypeName() {
		return this.folderTypeName;
	}
	
	public FolderType getFolderType() {
		return this.type;
	}
	
	public static FolderType getFolderType(File file) {
		return new FileInfoType(file).getFolderType();
	}
	
	public void setFolderType(FolderType type) {
		this.type = type;
	}
	
	/*private static String getNameWithFolderType(String name, FolderType type) {
		String mime = MimeUtils.getMimeType(name);
		name = MimeUtils.getNameWithoutExtension(name);
		mime = !mime.isEmpty() ? "."+mime : mime;
		return name + type.getFolderName() + mime;
	}*/	
}
