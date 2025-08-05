package JavaFXInterface.utils.controlsfx;

import java.lang.ref.WeakReference;
import java.util.Comparator;
import java.util.function.Predicate;

import org.controlsfx.control.tableview2.FilteredTableColumn;
import org.controlsfx.control.tableview2.FilteredTableView;
import org.controlsfx.control.tableview2.event.FilterEvent;

import impl.org.controlsfx.tableview2.FilteredColumnPredicate;
import impl.org.controlsfx.tableview2.TableRow2;
import impl.org.controlsfx.tableview2.TableView2Skin;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class BetterFilteredTableView<S> extends FilteredTableView<S> {
	
	
	public BetterFilteredTableView() {
		FilteredList<String> list;
		
		this.getColumns().addListener((ListChangeListener.Change<? extends TableColumn<S, ?>> change) -> {
			while(change.next()) {
				
			}
		});
		
		backingListProperty().addListener((ListChangeListener.Change<? extends S> change) -> {
			while(change.next()) {
				ObservableList<TableColumn<S, ?>> columns = getColumns();
				for(TableColumn<S, ?> column : columns) {
					if(column instanceof BetterFilteredTableColumn) {
						BetterFilteredTableColumn<S, ?> filterColumn = (BetterFilteredTableColumn<S, ?>) column;
						//filterColumn
					}
				}
				//System.out.println("YOUUUUUUUUUUUUUUUUUUU");
			}
		});
	}
	
	public BetterFilteredTableView(ObservableList<S> items) {
		super(items);
		// TODO Auto-generated constructor stub
	}
	
	private ChangeListener<Callback<TableView<S>, TableRow<S>>> rowFactoryListener;
	
	/**
	 * There is a glitch with {@link TableView2Skin} that does not allow to set row factory.
	 * <br>
	 * This function allows to workaround the problem, use row factory that creates a {@link TableRow2} for better performance. 
	 * <br>
	 * A link to the workaround is attached.
	 * @see <a href="https://groups.google.com/g/controlsfx-dev/c/-huuXC9Nflc/m/O-qCnGJoAQAJ">https://groups.google.com/g/controlsfx-dev/c/-huuXC9Nflc/m/O-qCnGJoAQAJ</a>
	 * @param rowFactory the factory, preferred to return {@link TableRow2} 
	 */
	public void setRealRowFactory(Callback<TableView<S>, TableRow<S>> rowFactory) {
		if(rowFactoryListener != null)
			rowFactoryProperty().removeListener(rowFactoryListener);
		rowFactoryListener = new ChangeListener<Callback<TableView<S>, TableRow<S>>>() {
            @Override
            public void changed(ObservableValue<? extends Callback<TableView<S>, TableRow<S>>> observable, Callback<TableView<S>, TableRow<S>> oldValue, Callback<TableView<S>, TableRow<S>> newValue) {
                rowFactoryProperty().unbind();
                if (rowFactoryProperty().get() != rowFactory) {
                    setRowFactory(rowFactory);
                }

            }
        };
        rowFactoryProperty().addListener(rowFactoryListener);
	}
	
	
	
	/*
    private ObjectProperty<ObservableList<S>> backingList =
            new SimpleObjectProperty<>(this, "backingList") {
                WeakReference<ObservableList<S>> oldItemsRef;

                @Override protected void invalidated() {
                    final ObservableList<S> oldItems = oldItemsRef == null ? null : oldItemsRef.get();
                    final ObservableList<S> newItems = getItems();

                    // Fix for RT-36425
                    if (newItems != null && newItems == oldItems) {
                        return;
                    }

                    // Fix for RT-35763
                    if (! (newItems instanceof SortedList)) {
                        getSortOrder().clear();
                    }

                    oldItemsRef = new WeakReference<>(newItems);
                }
            };
            */
	
    private ReadOnlyListWrapper<S> backingList;
    /**
     * Returns the original observable list, before it is wrapped into a 
     * {@link FilteredList} and a {@link SortedList}.
     *
     * @return The original {@link ObservableList}
     */
    public final ObservableList<S> getBackingList() {
    	return backingList == null ? null : backingList.get(); 
    }
	@Override
    public final void setBackingList(ObservableList<S> backingList) {
        super.setBackingList(backingList);
        backingListPropertyImpl().set(backingList);
	}
    public final ReadOnlyListProperty<S> backingListProperty() {
    	return backingListPropertyImpl().getReadOnlyProperty();
    }
    private ReadOnlyListWrapper<S> backingListPropertyImpl() {
        if (backingList == null) {
        	backingList = new ReadOnlyListWrapper<>(this, "backingList");
        }
        return backingList;
    }
}