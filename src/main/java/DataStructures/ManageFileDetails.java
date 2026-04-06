package DataStructures;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ManageFileDetails {
	
	private File mainFile;
	private List<FileOperationDetails> fileOperationDetailsList;
	
	public ManageFileDetails() {
		this.fileOperationDetailsList = new ArrayList<>();
	}
	
	public ManageFileDetails(File mainFile) {
		this();
		this.mainFile = mainFile;
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
	
	/*public void setFileOperationDetailsList(List<FileOperationDetails> fileOperationDetailsList) {
		this.fileOperationDetailsList = fileOperationDetailsList;
	}*/
	
	public void addFileOperationDetails(Collection<FileOperationDetails> fileOperationDetailsCollection) {
		if(fileOperationDetailsCollection != null)
			this.fileOperationDetailsList.addAll(fileOperationDetailsCollection);
	}
}
