import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.controlsfx.control.tableview2.TableView2;

import impl.org.controlsfx.tableview2.NestedTableColumnHeader2;
import impl.org.controlsfx.tableview2.TableHeaderRow2;
import impl.org.controlsfx.tableview2.TableView2Skin;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.skin.NestedTableColumnHeader;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.control.skin.TableHeaderRow;

public class TestingTableViewSkins {

    
    /**
     * Custom TableColumnHeader that lays out the sort icon at its leading edge.
     */
    public static class MyTableColumnHeader extends TableColumnHeader {

        public MyTableColumnHeader(TableColumnBase column) {
            super(column);
        }

        @Override
        protected void layoutChildren() {
            // call super to ensure that all children are created and installed
            super.layoutChildren();
            Node sortArrow = getSortArrow();
            // no sort indicator, nothing to do
            if (sortArrow == null || !sortArrow.isVisible()) return;
            if (getSortIconDisplay() == ContentDisplay.RIGHT) return;
            // re-arrange label and sort indicator
            double sortWidth = sortArrow.prefWidth(-1);
            double headerWidth = snapSizeX(getWidth()) - (snappedLeftInset() + snappedRightInset());
            double headerHeight = getHeight() - (snappedTopInset() + snappedBottomInset());

            // position sort indicator at leading edge
            sortArrow.resize(sortWidth, sortArrow.prefHeight(-1));
            positionInArea(sortArrow, snappedLeftInset(), snappedTopInset(),
                    sortWidth, headerHeight, 0, HPos.CENTER, VPos.CENTER);
            // resize label to fill remaining space
            getLabel().resizeRelocate(sortWidth, 0, headerWidth - sortWidth, getHeight());
        }

        // --------------- make sort icon location styleable
        // use StyleablePropertyFactory to simplify styling-related code
        private static final StyleablePropertyFactory<MyTableColumnHeader> FACTORY = 
                new StyleablePropertyFactory<>(TableColumnHeader.getClassCssMetaData());

        // default value (strictly speaking: an implementation detail)
        // PENDING: what about RtoL orientation? Is it handled correctly in
        // core?
        private static final ContentDisplay DEFAULT_SORT_ICON_DISPLAY = ContentDisplay.RIGHT;

        private static CssMetaData<MyTableColumnHeader, ContentDisplay> CSS_SORT_ICON_DISPLAY = 
                FACTORY.createEnumCssMetaData(ContentDisplay.class,
                        "-fx-sort-icon-display",
                        header -> header.sortIconDisplayProperty(),
                        DEFAULT_SORT_ICON_DISPLAY);

        // property with lazy instantiation
        private StyleableObjectProperty<ContentDisplay> sortIconDisplay;

        protected StyleableObjectProperty<ContentDisplay> sortIconDisplayProperty() {
            if (sortIconDisplay == null) {
                sortIconDisplay = new SimpleStyleableObjectProperty<>(
                        CSS_SORT_ICON_DISPLAY, this, "sortIconDisplay",
                        DEFAULT_SORT_ICON_DISPLAY);

            }
            return sortIconDisplay;
        }

        protected ContentDisplay getSortIconDisplay() {
            return sortIconDisplay != null ? sortIconDisplay.get()
                    : DEFAULT_SORT_ICON_DISPLAY;
        }

        protected void setSortIconDisplay(ContentDisplay display) {
            sortIconDisplayProperty().set(display);
        }

        /**
         * Returnst the CssMetaData associated with this class, which may
         * include the CssMetaData of its superclasses.
         * 
         * @return the CssMetaData associated with this class, which may include
         *         the CssMetaData of its superclasses
         */
        public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
            return FACTORY.getCssMetaData();
        }

        /** {@inheritDoc} */
        @Override
        public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
            return getClassCssMetaData();
        }

