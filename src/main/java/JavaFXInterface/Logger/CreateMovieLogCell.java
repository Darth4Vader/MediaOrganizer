package JavaFXInterface.Logger;

import java.util.logging.Level;

import DataStructures.FileOperationDetails;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Popup;

/**
 * Custom ListCell for displaying log records in a log view.
 * <p>This cell format is used to present log entries with a timestamp and message. 
 * The color of the log message changes according to the log level to differentiate 
 * the severity of the log entry.</p>
 */
public class CreateMovieLogCell extends ListCell<LoggerRecord> {
    
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
    
    /**
     * Constructs a new {@code CreateMovieLogCell} instance.
     * <p>Initializes the layout and style for displaying log records.</p>
     */
    public CreateMovieLogCell() {
        setStyle("-fx-padding: 0px;");
        
        textFlow = new TextFlow();
        timeStamp = new Text();
        textFlow.getChildren().add(timeStamp);
        message = new Text();
        textFlow.getChildren().add(message);
        
    	setOnMouseClicked(event -> {
			if (event.getClickCount() == 2 && getItem() != null) {
			LoggerRecord logRecord = getItem();
				if(logRecord.getLevel() == Level.SEVERE) {
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
				}
			}
		});
    }
    
    /**
     * Updates the cell's content based on the provided {@link LoggerRecord}.
     * <p>Sets the timestamp and message text, and applies color coding based on the log level.</p>
     * 
     * @param logRecord The {@link LoggerRecord} to be displayed in the cell.
     */
    private void set(LoggerRecord logRecord) {
        Level level = logRecord.getLevel();
        if(level == Level.WARNING) {
            message.setFill(Color.YELLOW);
        }
        else if(level == Level.FINER) {
            message.setFill(Color.GREEN);
        }
        if(level == Level.SEVERE) {
			message.setFill(Color.RED);
		}
        else {
            message.setFill(Color.BLACK);
        }
        timeStamp.setText(logRecord.getTimestamp() + "  ");
        if(level == Level.SEVERE) {
        	message.setText(logRecord.getMessage());
        }
        else {
	    	try {
	        	Object body = logRecord.getBody();
	        	System.out.println("LogRecord body: " + body);
	        	if(body instanceof FileOperationDetails) {
					FileOperationDetails fileOperationDetails = (FileOperationDetails) body;
					message.setText(fileOperationDetails.getSourceFile() + " (" + fileOperationDetails.getAction() + ") -> " + fileOperationDetails.getDestPath());
				}
				else {
					message.setText(logRecord.getMessage());
				}
	        }
			catch (Exception e) {
				e.printStackTrace();
				message.setText(logRecord.getMessage());
			}
        }
    }
    
    /**
     * Resets the cell's content to default values.
     * <p>Clears the timestamp and message texts and sets default color for the message.</p>
     */
    private void reset() {
        message.setFill(Color.BLACK);
        timeStamp.setText("");
        message.setText("");
    }
    
    @Override
    public void updateItem(LoggerRecord item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
            reset();
        }
        else {
            setGraphic(textFlow);
            set(item);
        }
        setAlignment(Pos.CENTER_LEFT);
    }
}