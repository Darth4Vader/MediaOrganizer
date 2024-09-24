package DirectoryWatcher;

import java.util.List;

public interface HandleFileChanges {
	void handleFileChanges(List<FileChange> fileChanges);
}