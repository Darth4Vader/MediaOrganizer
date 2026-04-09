package JavaFXInterface.Logger;

import java.io.File;

import DataStructures.FileOperationDetails;
import DataStructures.ManageFileDetails;
import JavaFXInterface.utils.UiThreadList;
import javafx.collections.ObservableList;

public class UiManageFileDetails extends ManageFileDetails {

    public UiManageFileDetails() {
        super(null, new UiThreadList<>());
    }

    public UiManageFileDetails(File mainFile) {
        super(mainFile, new UiThreadList<>());
    }
    
    @Override
	public ObservableList<FileOperationDetails> getFileOperationDetailsList() {
    	UiThreadList<FileOperationDetails> fileOperationDetailsList = (UiThreadList<FileOperationDetails>) super.getFileOperationDetailsList();
    	return fileOperationDetailsList.getObservableList();
	}
}