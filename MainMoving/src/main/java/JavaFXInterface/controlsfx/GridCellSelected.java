package JavaFXInterface.controlsfx;

import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

public class GridCellSelected<T> extends GridCell<T> {
	
	public GridCellSelected() {
		
	}
	
    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        
        if(item != null && !empty) {
        	GridView<T> gridView = getGridView();
        	if(gridView == null || !(gridView instanceof BetterGridView))
        		return;
        	BetterGridView<T> gridViewSelection = (BetterGridView<T>) gridView;
        	gridViewSelection.updateSelection(this);
        }
    }
}