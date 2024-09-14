package JavaFXInterfacePrev;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileRow {

    private List<File> files;
    
    private Integer selectedFile;

    public FileRow() {
        this.files = new ArrayList<>();
    }

    public FileRow(List<File> files) {
        super();
        this.files = files;
    }

    public void add(File file) {
        this.files.add(file);
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

	public File getSelectedFile() {
		return files.get(selectedFile);
	}

	public void setSelectedFile(Integer selectedFile) {
		if(selectedFile < 0 || selectedFile >= files.size())
			throw new IllegalArgumentException("The index \""+selectedFile+"\" is not allowed for an array in the size of \""+files.size()+"\"");
		this.selectedFile = selectedFile;
	}
}