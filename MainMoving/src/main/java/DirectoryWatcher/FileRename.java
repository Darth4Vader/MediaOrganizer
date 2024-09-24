package DirectoryWatcher;

import java.nio.file.Path;

import DirectoryWatcher.FileChange.FileChaneType;

public class FileRename extends FileChange {
	private Path newPath;

	public Path getNewPath() {
		return newPath;
	}

	public void setNewPath(Path newPath) {
		this.newPath = newPath;
	}

	public FileRename() {
		super(FileChaneType.RENAMED);
		// TODO Auto-generated constructor stub
	}

	public FileRename(Path path, Path newPath) {
		super(FileChaneType.RENAMED, path);
		this.newPath = newPath;
		// TODO Auto-generated constructor stub
	}
}