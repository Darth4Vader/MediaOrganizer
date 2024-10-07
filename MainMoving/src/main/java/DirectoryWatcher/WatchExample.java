package DirectoryWatcher;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.WatchEvent.Kind;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import DirectoryWatcher.FileChange.FileChaneType;
import javafx.application.Platform;

public class WatchExample implements Runnable {

    private final Set<Path> created = new LinkedHashSet<>();
    private final Set<Path> updated = new LinkedHashSet<>();
    private final Set<Path> deleted = new LinkedHashSet<>();

    private volatile boolean appIsRunning = true;
    // Decide how sensitive the polling is:
    private final int pollmillis = 100;
    private WatchService ws;
    private HandleFileChanges handleFileChanges;

    private Listener listener = this::fireEvents;//WatchExample::fireEvents;

    @FunctionalInterface
    interface Listener
    {
        public void fileChange(Set<Path> deleted, Set<Path> created, Set<Path> modified);
    }

    public WatchExample() {
    	this.canActivate = true;
    	setToRun();
    }
    
    public void setListener(Listener listener) {
        this.listener = listener;
    }
    
    private boolean canActivate;
    
    
    public void setToRun() {
        System.out.println("startup()");
        while(!canActivate);
        created.clear();
        updated.clear();
        deleted.clear();
        this.appIsRunning = true;
        this.ws = null;
    }

    public void shutdown() {
        System.out.println("shutdown()");
        this.appIsRunning = false;
        if(ws != null) try {
        	ws.close();
        	System.out.println("CClosed");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void run() {
        System.out.println();
        System.out.println("run() START watch");
        System.out.println();

        canActivate = false;
        try(WatchService autoclose = ws) {

            while(appIsRunning) {

                boolean hasPending = created.size() + updated.size() + deleted.size() > 0;
                System.out.println((hasPending ? "ws.poll("+pollmillis+")" : "ws.take()")+" as hasPending="+hasPending);

                // Use poll if last cycle has some events, as take() may block
                WatchKey wk = hasPending ? ws.poll(pollmillis,TimeUnit.MILLISECONDS) : ws.take();
                if (wk != null)  {
                    for (WatchEvent<?> event : wk.pollEvents()) {
                         Path parent = (Path) wk.watchable();
                         Path eventPath = (Path) event.context();
                         storeEvent(event.kind(), parent.resolve(eventPath));
                     }
                     boolean valid = wk.reset();
                     if (!valid) {
                         System.out.println("Check the path, dir may be deleted "+wk);
                     }
                }

                System.out.println("PENDING: cre="+created.size()+" mod="+updated.size()+" del="+deleted.size());
                
                // This only sends new notifications when there was NO event this cycle:
                if (wk == null && hasPending) {
                    listener.fileChange(deleted, created, updated);
                    deleted.clear();
                    created.clear();
                    updated.clear();
                }
            }
        }
        catch (InterruptedException e) {
            System.out.println("Watch was interrupted, sending final updates");
            fireEvents(deleted, created, updated);
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        finally {
            System.out.println("run() END watch");
            canActivate = true;
        }
    }
    
    public void register(Path dir) throws IOException {
    	Kind<?> [] kinds = { StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE};
    	register(kinds, dir);
    }

    public void register(Kind<?> [] kinds, Path dir) throws IOException {
        System.out.println("register watch for "+dir);

        // If dirs are from different filesystems WatchService will give errors later
        if (this.ws == null) {
            ws = dir.getFileSystem().newWatchService();
        }
        //System.out.println(dir.register(ws, kinds));
        try {
        	dir.register(ws, kinds);
        }
        catch (Throwable e) {
        	e.printStackTrace();
		}
    }

    /**
     * Save event for later processing by event kind EXCEPT for:
     * <li>DELETE followed by CREATE           => store as MODIFY
     * <li>CREATE followed by MODIFY           => store as CREATE
     * <li>CREATE or MODIFY followed by DELETE => store as DELETE
     */
    private void
    storeEvent(Kind<?> kind, Path path) {
        System.out.println("STORE "+kind+" path:"+path);

        boolean cre = false;
        boolean mod = false;
        boolean del = kind == StandardWatchEventKinds.ENTRY_DELETE;

        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
            mod = deleted.contains(path);
            cre = !mod;
        }
        else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
            cre = created.contains(path);
            mod = !cre;
        }
        addOrRemove(created, cre, path);
        addOrRemove(updated, mod, path);
        addOrRemove(deleted, del, path);
    }
    // Add or remove from the set:
    private static void addOrRemove(Set<Path> set, boolean add, Path path) {
        if (add) set.add(path);
        else     set.remove(path);
    }

    /*
    public static void fireEvents(Set<Path> deleted, Set<Path> created, Set<Path> modified) {
        System.out.println();
        System.out.println("fireEvents START");
        for (Path path : deleted)
            System.out.println("  DELETED: "+path);
        for (Path path : created)
            System.out.println("  CREATED: "+path);
        for (Path path : modified)
            System.out.println("  UPDATED: "+path);
        System.out.println("fireEvents END");
        System.out.println();
    }
    */
    
    /*private static final Logger LOGGER = Logger.getLogger(WatchExample.class.getName());
    
    static {
        LOGGER.setUseParentHandlers(false);
        LOGGER.setLevel(Level.ALL);
    }
    
    /**
     * Gets the logger instance for this class.
     *
     * @return the logger instance
     */
    /*public Logger getLogger() {
        return LOGGER;
    }*/
    
    
    public void fireEvents(Set<Path> deleted, Set<Path> created, Set<Path> modified) {
        System.out.println();
        System.out.println("fireEvents START");
        System.out.println(deleted + "\n"+created+"\n"+modified);
        //LinkedBlockingQueue<FileChange> list = new LinkedBlockingQueue<>();
        //BlockingQ
        List<FileChange> list = new ArrayList<>();
        if(deleted.size() == 1 && created.size() == 1) {
        	FileChange fileChange = new FileChange();
        	list.add(new FileRename(deleted.iterator().next(), created.iterator().next()));
        }
        else {
	        for (Path path : deleted) {
	            //System.out.println("  DELETED: "+path);
	        	list.add(new FileChange(FileChaneType.DELETED, path));
	        }
	        for (Path path : created) {
	            //System.out.println("  CREATED: "+path);
	        	list.add(new FileChange(FileChaneType.CREATED, path));
	        }
	        for (Path path : modified) {
	            //System.out.println("  UPDATED: "+path);
	        	list.add(new FileChange(FileChaneType.UPDATED, path));
	        }
        }
        if(handleFileChanges != null)
        	handleFileChanges.handleFileChanges(list);
        /*Platform.runLater(() -> {
        	System.out.println(list);
        });*/
        System.out.println("fireEvents END");
        System.out.println();
    }
    
    private void addToQueue() {
    	
    }

	public void setHandleFileChanges(HandleFileChanges handleFileChanges) {
		this.handleFileChanges = handleFileChanges;
	}
}