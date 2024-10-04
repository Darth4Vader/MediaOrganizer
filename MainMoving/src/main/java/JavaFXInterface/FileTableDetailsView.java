package JavaFXInterface;

import static com.sun.javafx.scene.control.TableColumnSortTypeWrapper.isAscending;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.tika.exception.TikaException;
import org.controlsfx.control.CheckListView;
import org.controlsfx.control.tableview2.FilteredTableColumn;
import org.controlsfx.control.tableview2.FilteredTableView;
import org.xml.sax.SAXException;

import DirectoryWatcher.FileChange;
import DirectoryWatcher.FileChange.FileChaneType;
import FileUtils.FileAttributesType;
import FileUtils.FileDetails;
import JavaFXInterface.SideFilesList.ExpandPanel;
import JavaFXInterface.controlsfx.BetterFilteredTableColumn;
import JavaFXInterface.controlsfx.BetterFilteredTableView;
import JavaFXInterface.controlsfx.FilteredTableColumnCheckView;
import OtherUtilities.ImageUtils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.image.ImageView;
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

public class FileTableDetailsView extends BetterFilteredTableView<FileDetails> implements FileTableHandler {
	
	private final ObservableList<String> fixedColumns;
	
    public FileTableDetailsView() {
    	this.fixedColumns = FXCollections.observableArrayList(
    			Arrays.asList(FileAttributesType.NAME, FileAttributesType.TYPE).stream().map((s) -> s.getName()).collect(Collectors.toList()));
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
        
    public FileTableDetailsView(File file) {
    	this();
    	setFile(Arrays.asList(file.listFiles()));
    }
    
    public void setFile(List<File> files) {
    	ObservableList<FileDetails> fileList = this.getBackingList();
    	if(fileList == null)
    		fileList = FXCollections.observableArrayList();
    	fileList.clear();
    	files.stream()
		.map(f -> {
			try {
				return new FileDetails(f);
			} catch (IOException | SAXException | TikaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}).filter(p -> p != null).forEach(fileList::add);
    	FilteredTableView.configureForFiltering(this, fileList);
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
    	
    	final javafx.scene.image.Image SIDE_ARROW = SwingFXUtils.toFXImage((BufferedImage) ImageUtils.getImageResource(ExpandPanel.class, "images/side_arrow.png"), null);
        /*Label arrow = new Label("^");// ▲^ new Label("›");
        arrow.setBorder(Border.stroke(Color.BLUEVIOLET));*/
    	ImageView filterImage = new ImageView();
    	filterImage.setImage(SIDE_ARROW);
    	filterImage.setPreserveRatio(true);
    	filterImage.setRotate(90.0F);
    	
    	
    	BorderPane filterButton = new BorderPane();
    	
    	filterImage.fitHeightProperty().bind(filterButton.heightProperty().multiply(0.5));
    	filterImage.fitWidthProperty().bind(filterButton.widthProperty().multiply(0.8));
    	
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
    	    	
    		    @Override
    		    public void updateItem(FileDetails item, boolean empty) {
    		        super.updateItem(item, empty);
    	            if (item == null || empty) {
    		            setText(null);
    		            setGraphic(null);
    		            imageView.setImage(null);
    		        } else {
    		        	setText(item.getValue(name));
    		            imageView.setImage(AppUtils.getImageOfFile(item.getFile()));
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
    	filterButton.prefWidthProperty().bind(colName.widthProperty().multiply(0.1));
        
    	
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
    	
    	columnGraphics.setOnMouseClicked((e) -> {
    		if(e.getButton() == MouseButton.SECONDARY) {
    			Set<String> keys = column.getTableView().getItems().stream().map(f -> f.getAllKeys()).flatMap(Set::stream).collect(Collectors.toSet());
    			CheckListView<String> keysView = new CheckListView<>();
    			keysView.setItems(FXCollections.observableArrayList(keys));
    			
    			
    			
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
    		}
    	});
    	
    	this.getColumns().add(column);
    }

	@Override
	public void handleFileChange(FileChange fileChange) {
		System.out.println(fileChange.getFileChaneType() + " " + fileChange.getPath());
		FileChaneType type = fileChange.getFileChaneType();
		File file = fileChange.getPath().toFile();
		ObservableList<FileDetails> fileList = this.getBackingList();
		switch(type) {
		case CREATED:
			if(!file.isHidden())
				try {
					fileList.add(new FileDetails(file));
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			break;
		case DELETED:
			//fileList.remove(0);
			break;
		case RENAMED:
			/*if(fileChange instanceof FileRename) {
			    int itemIndex = fileList.indexOf(file);
			    if (itemIndex != -1) {
			    	File newFile = ((FileRename) fileChange).getNewPath().toFile();
			        fileList.set(itemIndex, newFile);
			    }
			}*/
			break;
		case UPDATED:
			break;
		default:
			break;
		}
	}
}