package JavaFXInterface.FileExplorerView;

import static com.sun.javafx.scene.control.TableColumnSortTypeWrapper.isAscending;
import static com.sun.javafx.scene.control.TableColumnSortTypeWrapper.isDescending;
import static com.sun.javafx.scene.control.TableColumnSortTypeWrapper.setSortType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.controlsfx.control.CheckListView;

import com.sun.javafx.scene.control.TableColumnSortTypeWrapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableView;

public class FileViewUtils {

    public static void sortColumn(final boolean addColumn, TableColumnBase<?,?> tableColumnBase, Control tableControl) {
        // we only allow sorting on the leaf columns and columns
        // that actually have comparators defined, and are sortable
        if (tableColumnBase == null || tableColumnBase.getColumns().size() != 0 || tableColumnBase.getComparator() == null || !tableColumnBase.isSortable()) return;
//        final int sortPos = getTable().getSortOrder().indexOf(column);
//        final boolean isSortColumn = sortPos != -1;

        final ObservableList<TableColumnBase<?,?>> sortOrder = getSortOrder(tableControl);
        boolean isSortColumn = sortOrder.contains(tableColumnBase);
        // addColumn is true e.g. when the user is holding down Shift
        if (addColumn) {
            if (!isSortColumn) {
                setSortType(tableColumnBase, TableColumn.SortType.ASCENDING);
                sortOrder.add(tableColumnBase);
            } else if (TableColumnSortTypeWrapper.isAscending(tableColumnBase)) {
                setSortType(tableColumnBase, TableColumn.SortType.DESCENDING);
            } else {
                int i = sortOrder.indexOf(tableColumnBase);
                if (i != -1) {
                    sortOrder.remove(i);
                }
            }
        } else {
            // the user has clicked on a column header - we should add this to
            // the TableView sortOrder list if it isn't already there.
            if (isSortColumn && sortOrder.size() == 1) {
                // the column is already being sorted, and it's the only column.
                // We therefore move through the 2nd or 3rd states:
                //   1st click: sort ascending
                //   2nd click: sort descending
                //   3rd click: natural sorting (sorting is switched off)
                if (isAscending(tableColumnBase)) {
                    setSortType(tableColumnBase, TableColumn.SortType.DESCENDING);
                } else {
                    // remove from sort
                    sortOrder.remove(tableColumnBase);
                }
            } else if (isSortColumn) {
                // the column is already being used to sort, so we toggle its
                // sortAscending property, and also make the column become the
                // primary sort column
                if (isAscending(tableColumnBase)) {
                    setSortType(tableColumnBase, TableColumn.SortType.DESCENDING);
                } else if (isDescending(tableColumnBase)) {
                    setSortType(tableColumnBase, TableColumn.SortType.ASCENDING);
                }

                // to prevent multiple sorts, we make a copy of the sort order
                // list, moving the column value from the current position to
                // its new position at the front of the list
                List<TableColumnBase<?,?>> sortOrderCopy = new ArrayList<>(sortOrder);
                sortOrderCopy.remove(tableColumnBase);
                sortOrderCopy.add(0, tableColumnBase);
                sortOrder.setAll(tableColumnBase);
            } else {
                // add to the sort order, in ascending form
                setSortType(tableColumnBase, TableColumn.SortType.ASCENDING);
                sortOrder.setAll(tableColumnBase);
            }
        }
    }
    
    public static ObservableList<TableColumnBase<?,?>> getSortOrder(Object control) {
        if (control instanceof TableView) {
        	//((TableView<S>)control).getSortOrder();
            return ((TableView)control).getSortOrder();
        } else if (control instanceof TreeTableView) {
            return ((TreeTableView)control).getSortOrder();
        }
        return FXCollections.observableArrayList();
    }
    
    public static <T> void setCheckIndices(CheckListView<T> checkListView, Stream<T> listStream) {
    	//There is a bug when setting check to items that their indices are not sorted upward.
    	checkListView.getCheckModel().checkIndices(
    			listStream.map((item) -> checkListView.getCheckModel().getItemIndex(item))
				.mapToInt(i -> i)
				.sorted()
				.toArray());
    }
}
