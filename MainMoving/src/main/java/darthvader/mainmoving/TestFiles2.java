package darthvader.mainmoving;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import DirectoryWatcher.WatchExample;
import JavaFXInterface.AppUtils;
import javafx.application.Platform;

public class TestFiles2 {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		String path = new File("C:\\Users\\itay5\\OneDrive\\Pictures\\Main2024\\Star Wars").getAbsolutePath();
		AppUtils.getIconForFile(10, 10, path);
		
		/*
	    //final List<Path> dirs = Arrays.stream(args).map(Path::of).map(Path::toAbsolutePath).collect(Collectors.toList());
		final List<Path> dirs = Arrays.stream(new String[]{"C:\\Users\\itay5\\OneDrive\\מסמכים\\New folder (2)"}).map(Path::of).map(Path::toAbsolutePath).collect(Collectors.toList());
	    Kind<?> [] kinds = { StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE};

	    // Should launch WatchExample PER Filesystem:
	    WatchExample w = new WatchExample();
	    w.setListener(WatchExample::fireEvents);

	    for(Path dir : dirs)
	        w.register(kinds, dir);

	    // For 2 or more WatchExample use: new Thread(w[n]::run).start();
	    w.run();
	    
	    */
	}
}