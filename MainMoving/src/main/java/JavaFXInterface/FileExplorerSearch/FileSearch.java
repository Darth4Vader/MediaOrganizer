package JavaFXInterface.FileExplorerSearch;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileSearch {
	
	public static List<File> searchFiles(File searchFolder, String search) throws IOException {
		try (Stream<Path> walkStream = Files.walk(searchFolder.toPath())) {
		    return walkStream
		    		/*.filter(p -> p.toFile().isFile())*/
		    		/*.forEach(f -> {
		        if (f.toString().endsWith("file to be searched")) {
		            System.out.println(f + " found!");
		        }
		    })*/
		    .filter(f -> f.getFileName().toString().contains(search))
		    .map(path -> path.toFile())
		    .collect(Collectors.toList());
		}
		/*PathMatcher matcher = FileSystems.getDefault().getPathMatcher("regex:.*");
	    try (Stream<Path> files = Files.walk(Paths.get(searchDirectory))) {
	        return files
	                .filter(matcher::matches)
	                .collect(Collectors.toList());

	    }*/
	}

}
