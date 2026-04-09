package JavaFXInterface.Logger;

import java.io.File;
import java.util.List;

import DataStructures.ManageFileDetailsList;

public class UiManageFileDetailsList extends ManageFileDetailsList<UiManageFileDetails> {
	
	public UiManageFileDetailsList(List<UiManageFileDetails> manageFileDetailsList) {
		super(manageFileDetailsList);
	}
	
	@Override
	protected UiManageFileDetails createManageFileDetails(File file) {
		return new UiManageFileDetails(file);
	}
}
