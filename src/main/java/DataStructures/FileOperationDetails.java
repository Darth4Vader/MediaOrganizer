package DataStructures;

import java.io.File;

import DataStructures.ManageFolder.FileOperation;

public class FileOperationDetails {
	
	private File sourceFile;
	private String destPath;
	private FileOperation action;
	private boolean isFinished;
	private boolean isError;
	
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
	
	public void setSourceFile(File sourceFile) {
		this.sourceFile = sourceFile;
	}
	
	public void setDestPath(String destPath) {
		this.destPath = destPath;
	}
	
	public void setAction(FileOperation action) {
		this.action = action;
	}
	
	public boolean isFinished() {
		return isFinished;
	}
	
	public boolean isError() {
		return isError;
	}
	
	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}
	
	public void setError(boolean isError) {
		this.isError = isError;
	}
}