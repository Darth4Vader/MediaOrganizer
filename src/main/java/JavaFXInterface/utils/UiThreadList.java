package JavaFXInterface.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UiThreadList<T> implements List<T> {

    private final ObservableList<T> delegate = FXCollections.observableArrayList();

    private void runOnFx(Runnable action) {
        if (Platform.isFxApplicationThread()) {
            action.run();
        } else {
            Platform.runLater(action);
        }
    }

    // --- Mutating operations ---

    @Override
    public boolean add(T element) {
        if (Platform.isFxApplicationThread()) {
            return delegate.add(element);
        } else {
            Platform.runLater(() -> delegate.add(element));
            return true;
        }
    }

    @Override
    public void add(int index, T element) {
        runOnFx(() -> delegate.add(index, element));
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (Platform.isFxApplicationThread()) {
            return delegate.addAll(c);
        } else {
            Platform.runLater(() -> delegate.addAll(c));
            return true;
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        runOnFx(() -> delegate.addAll(index, c));
        return true;
    }

    @Override
    public T set(int index, T element) {
        if (Platform.isFxApplicationThread()) {
            return delegate.set(index, element);
        } else {
            Platform.runLater(() -> delegate.set(index, element));
            return null;
        }
    }

    @Override
    public T remove(int index) {
        if (Platform.isFxApplicationThread()) {
            return delegate.remove(index);
        } else {
            Platform.runLater(() -> delegate.remove(index));
            return null;
        }
    }

    @Override
    public boolean remove(Object o) {
        runOnFx(() -> delegate.remove(o));
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        runOnFx(() -> delegate.removeAll(c));
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        runOnFx(() -> delegate.retainAll(c));
        return true;
    }

    @Override
    public void clear() {
        runOnFx(delegate::clear);
    }

    // --- Read-only operations (no FX thread needed) ---

    @Override public int size() { return delegate.size(); }
    @Override public boolean isEmpty() { return delegate.isEmpty(); }
    @Override public boolean contains(Object o) { return delegate.contains(o); }
    @Override public Iterator<T> iterator() { return delegate.iterator(); }
    @Override public Object[] toArray() { return delegate.toArray(); }
    @Override public <E> E[] toArray(E[] a) { return delegate.toArray(a); }
    @Override public boolean containsAll(Collection<?> c) { return delegate.containsAll(c); }
    @Override public T get(int index) { return delegate.get(index); }
    @Override public int indexOf(Object o) { return delegate.indexOf(o); }
    @Override public int lastIndexOf(Object o) { return delegate.lastIndexOf(o); }
    @Override public ListIterator<T> listIterator() { return delegate.listIterator(); }
    @Override public ListIterator<T> listIterator(int index) { return delegate.listIterator(index); }
    @Override public List<T> subList(int fromIndex, int toIndex) { return delegate.subList(fromIndex, toIndex); }
    
    public ObservableList<T> getObservableList() {
		return delegate;
	}
}