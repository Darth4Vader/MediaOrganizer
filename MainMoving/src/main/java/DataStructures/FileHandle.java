package DataStructures;


import java.io.File;

import FileUtilities.MimeUtils;

public class FileHandle {
	
	private String originalName = "";
	private String fullPath = "";
	private String parentPath = "";
	private String parentName = "";
	private String nameWithoutType;
	private String type = "";
	
	public FileHandle(File file) {
		String name = file.getName();
		this.originalName = name;
		this.fullPath = file.getAbsolutePath();
		this.parentPath = file.getParent();
		this.parentName = file.getParentFile().getName();
		this.type = MimeUtils.getMimeType(file);
		if(!file.isDirectory()) {
			if(!this.type.isEmpty()) {
				this.nameWithoutType = MimeUtils.getNameWithoutExtension(file);
			}
			else {
				this.type = "";
				this.nameWithoutType = name;
			}
		}
		else {
			this.type = "";
			this.nameWithoutType = name;
		}
	}
	
	public String getOriginalName() {
		return this.originalName;
	}
	
	public String getParentPath() {
		return this.parentPath;
	}
	
	public String getParentName() {
		return this.parentName;
	}
	
	public File getFile() {
		return new File(this.fullPath);
	}
	
	public String getPath() {
		return this.fullPath;
	}
	
	public String getType() {
		return MimeUtils.getMimeTypeAsExtension(this.type);
	}
	
	public String getNameWithoutType() {
		System.out.println("hhh " + nameWithoutType);
		return this.nameWithoutType;
	}
	
}
