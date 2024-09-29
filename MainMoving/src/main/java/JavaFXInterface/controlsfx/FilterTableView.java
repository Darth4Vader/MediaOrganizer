package JavaFXInterface.controlsfx;

import java.util.function.Predicate;

import org.controlsfx.control.tableview2.FilteredTableColumn;
import org.controlsfx.control.tableview2.FilteredTableView;
import org.controlsfx.control.tableview2.event.FilterEvent;

import impl.org.controlsfx.tableview2.FilteredColumnPredicate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class FilterTableView<S, T> extends TableView<S> {

	private FilteredList<S> filterList;
	
    /**
     * This property allows defining a predicate for the column. 
     * This predicate can be nullified when the table's predicate is reset,
     * so it is convenient that this property can be set again dynamically via
     * the UI option {@link #southNode}.
     */
    private final ObjectProperty<Predicate<? super T>> predicate = new SimpleObjectProperty<Predicate<? super T>>(this, "predicate", null) {
        @Override
        protected void invalidated() {
            // Auto filter table based on column's predicate changes
            getFilteredTableView().ifPresent(FilteredTableView::filter);
        }
    };
    
    /**
     * The filter method forces the TableView to re-run its filtering algorithm. More
     * often than not it is not necessary to call this method directly, as it is
     * automatically called when the 
     * {@link #filterPolicyProperty() filter policy}, or the state of the
     * FilteredTableColumn {@link FilteredTableColumn#predicateProperty() filter predicate}
     * changes. In other words, this method should only be called directly when
     * something external changes and a filter is required.
     */
    public void filter() {
        
        Predicate<S> oldPredicate = getPredicate();
        
        final boolean filterExists = getVisibleLeafColumns().stream()
                .filter(FilteredTableColumn.class::isInstance)
                .map(FilteredTableColumn.class::cast)
                .filter(FilteredTableColumn::isFilterable)
                .noneMatch(f -> f.getPredicate() != null);
        
        // update the Predicate property
        setPredicate(filterExists ? null : new FilteredColumnPredicate(getVisibleLeafColumns()));

        // fire the onFilter event and check if it is consumed, if so, don't run the filtering
        FilterEvent<TableView<S>> filterEvent = new FilterEvent<>(FilteredTableView.this, FilteredTableView.this);
        fireEvent(filterEvent);
        if (filterEvent.isConsumed()) {
            setPredicate(oldPredicate);
            return;
        }
        // get the filter policy and run it
        Callback<TableView<S>, Boolean> filterPolicy = getFilterPolicy();
        if (filterPolicy == null) {
            return;
        }
        boolean success = filterPolicy.call(this);
        if (! success) {
            setPredicate(oldPredicate);
        }

    }
	
    public final void setPredicate(Predicate<? super T> value) { predicate.set(value); }
    public final Predicate<? super T> getPredicate() { return predicate.get(); }
    public final ObservableValue<Predicate<? super T>> predicateProperty() { return predicate; }
	
	public FilterTableView() {
		// TODO Auto-generated constructor stub
		itemsProperty().addListener(null);
	}
	
	@Override
	public void setItems(ObservableList<S> list) {
		super.setItems(list);
	}
	
}