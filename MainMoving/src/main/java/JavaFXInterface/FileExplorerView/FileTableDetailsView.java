package JavaFXInterface.FileExplorerView;

import static com.sun.javafx.scene.control.TableColumnSortTypeWrapper.isAscending;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.apache.tika.exception.TikaException;
import org.controlsfx.control.CheckListView;
import org.controlsfx.control.tableview2.FilteredTableColumn;
import org.controlsfx.control.tableview2.FilteredTableView;
import org.xml.sax.SAXException;

import JavaFXInterface.AppUtils;
import JavaFXInterface.FileExplorer.FileExplorer;
import JavaFXInterface.FileExplorer.SideFilesList.ExpandPanel;
import JavaFXInterface.utils.JavaFXImageUtils;
import JavaFXInterface.utils.controlsfx.BetterFilteredTableColumn;
import JavaFXInterface.utils.controlsfx.BetterFilteredTableView;
import JavaFXInterface.utils.controlsfx.FilteredTableColumnCheckView;
import JavaFXInterface.utils.controlsfx.TableViewUtils;
import OtherUtilities.ImageUtils;
import Utils.DirectoryWatcher.FileChange;
import Utils.DirectoryWatcher.FileChange.FileChaneType;
import Utils.FileUtils.FileAttributesType;
import Utils.FileUtils.FileDetails;
import impl.org.controlsfx.tableview2.TableRow2;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableView;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class FileTableDetailsView extends BetterFilteredTableView<FileDetails> implements FileViewMode<FileDetails> {
	
	private final ObservableList<String> fixedColumns;
	private MainFileExplorerView mainFileExplorerView;
	
	private FileTableDetailsManager fileTableDetailsManager;
	
    public FileTableDetailsView(MainFileExplorerView mainFileExplorerView) {
    	this.mainFileExplorerView = mainFileExplorerView;
    	this.fileTableDetailsManager = new FileTableDetailsManager(this);
    	this.fixedColumns = FXCollections.observableArrayList(
    			Arrays.asList(FileAttributesType.NAME, FileAttributesType.TYPE).stream().map((s) -> s.getName()).collect(Collectors.toList()));
    	
    	
		this.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
			System.out.println("Hello: " + e);
			processArrowKeys(e, this);
		});
		
		this.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
			System.out.println("Hello: " + e);
			if(e.getCode() == KeyCode.ENTER) {
				e.consume();
				FileDetails rowData = this.getFocusModel().getFocusedItem();
				if (rowData != null) {
					enterFolder(rowData);
				}
			}
		});
    	
		this.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				mainFileExplorerView.getFileExplorer().resetCurrentFileFocused();
			}
			else if(!oldValue) {
				FileExplorer fileExplorer = mainFileExplorerView.getFileExplorer();
				FileDetails rowData = this.getFocusModel().getFocusedItem();
				if (rowData != null) {
					File file = rowData.getFile();
					fileExplorer.setCurrentFileFocused(file);
				}
			}
		});
    	
		this.setRealRowFactory((fileView) -> {
			TableRow2<FileDetails> row = new TableRow2<>(this);
			row.setOnMouseClicked((e) -> {
				if (e.getClickCount() == 2 && !row.isEmpty()) {
					FileDetails rowData = row.getItem();
					enterFolder(rowData);
				}
			});
			row.focusedProperty().addListener((observable, oldValue, newValue) -> {
				FileExplorer fileExplorer = mainFileExplorerView.getFileExplorer();
				if (newValue) {
					FileDetails rowData = row.getItem();
					if(rowData != null) {
						File file = rowData.getFile();
						fileExplorer.setCurrentFileFocused(file);
					}
				}
			});
			return row;
		});
    	
    	this.fixedColumns.stream().forEach((s) -> addColumn(s));
    	this.setEditable(false);
    	
    	addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getTarget() instanceof TableColumnHeader) {
                event.consume();

                /*
                
                // Search for the clicked TableColumn:
                final TableColumnHeader columHead = (TableColumnHeader) event.getTarget();
                //columHead.setVisible(false);
                columHead.getTableColumn().setSortable(false);
                
                */
            }

        });
    	
    	//getStylesheets().add("tableNoSorting.css");
    }
    
    private void enterFolder(FileDetails fileDetails) {
		File file = fileDetails.getFile();
		mainFileExplorerView.getFileExplorer().enterFolder(file);
    }
    
    private <S> void processArrowKeys(KeyEvent event, TableView<S> tableView) {
    	System.out.println("Key Pressed " + event);
        if (event.getCode().isArrowKey()) {
            event.consume();

            TableViewFocusModel<S> model = tableView.getFocusModel();
            TableViewSelectionModel<S> model2 = tableView.getSelectionModel();
            switch (event.getCode()) {
                case UP:
                    //model.focusAboveCell();
                    model2.selectAboveCell();
                    break;
                case RIGHT:
                    //model.focusRightCell();
                	model2.selectRightCell();
                    break;
                case DOWN:
                    //model.focusBelowCell();
                    model2.selectBelowCell();
                    break;
                case LEFT:
                    //model.focusLeftCell();
                	model2.selectLeftCell();
                    break;
                default:
                    throw new AssertionError(event.getCode().name());
            }
            TableViewUtils.betterScrollTo(this, model2.getSelectedIndex());
        }
    }
        
    public FileTableDetailsView(MainFileExplorerView mainFileExplorerView, File file) {
    	this(mainFileExplorerView);
    	fileTableDetailsManager.setFiles(Arrays.asList(file.listFiles()));
    }
    
    private boolean doesColumnExists(String name) {
    	return this.getColumns().stream().anyMatch((c) -> c.getUserData().equals(name));
    }
    
    private boolean canRemoveColumn(TableColumn<FileDetails, ?> column, Collection<String> nameList) {
    	Object name = column.getUserData();
    	return !fixedColumns.contains(name) && !nameList.contains(name);
    }
    
    public void addColumn(String name) {
    	if(doesColumnExists(name) || name == null)
    		return;
    	FilteredTableColumn<FileDetails, ?> column;
    	
    	//Label filterButton = new Label("|");
    	
    	final Image SIDE_ARROW = JavaFXImageUtils.getImageResource(ExpandPanel.class, "images/side_arrow.png");
        /*Label arrow = new Label("^");// ▲^ new Label("›");
        arrow.setBorder(Border.stroke(Color.BLUEVIOLET));*/
    	ImageView filterImage = new ImageView();
    	filterImage.setImage(SIDE_ARROW);
    	filterImage.setPreserveRatio(true);
    	filterImage.setRotate(90.0F);
    	
    	filterImage.setFitWidth(10);
    	filterImage.setFitHeight(10);
    	
    	BorderPane filterButton = new BorderPane();
    	
    	/*filterImage.fitHeightProperty().bind(filterButton.heightProperty().multiply(0.5));
    	filterImage.fitWidthProperty().bind(filterButton.widthProperty().multiply(0.8));*/
    	
    	BorderPane.setMargin(filterImage, new Insets(15, 5, 15, 5));
    	
    	filterButton.setCenter(filterImage);
    	//filterButton.setGraphic(filterImage);
    	
    	//arrow.fitHeightProperty().bind(namePane.heightProperty().multiply(0.4));
    	//arrow.fitWidthProperty().bind(namePane.widthProperty());
        //arrow.setVisible(false);
        
        
    	//Button bton = new Button("|");
    	if(name.equals(FileAttributesType.NAME.getName())) {
    		FilteredTableColumn<FileDetails, FileDetails> nameCol = new FilteredTableColumn<>();
    		nameCol.setCellValueFactory((file) ->  new SimpleObjectProperty<FileDetails>(file.getValue()));
    		nameCol.setCellFactory((f) -> new TableCell<>() {
    			
    	    	private final ImageView imageView = new ImageView();
    	    	{
		            imageView.setFitWidth(50);
		            imageView.setFitHeight(50);
		            
		            
		            
		            
		            /*
		            imageView.addEventFilter(KeyEvent.KEY_PRESSED, this::processArrowKeys);
		            this.focusedProperty().addListener((observable, wasFocused, isFocused) -> {
		                if (isFocused) {
		                    getTableView().getFocusModel().focus(getIndex(), getTableColumn());
		                }
		            });
		            
		            tableViewProperty().addListener((obs, oldTable, newTable) ->
	                newTable.getFocusModel().focusedCellProperty().addListener((obs2, oldPos, newPos) -> {
	                    if (getIndex() == newPos.getRow() && getTableColumn() == newPos.getTableColumn()) {
	                        imageView.requestFocus();
	                    }
	                })
	        );
		            */
    	    	}
    	    	
    		    @Override
    		    public void updateItem(FileDetails item, boolean empty) {
    		        super.updateItem(item, empty);
    	            if (item == null || empty) {
    		            setText(null);
    		            setGraphic(null);
    		            imageView.setImage(null);
    		        } else {
    		        	setText(item.getValue(name));
    		            //load image in another thread, in order to make the images load in the background
    		        	Thread machineryThread = new Thread(() -> {
    		                Platform.runLater(() -> imageView.setImage(AppUtils.getImageOfFile(item.getFile())));
    		            });
    		            machineryThread.start();
    		            setGraphic(imageView);
    		        }
    		    }
    		});
    		column = nameCol;
    	}
    	else {
    		BetterFilteredTableColumn<FileDetails, String> otherCols = new BetterFilteredTableColumn<>();
    		otherCols.setCellValueFactory((file) -> new SimpleStringProperty(file.getValue().getValue(name)));
    		column = otherCols;
    		otherCols.setFilterable(true);
    		FilteredTableColumnCheckView<FileDetails, String> otherColCheck = new FilteredTableColumnCheckView<>(otherCols);
    		otherCols.setOnFilterAction(e -> {
    			Popup pop = new Popup();
    			pop.getContent().add(new VBox(otherColCheck));
    			pop.show(this.getScene().getWindow());
    			pop.setAutoHide(true);
    			pop.setOnHidden(evt -> {
    				filterButton.setVisible(!otherColCheck.getCheckModel().isEmpty());
    			});
    		});
    		otherCols.setPredicate((str) -> otherColCheck.getCheckModel().getCheckedItems().isEmpty() 
    				? true
    				: otherColCheck.getCheckModel().getCheckedItems().contains(str));
            
    		
    		filterButton.setOnMouseClicked((e) -> {
        		if(e.getButton() == MouseButton.PRIMARY) {
        			otherCols.getOnFilterAction().handle(new ActionEvent(e.getSource(), e.getTarget()));
        		}
    		});
            filterButton.disableProperty().bind(otherCols.filterableProperty().not());
    	}
    	column.setSortable(false);
    	column.setUserData(name);
    	
		if (name.equals(FileAttributesType.NAME.getName()))
			column.setPrefWidth(200);
		else
			column.setPrefWidth(100);
    	
    	
    	
    	
    	Label nameLbl = new Label(name);
    	nameLbl.setBorder(Border.stroke(Color.DARKRED));
    	nameLbl.setAlignment(Pos.TOP_LEFT);
    	HBox.setHgrow(nameLbl, Priority.ALWAYS);
    	nameLbl.setMaxWidth(Double.MAX_VALUE);
    	HBox namePane = new HBox();
    	namePane.getChildren().add(nameLbl);
    	namePane.setBorder(Border.stroke(Color.GREEN));
    	VBox.setVgrow(namePane, Priority.ALWAYS);
        
        
    	//final javafx.scene.image.Image SIDE_ARROW = SwingFXUtils.toFXImage((BufferedImage) ImageUtils.getImageResource(ExpandPanel.class, "images/side_arrow.png"), null);
        /*Label arrow = new Label("^");// ▲^ new Label("›");
        arrow.setBorder(Border.stroke(Color.BLUEVIOLET));*/
    	ImageView arrow = new ImageView();
    	arrow.setImage(SIDE_ARROW);
    	arrow.setPreserveRatio(true);
    	arrow.fitHeightProperty().bind(namePane.heightProperty().multiply(0.4));
    	arrow.fitWidthProperty().bind(namePane.widthProperty());
        arrow.setVisible(false);
        
        VBox colName = new VBox();
        colName.setPadding(Insets.EMPTY);
        colName.setAlignment(Pos.CENTER);
        //colName.setBackground(Background.fill(Color.WHITE));
        colName.getChildren().addAll(arrow, namePane);
        colName.setOnMouseClicked((e) -> {
        	if(e.getButton() == MouseButton.PRIMARY) {
        		//Allow to Sort
        		column.setSortable(true);
        		//Find The next Sort type
        		FileViewUtils.sortColumn(e.isShiftDown(), column, this);
        		//Now Sort, if can.
        		this.sort();
        		//Show The arrow if the column is currently Sorting
                final ObservableList<TableColumnBase<?,?>> sortOrder = FileViewUtils.getSortOrder(this);//getSortOrder(null);
                boolean isSortColumn = sortOrder.contains(column);
                if (! isSortColumn) {
                	arrow.setVisible(false);
                }
                else {
                	arrow.setVisible(true);
                	arrow.setRotate(isAscending(column) ? 270.0F : 90.0F);
                }
                //Finally don't allow to sort, in order for the default column header mouser press sort not to work.
        		column.setSortable(false);
        	}
        });
    	
    	nameLbl.setTextFill(Color.rgb(91, 119, 168)); //0070CE
    	
    	filterButton.setBorder(Border.stroke(Color.rgb(91, 119, 168)));
    	
        HBox columnGraphics = new HBox();
        columnGraphics.getChildren().add(colName);
        HBox.setHgrow(colName, Priority.ALWAYS);
        
    	//filterButton.prefHeightProperty().bind(colName.heightProperty());
        //filterButton.prefWidthProperty().bind(colName.widthProperty().multiply(0.1));
        
    	
    	columnGraphics.getChildren().add(filterButton);
        
        column.setGraphic(columnGraphics);
        
        
        columnGraphics.focusWithinProperty().addListener((observable, oldVal, newVal) -> {
        	if(newVal) {
        		columnGraphics.setBackground(Background.fill(Color.rgb(185, 209, 234, 0.3)));
        	}
        	else {
        		columnGraphics.setBackground(Background.EMPTY);
        	}
        });
    	
    	columnGraphics.setOnMouseEntered((e) -> {
    		if(!columnGraphics.isFocused()) {
        		columnGraphics.setBackground(Background.fill(Color.rgb(185, 209, 234, 0.3)));
        		filterButton.setVisible(true);
    		}
    	});
    	columnGraphics.setOnMouseExited((e) -> {
    		if(!columnGraphics.isFocused()) {
        		columnGraphics.setBackground(Background.EMPTY);
        		filterButton.setVisible(false);
    		}
    	});
    	
    	/*columnGraphics.setOnMousePressed((e) -> {
    		
    	});*/
    	
    	filterButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
    		columnGraphics.requestFocus();
    	});
    	
    	filterButton.visibleProperty().addListener((observable, oldVal, newVal) -> {
    		if(oldVal && !newVal) 
    			column.getTableView().requestFocus();
    	});
    	
    	columnGraphics.setOnMouseClicked((e) -> {
    		if(e.getButton() == MouseButton.SECONDARY) {
    			Set<String> keys = column.getTableView().getItems().stream().map(f -> f.getAllKeys()).flatMap(Set::stream).collect(Collectors.toSet());
    			CheckListView<String> keysView = new CheckListView<>();
    			//keysView.getStylesheets().add("disable-empty-rows.css");
    			keysView.setStyle(".table-row-cell:empty { -fx-background-color: white; -fx-border-color: white;} ");
    			keysView.setItems(FXCollections.observableArrayList(keys));
    			keysView.setMinHeight(USE_PREF_SIZE);
    			
    			//There is a bug when setting check to items that their indices are not sorted upward.
    			FileViewUtils.setCheckIndices(keysView, this.getColumns().stream()
    					.map((col) -> col.getUserData().toString()));
    			
    			Popup pop = new Popup();
    			Button btn = new Button();
    			btn.setOnMouseClicked((evt) -> {
    				ObservableList<String> checkedList = keysView.getCheckModel().getCheckedItems();
    				System.out.println(checkedList);
    				this.getColumns().removeIf((c) -> canRemoveColumn(c, checkedList));
    				for(String checked : checkedList) {
    					System.out.println("Checked: " + checked);
    					addColumn(checked);
    				}
    				pop.hide();
    			});
    			btn.setMaxWidth(Double.MAX_VALUE);
    			VBox view = new VBox();
    			view.getChildren().add(keysView);
    			view.getChildren().add(btn);
    			pop.getContent().add(view);
    			
    			pop.show(this.getScene().getWindow());
    			pop.setAutoHide(true);
    			// in order to prevent the tableView popup from showing
    			e.consume();
    		}
    	});
    	
    	this.getColumns().add(column);
    }
    
	@Override
	public FileTableView<FileDetails> getFileTableView() {
		return this.fileTableDetailsManager;
	}

	@Override
	public FileTableHandler getFileTableHandler() {
		return this.fileTableDetailsManager;
	}
	
	@Override
	public Control getFileView() {
		return this;
	}
	
	private class FileTableDetailsManager extends FileDetailsTableManager {
		
		private FileTableDetailsView fileTableDetailsView;
		
		public FileTableDetailsManager(FileTableDetailsView fileTableDetailsView) {
			this.fileTableDetailsView = fileTableDetailsView;
		}
		
		@Override
		public void setItems(ObservableList<FileDetails> items) {
			fileTableDetailsView.setBackingList(items);
		}
		
		@Override
		public ObservableList<FileDetails> getItems() {
			return fileTableDetailsView.getBackingList();
		}
		
		@Override
		public ObservableList<FileDetails> getSelectedItems() {
			return fileTableDetailsView.getSelectionModel().getSelectedItems();
		} 
		
		@Override
		public void setFiles(Collection<File> files) {
			super.setFiles(files);
			FilteredTableView.configureForFiltering(fileTableDetailsView, getItems());
		}
		
		@Override
		public void setFileToBeSelected(File file) {
			if(file == null)
	            return;
			Optional<FileDetails> opt = fileTableDetailsView.getItems().stream().filter((fd) -> file.equals(fd.getFile())).findAny();
			if(opt.isEmpty())
	            return;
			fileTableDetailsView.getSelectionModel().select(opt.get());
		}
	}
}