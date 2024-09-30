package JavaFXInterface.controlsfx;

import java.util.List;
import java.util.concurrent.Callable;

import org.controlsfx.control.CheckListView;
import org.controlsfx.control.tableview2.FilteredTableView;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.TableView;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public class FilteredTableColumnCheckView<S, T> extends CheckListView<T> {
	
	private BetterFilteredTableColumn<S, T> tableColumn;

	public FilteredTableColumnCheckView(BetterFilteredTableColumn<S, T> tableColumn) {
		// TODO Auto-generated constructor stub
		this.tableColumn = tableColumn;
		init();
	}

	public FilteredTableColumnCheckView(BetterFilteredTableColumn<S, T> tableColumn, ObservableList<T> items) {
		super(items);
		// TODO Auto-generated constructor stub
		this.tableColumn = tableColumn;
		init();
	}
	
	private void init() {
		getCheckModel().getCheckedItems().addListener(new ListChangeListener<T>() {
            @Override
            public void onChanged(javafx.collections.ListChangeListener.Change<? extends T> c) {
            	if(c.next()) {
                    TableView<S> tableView = tableColumn.getTableView();
                    if(tableView instanceof FilteredTableView<S>)
                    	((FilteredTableView<S>) tableView).filter();
                }
            }
        });
		
		
		
		ChangeListener<? super Boolean> chan22 = (obs, oldV, newV) -> {
			if(newV == true) {
				doB();
			}
			else {
				
			}
		};
		ChangeListener<? super Window> chan = (observable, oldValue, newValue) -> {
			if(newValue != null) {
				newValue.showingProperty().addListener(chan22);
			}
			else if(oldValue != null) {
				oldValue.showingProperty().removeListener(chan22);
			}
		};
		
		this.sceneProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue != null) {
				if(newValue.getWindow() != null) {
					newValue.getWindow().showingProperty().addListener(chan22);
				}
				newValue.windowProperty().addListener(chan);
			}
			else if(oldValue != null) {
				oldValue.windowProperty().removeListener(chan);
				if(oldValue.getWindow() != null) {
					oldValue.getWindow().showingProperty().removeListener(chan22);
				}
			}
		});
	}
	
	private void init2() {
		this.visibleProperty().addListener((observable) -> {
			System.out.println("Nice: " + observable);
		});
		
		this.visibleProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("Not0: " + observable);
		});
		
		this.needsLayoutProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("Not1: " + observable);
		});
		
		//this.sceneProperty().get().
		
		this.parentProperty().addListener((observable) -> {
			System.out.println("Nice: " + observable);
		});
		
		this.parentProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("Not2: " + observable);
		});
		
		System.out.println(this.isDisable() + " = " + this.isDisabled());
		
		BooleanProperty bool = new SimpleBooleanProperty();
		
		bool.addListener((observable, oldValue, newValue) -> {
			System.out.println("Ganga");
			if(newValue) {
				doB();
			}
		});
		
		ChangeListener<? super EventHandler<WindowEvent>> chan22 = (observable, oldValue, newValue) -> {
			System.out.println("Ganga");
			/*if(newValue) {
				doB();
			}*/
		};
		
		ChangeListener<? super Window> chan = (observable, oldValue, newValue) -> {
			System.out.println("Model: " + observable);
			if(newValue != null) {
				newValue.onShowingProperty().addListener(chan22);//.addListener(bool);
				//bool.bind(newValue.onShowingProperty());
			}
			else if(oldValue != null) {
				oldValue.onShowingProperty().removeListener(chan22);
			}
		};
		
		this.sceneProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("Nice45: " + observable);
			if(newValue != null) {
				if(newValue.getWindow() != null) {
					System.out.println("Under");
					System.out.println(newValue.getWindow().isShowing());
					//newValue.getWindow().onShownProperty().addListener(chan22);
					//newValue.getWindow().onShowingProperty().addListener(chan22);
					newValue.getWindow().showingProperty().addListener((obs, oldV, newV) -> {
						System.out.println("Blows");
						//System.out.println(c);
						if(newV == true) {
							doB();
						}
					});
				}
				newValue.windowProperty().addListener(chan);
				System.out.println(newValue.getWindow().showingProperty());
			}
			else if(oldValue != null) {
				oldValue.windowProperty().removeListener(chan);
			}
		});
		
		this.disableProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("Nice6: " + observable);
		});
		
		this.disabledProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("Nice7: " + observable);
		});
		//this.sceneProperty().get().windowProperty().get().
		
		Bindings.createBooleanBinding(() -> {
			Parent parent = this.parentProperty().getValue();
			return parent != null;
		}, this.parentProperty()).addListener((observable, oldValue, newValue) -> {
			System.out.println("Not3: " + observable);
		});
		
		//this.itemsProperty().bind
	}
	
	private boolean first = true;
	
	private void doB() {
		if(first) {
			this.getItems().setAll(tableColumn.getAllDistinctValues());
			
			this.tableColumn.addTableValuesChangeListener((Change<? extends S> change) -> {
				System.out.println("Dave");
				while(change.next()) {
					System.out.println("Dave " + change.getAddedSubList());
					List<T> list = tableColumn.getAllDistinctValues();
					
					ObservableList<T> checked = getCheckModel().getCheckedItems();
					
					getItems().setAll(list);
					
        			getCheckModel().checkIndices(checked
        					.stream()
        					.map((str) -> {
                				return getCheckModel().getItemIndex(str);
        	    			})
        					.filter(i -> i >= 0)
        	    			.mapToInt(i -> i)
        	    			.sorted()
        	    			.toArray());
        			
					
					//System.out.println();
					
					
					
					//FXCollections.observableArrayList()
					
					//this.getItems().stream().filter(null)
					/*for(T item : list) {
						if(!this.getItems().contains(item)) {
							this.getItems().add(item);
						}
					}*/
					
					
					/*if(change.wasAdded()) {
						ObservableList<? extends S> list = (ObservableList<? extends S>) change.getAddedSubList();
						for(S item : list) {
							
						}
					}
					
					ObservableList<? extends S> list = (ObservableList<? extends S>) change.getList();
					*/
				}
			});
			
			//sthis.getItems().addListener(new Listener(tableColumn.columnValueProperty));
		}
		first = false;
	}

}