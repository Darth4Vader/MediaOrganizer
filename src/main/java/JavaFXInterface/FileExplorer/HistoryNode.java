package JavaFXInterface.FileExplorer;

public class HistoryNode<T> {
	
	private T value;
	private HistoryNode<T> previousNode;
	private HistoryNode<T> nextNode;

	public HistoryNode(T value) {
		this.value = value;
		this.previousNode = null;
		this.nextNode = null;
	}
	
	public T getValue() {
		return value;
	}
	
	public HistoryNode<T> previous() {
		return previousNode;
	}
	
	public HistoryNode<T> next() {
		return nextNode;
	}
	
	public HistoryNode<T> add(T value) {
		HistoryNode<T> node = new HistoryNode<T>(value);
		setNextNode(node);
		return node;
	}
	
	public boolean hasPrevious() {
		return this.previousNode != null;
	}
	
	public boolean hasNext() {
		return this.nextNode != null;
	}
	
	void setPreviousNode(HistoryNode<T> node) {
		this.previousNode = node;
		node.nextNode = this;
	}
	
	void setNextNode(HistoryNode<T> node) {
		this.nextNode = node;
		node.previousNode = this;
	}

}
