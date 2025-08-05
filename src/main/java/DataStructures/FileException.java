package DataStructures;


import java.io.File;

public class FileException extends Exception {
	
	enum ExceptionType {
		FILE_NOT_EXISTS,
		FILE_CANNOT_BE_CREATED,
		FILE_MOVE_FAILED,
		NO_EXCEPTION
	}
	
	private final ExceptionType type;
	private final String originalPath;
	
	public FileException(File file) {
		this(file, ExceptionType.NO_EXCEPTION);
	}
	
	public FileException(File file, ExceptionType type) {
		this.type = type;
		this.originalPath = file.getAbsolutePath();
	}
	
}

class ActionAlreadyActivatedException extends Exception {
	
	public ActionAlreadyActivatedException() {
		super("The Action has been already activated");
	}
}
