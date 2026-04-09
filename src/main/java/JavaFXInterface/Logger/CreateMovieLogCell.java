package JavaFXInterface.Logger;

import DataStructures.FileOperationDetails;
import DataStructures.ManageFileDetails;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Custom ListCell for displaying log records in a log view.
 * <p>This cell format is used to present log entries with a timestamp and message. 
 * The color of the log message changes according to the log level to differentiate 
 * the severity of the log entry.</p>
 */
public class CreateMovieLogCell extends ListCell<UiManageFileDetails> {
    
    /**
     * Container for laying out and formatting text nodes.
     * <p>The {@link TextFlow} instance organizes {@link Text} nodes for timestamp and message
     * into a single visual representation within the cell.</p>
     */
    private TextFlow textFlow;
    
    /**
     * Displays the log message.
     * <p>The {@link Text} instance for the message is styled based on the log level:
     * red for warnings, green for finer logs, and black for other log levels.</p>
     */
    private Text message;
    
    /**
     * Displays the timestamp of the log entry.
     * <p>The {@link Text} instance for the timestamp provides the date and time when
     * the log entry was created, displayed before the log message.</p>
     */
    private Text timeStamp;
    
    private final ListView<FileOperationDetails> operationsListView = new ListView<>();
        
    private UiManageFileDetails currentItem;

    private ListChangeListener<FileOperationDetails> fileOperationDetailsSizeListener;

    private final VBox container = new VBox(2); // vertical layout
    
    private static final double DEFAULT_CELL_SIZE = 32.0; // Default height for each cell in the ListView
    
    /**
     * Constructs a new {@code CreateMovieLogCell} instance.
     * <p>Initializes the layout and style for displaying log records.</p>
     */
    public CreateMovieLogCell() {
        setStyle("-fx-padding: 0px;");
        container.setAlignment(Pos.TOP_LEFT);
        operationsListView.setMouseTransparent(false);
        
        // hide v scroolllbar
        //operationsListView.setStyle("-fx-control-inner-background: transparent; -fx-background-insets: 0; -fx-padding: 0; .list-cell:empty { -fx-opacity: 0; }");
        operationsListView.setStyle(".list-cell:empty { -fx-opacity: 0; }");
        
        // Optional: remove extra padding/margin
        operationsListView.setPadding(javafx.geometry.Insets.EMPTY);
        
        textFlow = new TextFlow();
        timeStamp = new Text();
        textFlow.getChildren().add(timeStamp);
        message = new Text();
        textFlow.getChildren().add(message);
        
    	setOnMouseClicked(event -> {
			if (event.getClickCount() == 2 && getItem() != null) {
				ManageFileDetails manageFileDeatDetails = getItem();
				/*VBox otherColCheck = new VBox();
				otherColCheck.setAlignment(Pos.CENTER);
				otherColCheck.setStyle("-fx-background-color: white; -fx-padding: 10px; -fx-border-color: black; -fx-border-width: 1px;");
				otherColCheck.getChildren().add(new Text("Error Details:"));
				Throwable exception = logRecord.getException();
				if(exception != null) {
					otherColCheck.getChildren().add(new Text("Exception: " + exception.toString()));
					for(StackTraceElement element : exception.getStackTrace()) {
						otherColCheck.getChildren().add(new Text("    at " + element.toString()));
					}
				}
				else {
					otherColCheck.getChildren().add(new Text("Message: " + logRecord.getMessage()));
				}
				
    			Popup pop = new Popup();
    			pop.getContent().add(new VBox(otherColCheck));
    			pop.show(this.getScene().getWindow());
    			pop.setAutoHide(true);*/
    			
				/*if(logRecord.getLevel() == Level.SEVERE) {
					// Create a new stage for the popup
					VBox otherColCheck = new VBox();
					otherColCheck.setAlignment(Pos.CENTER);
					otherColCheck.setStyle("-fx-background-color: white; -fx-padding: 10px; -fx-border-color: black; -fx-border-width: 1px;");
					otherColCheck.getChildren().add(new Text("Error Details:"));
					Throwable exception = logRecord.getException();
					if(exception != null) {
						otherColCheck.getChildren().add(new Text("Exception: " + exception.toString()));
						for(StackTraceElement element : exception.getStackTrace()) {
							otherColCheck.getChildren().add(new Text("    at " + element.toString()));
						}
					}
					else {
						otherColCheck.getChildren().add(new Text("Message: " + logRecord.getMessage()));
					}
					
        			Popup pop = new Popup();
        			pop.getContent().add(new VBox(otherColCheck));
        			pop.show(this.getScene().getWindow());
        			pop.setAutoHide(true);
				}*/
			}
		});
    	
        container.getChildren().add(textFlow);
        container.getChildren().add(operationsListView);
    }
    
    /**
     * Updates the cell's content based on the provided {@link LoggerRecord}.
     * <p>Sets the timestamp and message text, and applies color coding based on the log level.</p>
     * 
     * @param logRecord The {@link LoggerRecord} to be displayed in the cell.
     */
    private void set(UiManageFileDetails item) {
        this.currentItem = item;
        
        // Main file path
        Text mainFileText = new Text(item.getMainFile().getAbsolutePath() + "\n");
        mainFileText.setFill(Color.BLUE); // highlight main file
        textFlow.getChildren().add(mainFileText);

        // Bind ListView to the observable list
        operationsListView.setItems(item.getFileOperationDetailsList());
        
        this.fileOperationDetailsSizeListener = new ListChangeListener<>() {

			@Override
			public void onChanged(Change<? extends FileOperationDetails> c) {
				operationsListView.setPrefHeight(item.getFileOperationDetailsList().size() * DEFAULT_CELL_SIZE + 2 + 24);
			}
        };
        
        item.getFileOperationDetailsList().addListener(fileOperationDetailsSizeListener);

        // Set a custom cell factory for bold action
        operationsListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(FileOperationDetails op, boolean emptyOp) {
                super.updateItem(op, emptyOp);
                if (op == null || emptyOp) {
                    setText(null);
                } else {
                    setText(op.getAction() + ": " + op.getSourceFile() + " -> " + op.getDestPath());
                    setStyle("-fx-font-weight: normal;");
                    // Bold only the action part
                    Text text = new Text(op.getAction() + "");
                    text.setStyle("-fx-font-weight: bold;");
                }
            }
        });
    }
    
    /**
     * Resets the cell's content to default values.
     * <p>Clears the timestamp and message texts and sets default color for the message.</p>
     */
    private void reset() {
        message.setFill(Color.BLACK);
        timeStamp.setText("");
        message.setText("");
        textFlow.getChildren().clear();
        operationsListView.setItems(null);
        if(this.currentItem != null) {
        	this.currentItem.getFileOperationDetailsList().removeListener(fileOperationDetailsSizeListener);
        	this.currentItem = null;
        }
    }
    
    @Override
    public void updateItem(UiManageFileDetails item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
        	reset();
            setGraphic(null);
        }
        else {
        	reset();
        	setGraphic(container);
            set(item);
        }
        setAlignment(Pos.CENTER_LEFT);
    }
}