package DataStructures;

import java.io.File;
import java.util.List;

public class ManageFileDetailsList<T extends ManageFileDetails> {

    private List<T> manageFileDetailsList;

    public ManageFileDetailsList(List<T> manageFileDetailsList) {
        this.manageFileDetailsList = manageFileDetailsList;
    }

    public T addFile(File file) {
        T details = createManageFileDetails(file); // now type matches
        this.manageFileDetailsList.add(details);
        return details;
    }

    // Make this return T instead of ManageFileDetails
    protected T createManageFileDetails(File file) {
        return (T) new ManageFileDetails(file); // cast is safe for base class usage
    }
}