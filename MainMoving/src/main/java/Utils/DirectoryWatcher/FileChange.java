package Utils.DirectoryWatcher;

import java.nio.file.Path;

public class FileChange {
	
	public static enum FileChaneType {
		CREATED,
		DELETED,
		RENAMED,
		UPDATED
	}
	
	private FileChaneType fileChaneType;
	private Path path;
	
	public FileChange() {
	}
	
	public FileChange(FileChaneType fileChaneType) {
		super();
		this.fileChaneType = fileChaneType;
	}
	
	public FileChange(FileChaneType fileChaneType, Path path) {
		super();
		this.fileChaneType = fileChaneType;
		this.path = path;
	}
	
	public FileChaneType getFileChaneType() {
		return fileChaneType;
	}
	public Path getPath() {
		return path;
	}
	public void setFileChaneType(FileChaneType fileChaneType) {
		this.fileChaneType = fileChaneType;
	}
	public void setPath(Path path) {
		this.path = path;
	}
}