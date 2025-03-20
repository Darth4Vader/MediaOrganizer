package JavaFXInterface.controlsfx;

import java.util.List;

import org.controlsfx.control.GridView;

import impl.org.controlsfx.skin.GridRow;
import impl.org.controlsfx.skin.GridRowSkin;
import impl.org.controlsfx.skin.GridViewSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.Cell;
import javafx.scene.control.FocusModel;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.TableView.TableViewFocusModel;
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
		setFocusModel(new GridViewFocusModel<T>(this));
    }
    
    @Override
	public GridViewSkin<T> createDefaultSkin() {
		return new BetterGridViewSkin<T>(this);
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
	
	
    /** {@inheritDoc} */
    @Override
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
    	switch (attribute) {
            case FOCUS_ITEM: {
                Node row = (Node)super.queryAccessibleAttribute(attribute, parameters);
                if (row == null) return null;
                Node cell = (Node)row.queryAccessibleAttribute(attribute, parameters);
                /* cell equals to null means the row is a placeholder node */
                return cell != null ?  cell : row;
            }
            default: return super.queryAccessibleAttribute(attribute, parameters);
        }
    }

    // --- Focus Model
    //private ObjectProperty<FocusModel<T>> focusModel;

    /**
     * Sets the {@link FocusModel} to be used in the ListView.
     * @param value the FocusModel to be used in the ListView
     */
    /*public final void setFocusModel(FocusModel<T> value) {
        focusModelProperty().set(value);
    }*/

    /**
     * Returns the currently installed {@link FocusModel}.
     * @return the currently installed FocusModel
     */
    /*public final FocusModel<T> getFocusModel() {
        return focusModel == null ? null : focusModel.get();
    }*/

    /**
     * The FocusModel provides the API through which it is possible
     * to both get and set the focus on a single item within a ListView. Note
     * that it has a generic type that must match the type of the ListView itself.
     * @return the FocusModel property
     */
    /*public final ObjectProperty<FocusModel<T>> focusModelProperty() {
        if (focusModel == null) {
            focusModel = new SimpleObjectProperty<>(this, "focusModel");
        }
        return focusModel;
    }*/
    
    // --- Focus Model
    private ObjectProperty<GridViewFocusModel<T>> focusModel;
    public final void setFocusModel(GridViewFocusModel<T> value) {
        focusModelProperty().set(value);
    }
    public final GridViewFocusModel<T> getFocusModel() {
        return focusModel == null ? null : focusModel.get();
    }
    /**
     * Represents the currently-installed {@link TableViewFocusModel} for this
     * TableView. Under almost all circumstances leaving this as the default
     * focus model will suffice.
     * @return focusModel property
     */
    public final ObjectProperty<GridViewFocusModel<T>> focusModelProperty() {
        if (focusModel == null) {
            focusModel = new SimpleObjectProperty<>(this, "focusModel");
        }
        return focusModel;
    }
	
	
	
    public static class GridViewFocusModel<T> extends FocusModel<T> {
    	
    	private final GridView<T> gridView;
    	
    	public GridViewFocusModel(GridView<T> gridView) {
    		this.gridView = gridView;
            focusedIndexProperty().addListener(o -> {
            	System.out.println("changed22");
            	
            	GridRow<T> row = (GridRow<T>) gridView.queryAccessibleAttribute(AccessibleAttribute.FOCUS_ITEM);
            	GridRowSkin<?> skin = (GridRowSkin<?>) row.getSkin();
            	System.out.println("Hell " + skin.getCellAtIndex(0).getClass());
            	skin.getCellAtIndex(0).requestFocus();
            	System.out.println(gridView.getScene().getFocusOwner());
            });
    	}
    	
        /**
         * The position of the current item in the TableView which has the focus.
         */
    	/*
        private ReadOnlyObjectWrapper<TablePosition> focusedCell;
        public final ReadOnlyObjectProperty<TablePosition> focusedCellProperty() {
            return focusedCellPropertyImpl().getReadOnlyProperty();
        }
        private void setFocusedCell(TablePosition value) { focusedCellPropertyImpl().set(value);  }
        public final TablePosition getFocusedCell() { return focusedCell == null ? EMPTY_CELL : focusedCell.get(); }

        private ReadOnlyObjectWrapper<TablePosition> focusedCellPropertyImpl() {
            if (focusedCell == null) {
                focusedCell = new ReadOnlyObjectWrapper<>(EMPTY_CELL) {
                    private TablePosition old;
                    @Override protected void invalidated() {
                        if (get() == null) return;

                        if (old == null || !old.equals(get())) {
                            setFocusedIndex(get().getRow());
                            setFocusedItem(getModelItem(getValue().getRow()));

                            old = get();
                        }
                    }

                    @Override
                    public Object getBean() {
                        return TableViewFocusModel.this;
                    }

                    @Override
                    public String getName() {
                        return "focusedCell";
                    }
                };
            }
            return focusedCell;
        }
        */

		@Override
		protected int getItemCount() {
            if (gridView.getItems() == null) return -1;
            return gridView.getItems().size();
		}

		@Override
		protected T getModelItem(int index) {
            if (gridView.getItems() == null) return null;

            if (index < 0 || index >= getItemCount()) return null;

            return gridView.getItems().get(index);
		}
		
	    /**
	     * Causes the item at the given index to receive the focus.
	     *
	     * @param row The row index of the item to give focus to.
	     * @param column The column of the item to give focus to. Can be null.
	     */
	    /*public void focus(int row, int column) {
            if (row < 0 || row >= getItemCount()) {
                //setFocusedCell(EMPTY_CELL);
            } else {
                TablePosition<S,?> oldFocusCell = getFocusedCell();
                TablePosition<S,?> newFocusCell = new TablePosition<>(tableView, row, column);
                setFocusedCell(newFocusCell);

                if (newFocusCell.equals(oldFocusCell)) {
                    // manually update the focus properties to ensure consistency
                    setFocusedIndex(row);
                    setFocusedItem(getModelItem(row));
                }
            }
	    }*/

	    /**
	     * Tests whether the row / cell at the given location currently has the
	     * focus within the UI control.
	     * @param row the row
	     * @param column the column
	     * @return true if the row / cell at the given location currently has the
	     * focus within the UI control
	     */
	    /*public boolean isFocused(int row, int column) {
	    	return false;
	    }*/

	    /**
	     * Attempts to move focus to the cell above the currently focused cell.
	     */
	    public void focusAboveCell() {
            int focusedIndex = getFocusedIndex();
			if (focusedIndex == -1) {
				// focus the last item
				focus(getItemCount() - 1);
			} else if (focusedIndex > 0) {
				focus(focusedIndex - 1);
			}  	
	    }

	    /**
	     * Attempts to move focus to the cell below the currently focused cell.
	     */
	    public void focusBelowCell() {
            //TablePosition cell = getFocusedCell();
            if (getFocusedIndex() == -1) {
                focus(0);
            } else if (getFocusedIndex() != getItemCount() -1) {
                focus(getFocusedIndex() + 1);
            }	    	
	    }

	    /**
	     * Attempts to move focus to the cell to the left of the currently focused cell.
	     */
	    public void focusLeftCell() {
            /*TablePosition cell = getFocusedCell();
            if (cell.getColumn() <= 0) return;
            focus(cell.getRow(), getTableColumn(cell.getTableColumn(), -1));*/
	    }

	    /**
	     * Attempts to move focus to the cell to the right of the the currently focused cell.
	     */
	    public void focusRightCell() {
            /*TablePosition cell = getFocusedCell();
            if (cell.getColumn() == getColumnCount() - 1) return;
            focus(cell.getRow(), getTableColumn(cell.getTableColumn(), 1));*/
	    }
    	
    }
}