package JavaFXInterface.controlsfx;

import java.util.ArrayList;
import java.util.List;

import org.controlsfx.control.tableview2.FilteredTableColumn;
import org.controlsfx.control.tableview2.TableColumn2;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ListPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

/**
 * 
 * Set the filter Button for a given btn:
 * 
 * <pre>
 * {@code
 * btn.onActionProperty().bind(onFilterActionProperty());
 * btn.disableProperty().bind(filterableProperty().not());
 * }
 * </pre>
 * 
 * Alternatively, a filter can also be other types of Node, like Label lbl, 
 * with a {@link MouseEvent} activating it:
 * 
 * <pre>
 * {@code
 * lbl.setOnMouseClicked((e) -> {
 * 	if(e.getButton() == MouseButton.SECONDARY) {
 * 		otherCols.getOnFilterAction().handle(new ActionEvent(e.getSource(), e.getTarget()));
 * 	}
 * });
 * </pre>
 * 
 * 
 * @param <S>
 * @param <T>
 */
public class BetterFilteredTableColumn<S, T> extends FilteredTableColumn<S, T> {
	
	public ListPropertyBase<S> columnValueProperty = new SimpleListProperty<>();
	
	//private Button btn;
	
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
		/*Node graphics = getGraphic();
		if(graphics instanceof Button)
			btn = (Button) graphics;*/
		
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
	
	public void setOpenFilter(Button filterBtn) {
		/*ObjectProperty<EventHandler <? super MouseEvent>> e;
		new ActionEvent().co
		btn.onActionProperty().bind(Bindings.createObjectBinding((e) -> {
			EventHandler <? super MouseEvent> h = e.get();
			//return new ActionEvent(h., 0, getId(), 0, 0)
		}, e));
		filterBtn.onMouseClickedProperty().bind(btn.onActionProperty());
		filterBtn.onActionProperty().bind(btn.onActionProperty());
		filterBtn.disableProperty().bind(btn.disableProperty());*/
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
