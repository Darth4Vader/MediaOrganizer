package JavaFXInterface.controlsfx;

import org.controlsfx.control.GridView;

import impl.org.controlsfx.skin.GridViewSkin;
import javafx.scene.AccessibleAttribute;
import javafx.scene.control.FocusModel;

public class BetterGridViewSkin<T> extends GridViewSkin<T> {

	public BetterGridViewSkin(GridView<T> control) {
		super(control);
	}
	
    /** {@inheritDoc} */
    @Override 
    protected Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
    	switch (attribute) {
            case FOCUS_ITEM: {
            	GridView<T> skinnable = getSkinnable();
            	if(skinnable instanceof GridViewSelection) {
                    FocusModel<?> fm = ((GridViewSelection<T>) skinnable).getFocusModel();
                    if (fm == null) {
                        /*if (placeholderRegion != null && placeholderRegion.isVisible()) {
                            return placeholderRegion.getChildren().get(0);
                        } else {*/
                            return null;
                        //}
                    }

                    int focusedIndex = fm.getFocusedIndex();
                    if (focusedIndex == -1) {
                        /*if (placeholderRegion != null && placeholderRegion.isVisible()) {
                            return placeholderRegion.getChildren().get(0);
                        }*/
                        if (getItemCount() > 0) {
                            focusedIndex = 0;
                        } else {
                            return null;
                        }
                    }
                    return getFlow().getPrivateCell(focusedIndex);      		
            	}
            	return null;
            }
            default: return super.queryAccessibleAttribute(attribute, parameters);
        }
    }
}
