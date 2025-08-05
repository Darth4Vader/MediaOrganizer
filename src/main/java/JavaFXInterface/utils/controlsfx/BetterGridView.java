package JavaFXInterface.utils.controlsfx;

import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Cell;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class BetterGridView<T> extends GridView<T> {
	
    public BetterGridView() {
        this(FXCollections.observableArrayList());
    }
    
    public BetterGridView(ObservableList<T> items) {
    	super(items);
        addEventFilter(KeyEvent.KEY_PRESSED, event -> {
        	if(event.getCode() == KeyCode.CONTROL) {
        		getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        	}
        });
        addEventFilter(KeyEvent.KEY_RELEASED, event -> {
        	if(event.getCode() == KeyCode.CONTROL) {
        		getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        	}
        });
		this.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
			processArrowKeys(e, this);
		});
		addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
	    	MultipleSelectionModel<T> selectionModel = getSelectionModel();
	    	if(selectionModel == null) return;
			Node node = event.getPickResult().getIntersectedNode();
			node = getNodeCellParentOfView(node, this);
			if(node instanceof GridCell) {
				IndexedCell<?> cell = (IndexedCell<?>) node;
		    	selectionModel.select(cell.getIndex());
			}
		});
    }
	
    private void processArrowKeys(KeyEvent event, GridView<T> gridView) {
    	if(gridView == null) return;
        if (event.getCode().isArrowKey()) {
            event.consume();
            GridViewMultipleSelectionModel<T> sm = gridView.getSelectionModel();
            if(sm != null) {
	            switch (event.getCode()) {
	                case UP:
	                	sm.selectAboveCell();
	                    break;
	                case RIGHT:
	                	sm.selectRightCell();
	                    break;
	                case DOWN:
	                	sm.selectBelowCell();
	                    break;
	                case LEFT:
	                	sm.selectLeftCell();
	                    break;
	                default:
	                    throw new AssertionError(event.getCode().name());
	            }
            }
            GridViewFocusModel<T> fm = gridView.getFocusModel();
            if(fm != null)
            	gridView.scrollTo(fm.getFocusedIndex());
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