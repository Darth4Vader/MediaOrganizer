package JavaFXInterface.FileExplorer;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class HistoryList<T> {
	
	private HistoryNode<T> firstNode;
	private HistoryNode<T> lastNode;
	
	private HistoryNode<T> currentNode;

	public HistoryList() {
		this.firstNode = null;
		this.lastNode = null;
		this.currentNode = null;
	}
	
	public void add(T value) {
		if (this.firstNode == null || this.currentNode == null) {
			this.firstNode = new HistoryNode<T>(value);
			this.lastNode = this.firstNode;
			this.currentNode = this.firstNode;
		} else {
			this.currentNode.setNextNode(new HistoryNode<T>(value));
			this.currentNode = this.currentNode.next();
			this.lastNode = this.currentNode;
		}
		refreshPreviousNextProperties();
	}
	
	public T getCurrentValue() {
		return this.currentNode != null ? this.currentNode.getValue() : null;
	}
	
	public T getPreviousValue() {
		if (this.currentNode == null || this.currentNode.previous() == null)
			return null;
		this.currentNode = this.currentNode.previous();
		refreshPreviousNextProperties();
		return this.currentNode.getValue();
	}
	
	public T getNextValue() {
		if (this.currentNode == null || this.currentNode.next() == null)
			return null;
		this.currentNode = this.currentNode.next();
		refreshPreviousNextProperties();
		return this.currentNode.getValue();
	}
	
	private BooleanProperty hasPreviousProperty = new SimpleBooleanProperty(false);
	
	public BooleanProperty hasPrevious() {
		return hasPreviousProperty;
	}
	
	private BooleanProperty hasNextProperty = new SimpleBooleanProperty(false);
	
	public BooleanProperty hasNext() {
		return hasNextProperty;
	}
	
	private void refreshPreviousNextProperties() {
        hasPreviousProperty.set(this.currentNode.hasPrevious());
        hasNextProperty.set(this.currentNode.hasNext());
    }

}
