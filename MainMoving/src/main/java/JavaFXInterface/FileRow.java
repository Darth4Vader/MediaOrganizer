package JavaFXInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileRow {

    private List<File> files;

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
}