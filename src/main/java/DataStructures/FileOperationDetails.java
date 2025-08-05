package DataStructures;

import java.io.File;

import DataStructures.ManageFolder.FileOperation;
import DataStructures.ManageFolder.ManageFile;

public class FileOperationDetails {
	
	private File sourceFile;
	private String destPath;
	private FileOperation action;
	
	public FileOperationDetails() {
		
	}

	public FileOperationDetails(File sourceFile, String destPath, FileOperation action) {
		this.sourceFile = sourceFile;
		this.destPath = destPath;
		this.action = action;
	}
	
	public File getSourceFile() {
		return sourceFile;
	}
	
	public String getDestPath() {
		return destPath;
	}
	
	public FileOperation getAction() {
		return action;
	}

}
