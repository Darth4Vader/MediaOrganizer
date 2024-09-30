package JavaFXInterface.controlsfx;

import java.util.ArrayList;
import java.util.List;

import org.controlsfx.control.tableview2.FilteredTableColumn;

import javafx.beans.property.ListPropertyBase;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.control.TableView;

public class BetterFilteredTableColumn<S, T> extends FilteredTableColumn<S, T> {
	
	public ListPropertyBase<S> columnValueProperty = new SimpleListProperty<>();
	
    private final List<ListChangeListener<S>> tableValuesChangeListener
    	= new ArrayList<>();
	
	public BetterFilteredTableColumn() {
		super();
		initialize();
	}

	public BetterFilteredTableColumn(String text) {
		super(text);
		initialize();
	}
	
	private void initialize() {
		columnValueProperty.addListener((ListChangeListener.Change<? extends S> change) -> {
		    for (ListChangeListener<S> listener : tableValuesChangeListener) {
		    	listener.onChanged(change);
		    }
		});
		tableViewProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue != null) {
				BetterFilteredTableView<S> table = getFilterTable();
				if(table != null) {
					columnValueProperty.bind(table.backingListProperty());
				}
			}
		});
	}

	public void addTableValuesChangeListener(ListChangeListener<S> listener) {
		tableValuesChangeListener.add(listener);
	}
	
	public void removeTableValuesChangeListener(ListChangeListener<S> listener) {
		tableValuesChangeListener.remove(listener);
	}
	
	public void setColumnValue() {
		
	}
	
	private BetterFilteredTableView<S> getFilterTable() {
		TableView<S> tableView = getTableView();
		return tableView instanceof BetterFilteredTableView<S>
			? (BetterFilteredTableView<S>) tableView
			: null;
	}
	
	public List<T> getAllDistinctValues() {
		TableView<S> tableView = getTableView();
		return !(tableView instanceof BetterFilteredTableView<S>)
			? null
			: getAllDistinctValuesFromList(((BetterFilteredTableView<S>) tableView).getBackingList());
	}
	
	public List<T> getAllDistinctFilteredValues() {
		TableView<S> tableView = getTableView();
		return !(tableView instanceof BetterFilteredTableView<S>)
			? null
			: getAllDistinctValuesFromList(((BetterFilteredTableView<S>) tableView).getItems());
	}
	
	private List<T> getAllDistinctValuesFromList(List<S> list) {
		return list.stream()
				.map(f -> this.getCellData(f))
		        .distinct()
		        .toList();
	}
}
