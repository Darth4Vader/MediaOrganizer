package DataStructures;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ManageFileDetails {
	
	private File mainFile;
	private List<FileOperationDetails> fileOperationDetailsList;
	
	public ManageFileDetails() {
		this(null);
	}
	
	public ManageFileDetails(File mainFile) {
		this(mainFile, new ArrayList<>());
		this.mainFile = mainFile;
	}
	
	public ManageFileDetails(File mainFile, List<FileOperationDetails> fileOperationDetailsList) {
		this.mainFile = mainFile;
		this.fileOperationDetailsList = fileOperationDetailsList;
	}
	
	public File getMainFile() {
		return mainFile;
	}
	
	public List<FileOperationDetails> getFileOperationDetailsList() {
		return fileOperationDetailsList;
	}
	
	public void setMainFile(File mainFile) {
		this.mainFile = mainFile;
	}
	
	public void addFileOperationDetails(Collection<FileOperationDetails> fileOperationDetailsCollection) {
		if(fileOperationDetailsCollection != null)
			this.fileOperationDetailsList.addAll(fileOperationDetailsCollection);
	}
}
