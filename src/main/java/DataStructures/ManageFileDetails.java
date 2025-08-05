package DataStructures;

import java.io.File;
import java.util.List;

import DataStructures.ManageFolder.FileOperation;
import DataStructures.ManageFolder.ManageFile;

public class ManageFileDetails {
	
	private FileOperationDetails mainFileOperationDetails;
	private List<FileOperationDetails> fileOperationDetailsList;
	
	public ManageFileDetails() {
		
	}

	public ManageFileDetails(FileOperationDetails mainFileOperationDetails,
			List<FileOperationDetails> fileOperationDetailsList) {
		this.mainFileOperationDetails = mainFileOperationDetails;
		this.fileOperationDetailsList = fileOperationDetailsList;
	}
	
	public FileOperationDetails getMainFileOperationDetails() {
		return mainFileOperationDetails;
	}
	
	public List<FileOperationDetails> getFileOperationDetailsList() {
		return fileOperationDetailsList;
	}

}