//-------- reflection acrobatics .. might use lookup and/or keeping aliases around
        private Node getSortArrow() {
            return (Node) invokeGetFieldValue(TableColumnHeader.class, this, "sortArrow");
        }

        private Label getLabel() {
            return (Label) invokeGetFieldValue(TableColumnHeader.class, this, "label");
        }

    }

    /**
     * Custom nested columnHeader, headerRow und skin only needed to 
     * inject the custom columnHeader in their factory methods.
     */
    public static class MyNestedTableColumnHeader extends NestedTableColumnHeader2 {

        public MyNestedTableColumnHeader(TableColumnBase column) {
            super(column);
        }
        
        /** {@inheritDoc} */
        @Override protected TableColumnHeader createTableColumnHeader(final TableColumnBase col) {
            if (col == null || col.getColumns().isEmpty() || col == getTableColumn())  {
                final TableColumnHeader tableColumnHeader = new MyTableColumnHeader(col);
                invokeGetMethodValue(NestedTableColumnHeader2.class, this, "addMousePressedListener", TableColumnHeader.class, tableColumnHeader);
                invokeGetMethodValue(NestedTableColumnHeader2.class, this, "addMouseReleasedListener", TableColumnHeader.class, tableColumnHeader);
                /*addMousePressedListener(tableColumnHeader);
                addMouseReleasedListener(tableColumnHeader);*/
                return tableColumnHeader;
            } else {
                final MyNestedTableColumnHeader rootHeader = new MyNestedTableColumnHeader(col);
                final ObservableList<Node> rootChildren = rootHeader.getChildren();
                rootChildren.addListener(new InvalidationListener() {
                    @Override
                    public void invalidated(Observable o) {
                        if (rootChildren.size() > 0) {
                            final TableColumnHeader tableColumnHeader = (TableColumnHeader) rootChildren.get(0);
                            invokeGetMethodValue(NestedTableColumnHeader2.class, this, "addMouseReleasedListener", TableColumnHeader.class, tableColumnHeader);
                            //addMouseReleasedListener(tableColumnHeader);
                            rootChildren.removeListener(this);
                        }
                    }
                });
                //addMousePressedListener(rootHeader);
                invokeGetMethodValue(NestedTableColumnHeader2.class, this, "addMousePressedListener", TableColumnHeader.class, rootHeader);
                return rootHeader;
            }
        }
    }

    public static class MyTableHeaderRow extends TableHeaderRow2 {

        public MyTableHeaderRow(TableView2Skin tableSkin) {
            super(tableSkin);
        }

        @Override
        protected NestedTableColumnHeader createRootHeader() {
        	//invokeGetFieldValue(TableHeaderRow2.class, this, "rootHeader2");
        	//NestedTableColumnHeader2 n = (NestedTableColumnHeader2) invokeGetFieldValue(TableHeaderRow2.class, this, "rootHeader2");
        	MyNestedTableColumnHeader n = new MyNestedTableColumnHeader(null);
        	invokeSetFieldValue(TableHeaderRow2.class, this, "rootHeader2", n);
            return n;
        }
    }

    public static class MyTableViewSkin<T> extends TableView2Skin<T> {

        public MyTableViewSkin(TableView2<T> table) {
            super(table);
        }

        @Override
        protected TableHeaderRow createTableHeaderRow() {
            return new MyTableHeaderRow(this);
        }

    }
    
    /**
     * Reflectively access hidden field's value.
     * 
     * @param declaringClass the declaring class
     * @param target the instance to look up
     * @param name the field name
     * @return value of the field or null if something happened
     */
    public static Object invokeGetFieldValue(Class declaringClass, Object target, String name) {
        try {
            Field field = declaringClass.getDeclaredField(name);
            field.setAccessible(true);
            return field.get(target);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Reflectively access hidden method's value without parameters.
     * 
     * @param declaringClass the declaring class
     * @param target the instance to look up
     * @param name the method name
     * @return value of the field or null if something happened
     */
    public static Object invokeGetMethodValue(Class declaringClass, Object target, String name) {
        try {
            Method field = declaringClass.getDeclaredMethod(name);
            field.setAccessible(true);
            return field.invoke(target);
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Reflectively access method without paramters.
     * @param declaringClass the declaring class
     * @param target the instance to look up
     * @param name the method name
     */
    public static void invokeMethod(Class declaringClass, Object target, String name) {
        try {
            Method method = declaringClass.getDeclaredMethod(name);
            method.setAccessible(true);
            method.invoke(target);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
     * Reflectively access hidden method value with a single parameter.
     * 
     * @param declaringClass the declaring class
     * @param target the instance to look up
     * @param name the field name
     * @return value of the field or null if something happened
     */
    public static Object invokeGetMethodValue(Class declaringClass, Object target, String name, Class paramType, Object param) {
        try {
            Method field = declaringClass.getDeclaredMethod(name, paramType);
            field.setAccessible(true);
            return field.invoke(target, param);
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void invokeSetFieldValue(Class<?> declaringClass, Object target, String name, Object value) {
        try {
            Field field = declaringClass.getDeclaredField(name);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
