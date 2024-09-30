package JavaFXInterface.controlsfx;

import java.util.ArrayList;
import java.util.List;

import org.controlsfx.control.tableview2.FilteredTableColumn;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ListPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class BetterFilteredTableColumn<S, T> extends FilteredTableColumn<S, T> {

	private ObservableList<T> columnValuesList;
	
	
	public BetterFilteredTableColumn() {
		super();
		// TODO Auto-generated constructor stub
		init();
	}

	public BetterFilteredTableColumn(String text) {
		super(text);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public ListPropertyBase<S> columnValueProperty = new SimpleListProperty<>();
	
    private final List<ListChangeListener<S>> tableValuesChangeListener
    	= new ArrayList<>();
	
	private void init() {
		columnValueProperty.addListener((ListChangeListener.Change<? extends S> change) -> {
			//prop.set(null)
			System.out.println("Blame You");
		    for (ListChangeListener<S> listener : tableValuesChangeListener) {
		        //listener.
		    	listener.onChanged(change);
		    }
		});
		/*columnValueProperty.addListener((observable, oldValue, newValue) -> {
			//prop.set(null)
			System.out.println("Blame You");
			System.out.println(newValue);
		    for (ChangeListener<? super S> listener : tableValuesChangeListener) {
		        //listener.
		    	listener.changed(observable, oldValue, newValue);
		    }
		});*/
		/*columnValueProperty.addListener((observable) -> {
			System.out.println("Blame");
			System.out.println(observable);
		});*/
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
