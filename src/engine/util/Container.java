package engine.util;

import java.util.LinkedList;
import java.util.List;

public class Container<T> {
    transient protected final LinkedList<T> children = new LinkedList<>();
    transient protected T parent;

    public T getParent() {
        return parent;
    }

    public List<T> getChildren() {
        return children;
    }

    public void addChildren(T t) {
        children.add(t);
    }

    public void removeChildren(T t) {
        children.remove(t);
    }

}
