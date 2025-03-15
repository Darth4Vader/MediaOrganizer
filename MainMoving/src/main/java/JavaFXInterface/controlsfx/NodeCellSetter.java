package JavaFXInterface.controlsfx;

import java.io.File;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Node;
import javafx.scene.control.Cell;

public interface NodeCellSetter<T> {
	
	public void set(T item);
	
	public void set(T file, Cell<T> cell);
	
	public boolean isSame(T item);
	
	public void reset();
	
	public Node getView();
	
	public void bindWidth(ReadOnlyDoubleProperty property);
	
	public void bindHeight(ReadOnlyDoubleProperty property);
}