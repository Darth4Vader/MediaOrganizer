package JavaFXInterface.utils.controlsfx;

import java.util.Optional;

import org.controlsfx.control.tableview2.TableView2;

import impl.org.controlsfx.tableview2.TableView2VirtualFlow;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.skin.TableViewSkinBase;
import javafx.scene.control.skin.VirtualFlow;

public class TableViewUtils {

    public static <S> void betterScrollTo(TableView2<S> tableView, int toIndex) {
    	TableViewSkinBase<?, ?, ?, ?, ?> skin = (TableViewSkinBase<?, ?, ?, ?, ?>) tableView.getSkin();
    	Optional<VirtualFlow> vfOpt = skin.getChildren().stream()
    			.filter(child -> child instanceof VirtualFlow)
    			.map(VirtualFlow.class::cast)
    			.findFirst();
    	if(vfOpt.isPresent()) {
    		VirtualFlow<IndexedCell<S>> vf = (VirtualFlow<IndexedCell<S>>) vfOpt.get();
        	if(vf instanceof TableView2VirtualFlow) {
        		TableView2VirtualFlow<IndexedCell<S>> virtualFlow = (TableView2VirtualFlow<IndexedCell<S>>) vf;
        		
        		IndexedCell<S> firstFullyVisibleCell = virtualFlow.getFirstVisibleCellWithinViewport();
        		IndexedCell<S> lastFullyVisibleCell = virtualFlow.getLastVisibleCellWithinViewport();
        		IndexedCell<S> lastVisibleCell = virtualFlow.getLastVisibleCell();
        		
        		if(firstFullyVisibleCell != null && lastFullyVisibleCell != null && lastVisibleCell != null) {
        			int firstFullyVisibleIndex = firstFullyVisibleCell.getIndex();
        			int lastFullyVisibleIndex = lastFullyVisibleCell.getIndex();
        			int lastIndex = lastVisibleCell.getIndex();
					if(toIndex <= firstFullyVisibleIndex) {
						tableView.scrollTo(toIndex);
					}
					else if(toIndex >= lastIndex) {
						tableView.scrollTo((toIndex - lastFullyVisibleIndex) + firstFullyVisibleIndex);
					}
				}
        	}
        }
    }

}
