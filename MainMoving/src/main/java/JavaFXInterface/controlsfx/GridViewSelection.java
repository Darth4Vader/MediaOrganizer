package JavaFXInterface.controlsfx;

import java.util.List;

import org.controlsfx.control.GridView;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Cell;
import javafx.scene.control.IndexedCell;
import javafx.scene.input.MouseEvent;

public class GridViewSelection<T> extends GridView<T> {
	
	private ObservableList<T> selectionList;
	
    public GridViewSelection() {
        this(FXCollections.observableArrayList());
    }
    
    public GridViewSelection(ObservableList<T> items) {
    	super(items);
		this.selectionList = FXCollections.observableArrayList();
		getItems().addListener((ListChangeListener<? super T>) (change -> {
			while(change.next()) {
				List<?> removed = change.getRemoved();
				if(removed != null) {
					selectionList.removeAll(removed);
				}
			}
		}));
		installMultipleSelection();
    }
	
	private void installMultipleSelection() {
		addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
			Node node = event.getPickResult().getIntersectedNode();
			node = getNodeCellParentOfView(node, this);
			if(node instanceof IndexedCell) {
				@SuppressWarnings("unchecked")
				IndexedCell<T> cell = (IndexedCell<T>) node;
				T item = cell.getItem();
                if(selectionList.contains(item)) {
                	selectionList.remove(item);
                } else {
                	selectionList.add(item);
                }
            	updateSelection(cell);
			}
		});
	}
	
	public void addSelectionListener(ListChangeListener<? super T> listener) {
		selectionList.addListener(listener);
	}
	
    public void updateSelection(IndexedCell<T> cell) {
        if (cell.isEmpty()) return;
        int index = cell.getIndex();
        if (index == -1) return;
        boolean isSelected = selectionList.contains(cell.getItem());
        cell.updateSelected(isSelected);
    }
    
	public ObservableList<T> getSelectedItems() {
		return selectionList;
	}
	
	public void addSelectedItem(T item) {
        if(item != null && !selectionList.contains(item)) {
        	selectionList.add(item);
        }
	}
	
	@SuppressWarnings("unchecked")
	private static <T> Cell<T> getNodeCellParentOfView(Node node, GridView<T> view){
		while(node != null && !node.equals(view)) {
			if(node instanceof Cell)
				return (Cell<T>) node;
			node = node.getParent();
		}
		return null;
	}
}