import static com.sun.nio.file.ExtendedWatchEventModifier.FILE_TREE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.TimeUnit;

import DirectoryWatcher.DirectoryWatcher;

public class TestFiles {

    public static void main(String[] args) throws Exception
    {
        FileSystem fs = FileSystems.getDefault();
        WatchService ws = fs.newWatchService();
        Path pTemp = Paths.get("C:\\Users\\itay5\\OneDrive\\מסמכים\\New folder (2)");
        pTemp.register(ws, new WatchEvent.Kind[] {ENTRY_MODIFY, ENTRY_CREATE, ENTRY_DELETE}, FILE_TREE);
        while(true)
        {
            WatchKey k = ws.take();
            for (WatchEvent<?> e : k.pollEvents())
            {
                Object c = e.context();
                System.out.printf("%s %d %s\n", e.kind(), e.count(), c);
                System.out.println(k.watchable());
                //System.out.println(e.);
            }
            k.reset();
        }
    	
    	/*
    	DirectoryWatcher watcher = new DirectoryWatcher.Builder()
    	        .addDirectories("C:\\Users\\itay5\\OneDrive\\מסמכים\\New folder (2)")
    	        .setPreExistingAsCreated(true)
    	        .build(new DirectoryWatcher.Listener() {
    	            public void onEvent(DirectoryWatcher.Event event, Path path) {
    	                switch (event) {
    	                    case ENTRY_CREATE:
    	                        System.out.println(path + " created.");
    	                        break;

    	                    case ENTRY_MODIFY:
     	                        System.out.println(path + " modified.");
    	                        break;

    	                    case ENTRY_DELETE:
     	                        System.out.println(path + " deleted.");
    	                        break;
    	                }
    	            }
    	        });

    	try {
    	    watcher.start(); // Actual watching starts here
    	    TimeUnit.SECONDS.sleep(30);
    	    watcher.stop(); // Stop watching
    	} catch (Exception e) {
    	    // Do something
    	}
    	*/
    }

}
