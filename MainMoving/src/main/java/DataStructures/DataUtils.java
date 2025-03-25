package DataStructures;

import java.io.File;
import java.nio.file.Path;

public class DataUtils {

	public static Path getRelativePathFromOfFile(File folder, File relativeFile) {
		Path filePath = relativeFile.toPath();
		Path folderPath = folder.toPath();
		if(filePath.startsWith(folderPath)) {
			return filePath.subpath(folderPath.getNameCount(), filePath.getNameCount());
		}
		return null;
	}
	
	public static File getFileFromRelativePath(File folder, String relativePath) {
		return new File(folder, relativePath);
	}

}
